package lt.blaster.galleryvideoconceptapp.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.googlecode.mp4parser.authoring.Movie;

import java.io.IOException;

import lt.blaster.galleryvideoconceptapp.PresenterView;
import lt.blaster.galleryvideoconceptapp.tools.MovieTools;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public class MainPresenterImpl implements MainPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private PresenterView view;

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
        } catch (IOException cause) {
            Log.d(TAG, "Could not open video stream", cause);
        }
    }
}
