package com.example.dawoon.myapplication;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by john on 2015. 8. 23..
 */
public class SearchResult extends Observable {

    private class MyHttpTask extends AsyncTask<String, Integer, String> {

        public String keyword;

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
                this.keyword = mSearchStr;
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
            if (this.keyword.equals(keyword1)) {
                setResult1(Integer.valueOf(s));
            }
            else {
                setResult2(Integer.valueOf(s));
            }
            super.onPostExecute(s);
        }
    }

    private String keyword1;
    private String keyword2;

    private int result1 = -1;
    private int result2 = -1;

    private MyHttpTask task1;
    private MyHttpTask task2;

    public SearchResult(String keyword1, String keyword2) {
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.task1 = new MyHttpTask();
        this.task2 = new MyHttpTask();
    }

    public void startSearch() {
        this.task1.execute(keyword1);
        this.task2.execute(keyword2);
    }

    public String getKeyword1() {
        return this.keyword1;
    }

    public String getKeyword2() {
        return  this.keyword2;
    }

    public int getResult1() {
        return this.result1;
    }

    public int getResult2() {
        return this.result2;
    }

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : this.observers) {
            o.update(this, null);
        }
    }

    public void setResult1(int result1) {
        this.result1 = result1;
        if (-1 < this.result2) { notifyObservers(); }
    }

    public void setResult2(int result2) {
        this.result2 = result2;
        if (-1 < this.result1) { notifyObservers(); }
    }


}
