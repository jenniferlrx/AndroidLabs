package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForcast extends AppCompatActivity {
    private ImageView imageView;
    private TextView currentTempView;
    private TextView minTempView;
    private TextView maxTempView;
    private TextView uvView;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forcast);

        imageView = findViewById(R.id.icon);

        currentTempView = findViewById(R.id.current_temp);
        minTempView = findViewById(R.id.min_temp);
        maxTempView = findViewById(R.id.max_temp);
        uvView = findViewById(R.id.uv);

        bar = findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();


    }

    class ForecastQuery extends AsyncTask<String, Integer, String>{
        private Bitmap image;
        private String currentTemp;
        private String maxTemp;
        private String minTemp;
        private String uv;
        private String iconName;

        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    if(EVENT_TYPE == START_TAG){
                        String tagName = xpp.getName(); // What kind of tag?
                        if(tagName.equals("temperature")){
                            currentTemp = xpp.getAttributeValue(null,"value");
                            publishProgress(25);

                            minTemp = xpp.getAttributeValue(null,"min");
                            publishProgress(50);

                            maxTemp = xpp.getAttributeValue(null,"max");
                            publishProgress(75);
                            }
                        else if(tagName.equals("weather")){
                            iconName = xpp.getAttributeValue(null,"icon");
                            Log.i("Image name is: ", iconName);
                            if(fileExistance(iconName+".png")){
                                FileInputStream fis = null;
                                try{
                                    fis = openFileInput(iconName+".png");
                                    image = BitmapFactory.decodeStream(fis);
                                    Log.i("Image location: ", "FROM local");
                                }catch(FileNotFoundException e){Log.i("Image location: ", "NOT from local");}
                            }else{
                                String iconUrlString = "http://openweathermap.org/img/w/" + iconName + ".png";
                                URL iconUrl = new URL(iconUrlString);
                                urlConnection = (HttpURLConnection)iconUrl.openConnection();
                                urlConnection.connect();
                                int responseCode = urlConnection.getResponseCode();
                                if(responseCode == 200){
                                    image = BitmapFactory.decodeStream(urlConnection.getInputStream());

                                    FileOutputStream outputStream = openFileOutput(iconName+".png", Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.i("Image source: ", "downloaded");
                                }
                            }
                            publishProgress(100);
                            break;
                        }
                    }
                    xpp.next(); // move the pointer to next XML element
                }
                urlConnection.disconnect();
                inStream.close();

                ret = currentTemp+" "+ minTemp+" "+ maxTemp+" ";

            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}

            queryURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
            try {
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject json = new JSONObject(result);
                uv = json.getDouble("value")+"";

                urlConnection.disconnect();
                inStream.close();
                ret = ret + uv;
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(JSONException jsone)      {ret = "JSON Exception.";}
            return ret;
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            String [] nums = sentFromDoInBackground.split(" ");
            currentTempView.setText("Current temperature: "+nums[0]);
            minTempView.setText("Min temperature: "+nums[1]);
            maxTempView.setText("Max temperature: "+nums[2]);
            uvView.setText("Current UV index: "+nums[3]);
            bar.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(image);
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(values[0]);
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
     }
}
