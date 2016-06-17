package lt.blaster.galleryvideoconceptapp.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import lt.blaster.galleryvideoconceptapp.R;
import lt.blaster.galleryvideoconceptapp.tools.IntentCreator;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public class MainViewImpl extends FrameLayout implements MainView, View.OnClickListener {
    private MainPresenter presenter;
    private IntentCreator intentCreator;
    private VideoView videoView;

    public MainViewImpl(Context context) {
        super(context);
    }

    public MainViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(@NonNull MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO && resultCode == Activity.RESULT_OK) {
            resolveVideoIntent(data);
        }
    }

    private void resolveVideoIntent(Intent data) {
        videoView.setVideoURI(data.getData());
        videoView.start();
    }

    @Override
    public void setIntentCreator(@NonNull IntentCreator intentCreator) {
        this.intentCreator = intentCreator;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.galleryButton).setOnClickListener(this);
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            presenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View view) {
        Activity activity = ((Activity) getContext());
        String title = activity.getString(R.string.title_pick_video);
        Intent intent = intentCreator.createVideoPickerIntent(title);
        activity.startActivityForResult(intent, REQUEST_VIDEO);
    }
}
