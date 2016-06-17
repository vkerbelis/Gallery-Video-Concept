package lt.blaster.galleryvideoconceptapp.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import lt.blaster.galleryvideoconceptapp.ConceptApplication;
import lt.blaster.galleryvideoconceptapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpView() {
        MainView rootView = (MainView) findViewById(R.id.rootView);
        MainPresenter presenter = new MainPresenterImpl();
        rootView.setPresenter(presenter);
        rootView.setIntentCreator(ConceptApplication.getIntentCreator(this));
    }
}
