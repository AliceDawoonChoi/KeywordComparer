package com.example.dawoon.myapplication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    private long rowID; // database row ID of the contact
    //
    int first_result = 0;
    int second_result = 0;

    // ListView
//    ArrayList<String> SearchResult;
//    ArrayAdapter<String> resultAdapter;
    private ArrayList<Array1> mArrayList;
    private ListView lv;
    private CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        try {

            //////////////////////////////////////////////////

            // get DatabaseConnector to interact with the SQLite database
            DatabaseConnector databaseConnector;
            databaseConnector = new DatabaseConnector(this);

            //////////////////////////////////////////////////

            // get DatabaseConnector to interact with the SQLite database

            //result_List = new ArrayList<ResultListView>();
            // ResultListView result_List;

            //////////////////// 초기화////////////////
//            SearchResult = new ArrayList<String>();
//            resultAdapter = new ArrayAdapter<String>(this, layout.layout_result, SearchResult);

            mArrayList = new ArrayList<Array1>();
            ///////////////////////////////////////////



            Cursor cursor = databaseConnector.getAllContacts();

            while (cursor.moveToNext()) {
//                int id = cursor.getInt(0);
//                String firstKey = cursor.getString(1);
//                String secondKey = cursor.getString(2);
//                int result1 = cursor.getInt(3);
//                int result2 = cursor.getInt(4);

                String firstKey = cursor.getString(0);
                String secondKey = cursor.getString(1);
                int result1 = cursor.getInt(2);
                int result2 = cursor.getInt(3);
//                String date = cursor.getString(5);

////                String list[] = {String.valueOf(id), firstKey, secondKey, String.valueOf(result1) ,String.valueOf(result2) };
//                db 불러와서 값 넣기.
//                SearchResult.add(list);
//                SearchResult.add(id, list);
//                SearchResult.add(firstKey + " VS " + secondKey + " : " + result1 + " VS " + result2);

                mArrayList.add(new Array1(firstKey,secondKey, result1, result2));
                adapter = new CustomAdapter(this, mArrayList);
                lv = (ListView)findViewById(id.result_ListView);
                lv.setAdapter(adapter);
            }
            cursor.close();

//            adapter = new CustomAdapter(this, mArrayList);
//            lv = (ListView)findViewById(id.result_ListView);
//            lv.setAdapter(adapter);


//            // 어댑터 준비
//            resultAdapter = new ArrayAdapter<String>(this, layout.layout_result, SearchResult);
//
//            // 어댑터 연결
//            ListView result_list = (ListView) findViewById(id.list_item);
//            result_list.setAdapter(resultAdapter);
//
//            // 리스트뷰 속성
//            // 항목을 선택하는 모드
//            result_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//            // 항목 사이의 구분선(검정)
//            result_list.setDivider(new ColorDrawable(Color.BLACK));
//            // 구분선 높이 지정
//            result_list.setDividerHeight(2);


//
//            // 검색 키들.
//            TextView textview_FirstKeyword = (TextView) findViewById(id.firstKey);
//            TextView textview_SecondKeyword = (TextView) findViewById(id.secondKey);
//
//            Intent intent1 = getIntent();
//
//            String firstKeyword = intent1.getStringExtra("FirstKeyword");
//            String secondKeyword = intent1.getStringExtra("SecondKeyword");
//
//            textview_FirstKeyword.setText(String.valueOf(firstKeyword));
//            textview_SecondKeyword.setText(String.valueOf(secondKeyword));
            // 검색 키 관련 끝
        } catch (Exception e){
            e.toString();
        }

    }

    private class CustomAdapter extends BaseAdapter{
        Context context;
        private LayoutInflater mInflater;

        private CustomAdapter(Context context, ArrayList<Array1> mArrayList){
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount(){
            return mArrayList.size();
        }
        @Override
        public Object getItem(int i){
            return mArrayList.get(i);
        }
        @Override
        public long getItemId(int i){
            return i;
        }
        class ViewHolder{
            public TextView a;
            public TextView b;
            public TextView c;
            public TextView d;
        }
        @Override
        public View getView(int position, View converView, ViewGroup viewGroup){
            ViewHolder holder;
            if (converView == null){
                converView = mInflater.inflate(layout.layout_result, null);
                holder = new ViewHolder();
                holder.a = (TextView) converView.findViewById(R.id.tv1);
                holder.b = (TextView) converView.findViewById(id.tv2);
                holder.c = (TextView) converView.findViewById(id.tv3);
                holder.d = (TextView) converView.findViewById(id.tv4);
                converView.setTag(holder);
            }else{
                holder = (ViewHolder) converView.getTag();
            }
            holder.a.setText(mArrayList.get(position).firstkeyword);
            holder.a.setText(mArrayList.get(position).secondkeyword);
            holder.a.setText(mArrayList.get(position).result1);
            holder.a.setText(mArrayList.get(position).result2);
            return converView;
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

        Button compareBtn = (Button) findViewById(id.compareBtn);
        compareBtn.setEnabled(false);

        this.searchResult = new SearchResult(firstKeyword, secondkeyword);
        this.searchResult.addObserver(this);
        this.searchResult.startSearch();
    }


    @Override
    public void update(Observable observable, Object data) {
        Log.d("########## " + this.searchResult.getKeyword1(), String.valueOf(this.searchResult.getResult1()));
        Log.d("########## " + this.searchResult.getKeyword2(), String.valueOf(this.searchResult.getResult2()));

        // save to database

        // get DatabaseConnector to interact with the SQLite database
        DatabaseConnector databaseConnector;
        databaseConnector = new DatabaseConnector(this);

        // insert the contact information into the database
        rowID = databaseConnector.insertContact(
                this.searchResult.getKeyword1(),
                this.searchResult.getKeyword2(),
                this.searchResult.getResult1(),
                this.searchResult.getResult2());


//        databaseConnector.insertContact(this.searchResult.getKeyword1(),
//                this.searchResult.getKeyword2(), this.searchResult.getResult1(), this.searchResult.getResult2());
//        listener.onAddEditCompleted(rowID);


//        SearchResult.add(this.searchResult.getKeyword1() + " VS " + this.searchResult.getKeyword2()
//                + " : " + String.valueOf(this.searchResult.getResult1()) + " VS " +
//                String.valueOf(this.searchResult.getResult2()));



        this.searchResult.deleteObserver(this);

        Button compareBtn = (Button) findViewById(id.compareBtn);
        compareBtn.setEnabled(true);
    }
}
