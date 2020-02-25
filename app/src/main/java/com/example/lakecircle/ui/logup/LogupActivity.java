package com.example.lakecircle.ui.logup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lakecircle.R;

public class LogupActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private String mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);

        mSpinner = findViewById(R.id.logup_spinner);

        initView();
    }

    private void initView() {

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this, R.array.pwd_question, android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mQuestion = mSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
