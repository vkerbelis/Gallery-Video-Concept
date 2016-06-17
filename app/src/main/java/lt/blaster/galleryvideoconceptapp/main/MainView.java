package lt.blaster.galleryvideoconceptapp.main;

import android.support.annotation.NonNull;

import lt.blaster.galleryvideoconceptapp.PresenterView;
import lt.blaster.galleryvideoconceptapp.tools.IntentCreator;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public interface MainView extends PresenterView<MainPresenter> {
    void setIntentCreator(@NonNull IntentCreator intentCreator);
}
