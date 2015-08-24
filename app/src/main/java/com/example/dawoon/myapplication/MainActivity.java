package com.example.dawoon.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {

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

    private SearchResult searchResult = null;

    public void compareOnClick(View v) throws IOException {

        EditText text_FirstKeyword = null;
        text_FirstKeyword = (EditText) findViewById(R.id.firstKey);
        EditText text_SecondKeyword = null;
        text_SecondKeyword = (EditText) findViewById(R.id.secondKey);

        String firstKeyword = text_FirstKeyword.getText().toString();
        String secondkeyword = text_SecondKeyword.getText().toString();

//        Toast.makeText(getApplicationContext(), "Search 뭐시기뭐시기", Toast.LENGTH_LONG).show();
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//
//        intent.putExtra("FirstKeyword", firstKeyword);
//        intent.putExtra("SecondKeyword", secondkeyword);

        // startActivity(intent);

        Button compareBtn = (Button) findViewById(R.id.compareBtn);
        compareBtn.setEnabled(false);

        this.searchResult = new SearchResult(firstKeyword, secondkeyword);
        this.searchResult.addObserver(this);
        this.searchResult.startSearch();

        //(new MyHttpTask()).execute(firstKeyword);



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

    @Override
    public void update(Observable observable, Object data) {
        Log.d("########## " + this.searchResult.getKeyword1(), String.valueOf(this.searchResult.getResult1()));
        Log.d("########## " + this.searchResult.getKeyword2(), String.valueOf(this.searchResult.getResult2()));

        // save to database

        this.searchResult.deleteObserver(this);

        Button compareBtn = (Button) findViewById(R.id.compareBtn);
        compareBtn.setEnabled(true);
    }
}
