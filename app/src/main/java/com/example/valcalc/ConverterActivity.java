package com.example.valcalc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

import okhttp3.*;

public class ConverterActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private Boolean check1 = false;
    private Rest val;
    private String finalString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        mTextViewResult = findViewById(R.id.text_view_result1);
        setData();
        OkHttpClient client = new OkHttpClient();
        final String url = "https://www.cbr-xml-daily.ru/daily_json.js";
        final String url1 = "https://www.cbr-xml-daily.ru/archive/" ;
        final String url2 = "/daily_json.js";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    JSONtoValute JSONtest = new JSONtoValute();
                    val = JSONtest.getValute(myResponse);
                    check1 = true;

                }
            }
        });
        try {
            while (!check1) {
                Thread.sleep(100);
            }
            check1 = false;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView data = findViewById(R.id.textView);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(ConverterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TextView tv = findViewById(R.id.textView);
                        String str = String.valueOf(dayOfMonth) + '/' + String.valueOf(month + 1) + '/' + String.valueOf(year);
                        tv.setText(str);
                    }

                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        data.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                OkHttpClient client = new OkHttpClient();
                final ArrayList<Integer> list = parseDate(String.valueOf(s));
                String day;
                String month;
                if (list.get(1) < 10) {
                    month = "0" + String.valueOf(list.get(1));
                } else month = String.valueOf(list.get(1));
                if (list.get(0) < 10) {
                    day = "0" + String.valueOf(list.get(0));
                } else day = String.valueOf(list.get(0));

                String URL = url1 + String.valueOf(list.get(2)) + "/" + month + "/" + day + url2;

                requestTo(URL);

                try {
                    while (!check1) {
                        Thread.sleep(100);
                    }
                    check1 = false;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        EditText edt = findViewById(R.id.editText);
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //calc();
            }
        });

        Spinner sp = (Spinner) findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // calc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//Another interface callback
            }
        });


        Spinner sp1 = (Spinner) findViewById(R.id.spinner2);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //calc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//Another interface callback
            }
        });

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ConverterActivity.this, ArchiveActivity.class);
                startActivity(intent);
            }
        });

        Button buttonM = findViewById(R.id.buttonM);
        buttonM.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ConverterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                calc();
            }
        });
    }
    private void calc() {
        Spinner sp1 = findViewById(R.id.spinner);
        Spinner sp2 = findViewById(R.id.spinner2);
        EditText txt1 = findViewById(R.id.editText);
        TextView txt2 = findViewById(R.id.textView2);
        TextView txtData = findViewById(R.id.textView);
        float valEUR = Float.valueOf(val.Valute.EUR.Value);
        float valUSD = Float.valueOf(val.Valute.USD.Value);
        float valJPY = Float.valueOf(val.Valute.JPY.Value) / 100;
        float caefin = 0f;
        float caefout = 0f;
        if (String.valueOf(sp1.getSelectedItem()).equals("RUB")) {
            caefin = 1f;
        }
        if (String.valueOf(sp1.getSelectedItem()).equals("USD")) {
            caefin = valUSD;
        }
        if (String.valueOf(sp1.getSelectedItem()).equals("EUR")) {
            caefin = valEUR;
        }
        if (String.valueOf(sp1.getSelectedItem()).equals("JPY")) {
            caefin = valJPY;
        }
        if (String.valueOf(sp2.getSelectedItem()).equals("RUB")) {
            caefout = 1f;
        }
        if (String.valueOf(sp2.getSelectedItem()).equals("USD")) {
            caefout = valUSD;
        }
        if (String.valueOf(sp2.getSelectedItem()).equals("EUR")) {
            caefout = valEUR;
        }
        if (String.valueOf(sp2.getSelectedItem()).equals("JPY")) {
            caefout = valJPY;
        }

        Integer inV;

        if (String.valueOf(txt1.getText()).equals("")) {
            return;
        }

        Float kurs = Integer.valueOf(String.valueOf(txt1.getText())) * caefin / caefout;

        txt2.setText(String.valueOf(kurs));

        finalString = String.valueOf(txtData.getText())+" "+
                String.valueOf(txt1.getText())+" "+String.valueOf(sp1.getSelectedItem())+"<-->"+
                String.valueOf(kurs)+" "+String.valueOf(sp2.getSelectedItem())+"\n";

        File internalStorageDir = getFilesDir();
        File hist = new File(internalStorageDir, "hist.txt");
        int count = 0;
        try {
            FileInputStream fis = new FileInputStream(hist);
            int data = fis.read();
            char content;
            while(data != -1) {
                content = (char) data;
                finalString+=content;
                if (content == '\n')
                    count++;
                if (count == 9)
                    break;
                data = fis.read();
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(hist);
            fos.write(finalString.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setData(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String data = String.valueOf(day)+'/'+String.valueOf(month)+'/'+String.valueOf(year);
        TextView dat = findViewById(R.id.textView);
        dat.setText(data);
    }


    private ArrayList<Integer> parseDate(String string) {
        ArrayList<Integer> list = new ArrayList<>();
        String[] strList;
        strList = string.split("/");
        for (int i = 0; i < strList.length; i++) {
            list.add(Integer.valueOf(strList[i]));
        }

        return list;
    }

    private ArrayList<Integer> downgrade(int day, int month, int year) {
        int nDay = day;
        int nMonth = month;
        int nYear = year;

        if (day > 1) {
            nDay = day - 1;
        } else {
            if (month > 1) {
                Calendar c = Calendar.getInstance();
                c.set(year, month - 1, day);
                nDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                nMonth = month - 1;
            } else {
                nYear = year - 1;
                nDay = 31;
                nMonth = 12;
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        list.add(nDay);
        list.add(nMonth);
        list.add(nYear);
        return list;
    }

    private void requestTo(final String URL) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (String.valueOf(response.code()).equals("200")) {
                    String myResponse = response.body().string();

                    JSONtoValute JSONtest = new JSONtoValute();
                    val = JSONtest.getValute(myResponse);
                    check1 = true;

                } else {
                    ArrayList<Integer> list = new ArrayList<>();
                    String[] strMass = URL.split("/");

                    int iyear = Integer.valueOf(strMass[4]);
                    int imonth = Integer.valueOf(strMass[5]);
                    int iday = Integer.valueOf(strMass[6]);
                    list = downgrade(iday, imonth, iyear);
                    String day;
                    String month;
                    String year = String.valueOf(list.get(2));

                    if (list.get(1) < 10) {
                        month = "0" + String.valueOf(list.get(1));
                    } else month = String.valueOf(list.get(1));
                    if (list.get(0) < 10) {
                        day = "0" + String.valueOf(list.get(0));
                    } else day = String.valueOf(list.get(0));
                    final String url1 = "https://www.cbr-xml-daily.ru/archive/" ;
                    final String url2 = "/daily_json.js";

                    String NURL = url1 + year + "/" + month + "/" + day + url2;
                    requestTo(NURL);


                }
            }
        });
    }
}