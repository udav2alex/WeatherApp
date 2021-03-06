package ru.gressor.weatherapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.gressor.weatherapp.R;

public class ErrorMessageActivity extends AppCompatActivity {
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_message);

        Intent intent = getIntent();
        ((TextView)findViewById(R.id.textViewProviderMessage))
                .setText(intent.getStringExtra(ERROR_MESSAGE));

        findViewById(R.id.buttonCloseErrorMessage).setOnClickListener(onClickListenerButtonClose);
    }

    private View.OnClickListener onClickListenerButtonClose = v -> finish();
}
