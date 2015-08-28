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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static com.example.dawoon.myapplication.R.id;
import static com.example.dawoon.myapplication.R.layout;

public class MainActivity extends Activity implements Observer {

    private long rowID; // database row ID of the contact

    private ArrayList<Array1> mArrayList;
    private ListView lv;
    private CustomAdapter adapter;
    private EditText firstkey1;
    private EditText secondkey2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        try {
            loadDB();
            lv.setOnItemLongClickListener(itemLongClickListener);
        }
        catch (Exception e){
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
                    final String tag = "tag";

                    // create a new AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    // set the AlertDialog's title
                    builder.setTitle("Option");

                    Intent intent = new Intent();
                    intent.putExtra(intent.EXTRA_TEXT, getString(R.string.bing_first, ((ViewHolder) view.getTag()).a.getText().toString()));
                    intent.putExtra(intent.EXTRA_TEXT, getString(R.string.bing_second, ((ViewHolder) view.getTag()).b.getText().toString()));

                    // set list of items to display in dialog
                    builder.setItems(R.array.dialog_items,
                            new DialogInterface.OnClickListener() {
                                // responds to user touch by sharing, editing or
                                // deleting a saved search
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ViewHolder vh = new ViewHolder();
                                    vh = (ViewHolder) view.getTag();
                                    String a = vh.a.getText().toString();
                                    String b = vh.b.getText().toString();
                                    int e = Integer.parseInt(vh.e.getText().toString());

                                    switch (which) {
                                        case 0: // Edit : Delete DB and put text to textView
                                            firstkey1.setText(a);
                                            secondkey2.setText(b);
                                            delete1(e);
                                            loadDB();
                                            break;
                                        case 1: // Delete Delete DB
                                            firstkey1.setText("");
                                            secondkey2.setText("");
                                            delete1(e);
                                            loadDB();
                                            break;
                                        case 2: // search first keyword
                                            shareSearch(a);
                                            break;
                                        case 3: // search second keyword
                                            shareSearch(b);
                                            break;
                                    }

                                }

                            }
                    );

                    builder.create().show(); // display the AlertDialog
                    return true;
                }
            };


    private void shareSearch(String tag)
    {
        String urlString = "http://www.bing.com/search?q=" +  Uri.encode(tag, "UTF-8");

        // create an Intent to launch a web browser
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

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
        public View getView(int position, View converView, ViewGroup viewGroup) {

            ViewHolder holder;

            if (converView == null) {
                converView = mInflater.inflate(layout.layout_result, null);
                holder = new ViewHolder();
                holder.a = (TextView) converView.findViewById(R.id.tv1);
                holder.b = (TextView) converView.findViewById(id.tv2);
                holder.c = (TextView) converView.findViewById(id.tv3);
                holder.d = (TextView) converView.findViewById(id.tv4);
                holder.e = (TextView) converView.findViewById(id.tv5);
                converView.setTag(holder);
            }
            else {
                holder = (ViewHolder) converView.getTag();
            }

            holder.a.setTextColor(Color.parseColor("black"));
            holder.b.setTextColor(Color.parseColor("black"));
            holder.c.setTextColor(Color.parseColor("black"));
            holder.d.setTextColor(Color.parseColor("black"));

            DecimalFormat df = new DecimalFormat("###,###,###");
            String ab = df.format(mArrayList.get(position).result1);

            holder.a.setText(mArrayList.get(position).firstkeyword);
            holder.b.setText(mArrayList.get(position).secondkeyword);
            holder.c.setText(df.format(mArrayList.get(position).result1));
            holder.d.setText(df.format(mArrayList.get(position).result2));
            holder.e.setText(String.valueOf(mArrayList.get(position).id));

            if (mArrayList.get(position).result1 > mArrayList.get(position).result2) {
                holder.a.setTextColor(Color.parseColor("green"));
                holder.c.setTextColor(Color.parseColor("green"));
                holder.b.setTextColor(Color.parseColor("red"));
                holder.d.setTextColor(Color.parseColor("red"));
            }
            else {
                holder.b.setTextColor(Color.parseColor("green"));
                holder.d.setTextColor(Color.parseColor("green"));
                holder.a.setTextColor(Color.parseColor("red"));
                holder.c.setTextColor(Color.parseColor("red"));
            }

            return converView;
        }
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

    public void loadDB() {
        // get DatabaseConnector to interact with the SQLite database
        DatabaseConnector databaseConnector;
        databaseConnector = new DatabaseConnector(this);

        mArrayList = new ArrayList<Array1>();

        firstkey1 = (EditText)findViewById(id.firstKey);
        secondkey2 = (EditText)findViewById(id.secondKey);

        Cursor cursor = databaseConnector.getAllContacts();

        while (cursor.moveToNext()) {

            String firstKey = cursor.getString(0);
            String secondKey = cursor.getString(1);
            int result1 = cursor.getInt(2);
            int result2 = cursor.getInt(3);
            int id = cursor.getInt(4);

            mArrayList.add(new Array1(firstKey, secondKey, result1, result2, id));
        }

        cursor.close();

        adapter = new CustomAdapter(this, mArrayList);
        lv = (ListView)findViewById(id.result_ListView);
        lv.setAdapter(adapter);
    }

    public void compareOnClick(View v) throws IOException {

        switch (v.getId()) {
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
    }

    @Override
    public void update(Observable observable, Object data) {

        // get DatabaseConnector to interact with the SQLite database
        DatabaseConnector databaseConnector;
        databaseConnector = new DatabaseConnector(this);

        // insert the contact information into the database
        rowID = databaseConnector.insertContact(
                this.searchResult.getKeyword1(),
                this.searchResult.getKeyword2(),
                this.searchResult.getResult1(),
                this.searchResult.getResult2());

        MAinDialog1 mDialog = new MAinDialog1(this.searchResult.getKeyword1(),
                this.searchResult.getKeyword2(),
                this.searchResult.getResult1(),
                this.searchResult.getResult2());
        mDialog.show(getFragmentManager(), "Compare");

        this.searchResult.deleteObserver(this);

        Button compareBtn = (Button) findViewById(id.compareBtn);
        compareBtn.setEnabled(true);

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
        public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

            mLayoutInflater = getActivity().getLayoutInflater();

            mBuilder.setView(mLayoutInflater.inflate(layout.dialog, null));
            mBuilder.setTitle("The Result");

            DecimalFormat df = new DecimalFormat("###,###,###");
            mBuilder.setMessage(f1 + " : " + df.format(re1) + " VS " + f2 + " : " + df.format(re2));

            // set the AlertDialog's negative Button
            mBuilder.setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        // called when "Cancel" Button is clicked
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel(); // dismiss dialog
                        }
                    }
            );

            return mBuilder.create();
        }

        @Override
        public void onStop(){
            super.onStop();
        }
    }
}
