package com.example.minhy.seakingapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static String IP_ADDRESS = "192.168.43.181";
    private static String IP_HYEFI_ADDRESS = "192.168.43.87";
    private static String TAG = "phptest";
    private static String H = "httpconnection";
    private static String link;

    private TextView mTextViewResult;
    private ArrayList<PersonalData> mArrayList;
    private UsersAdapter mAdapter;
    private String mJsonString;
    private WebView webView_graph;
    private WebSettings webSettings_graph;
    private Toolbar myToolbar;
    private TextView mTextViewDate;
    private TextView mTextViewTemp;
    private TextView mTextViewpH;
    private ImageView imageView_temp;
    private ImageView imageView_temp_warn;
    private ImageView imageView_pH;
    private ImageView imageView_pH_warn;
    private String daytime;
    private int monthInt;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scroll);
        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.red_warning);
        imageView_temp = (ImageView) findViewById(R.id.imageView_Temp);
        imageView_temp_warn = (ImageView) findViewById(R.id.imageView_Temp_Warn);
        imageView_pH = (ImageView) findViewById(R.id.imageView_pH);
        imageView_pH_warn = (ImageView) findViewById(R.id.imageView_pH_Warn);


        //mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        //mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        mArrayList = new ArrayList<>();

        mAdapter = new UsersAdapter(this, mArrayList);
        webView_graph = (WebView) findViewById(R.id.webview_graph);
        webView_graph.setWebViewClient(new MyAppWebViewClient());
        webSettings_graph = webView_graph.getSettings();
        webSettings_graph.setJavaScriptEnabled(true);
        webSettings_graph.setBuiltInZoomControls(true);
        webView_graph.loadUrl("http://" + IP_ADDRESS + "/real_graph_2.php");

        Log.d("webView 생성?", "OK");

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        mTextViewDate = (TextView) findViewById(R.id.textView_ShowDate);
        mTextViewTemp = (TextView) findViewById(R.id.textView_ShowTemp);
        mTextViewpH = (TextView) findViewById(R.id.textView_ShowpH);

        //Jsondata
        GetData task = new GetData();
        task.execute("http://" + IP_ADDRESS + "/getjson_sensor.php", "");
//        task.execute("http://" + IP_HYEFI_ADDRESS + "/getjson_sensor.php", "");
        mHandler.sendEmptyMessage(0);



        FirebaseMessaging.getInstance().subscribeToTopic("News");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.d("newToken", newToken);
            }
        });

        Button button_streaming = (Button) findViewById(R.id.Streaming);
        button_streaming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //웹 뷰 연결하려다, URI 이용하여 바로 인터넷 접속으로 바꿈
//                setContentView(R.layout.webview);
//                webView = (WebView)findViewById(R.id.webview_login);
//                webView.setWebViewClient(new WebViewClient());
//                webSettings = webView.getSettings();
//                webSettings.setJavaScriptEnabled(true);
//                webView.loadUrl("http://192.168.43.161:29444/videostream.cgi?user=admin&pwd=88888888");

                //URI로 접속. Internet으로 연결하는 Intent.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.43.161:29444/pda.htm"));
                startActivity(intent);

            }
        });

        Button button_picture = (Button) findViewById(R.id.Picture);
        button_picture.setOnClickListener(new View.OnClickListener() {

            //사진 찍으면 보내주는 코드 작성
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CheckPictureMain.class);
                startActivity(intent);
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.first:
                ArrayList<PersonalData> personalData = new ArrayList<>();

                mAdapter.notifyDataSetChanged();

                for (int i = 0; i < mArrayList.size(); i++) {

                    personalData.add(mArrayList.get(i));
                    Log.d("mArrayList"+"get("+i+")",mArrayList.get(i).getMember_date());
                }

                Intent intent = new Intent(MainActivity.this, MonthMain.class);
                intent.putParcelableArrayListExtra("data", personalData);
                startActivity(intent);
                break;

            case R.id.second:
                ArrayList<PersonalData> personalData2 = new ArrayList<>();
                Update();
                mAdapter.notifyDataSetChanged();

                for (int i = 0; i < mArrayList.size(); i++) {

                    personalData2.add(mArrayList.get(i));
                }


                Intent intent2 = new Intent(MainActivity.this, SpecificMain.class);
                intent2.putParcelableArrayListExtra("data", personalData2);
                startActivity(intent2);
                break;

            case android.R.id.home:
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:112"));
                startActivity(intent1);

        }

        return true;
    }

    @Override
    public void onRefresh() {

        mAdapter.notifyDataSetChanged();

    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute()");

        }


        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "result값");
            super.onPostExecute(result);

            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null) {

                //mTextViewResult.setText(errorString);
            } else {

                Log.d(TAG, "Server");
                mJsonString = result;
                Log.d("json", "showresult 전");

                if(mArrayList.size()==0){
                    showResult();
                    checkTemp();
                }else {
                    Update();
                    checkTemp();
                }

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "country=" + params[1];

            Log.d(TAG, serverURL);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                Log.d(H, "post");

                httpURLConnection.setDoInput(true);
                Log.d(H, "true");
                httpURLConnection.connect();
                Log.d(H, "connected");


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();
                e.printStackTrace();

                return null;
            }

        }
    }

    private void showResult() {

        String TAG_JSON = "webnautes";
        String TAG_DATE = "date";
        String TAG_TEMP = "temperature";
        String TAG_PH = "pH";

        Log.d("jsonstart", "before try");


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            Log.d("jsonObject", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            Log.d("jsonArray", jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String date = item.getString(TAG_DATE);
                String temperature = item.getString(TAG_TEMP);
                String ph = item.getString(TAG_PH);
                String datetime = getDay(date);
                Log.d("json_date",date);
                Log.d("json_temp",temperature);
                Log.d("json_ph",ph);
                Log.d("json_datetime", datetime);

                final PersonalData personalData = new PersonalData();

                personalData.setMember_date(date);
                personalData.setMember_temp(temperature);
                personalData.setMember_ph(ph);
                personalData.setSp_date(datetime);
                Log.d("PersonalData",personalData.getMember_date());

                Log.d("sep_Date_Set", datetime);

                mTextViewDate.setText(datetime);
                mTextViewDate.setTypeface(mTextViewDate.getTypeface(), Typeface.BOLD);
                mTextViewTemp.setText(temperature);
                mTextViewpH.setText(ph);

                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
                Log.d("setTEXT2", date + "   " + temperature + "   " + ph);

                // 최근 시간대부터 뜨게 하기 위한 sort

                Collections.sort(mArrayList, new Comparator<PersonalData>() {
                    @Override
                    public int compare(PersonalData o1, PersonalData o2) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        int compareResult = 0;
                        try {
                            Date o1Date = format.parse(o1.getMember_date());
                            Date o2Date = format.parse(o2.getMember_date());
                            compareResult = o2Date.compareTo(o1Date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                            compareResult = o2.getMember_date().compareTo(o1.getMember_date());
                        }

                        return compareResult;
                    }
                });

                Log.d("setTEXT3", date + "   " + temperature + "   " + ph);
                mAdapter.notifyDataSetChanged();
                Log.d("setTEXT4", date + "   " + temperature + "   " + ph);
            }


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }

    //Update 함수 작업 중.

    private void Update() {

        String TAG_JSON = "webnautes";
        String TAG_DATE = "date";
        String TAG_TEMP = "temperature";
        String TAG_PH = "pH";

        Log.d("jsonstart UPDATEHYEM", "before try");


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            Log.d("jsonObject UPDATEHYEM", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            Log.d("jsonArray UPDATEHYEM", jsonArray.toString());
            Log.d("UPDATEHYEM jsonleng", String.valueOf(jsonArray.length()));
            Log.d("UPDATEHYEM marrays", String.valueOf(mArrayList.size()));


            for (int i = 0; i < jsonArray.length()-mArrayList.size(); i++) {

                JSONObject item = jsonArray.getJSONObject(jsonArray.length()-1);
                Log.d("UPDATEHYEM item", String.valueOf(item));

                String date = item.getString(TAG_DATE);
                String temperature = item.getString(TAG_TEMP);
                String ph = item.getString(TAG_PH);
                String datetime = getDay(date);

                Log.d("json_date UPDATEHYEM",date);
                Log.d("json_temp UPDATEHYEM ",temperature);
                Log.d("json_ph UPDATEHYEM ",ph);
                Log.d("json_dt UPDATEHYEM ", datetime);

                final PersonalData personalData = new PersonalData();

                personalData.setMember_date(date);
                personalData.setMember_temp(temperature);
                personalData.setMember_ph(ph);
                personalData.setSp_date(datetime);
                Log.d("PerDa UPDATEHYEM ",personalData.getMember_date());


                Log.d("sep_Date UPDATEHYEM ", datetime);

                mTextViewDate.setText(datetime);
                mTextViewDate.setTypeface(mTextViewDate.getTypeface(), Typeface.BOLD);
                mTextViewTemp.setText(temperature);
                mTextViewpH.setText(ph);

                Log.d("HYEM",personalData.getMember_date());
                mArrayList.add(personalData);
                Log.d("UPDATEHYEM AR-1",mArrayList.get(jsonObject.length()-1).getMember_date());
                Log.d("UPDATEHYEM AR",mArrayList.get(jsonObject.length()).getMember_date());
                Collections.sort(mArrayList, new Comparator<PersonalData>() {
                    @Override
                    public int compare(PersonalData o1, PersonalData o2) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        int compareResult = 0;
                        try {
                            Date o1Date = format.parse(o1.getMember_date());
                            Date o2Date = format.parse(o2.getMember_date());
                            compareResult = o2Date.compareTo(o1Date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                            compareResult = o2.getMember_date().compareTo(o1.getMember_date());
                        }

                        return compareResult;
                    }
                });
                mAdapter.notifyDataSetChanged();
                Log.d("setTEXT2 UPDATEHYEM ", date + "   " + temperature + "   " + ph);
                }


        } catch (JSONException e) {

            Log.d(TAG, "showResult UPDATEHYEM : ", e);
        }



    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //GetData를 통해 계속 데이터 가져오기.
            GetData task = new GetData();
            task.execute("http://" + IP_ADDRESS + "/getjson_sensor.php", "");
//            task.execute("http://" + IP_HYEFI_ADDRESS + "/getjson_sensor.php", "");
            mAdapter.notifyDataSetChanged();
            mHandler.sendEmptyMessageDelayed(0,500);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView_graph.canGoBack()) {
            webView_graph.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    public String getDay(String string) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String day;

        try {
            Date convertedDate = formatter.parse(string);
            day = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(convertedDate);
            daytime = day;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return daytime;
    }

    public int getMonth(String string) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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


    public void checkTemp() {

        float temper;
        float pH;
        temper = Float.parseFloat(mArrayList.get(0).getMember_temp());
        pH = Float.parseFloat(mArrayList.get(0).getMember_ph());


        if (getMonth(mArrayList.get(0).getMember_date()) == 3 | getMonth(mArrayList.get(0).getMember_date()) == 4 | getMonth(mArrayList.get(0).getMember_date()) == 5) {
            if (temper > 14 | temper < 5) {

                mTextViewTemp.setTextColor(0xAAFF0000);
                imageView_temp.setVisibility(View.GONE);
                imageView_temp_warn.setVisibility(View.VISIBLE);

            }else{

                mTextViewTemp.setTextColor(0xAA000000);
                imageView_temp.setVisibility(View.VISIBLE);
                imageView_temp_warn.setVisibility(View.GONE);

            }
        } else if (getMonth(mArrayList.get(0).getMember_date()) == 6 | getMonth(mArrayList.get(0).getMember_date()) == 7 | getMonth(mArrayList.get(0).getMember_date()) == 8) {
            if (temper > 27 | temper < 15) {

                mTextViewTemp.setTextColor(0xAAFF0000);
                imageView_temp.setVisibility(View.GONE);
                imageView_temp_warn.setVisibility(View.VISIBLE);

            }else{

                mTextViewTemp.setTextColor(0xAA000000);
                imageView_temp.setVisibility(View.VISIBLE);
                imageView_temp_warn.setVisibility(View.GONE);

            }
        } else if (getMonth(mArrayList.get(0).getMember_date()) == 9 | getMonth(mArrayList.get(0).getMember_date()) == 10 | getMonth(mArrayList.get(0).getMember_date()) == 11) {
            if (temper > 25 | temper < 13) {

                mTextViewTemp.setTextColor(0xAAFF0000);
                imageView_temp.setVisibility(View.GONE);
                imageView_temp_warn.setVisibility(View.VISIBLE);

            }else{

                mTextViewTemp.setTextColor(0xAA000000);
                imageView_temp.setVisibility(View.VISIBLE);
                imageView_temp_warn.setVisibility(View.GONE);

            }
        } else if (getMonth(mArrayList.get(0).getMember_date()) == 12 | getMonth(mArrayList.get(0).getMember_date()) == 1 | getMonth(mArrayList.get(0).getMember_date()) == 2) {
            if (temper > 12 | temper < 3) {

                mTextViewTemp.setTextColor(0xAAFF0000);
                imageView_temp.setVisibility(View.GONE);
                imageView_temp_warn.setVisibility(View.VISIBLE);

            }else{

                mTextViewTemp.setTextColor(0xAA000000);
                imageView_temp.setVisibility(View.VISIBLE);
                imageView_temp_warn.setVisibility(View.GONE);

            }
        }

        if (pH < 7 | pH > 8) {
            mTextViewpH.setTextColor(0xAAFF0000);
            imageView_pH.setVisibility(View.GONE);
            imageView_pH_warn.setVisibility(View.VISIBLE);
        } else {
            mTextViewpH.setTextColor(0xAA000000);
            imageView_pH.setVisibility(View.VISIBLE);
            imageView_pH_warn.setVisibility(View.GONE);
        }

    }

}

