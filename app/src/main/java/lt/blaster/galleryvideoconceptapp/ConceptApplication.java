package lt.blaster.galleryvideoconceptapp;

import android.app.Application;
import android.content.Context;

import lt.blaster.galleryvideoconceptapp.tools.AndroidIntentCreator;
import lt.blaster.galleryvideoconceptapp.tools.IntentCreator;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public class ConceptApplication extends Application {
    private IntentCreator intentCreator;

    @Override
    public void onCreate() {
        super.onCreate();
        intentCreator = new AndroidIntentCreator();
    }

    public static IntentCreator getIntentCreator(Context context) {
        return ((ConceptApplication) context.getApplicationContext()).intentCreator;
    }
}
