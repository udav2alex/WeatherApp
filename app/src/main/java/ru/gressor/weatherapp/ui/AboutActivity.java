package ru.gressor.weatherapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import ru.gressor.weatherapp.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setFinishOnTouchOutside(true);
        findViewById(R.id.buttonCloseAbout).setOnClickListener(onClickListenerButtonClose);
    }

    private View.OnClickListener onClickListenerButtonClose = v -> finish();
}
