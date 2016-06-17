package lt.blaster.galleryvideoconceptapp.tools;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public interface IntentCreator {
    @NonNull
    Intent createVideoPickerIntent(String title);
}
