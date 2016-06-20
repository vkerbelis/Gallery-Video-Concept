package lt.blaster.galleryvideoconceptapp.tools;

import android.support.annotation.Nullable;

import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.20.
 */
public final class MovieTools {

    private MovieTools() {
        // Empty hidden constructor
    }

    public static List<Track> getTrimmedMovieTrackList(List<Track> tracks,
                                                       double startTime, double endTime) {
        double correctedStartTime = startTime;
        double correctedEndTime = endTime;
        // Here we try to find a track that has sync samples. Since we can only start decoding
        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
        // such a frame
        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                correctedStartTime = correctTimeToSyncSample(track, startTime, false);
                correctedEndTime = correctTimeToSyncSample(track, endTime, true);
                break;
            }
        }
        return getTrimmedMovieListInner(tracks, correctedStartTime, correctedEndTime);
    }

    public static Movie createTrimmedMovie(String path) throws IOException {
        Movie movie = MovieCreator.build(new FileDataSourceImpl(path));
        List<Track> tracks = MovieTools.getTrimmedMovieTrackList(movie.getTracks(), 10, 20);
        movie.setTracks(tracks);
        return movie;
    }

    @Nullable
    private static List<Track> getTrimmedMovieListInner(List<Track> tracks,
                                                        double startTime, double endTime) {
        LinkedList<Track> resultTracks = new LinkedList<>();
        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            double lastTime = -1;
            long startSample = -1;
            long endSample = -1;

            for (int i = 0; i < track.getSampleDurations().length; i++) {
                long delta = track.getSampleDurations()[i];


                if (currentTime > lastTime && currentTime <= startTime) {
                    startSample = currentSample;
                }
                if (currentTime > lastTime && currentTime <= endTime) {
                    endSample = currentSample;
                }
                lastTime = currentTime;
                currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
            try {
                resultTracks.add(new AppendTrack(new CroppedTrack(track, startSample, endSample)));
            } catch (IOException cause) {
                return null;
            }
        }
        return resultTracks;
    }

    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];
            long[] samples = track.getSyncSamples();
            if (Arrays.binarySearch(samples, currentSample + 1) >= 0) {
                // samples always start with 1 but we start with zero therefore +1
                timeOfSyncSamples[Arrays.binarySearch(samples, currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}
