package com.example.valcalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ArchiveActivity extends AppCompatActivity {
    String finalStr = "";
    ArrayList<String> histList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

//
        File internalStorageDir = getFilesDir();
        File hist = new File(internalStorageDir, "hist.txt");
        try {
            FileInputStream inputStream = new FileInputStream(hist);
            int data = inputStream.read();
            char content;
            while(data != -1) {
                content = (char) data;
                if (content!='\n')
                    finalStr+=content;
                else{
                    histList.add(finalStr);
                    finalStr = "";
                }
                data = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ArchiveActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ArchiveActivity.this, ConverterActivity.class);
                startActivity(intent);
            }
        });

        ListView lv = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, histList);

        lv.setAdapter(adapter);
    }
}
