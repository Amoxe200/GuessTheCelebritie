package com.example.guesscelebrity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;


    public class DownloadWebContent extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {
           String theHtml = "";
           URL myUrl;
           HttpURLConnection connection;

            try {
                myUrl = new URL(urls[0]);
                    connection = (HttpURLConnection) myUrl.openConnection();
                    InputStream input = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(input);

                    int dataReader = reader.read();
                    while (dataReader != -1 )
                    {
                        char currentLetter = (char) dataReader;
                        theHtml += currentLetter;

                        dataReader = reader.read();
                    }

                    return  theHtml;


            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }

        }
    }

    public class DownloadWebImages extends  AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {

            URL myImageUrl;
            HttpURLConnection imageConnection;
            InputStream in;
            Bitmap myBitmap;

            try {

                myImageUrl = new URL(urls[0]);
                imageConnection = (HttpURLConnection) myImageUrl.openConnection();
                imageConnection.connect();
                in = imageConnection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(in);
                return  myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void findName(String result)
    {
        ArrayList<String> listName = new ArrayList<String>();
        Pattern pattern = Pattern.compile("alt=\"(.*?)\"");
        Matcher matcher = pattern.matcher(result);

        while (matcher.find())
        {
            listName.add(matcher.group(1));
        }
            Log.i("Names", String.valueOf(listName));
    }

    public String findLink(String result)
    {
        ArrayList<String> link = new ArrayList<>();
        Pattern p = Pattern.compile("thumbnail(.*?)\"");
        Matcher matcher = p.matcher(result);

        while (matcher.find())
        {
            link.add(matcher.group(1));
        }
            Log.i("Names", String.valueOf(link));

        // pick a random link from the ArrayList

        Random ran = new Random();
        int randomIndex = ran.nextInt(link.size());
        String randomElement = link.get(randomIndex);

        return randomElement;
    }


    static void linkTheNameAndTheLink(ArrayList<String> link1)
    {

    }

    public void clickedOne(View view)
    {
        Log.i("Tag", view.getTag().toString());
        if (view.getTag().toString().equals(view.getTag()))
        {
            Toast.makeText(this, "btn1", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadWebContent downlaod = new DownloadWebContent();
        DownloadWebImages downloadWebImages = new DownloadWebImages();
        Bitmap myImage;


        String result = "";
        String imageLink;
        String firstUrl = "https://famousheights.net/5ft-5in-165-cm";
        String urlForImage = "https://famousheights.net/thumbnail";
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        imageView = findViewById(R.id.imageView);

        try
        {
            result = downlaod.execute(firstUrl).get();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        String scondOne = findLink(result);
        String thirdOne = urlForImage + scondOne;

        try {
            myImage = downloadWebImages.execute(thirdOne).get();
            imageView.setImageBitmap(myImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        findName(result);
    }
}