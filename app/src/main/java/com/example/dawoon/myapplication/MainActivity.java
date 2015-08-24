package com.example.dawoon.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
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

import static com.example.dawoon.myapplication.R.id;
import static com.example.dawoon.myapplication.R.layout;

public class MainActivity extends Activity implements Observer {

    // ArrayList<ResultListView> arr_result;

    // db 연결
//    SQLiteDatabase db;
//    Cursor cursor;
//    String tableName = "searchResult";


    private MainActivity listener;
    // callback method implemented by MainActivity
//    public interface MainActivitytListener
//    {
//        // called after edit completed so contact can be redisplayed
//        public void onAddEditCompleted(long rowID);
//    }

    private long rowID; // database row ID of the contact
    //
    int first_result = 0;
    int second_result = 0;

    // ListView
    ArrayList<String> SearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        try {
            // db 오픈
//            db = openOrCreateDatabase("searchResult", MODE_PRIVATE, null);

            // 테이블 생성
            // id, firstkey, secondkey, result1, result2, date8
//            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
//                    "id integer PRIMARY KEY autoincrement, " + "firstkey text NOT NULL," +
//                    "secondkey text NOT NULL, " + "result1 integer NOT NULL, " +
//                    " result2 integer NOT NULL, " + " date date)";
//            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
//                    "id integer PRIMARY KEY autoincrement, " + "firstkey text NOT NULL," +
//                    "secondkey text NOT NULL, " + "result1 integer NOT NULL, " +
//                    " result2 integer NOT NULL, )";
//            db.execSQL(sql);


            //result_List = new ArrayList<ResultListView>();
            // ResultListView result_List;
            SearchResult = new ArrayList();
//            new ArrayList<String>(savedSearches.getAll().keySet());

            //db 불러와서 값 넣기.

//            cursor = db.rawQuery("SELECT * FROM searchResult ORDER BY date", null);
//
//
//            while (cursor.moveToNext()) {
//                int id = cursor.getInt(0);
//                String firstKey = cursor.getString(1);
//                String secondKey = cursor.getString(2);
//                int result1 = cursor.getInt(3);
//                int result2 = cursor.getInt(4);
//                String date = cursor.getString(5);
//
//                String list[] = {String.valueOf(id), firstKey, secondKey, String.valueOf(result1) ,String.valueOf(result2) };
//                //db 불러와서 값 넣기.
////                SearchResult.add(list);
//
////                SearchResult.add(id, list);
//                SearchResult.add(firstKey + " VS " + secondKey + " : " + result1 + " VS " + result2);
//
//                //sam = firstKey + " VS " + secondKey + " : " + result1 + " VS " + result2;
//
//            }
//            cursor.close();


            // 어댑터 준비
            ArrayAdapter<String> resultAdapter;
            resultAdapter = new ArrayAdapter<String>(this, layout.layout_result, SearchResult);

            // 어댑터 연결
            ListView result_list = (ListView) findViewById(id.list_item);
            result_list.setAdapter(resultAdapter);

            // 리스트뷰 속성
            // 항목을 선택하는 모드
            result_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // 항목 사이의 구분선(검정)
            result_list.setDivider(new ColorDrawable(Color.BLACK));
            // 구분선 높이 지정
            result_list.setDividerHeight(2);

            // 검색 키들.
            TextView textview_FirstKeyword = (TextView) findViewById(id.firstKey);
            TextView textview_SecondKeyword = (TextView) findViewById(id.secondKey);

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
        text_FirstKeyword = (EditText) findViewById(id.firstKey);
        EditText text_SecondKeyword = null;
        text_SecondKeyword = (EditText) findViewById(id.secondKey);

        String firstKeyword = text_FirstKeyword.getText().toString();
        String secondkeyword = text_SecondKeyword.getText().toString();

//        Toast.makeText(getApplicationContext(), "Search 뭐시기뭐시기", Toast.LENGTH_LONG).show();
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//
//        intent.putExtra("FirstKeyword", firstKeyword);
//        intent.putExtra("SecondKeyword", secondkeyword);

        // startActivity(intent);

        Button compareBtn = (Button) findViewById(id.compareBtn);
        compareBtn.setEnabled(false);

        this.searchResult = new SearchResult(firstKeyword, secondkeyword);
        this.searchResult.addObserver(this);
        this.searchResult.startSearch();


        // getResult from SearchResult.java
        first_result = this.searchResult.getResult1();
        Log.d("~~~~~~~~~~~~~~~~~~~~", searchResult.getKeyword1());
        Log.d("~~~~~~~~~~~~~~~~~~~~~~", searchResult.getKeyword2());
        Log.d("~~~~~~~~~~~~~~~~~~~~", String.valueOf(searchResult.getResult1()));
        Log.d("~~~~~~~~~~~~~~~~~~~~~~", String.valueOf(searchResult.getResult2()));
        second_result =  this.searchResult.getResult2();




        // 현재 결과값이 자꾸 -1로 나오는 관계로...
        first_result = 1;
        second_result = 2;

    }

    public void onAttach(Activity activity)
    {
        listener.onAttach(activity);
    }

    @Override
    public void update(Observable observable, Object data) {
        Log.d("########## " + this.searchResult.getKeyword1(), String.valueOf(this.searchResult.getResult1()));
        Log.d("########## " + this.searchResult.getKeyword2(), String.valueOf(this.searchResult.getResult2()));

        // save to database

        // get DatabaseConnector to interact with the SQLite database
        DatabaseConnector databaseConnector;
        databaseConnector = new DatabaseConnector();

        // insert the contact information into the database
        rowID = databaseConnector.insertContact(
                this.searchResult.getKeyword1(),
                this.searchResult.getKeyword2(),
                this.searchResult.getResult1(),
                this.searchResult.getResult2());


        databaseConnector.insertContact(this.searchResult.getKeyword1(),
                this.searchResult.getKeyword2(), this.searchResult.getResult1(), this.searchResult.getResult2());
//        listener.onAddEditCompleted(rowID);



        this.searchResult.deleteObserver(this);

        Button compareBtn = (Button) findViewById(id.compareBtn);
        compareBtn.setEnabled(true);
    }
}
