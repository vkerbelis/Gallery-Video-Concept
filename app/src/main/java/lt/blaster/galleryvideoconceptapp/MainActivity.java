package lt.blaster.galleryvideoconceptapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MainPresenter presenter;
    private MainView rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();
    }

    private void setUpView() {
        rootView = (MainView) findViewById(R.id.rootView);
        presenter = new MainPresenterImpl();
        rootView.setPresenter(presenter);
    }
}
