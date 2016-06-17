package lt.blaster.galleryvideoconceptapp.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 16.6.17.
 */
public class MainViewImpl extends FrameLayout implements MainView {
    private MainPresenter presenter;

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
    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }
}
