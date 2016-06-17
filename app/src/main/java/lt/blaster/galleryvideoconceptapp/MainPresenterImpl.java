package lt.blaster.galleryvideoconceptapp;

import android.support.annotation.NonNull;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public class MainPresenterImpl implements MainPresenter {
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
}
