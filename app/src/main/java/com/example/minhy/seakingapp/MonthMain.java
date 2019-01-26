package com.example.minhy.seakingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MonthMain extends AppCompatActivity {

    private ArrayList<PersonalData> personalData;
    private ArrayList<PersonalData> msetpersonalData;
    private TextView mTextViewResult;
    private UsersAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Toolbar myToolbar;
    int monthInt;
    final int[] selectedIndex = {0};
    final String[] months = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    int selectedmonth;
    float temperMIN;
    float temperMAX;
    float phMIN;
    float phMAX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_main);

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        temperMAX = 0;
        temperMIN = 0;
        phMAX = 8;
        phMIN = 7;

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        msetpersonalData = new ArrayList<>();
        mAdapter = new UsersAdapter(this, msetpersonalData);
        mRecyclerView.setAdapter(mAdapter);



        msetpersonalData.clear();
        mAdapter.notifyDataSetChanged();


        Log.d("두번째 액티비티", "생성완료");

        Intent intent = getIntent();
        personalData = intent.getParcelableArrayListExtra("data");

        mAdapter = new UsersAdapter(this, msetpersonalData);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);


        for (int i = 0; i < personalData.size(); i++) {
            Log.d("두번째 액티비티 날짜", personalData.get(i).getMember_date());
            personalData.get(i).setMember_month(getMonth(personalData.get(i).getMember_date()));
            Log.d("두번째 액티비티 달", String.valueOf(personalData.get(i).getMember_month()));
            Log.d("두번째 액티비티 온도", personalData.get(i).getMember_temp());
        }

        Button button_month = (Button) findViewById(R.id.button_month);
        button_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MonthMain.this);
                dialog.setTitle("달을 선택하세요")
                        .setSingleChoiceItems(months, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedIndex[0] = which;
                                Log.d("싱글초이스1", String.valueOf(which));
                                Log.d("싱글초이스2", String.valueOf(selectedIndex[0]));

                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                msetpersonalData.clear();
                                Toast.makeText(MonthMain.this, months[selectedIndex[0]], Toast.LENGTH_SHORT).show();
                                Log.d("클릭 후", String.valueOf(months[selectedIndex[0]]));
                                selectedmonth = Integer.parseInt(months[selectedIndex[0]]);
                                Log.d("int로 보내고싶다", String.valueOf(selectedmonth));

                                setMINMAX(selectedmonth);
                                Log.d("setMIN,MAX",String.valueOf(temperMAX));
                                Log.d("setMIN,MAX",String.valueOf(temperMIN));

                                for (int i = 0; i < personalData.size(); i++) {

                                    Log.d("달 보여주기", String.valueOf(personalData.get(i).getMember_month()));
                                    Log.d("인덱스를 봐보자", String.valueOf(selectedmonth));

                                    if (selectedmonth == personalData.get(i).getMember_month()) {

                                        String date = personalData.get(i).getMember_date();
                                        String temperature = personalData.get(i).getMember_temp();
                                        String ph = personalData.get(i).getMember_ph();

                                        PersonalData setpersonalData = new PersonalData();

                                        setpersonalData.setMember_date(date);
                                        setpersonalData.setMember_temp(temperature);
                                        setpersonalData.setMember_ph(ph);
                                        setpersonalData.setTemper_MIN(temperMIN);
                                        setpersonalData.setTemper_MAX(temperMAX);
                                        setpersonalData.setPhMAX(phMAX);
                                        setpersonalData.setPhMIN(phMIN);

                                        msetpersonalData.add(setpersonalData);
                                        mAdapter.notifyDataSetChanged();


                                    }
                                }
                            }
                        }).create().show();

            }

        });
    }

    public int getMonth(String string) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String month;

        try {
            Date convertedDate = formatter.parse(string);
            month = new SimpleDateFormat("MM").format(convertedDate);
            monthInt = Integer.parseInt(month);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return monthInt;
    }

    public void setMINMAX(Integer month){

        if (month == 3 | month == 4 | month == 5) {
            temperMIN = 5;
            temperMAX = 14;
        }

        if (month == 6 | month == 7 | month == 8) {
            temperMIN = 15;
            temperMAX = 27;
        }
        if (month == 9 | month == 10 | month == 11) {
            temperMIN = 13;
            temperMAX = 25;
        }
        if (month == 12 | month == 1 | month == 2) {
            temperMIN = 3;
            temperMAX = 12;
        }

        phMIN = 7;
        phMAX = 8;

    }
}

