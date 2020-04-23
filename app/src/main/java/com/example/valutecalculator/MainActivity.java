package com.example.valutecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();

    }

    private void addListenerOnButton() {
        Button calculator = (Button) findViewById(R.id.CalculatorButton);
        Button rate = (Button) findViewById(R.id.RateButton);
        Button history = (Button) findViewById(R.id.historyButton);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(".RateActivity");
                startActivity(intent1);
            }
        });

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(".CalculatorActivity");
                startActivity(intent2);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(".HistoryActivity");
                startActivity(intent3);
            }
        });
    }
}
