package com.example.administrator.xmlweather10;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import android.widget.LinearLayout.LayoutParams;

public class XmlWeatherActivity extends AppCompatActivity implements Runnable {
    HttpURLConnection httpURLConnection = null;
    InputStream din = null;
    private String cityname;
    private EditText mCityname;
    private Button mFind;
    private LinearLayout mShowTV;

    Vector<String> date = new Vector<String>();
    Vector<String> low = new Vector<String>();
    Vector<String> high = new Vector<String>();
    Vector<String> day = new Vector<String>();
    Vector<String> night = new Vector<String>();
    Vector<String> type = new Vector<String>();
    Vector<String> fengxiang = new Vector<String>();
    Vector<String> fengli = new Vector<String>();

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
                Toast.makeText(XmlWeatherActivity.this, "正在查询天气...", Toast.LENGTH_LONG).show();
                Thread th = new Thread(XmlWeatherActivity.this);
                th.start();
            }
        });
    }

    public void parseData() {
        String weatherUrl = "http://wthrcdn.etouch.cn/WeatherApi?city=" + cityname;
        try {
            URL url = new URL(weatherUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            din = httpURLConnection.getInputStream();
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(din, "UTF-8");
            int evtType = xmlPullParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlPullParser.getName();
                        if (tag.equalsIgnoreCase("forecast")) {
                            //
                        }
                        break;
                    case XmlPullParser.END_TAG:
                    default:
                        break;
                }
                evtType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        low.removeAllElements();
        high.removeAllElements();
        date.removeAllElements();
        day.removeAllElements();
        night.removeAllElements();
        type.removeAllElements();
        fengxiang.removeAllElements();
        fengli.removeAllElements();
        parseData();
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showData();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void showData() {
        mShowTV.removeAllViews();
        mShowTV.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.weight = 80;
        params.height = 50;
        for (int i = 0; i < date.size(); i++) {
            //
        }
    }
}
