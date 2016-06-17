package lt.blaster.galleryvideoconceptapp;

import android.support.annotation.NonNull;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public interface PresenterView<P> {
    void setPresenter(@NonNull P presenter);
}
