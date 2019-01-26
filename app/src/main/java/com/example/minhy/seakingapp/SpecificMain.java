package com.example.minhy.seakingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SpecificMain extends AppCompatActivity {


    private String TAG = "hehehe";
    private ArrayList<PersonalData> personalData;
    private ArrayList<PersonalData> msetpersonalData;
    private TextView mTextViewResult;
    private TextView TextView_temp;
    private UsersAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Toolbar myToolbar;
    String date;
    String msg;
    int monthInt;
    float temperMIN;
    float temperMAX;
    float phMIN;
    float phMAX;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_main);

        final Calendar cal = Calendar.getInstance();

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        TextView_temp = (TextView)findViewById(R.id.textView_list_temp);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        msetpersonalData = new ArrayList<>();
        msetpersonalData.clear();

        Intent intent = getIntent();
        personalData = intent.getParcelableArrayListExtra("data");

        mAdapter = new UsersAdapter(this, msetpersonalData);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);


        for (int i = 0; i < personalData.size(); i++) {
            personalData.get(i).setSp_date(getDate(personalData.get(i).getMember_date()));
            Log.d(TAG+"날짜 잘 나오니?",personalData.get(i).getSp_date());
        }

        Button button_month = (Button) findViewById(R.id.button_date);
        button_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog dialog = new DatePickerDialog(SpecificMain.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String msg = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
                        Toast.makeText(SpecificMain.this, msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG + "1", msg);
                        monthInt = month + 1;

                        setMINMAX(monthInt);

                        Log.d("Sp_date & msg", msg);

                        for(int i =0; i<personalData.size(); i++){

                            Log.d("Sp_date",personalData.get(i).getSp_date());

                            if(msg.equals(personalData.get(i).getSp_date())){
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

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();

                msetpersonalData.clear();

            }
        });

    }


    public String getDate(String string) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            Date convertedDate = formatter.parse(string);
            date = new SimpleDateFormat("yyyy-MM-dd").format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
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
