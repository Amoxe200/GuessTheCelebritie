package com.example.guesscelebrity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.LoginFilter;
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

public class MainActivity extends AppCompatActivity{
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

    public ArrayList<String> findName(String result)
    {
        ArrayList<String> listName = new ArrayList<String>();
        Pattern pattern = Pattern.compile("alt=\"(.*?)\"");
        Matcher matcher = pattern.matcher(result);

        while (matcher.find())
        {
            listName.add(matcher.group(1));
        }
         return listName;
    }

    public ArrayList<String> findLink(String result)
    {
        ArrayList<String> link = new ArrayList<>();
        Pattern p = Pattern.compile("thumbnail(.*?)\"");
        Matcher matcher = p.matcher(result);
        while (matcher.find())
        {
            link.add(matcher.group(1));
        }
            Log.i("Names", String.valueOf(link));
        return link;
    }

    public String returnRandom (ArrayList<String> links)
    {
        Random rand = new Random();
        int randomIndex = rand.nextInt(links.size());

        if (randomIndex == 10)
        {
           randomIndex =  rand.nextInt(links.size());
        }

        String randomElement = links.get(randomIndex);
        return randomElement;
    }

    public String mannageString(String theOne)
    {
        String name = "";
        String firstName;
        String secondName;
        String thirdName;
        String returnedOne = "";

        Pattern p = Pattern.compile("/(.*?)-photo");
        Matcher matcher = p.matcher(theOne);
        while (matcher.find())
        {
            name = matcher.group(1);
        }

        String[] splitedString = name.split("-");

        if (splitedString.length <= 1)
            {
                firstName = splitedString[0];
                returnedOne = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
            }

        else if (splitedString.length > 1)
            {
                firstName = splitedString[0];
                secondName = splitedString[1];
                returnedOne = secondName.substring(0,1).toUpperCase() + secondName.substring(1) + " " + firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
             }

        Log.i("test3", returnedOne);
        return returnedOne;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadWebContent downlaod = new DownloadWebContent();
        final DownloadWebImages downloadWebImages = new DownloadWebImages();
        Bitmap myImage;

        String result = "";
        String imageLink;
        String firstUrl = "https://famousheights.net/5ft-5in-165-cm";
        final String urlForImage = "https://famousheights.net/thumbnail";
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

        // after the connection of the app with the website i get the lists of the names and store it in an arrayList
        ArrayList<String> listName = findName(result);
        Log.i("test1", String.valueOf(listName));

        // here the i call the function that returns the random link and splited string
        final ArrayList<String> listLink = findLink(result);
        String scondOne = returnRandom(listLink);
        String thirdOne = urlForImage + scondOne;

        final String theOne = mannageString(scondOne);
        Log.i("theOne", theOne);

        try {
            myImage = downloadWebImages.execute(thirdOne).get();
            imageView.setImageBitmap(myImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // change the textView of the buttons

        Random random = new Random();
        int randNumber = random.nextInt(4);
        Log.i("randomNumber", String.valueOf(randNumber));

        // this is where i change the textView of my buttons
        if (randNumber == 0)
        {
            btn1.setText(theOne);

            btn2.setText(listName.get(random.nextInt(listName.size())));
                if (btn1.getText().toString().equals(btn2.getText().toString()))
                {
                    btn2.setText(listName.get(random.nextInt(listName.size())));
                }

            btn3.setText(listName.get(random.nextInt(listName.size())));
            if (btn1.getText().toString().equals(btn3.getText().toString()))
            {
                btn3.setText(listName.get(random.nextInt(listName.size())));
            }

            btn4.setText(listName.get(random.nextInt(listName.size())));
            if (btn1.getText().toString().equals(btn4.getText().toString()))
            {
                btn4.setText(listName.get(random.nextInt(listName.size())));
            }
        }
        else if (randNumber == 1)
        {
            btn2.setText(theOne);

            btn1.setText(listName.get(random.nextInt(listName.size())));
            if (btn2.getText().toString().equals(btn1.getText().toString()))
            {
                btn1.setText(listName.get(random.nextInt(listName.size())));
            }

            btn3.setText(listName.get(random.nextInt(listName.size())));
            if (btn2.getText().toString().equals(btn3.getText().toString()))
            {
                btn3.setText(listName.get(random.nextInt(listName.size())));
            }

            btn4.setText(listName.get(random.nextInt(listName.size())));
            if (btn2.getText().toString().equals(btn4.getText().toString()))
            {
                btn4.setText(listName.get(random.nextInt(listName.size())));
            }
        }
        else if (randNumber == 2)
        {
            btn3.setText(theOne);

            btn1.setText(listName.get(random.nextInt(listName.size())));
            if (btn3.getText().toString().equals(btn1.getText().toString()))
            {
                btn1.setText(listName.get(random.nextInt(listName.size())));
            }

            btn2.setText(listName.get(random.nextInt(listName.size())));
            if (btn3.getText().toString().equals(btn2.getText().toString()))
            {
                btn2.setText(listName.get(random.nextInt(listName.size())));
            }

            btn4.setText(listName.get(random.nextInt(listName.size())));
            if (btn3.getText().toString().equals(btn4.getText().toString()))
            {
                btn4.setText(listName.get(random.nextInt(listName.size())));
            }
        }
        else if (randNumber == 3)
        {
            btn4.setText(theOne);

            btn1.setText(listName.get(random.nextInt(listName.size())));
            if (btn4.getText().toString().equals(btn1.getText().toString()))
            {
                btn1.setText(listName.get(random.nextInt(listName.size())));
            }

            btn2.setText(listName.get(random.nextInt(listName.size())));
            if (btn4.getText().toString().equals(btn2.getText().toString()))
            {
                btn2.setText(listName.get(random.nextInt(listName.size())));
            }

            btn3.setText(listName.get(random.nextInt(listName.size())));
            if (btn4.getText().toString().equals(btn3.getText().toString()))
            {
                btn3.setText(listName.get(random.nextInt(listName.size())));
            }

        }
        
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newOne = returnRandom(listLink);
                String linkOfImage = urlForImage + newOne;
                Log.i("newOne", newOne);
                Log.i("newOne", linkOfImage);
                String correctAnswer = mannageString(newOne);
                Log.i("newOne", correctAnswer);
                Bitmap imageTwo;

                Button button = (Button) v;
                if (v.getTag().toString().equals(v.getTag()))
                {
                        if (theOne == ((Button) v).getText())
                        {
                            Toast.makeText(v.getContext(), "Correct Bro congrats", Toast.LENGTH_SHORT).show();
                                // change the picture
                            try {
                                imageTwo = downloadWebImages.execute(linkOfImage).get();
                                imageView.setImageBitmap(imageTwo);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        };

        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
        btn3.setOnClickListener(onClickListener);
        btn4.setOnClickListener(onClickListener);

    }

}