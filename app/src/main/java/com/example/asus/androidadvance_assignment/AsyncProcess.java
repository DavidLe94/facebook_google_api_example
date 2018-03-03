package com.example.asus.androidadvance_assignment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AsyncProcess extends AsyncTask<String, Integer, String> {
    Context context;
    ListView lv;
    ArrayList<RSSItem> arrayList;
    CustomNewsLayout adapter;
    int nodeListCounter = 0;



    public AsyncProcess(Context context, ListView lv) {
        this.context = context;
        this.lv = lv;
    }

    @Override
    protected void onPreExecute() {
        arrayList = new ArrayList<RSSItem>();
        adapter = new CustomNewsLayout(context, R.layout.custom_news_layout, arrayList);
    }
    @Override
    protected String doInBackground(String... params) {
        if (params[0] != null){
            String xml =  readFromURL(params[0]);
            XMLParser parser = new XMLParser();
            Document doc = parser.getDocument(xml);
            NodeList nodeList = doc.getElementsByTagName("item");
            NodeList nodeListDescription = doc.getElementsByTagName("description");

            String title="";
            String image="";
            String postDate = "";
            String link = "";
            String description = "";
            nodeListCounter = nodeList.getLength();

            for(int i=0; i<nodeListCounter; i++) {
                Element e = (Element) nodeList.item(i);
                title = parser.getValue(e, "title");
                postDate = parser.getValue(e, "pubDate");
                link = parser.getValue(e, "link");
                description = parser.getValue(e, "description");
                //Toast.makeText(context, description, Toast.LENGTH_SHORT).show();

                image = nodeListDescription.item(i + 1).getTextContent() + "<br/>";
                RSSItem RSSItem = new RSSItem(title, image, postDate, link, description);

                arrayList.add(RSSItem);
                publishProgress((i*100) / nodeListCounter);
                SystemClock.sleep(100);

            }
            return null;
        }
        else{
            return null;
        }
    }


    @Override
    protected void onPostExecute(String xml) {
        //progressDialog.dismiss();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RSSItem tt = arrayList.get(position);
                String idx = tt.getLink();

                Intent i = new Intent(context, BrowserActivity.class);
                Bundle b = new Bundle();
                b.putString("linkData", idx);
                i.putExtra("data", b);
                context.startActivity(i);
            }
        });
    }
    /*
    private String getImageLinkFromCDATA(String link){
        int begin = link.indexOf("http://static");
        int end = link.indexOf(".jpg");
        return link.substring(begin, end+4);
    }
    */

    private static String readFromURL(String theUrl){
        StringBuilder content = new StringBuilder();
        try{
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream())
            );

            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return content.toString();
    }

}
