package ru.gressor.weatherapp.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import ru.gressor.weatherapp.R;
import ru.gressor.weatherapp.data_types.local_dto.PositionPoint;

public class SelectTownActivity extends AppCompatActivity {
    public static final int GET_TOWN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_town);

        initTownList();
        initClickListeners();
    }

    private void initTownList() {
        ListView townList = findViewById(R.id.townList);
        townList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.town_list,
                android.R.layout.simple_list_item_single_choice);

        townList.setAdapter(adapter);
    }
    private void initClickListeners() {
        findViewById(R.id.buttonSelectTown).setOnClickListener(buttonSelectTownOnClickListener);
        findViewById(R.id.imageViewClear).setOnClickListener(imageViewClearOnClickListener);
        findViewById(R.id.imageViewBack).setOnClickListener(imageViewBackOnClickListener);
    }

    private View.OnClickListener imageViewBackOnClickListener = (v) -> {
        setResult(RESULT_CANCELED, null);
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
            Snackbar.make(findViewById(R.id.townList),
                    R.string.no_town_selected, Snackbar.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        PositionPoint selectedPosition = new PositionPoint(result, "");
        intent.putExtra(PositionPoint.CURRENT_POSITION, selectedPosition);
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
