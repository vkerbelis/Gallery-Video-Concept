package lt.blaster.galleryvideoconceptapp;

import android.support.annotation.NonNull;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public interface Presenter<V> {
    void takeView(@NonNull V view);

    void dropView(@NonNull V view);

    boolean hasView();
}
