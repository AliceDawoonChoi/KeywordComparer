package com.example.dawoon.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private EditText firstkey1;
    private EditText secondkey2;
    private int id_DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        try {

            //////////////////////////////////////////////////
//
//            // get DatabaseConnector to interact with the SQLite database
//            DatabaseConnector databaseConnector;
//            databaseConnector = new DatabaseConnector(this);
//
//            //////////////////////////////////////////////////
//
//            // get DatabaseConnector to interact with the SQLite database
//
//            //result_List = new ArrayList<ResultListView>();
//            // ResultListView result_List;
//
//            //////////////////// 초기화////////////////
////            SearchResult = new ArrayList<String>();
////            resultAdapter = new ArrayAdapter<String>(this, layout.layout_result, SearchResult);
//
//            mArrayList = new ArrayList<Array1>();
//            ///////////////////////////////////////////
//            firstkey1 = (EditText)findViewById(id.firstKey);
//            secondkey2 = (EditText)findViewById(id.secondKey);
//
//
//
//            Cursor cursor = databaseConnector.getAllContacts();
//
//            while (cursor.moveToNext()) {
////                int id = cursor.getInt(0);
////                String firstKey = cursor.getString(1);
////                String secondKey = cursor.getString(2);
////                int result1 = cursor.getInt(3);
////                int result2 = cursor.getInt(4);
//
//                String firstKey = cursor.getString(0);
//                String secondKey = cursor.getString(1);
//                int result1 = cursor.getInt(2);
//                int result2 = cursor.getInt(3);
//                int id = cursor.getInt(4);
////                String date = cursor.getString(5);
//
//////                String list[] = {String.valueOf(id), firstKey, secondKey, String.valueOf(result1) ,String.valueOf(result2) };
////                db 불러와서 값 넣기.
////                SearchResult.add(list);
////                SearchResult.add(id, list);
////                SearchResult.add(firstKey + " VS " + secondKey + " : " + result1 + " VS " + result2);
//
//                mArrayList.add(new Array1(firstKey, secondKey, result1, result2, id));
//
//            }
//            cursor.close();
//
//            adapter = new CustomAdapter(this, mArrayList);
//            lv = (ListView)findViewById(id.result_ListView);
//            lv.setAdapter(adapter);

            /////////////////////////test
            loadDB();
            
            lv.setOnItemLongClickListener(itemLongClickListener);

//            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                    // 쭉 눌렀을 때 다이얼로그 창이 떠야함.
//                    Toast.makeText(getApplicationContext(), (position + 1) + "까꿍", Toast.LENGTH_SHORT).show();
//android.widget.AdapterView.OnItemLongClickListener itemLongClickListener;
//                    return false;
//                }
//            });
            // 검색 키 관련 끝
        } catch (Exception e){
            e.toString();
        }

    }


    AdapterView.OnItemLongClickListener itemLongClickListener =
            new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                               int position, final long id)
                {
                    // get the tag that the user long touched
                    final String tag = "tag";//((TextView) view).getText().toString();

                    // create a new AlertDialog
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(MainActivity.this);

                    // set the AlertDialog's title
                    builder.setTitle(
                            "Edit or Delete");



                    // set list of items to display in dialog
                    builder.setItems(R.array.dialog_items,
                            new DialogInterface.OnClickListener() {
                                // responds to user touch by sharing, editing or
                                // deleting a saved search
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    //mArrayList = (ArrayList<Array1>) view.getTag();

                                    ViewHolder vh = new ViewHolder();
                                    vh = (ViewHolder) view.getTag();
                                    String a = vh.a.getText().toString();
                                    String b = vh.b.getText().toString();
                                    int e = Integer.parseInt(vh.e.getText().toString());

                                    switch (which) {
                                        case 0: // Edit : Delete DB and put text to textView\
                                            firstkey1.setText(a);
                                            secondkey2.setText(b);
                                            delete1(e);
                                            loadDB();
//                                            delete1(e);
                                            break;
                                        case 1: // Delete Delete DB
                                            firstkey1.setText("");
                                            secondkey2.setText("");
                                            delete1(e);
                                            loadDB();
//                                            delete1(e);
                                            break;
                                        case 2 : // search firstkeyword
                                            shareSearch(a);
                                            break;
                                        case 3 : // search secondkeyword
                                            shareSearch(b);
                                            break;
                                    }

                                }

                            } // end DialogInterface.OnClickListener
                    ); // end call to builder.setItems

                    builder.create().show(); // display the AlertDialog
                    return true;
                } // end method onItemLongClick
            }; // end OnItemLongClickListener declaration


    private void shareSearch(String tag)
    {
//        // create the URL representing the search
//        String urlString = "http://www.bing.com/search?q=" +  Uri.encode(tag, "UTF-8");
//
//        // create Intent to share urlString
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
//                tag);
//        shareIntent.putExtra(Intent.EXTRA_TEXT,
//                getString(R.string.shareMessage, urlString));
//        shareIntent.setType("text/plain");
//
//        // display apps that can share text
//        startActivity(Intent.createChooser(shareIntent,
//                tag));

        String urlString = "http://www.bing.com/search?q=" +  Uri.encode(tag, "UTF-8");

        // create an Intent to launch a web browser
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(urlString));

        startActivity(webIntent); // launches web browser to view results

    }

    class ViewHolder{
        public TextView a;
        public TextView b;
        public TextView c;
        public TextView d;
        public TextView e;
    }

    private class CustomAdapter extends BaseAdapter{
        Context context;
        private LayoutInflater mInflater;

        private CustomAdapter(){}

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
                holder.e = (TextView) converView.findViewById(id.tv5);
                converView.setTag(holder);
            }else{
                holder = (ViewHolder) converView.getTag();
            }

            holder.a.setTextColor(Color.parseColor("black"));
            holder.b.setTextColor(Color.parseColor("black"));
            holder.c.setTextColor(Color.parseColor("black"));
            holder.d.setTextColor(Color.parseColor("black"));

            holder.a.setText(mArrayList.get(position).firstkeyword);
            holder.b.setText(mArrayList.get(position).secondkeyword);
            holder.c.setText(String.valueOf(mArrayList.get(position).result1));
            holder.d.setText(String.valueOf(mArrayList.get(position).result2));
            holder.e.setText(String.valueOf(mArrayList.get(position).id));

            if (mArrayList.get(position).result1 > mArrayList.get(position).result2){
                holder.a.setTextColor(Color.parseColor("#222A8C"));
                holder.c.setTextColor(Color.parseColor("#222A8C"));
            }else{
                holder.b.setTextColor(Color.parseColor("#222A8C"));
                holder.d.setTextColor(Color.parseColor("#222A8C"));
            }

            return converView;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////         Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.test, menu);
////        super.onCreateContextMenu();
//        return true;
//    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo m){
//        getMenuInflater().inflate(R.menu.test, menu);
//        super.onCreateContextMenu(menu, v, m);
//    }

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

    public void loadDB() {
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
        firstkey1 = (EditText)findViewById(id.firstKey);
        secondkey2 = (EditText)findViewById(id.secondKey);



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
            int id = cursor.getInt(4);
//                String date = cursor.getString(5);

////                String list[] = {String.valueOf(id), firstKey, secondKey, String.valueOf(result1) ,String.valueOf(result2) };
//                db 불러와서 값 넣기.
//                SearchResult.add(list);
//                SearchResult.add(id, list);
//                SearchResult.add(firstKey + " VS " + secondKey + " : " + result1 + " VS " + result2);

            mArrayList.add(new Array1(firstKey, secondKey, result1, result2, id));

        }
        cursor.close();

        adapter = new CustomAdapter(this, mArrayList);
        lv = (ListView)findViewById(id.result_ListView);
        lv.setAdapter(adapter);


    }

    public void compareOnClick(View v) throws IOException {

        switch (v.getId()){
//            case R.id.btn1 : // 다이얼로그 버튼
//                MAinDialog1 d = new MAinDialog1("","",0,0);
//                d.dismiss();
//                break;
            case id.result_ListView : // 리스트뷰

                break;
            case id.compareBtn : // CompareButton

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

                break;
        }


        /////////////////////////////////
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

        MAinDialog1 mDialog = new MAinDialog1(this.searchResult.getKeyword1(),
                this.searchResult.getKeyword2(),
                this.searchResult.getResult1(),
                this.searchResult.getResult2());
        mDialog.show(getFragmentManager(), "Compare");




        this.searchResult.deleteObserver(this);

        Button compareBtn = (Button) findViewById(id.compareBtn);
        compareBtn.setEnabled(true);

        ///////////// test

        loadDB();
    }

    public void delete1(int id){

        DatabaseConnector databaseConnector;
        databaseConnector = new DatabaseConnector(this);

        databaseConnector.deleteContact(id);
    }

    // LongClick Dialog
    public static class LIstviewDialog extends DialogFragment{
        String f1;
        String f2;
        int re1;
        int re2;

    }

    //Dialog

    public static class MAinDialog1 extends DialogFragment{
        String f1;
        String f2;
        int re1;
        int re2;
        private LayoutInflater mLayoutInflater;

        public MAinDialog1(String first, String second, int r1, int r2){
            f1 = first;
            f2 = second;
            re1 = r1;
            re2 = r2;
        }

        @Override
        public android.app.Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
             mLayoutInflater = getActivity().getLayoutInflater();
            mBuilder.setView(mLayoutInflater.inflate(layout.dialog, null));
            mBuilder.setTitle("The Result");
            mBuilder.setMessage(f1 + " : " + String.valueOf(re1) + " VS " + f2 + " : " + String.valueOf(re2));

            // set the AlertDialog's negative Button
            mBuilder.setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        // called when "Cancel" Button is clicked
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel(); // dismiss dialog
                        }
                    }
            ); // end call to setNegativeButton


            return mBuilder.create();
        }
        @Override
        public void onStop(){
            super.onStop();
        }
    }


}
