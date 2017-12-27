package com.example.administrator.xmlweather10;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class XmlWeatherActivity extends AppCompatActivity{
    HttpURLConnection httpURLConnection = null;
    ArrayList<WeatherInf> weatherInfs = new ArrayList<>();
    String cityname = "广州";
    private EditText mCityname;
    private Button mFind;
    private LinearLayout mShowTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_weather);
        setTitle("天气查询XML");
        mCityname = (EditText) findViewById(R.id.cityname);
        mFind = (Button) findViewById(R.id.search);
        mShowTV = (LinearLayout) findViewById(R.id.show_weather);
        mFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowTV.removeAllViews();
                cityname = mCityname.getText().toString();
                Toast.makeText(XmlWeatherActivity.this,"正在查询天气信息...",Toast.LENGTH_LONG).show();
                GetXml gx = new GetXml(cityname);
                gx.start();
            }
        });
    }
    private final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    showData();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    class GetXml extends Thread{
        private String urlstr =  "http://wthrcdn.etouch.cn/WeatherApi?city=";
        public GetXml(String cityname){
            try{
                urlstr = urlstr+ URLEncoder.encode(cityname,"UTF-8");
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }

        @Override
        public void run() {
            InputStream din = null;
            try{
                URL url = new URL(urlstr);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                din = httpURLConnection.getInputStream();
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(din,"UTF-8");
                WeatherInf pw = null;
                M m = null;
                int eveType = xmlPullParser.getEventType();
                while(eveType != XmlPullParser.END_DOCUMENT){
                    if(eveType == XmlPullParser.START_TAG){
                        String tag = xmlPullParser.getName();
                        if(tag.equalsIgnoreCase("weather")){
                            pw = new WeatherInf();
                        }
                        //下个节点
                        if(tag.equalsIgnoreCase("date")){
                            if(pw != null){
                                pw.date = xmlPullParser.nextText();
                            }
                        }
                        //下一个节点
                        if(tag.equalsIgnoreCase("high")){
                            if(pw != null){
                                pw.high = xmlPullParser.nextText();
                            }
                        }
                        if(tag.equalsIgnoreCase("low")){
                            if(pw != null){
                                pw.low = xmlPullParser.nextText();
                            }
                        }
                        if(tag.equalsIgnoreCase("day")){
                            m = new M();
                        }
                        if(tag.equalsIgnoreCase("night")){
                            m = new M();
                        }
                        if(tag.equalsIgnoreCase("type")){
                            if(m != null){
                                m.type = xmlPullParser.nextText();
                            }
                        }
                        if(tag.equalsIgnoreCase("fengxiang")){
                            if(m != null){
                                m.fengxiang = xmlPullParser.nextText();
                            }
                        }
                        if(tag.equalsIgnoreCase("fengli")){
                            if(m != null){
                                m.fengli = xmlPullParser.nextText();
                            }
                        }
                    }
                    else if(eveType == XmlPullParser.END_TAG){
                        String tag = xmlPullParser.getName();
                        if (tag.equalsIgnoreCase("weather")){
                            weatherInfs.add(pw);
                            pw = null;
                        }
                        if(tag.equalsIgnoreCase("date")){
                            pw.day = m;

                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void showData(){
        //显示
    }

}
