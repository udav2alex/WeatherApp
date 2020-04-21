package ru.gressor.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SelectTown extends AppCompatActivity {
    public static final int GET_TOWN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_town);

        initTownList();

        Button buttonSelectTown = findViewById(R.id.buttonSelectTown);
        buttonSelectTown.setOnClickListener(buttonSelectTownOnClickListener);

        ImageView imageViewClear = findViewById(R.id.imageViewClear);
        imageViewClear.setOnClickListener(imageViewClearOnClickListener);

        ImageView imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(imageViewBackOnClickListener);
    }

    private void initTownList() {
        ListView townList = findViewById(R.id.townList);
        townList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.town_list,
                android.R.layout.simple_list_item_single_choice);

        townList.setAdapter(adapter);
    }

    private View.OnClickListener imageViewBackOnClickListener = (v) -> {
        setResult(RESULT_OK, null);
        finish();
    };

    private View.OnClickListener imageViewClearOnClickListener = (v) -> {
        EditText editTextTown = findViewById(R.id.editTextTown);
        editTextTown.setText("");
    };

    private View.OnClickListener buttonSelectTownOnClickListener = (v) -> {
        EditText editTextTown = findViewById(R.id.editTextTown);
        String text = editTextTown.getText().toString().trim();

        if (text.equals("")) {
            passResult(getTownFromList());
        } else {
            passResult(text);
        }
    };

    private void passResult(String result) {
        if (result == null) {
            Toast.makeText(this, R.string.no_town_selected, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        DestinationPoint destinationPoint = new DestinationPoint(result, "");
        intent.putExtra("destinationPoint", destinationPoint);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String getTownFromList() {
        ListView townList = findViewById(R.id.townList);
        int position = townList.getCheckedItemPosition();

        if (position != AdapterView.INVALID_POSITION)
            return townList.getAdapter().getItem(position).toString();

        return null;
    }
}
