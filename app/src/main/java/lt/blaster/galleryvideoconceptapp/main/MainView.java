package lt.blaster.galleryvideoconceptapp.main;

import android.content.Intent;
import android.support.annotation.NonNull;

import lt.blaster.galleryvideoconceptapp.PresenterView;
import lt.blaster.galleryvideoconceptapp.tools.IntentCreator;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public interface MainView extends PresenterView<MainPresenter> {
    int REQUEST_VIDEO = 7;

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void setIntentCreator(@NonNull IntentCreator intentCreator);
}
