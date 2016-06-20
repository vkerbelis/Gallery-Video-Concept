package lt.blaster.galleryvideoconceptapp.main;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.googlecode.mp4parser.authoring.Movie;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import lt.blaster.galleryvideoconceptapp.PresenterView;
import lt.blaster.galleryvideoconceptapp.tools.FileTools;
import lt.blaster.galleryvideoconceptapp.tools.MovieTools;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public class MainPresenterImpl implements MainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private PresenterView view;
    private boolean sawInputEOS;
    private boolean sawOutputEOS;
    private BufferedOutputStream outputStream;

    @Override
    public void takeView(@NonNull MainView view) {
        this.view = view;
    }

    @Override
    public void dropView(@NonNull MainView view) {
        this.view = null; // NOPMD
    }

    @Override
    public boolean hasView() {
        return view != null;
    }

    @Override
    public void trimVideo(String filePath) {
        try {
            Movie movie = MovieTools.createTrimmedMovie(filePath);
            MovieTools.writeMovieToFile(movie, FileTools.getExternalFile("output-video.mp4"));
        } catch (IOException cause) {
            Log.d(TAG, "Could not open video stream", cause);
        }
    }

    @Override
    public void encodeVideo(String filePath) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "video.h264");
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(file));
                Log.i(TAG, "OutputStream initialized");
            } catch (Exception e) {
                e.printStackTrace();
            }


            MediaExtractor extractor = new MediaExtractor();
            extractor.setDataSource(filePath);
            Log.d(TAG, String.format("TRACKS #: %d", extractor.getTrackCount()));
            MediaFormat format = extractor.getTrackFormat(0);
            String mime = format.getString(MediaFormat.KEY_MIME);
            Log.d(TAG, String.format("MIME TYPE: %s", mime));
            MediaCodec codec = MediaCodec.createEncoderByType(mime);
            MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", 320, 240);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2000000);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
            codec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            codec.start();


            int inputBufIndex = codec.dequeueInputBuffer(5000L);
            if (inputBufIndex >= 0) {
                ByteBuffer dstBuf = codec.getInputBuffer(inputBufIndex);

                int sampleSize = extractor.readSampleData(dstBuf, 0);
                long presentationTimeUs = 0;
                if (sampleSize < 0) {
                    sawInputEOS = true;
                    sampleSize = 0;
                } else {
                    presentationTimeUs = extractor.getSampleTime();
                }

                codec.queueInputBuffer(inputBufIndex,
                        0,
                        sampleSize,
                        presentationTimeUs,
                        sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
                if (!sawInputEOS) {
                    extractor.advance();
                }
            }


            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            final int res = codec.dequeueOutputBuffer(info, 5000L);
            if (res >= 0) {
                int outputBufIndex = res;
                ByteBuffer buf = codec.getOutputBuffer(outputBufIndex);

                final byte[] chunk = new byte[info.size];
                buf.get(chunk); // Read the buffer all at once
                buf.clear(); // ** MUST DO!!! OTHERWISE THE NEXT TIME YOU GET THIS SAME BUFFER BAD THINGS WILL HAPPEN

                if (chunk.length > 0) {
                    outputStream.write(chunk, 0, chunk.length);
                    Log.d(TAG, "Wrote: " + chunk.length);
                }
                codec.releaseOutputBuffer(outputBufIndex, false /* render */);

                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    sawOutputEOS = true;
                }
                codec.dequeueOutputBuffer(info, 0);
            }else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                final MediaFormat oformat = codec.getOutputFormat();
                Log.d(TAG, "Output format has changed to " + oformat);
            }

        } catch (IOException cause) {
            Log.d(TAG, "Could not set data source", cause);
        }
    }
}
