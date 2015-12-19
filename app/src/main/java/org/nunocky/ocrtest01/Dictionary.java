package org.nunocky.ocrtest01;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Owner on 2015/11/12.
 */
public class Dictionary extends AsyncTask<Void, Void, String> {

    private TextView textView;
    private DictionaryConfiguration dictionaryConfiguration;
    private String result;
    private String xml;
    private Context context;
    static final String BR = System.getProperty("line.separator");
    private static final String TAG = "AndroidOCR.java";


    public Dictionary(TextView textView, DictionaryConfiguration dictionaryConfiguration, Context context) {
        super();
        this.textView = textView;
        this.dictionaryConfiguration = dictionaryConfiguration;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... value) {
        Object content = null;

        try {
            Log.d(TAG, dictionaryConfiguration.getUrl());
            URL url = new URL(dictionaryConfiguration.getUrl());

            // 接続用HttpURLConnectionオブジェクト作成
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            xml = InputStreamToString(con.getInputStream());

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));
            int eventType = xpp.getEventType();

            while ((eventType = xpp.next()) != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "ItemID".equals(xpp.getName())) {
                    xml = xpp.nextText();
                }
            }
            dictionaryConfiguration.setItem(xml);
            URL url1 = new URL(dictionaryConfiguration.getUrl1());
            Log.d(TAG, dictionaryConfiguration.getUrl1());
            HttpURLConnection con1 = (HttpURLConnection) url1.openConnection();

            result = InputStreamToString(con1.getInputStream());
            XmlPullParserFactory factory1 = XmlPullParserFactory.newInstance();
            factory1.setNamespaceAware(true);

            XmlPullParser xpp1 = factory1.newPullParser();
            xpp1.setInput(new StringReader(result));
            int eventType1 = xpp1.getEventType();
            result = "";
            while (eventType1 != XmlPullParser.END_DOCUMENT) {
/*
                if (eventType1 == XmlPullParser.START_DOCUMENT) {
                    Log.d("XmlPullParserSample", "Start document");
                } else if (eventType1 == XmlPullParser.END_DOCUMENT) {
                    Log.d("XmlPullParserSample", "End document");
                } else if (eventType1 == XmlPullParser.START_TAG) {
                    Log.d("XmlPullParserSample", "Start tag " + xpp1.getName());
                 //   Log.d("XmlPullParserSample", xpp1.nextText()+"Text");
                } else if (eventType1 == XmlPullParser.END_TAG) {
                    Log.d("XmlPullParserSample", "End tag " + xpp1.getName());
                } else if (eventType1 == XmlPullParser.TEXT) {
                    Log.d("XmlPullParserSample", xpp1.getText()+"Text");

                }
*/
                if (eventType1 == XmlPullParser.START_TAG) {
                    Log.d("XmlPullParserSample", "Start tag " + xpp1.getName());
                    eventType1 = xpp1.next();
                    if (eventType1 == XmlPullParser.TEXT) {
                        Log.d("XmlPullParserSample", xpp1.getText() + "Text");
                        String tmp = xpp1.getText().trim();
                        if (!"".equals(tmp)) {
                            result += tmp + BR;
                        }

                    }
                }
                eventType1 = xpp1.next();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        Log.d(TAG, "xml:" + xml);
        Log.d(TAG, "result" + result);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
        ResearchHistory researchHistory = new ResearchHistory(context);
        if(dictionaryConfiguration.isKind()) {
            researchHistory.Insert(0, 0, dictionaryConfiguration.getWord(), result);
        }else{
            researchHistory.Insert(0, 1, dictionaryConfiguration.getWord(), result);
        }

    }

    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }


}
