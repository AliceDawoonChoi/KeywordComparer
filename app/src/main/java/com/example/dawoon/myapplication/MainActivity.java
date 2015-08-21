package com.example.dawoon.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.os.AsyncTask;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

   // ArrayList<ResultListView> arr_result;

    // db 연결
    SQLiteDatabase db;
    Cursor cursor;
    String tableName = "searchResult";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // db 오픈
            db = openOrCreateDatabase("searchResult", MODE_PRIVATE, null);

            // 테이블 생성
            // id, firstkey, secondkey, result1, result2, date8
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "id integer PRIMARY KEY autoincrement, " + "firstkey text NOT NULL," +
                    "secondkey text NOT NULL, " + "result1 integer NOT NULL, " +
                    " result2 integer NOT NULL, " + " date date)";
            db.execSQL(sql);

            ArrayList<String> arr_SearchResult = new ArrayList<String>();

            //result_List = new ArrayList<ResultListView>();
            // ResultListView result_List;


            //db 불러와서 값 넣기.

            cursor = db.rawQuery("SELECT * FROM searchResult ORDER BY date", null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String firstKey = cursor.getString(1);
                String secondKey = cursor.getString(2);
                int result1 = cursor.getInt(3);
                int result2 = cursor.getInt(4);
                String date = cursor.getString(5);

                //db 불러와서 값 넣기.
                arr_SearchResult.add(date + firstKey + " " + result1 + secondKey + " " + result2);
            }
            cursor.close();


            // 어댑터 준비
            ArrayAdapter<String> resultAdapter;
            resultAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arr_SearchResult);

            // 어댑터 연결
            ListView result_list = (ListView) findViewById(R.id.search_listView);
            result_list.setAdapter(resultAdapter);

            // 리스트뷰 속성
            // 항목을 선택하는 모드
            result_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // 항목 사이의 구분선(검정)
            result_list.setDivider(new ColorDrawable(Color.BLACK));
            // 구분선 높이 지정
            result_list.setDividerHeight(2);

            // 검색 키들.
            TextView textview_FirstKeyword = (TextView) findViewById(R.id.firstKey);
            TextView textview_SecondKeyword = (TextView) findViewById(R.id.secondKey);

            Intent intent1 = getIntent();

            String firstKeyword = intent1.getStringExtra("FirstKeyword");
            String secondKeyword = intent1.getStringExtra("SecondKeyword");

            textview_FirstKeyword.setText(String.valueOf(firstKeyword));
            textview_SecondKeyword.setText(String.valueOf(secondKeyword));
            // 검색 키 관련 끝
        } catch (Exception e){
            e.toString();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyHttpTask extends AsyncTask<String, Integer, String> {

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String mSearchStr = params[0];
                String searchStr = URLEncoder.encode(mSearchStr, "UTF-8");
                String bingUrl = "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27web%27&Query=%27" + searchStr + "%27&$top=1&$format=json";

                String accountKey = "r09ZcqXB+F795vzZwVFmZwsXF1/+xHOD6F6iM+iWD/s=";
                String accountKeyEnc = Base64.encodeToString((accountKey + ":" + accountKey).getBytes(), Base64.NO_WRAP);

                URL url = null;
                url = new URL(bingUrl);

                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
                InputStream response = urlConnection.getInputStream();
                String res = readStream(response);

                JSONObject json = new JSONObject(res);
                return json.getJSONObject("d").getJSONArray("results").getJSONObject(0).getString("WebTotal");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("###### HTTP result", s);
            super.onPostExecute(s);
        }
    }

    public void compareOnClick(View v) throws IOException {

        EditText text_FirstKeyword = null;
        text_FirstKeyword = (EditText) findViewById(R.id.firstKey);
        EditText text_SecondKeyword = null;
        text_SecondKeyword = (EditText) findViewById(R.id.secondKey);

        String firstKeyword1 = text_FirstKeyword.getText().toString();
        String secondkeyword1 = text_SecondKeyword.getText().toString();


        Toast.makeText(getApplicationContext(), "Search 뭐시기뭐시기", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));

        intent.putExtra("FirstKeyword", firstKeyword1);
        intent.putExtra("SecondKeyword", secondkeyword1);

        // startActivity(intent);

        (new MyHttpTask()).execute(firstKeyword1);

        // 검색 결과
//        String googleURL = "http://www.google.com/search?q=";
//
////        googleURL = googleURL + URLEncoder.encode(firstKeyword1 + "&btnG=Search&meta=", "utf-8");
//        googleURL = googleURL + firstKeyword1;
//        URL search_result = new URL(googleURL);
//
//
//        HttpURLConnection connection = null;
//        /*
//        BufferedReader bin = new BufferedReader(new InputStreamReader(search_result.openStream()));
//        String line;
//        while ((line = bin.readLine()) != null)
//        {
//            System.out.println(line);
//        }
//        bin.close();
//*/
//
//        String result11 = null;
//        String result22 = null;
//
//        connection = (HttpURLConnection) search_result.openConnection();
//        connection.setRequestMethod("GET");
//        connection.setConnectTimeout(3000);
//        connection.setReadTimeout(3000);
//        InputStream inputStream = null;
//
//
//        try {
//            connection.connect();
//        }catch(Exception e)
//        {
//            e.toString();
//        }
//
//        //if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
//        try {
//            if (connection != null) {
//                inputStream = new BufferedInputStream(connection.getInputStream());
//                result11 = inputStream.toString().replaceAll("[^0-9]", "");
//                //}
//            }
//        }catch (Exception e)
//        {
//            e.toString();
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
//        googleURL= "http://www.google.com/search?q=";
//
//        googleURL = googleURL + URLEncoder.encode(secondkeyword1 + "&btnG=Search&meta=", "utf-8");
//        search_result = new URL(googleURL);
//
//
//        connection = null;
//        connection = (HttpURLConnection) search_result.openConnection();
//        connection.setRequestMethod("GET");
//        connection.setConnectTimeout(3000);
//        connection.setReadTimeout(3000);
//
//        inputStream = null;
//
//        //connection.connect();
//
//     try {
//        if (connection != null) {
//            inputStream = connection.getInputStream();
//            result11 = inputStream.toString().replaceAll("[^0-9]", "");
//            //}
//        }
//    }catch (Exception e)
//    {
//        e.toString();
//        Log.e("Error", e.getMessage());
//        e.printStackTrace();
//    }


//        //db로 저장하기
//        String sql = "insert into " + tableName + " values( '" + firstKeyword1 +
//                "', '" + secondkeyword1 + "', " + result11 + ", " + result22 + ");";
//        db.execSQL(sql);
//
//        // db 다시 불러오기
//        cursor = db.rawQuery("SELECT * FROM searchResult ORDER BY _date", null);
//
//        while(cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String firstKey = cursor.getString(1);
//            String secondKey = cursor.getString(2);
//            int result1 = cursor.getInt(3);
//            int result2 = cursor.getInt(4);
//            String date = cursor.getString(5);
//
//            //db 불러와서 값 넣기.
//           // arr_SearchResult.add(date + firstKey + " " + result1 + secondKey + " " + result2);
//
//        }
//        cursor.close();
    }

}
