package lt.blaster.galleryvideoconceptapp.tools;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public class AndroidIntentCreator implements IntentCreator {

    @NonNull
    @Override
    public Intent createVideoPickerIntent(String title) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, title);
    }
}
