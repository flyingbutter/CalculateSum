package com.example.muhammed.calculatesum;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.location.LocationManager.GPS_PROVIDER;
import static com.example.muhammed.calculatesum.R.id.imageView;


public class MainActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ImageView mImageView;
    private String APIkey = "23cf8ff7b358466abc954b8d30a7f18e";
    public static final String TAG = "MyTag";
    StringRequest stringRequest; // Assume this exists.
    RequestQueue mRequestQueue;  // Assume this exists.
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(imageView);
        mImageView.setImageBitmap(BitmapFactory.decodeFile("pathToImageFile"));
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position ==1){
                    showHistory(findViewById(android.R.id.content));
                }
                else if(position == 2)
                {
                    showGpsLocation(findViewById(android.R.id.content));
                }
                else if(position == 3)
                {
                    showUserProfile(findViewById(android.R.id.content));
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

setupDrawer();
readUserName();

        if(ShowLocation.longitude!=0 && ShowLocation.longitude!=0)
        {

            setCity();
            setWeather();


        }


    }

    public void setWeather(){



String url="http://api.openweathermap.org/data/2.5/weather?lat="+ShowLocation.latitude+"&lon="+ShowLocation.longitude+"&appid="+APIkey;
        // Instantiate the RequestQueue.
         mRequestQueue = Volley.newRequestQueue(this);
        final TextView mTextView = (TextView) findViewById(R.id.textView4);
// Request a string response from the provided URL.
         stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Gson gson;
                        WeatherInfo weatherInfo = null;
                            try{
                                 gson = new Gson();
                                 weatherInfo = gson.fromJson(response, WeatherInfo.class);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            double temp=weatherInfo.main.temp;
                            double temp_min=weatherInfo.main.temp_min;
                             double temp_max=weatherInfo.main.temp_max;
                            String weather=weatherInfo.getWeather().get(0).description;
                        String icon=weatherInfo.getWeather().get(0).icon;

                        TextView minTemp=(TextView)findViewById(R.id.textView10);
                        minTemp.setText(String.valueOf((temp_min-273.15)));

                        TextView maxTemp=(TextView)findViewById(R.id.textView4);
                        maxTemp.setText(String.valueOf((temp_max-273.15)));
                        URL  url = null;
                        try {
                            url = new URL("http://openweathermap.org/img/w/"+icon+".png");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        new DownloadImageTask((ImageView) findViewById(R.id.imageView3))
                                .execute(String.valueOf(url));



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Bitmap  resized = Bitmap.createScaledBitmap(result,(int)(result.getWidth()*4), (int)(result.getHeight()*4), true);

            bmImage.setImageBitmap(resized);
        }
    }

public void setCity(){
    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    try {
        List<Address> listAddresses = geocoder.getFromLocation(ShowLocation.latitude, ShowLocation.longitude, 1);
        if(null!=listAddresses&&listAddresses.size()>0){

            String _Location = listAddresses.get(0).getAddressLine(0);
            TextView txtCity=(TextView)findViewById(R.id.textView5);
            txtCity.setText(_Location);

        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }
    @Override
    protected void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
    private void addDrawerItems() {
        String[] pageArray = { "Calculator", "Show History", "Show Location","User Profile"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pageArray);
        mDrawerList.setAdapter(mAdapter);
    }
    public void showHistory(View view) {
        Intent intent = new Intent(this, ShowHistory.class);
        startActivity(intent);
    }
    public void showUserProfile(View view) {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }


    public void showGpsLocation(View view) {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return;
        }

        Intent intent = new Intent(this, ShowLocation.class);
        startActivity(intent);
    }

    private void readUserName() {

        View view=this.findViewById(android.R.id.content);
        Context context=view.getContext();

        File imgFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "profilePic.jpg");
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(imageView);

            myImage.setImageBitmap(myBitmap);

        }
        else{



            ImageView myImage = (ImageView) findViewById(imageView);

            myImage.setImageResource(R.mipmap.def_avatar);

        }
        try {
            InputStream inputStream = context.openFileInput(getString(R.string.name_text_file));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {

                    TextView txtView=(TextView)findViewById(R.id.textView3);
                    String name=receiveString;
                    txtView.setText(name);

                }

                inputStream.close();

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name) {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(getString(R.string.history_save_file), Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void calculate(View view) {
        // Do something in response to button
        EditText firstNo = (EditText) findViewById(R.id.editText);
        String firstInt = firstNo.getText().toString();

        EditText secondNo = (EditText) findViewById(R.id.editText2);
        String secondInt = secondNo.getText().toString();

        TextView txtView=(TextView)findViewById(R.id.textView2);

        try {


            int sum=Integer.parseInt(firstInt)+Integer.parseInt(secondInt);
            txtView.setText(sum+"");
            writeToFile(firstInt+"+"+secondInt+"="+sum+"\n",view.getContext());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class Coord{
        private double lon;
        private double lat;

        public double getLon(){
            return lon;
        }
        public void setLon(double input){
            this.lon = input;
        }
        public double getLat(){
            return lat;
        }
        public void setLat(double input){
            this.lat = input;
        }
    }
    public class Weather{
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId(){
            return id;
        }
        public void setId(int input){
            this.id = input;
        }
        public String getMain(){
            return main;
        }
        public void setMain(String input){
            this.main = input;
        }
        public String getDescription(){
            return description;
        }
        public void setDescription(String input){
            this.description = input;
        }
        public String getIcon(){
            return icon;
        }
        public void setIcon(String input){
            this.icon = input;
        }
    }
    public class Main{
        private double temp;
        private double pressure;
        private double humidity;
        private double temp_min;
        private double temp_max;
        private double seaLevel;
        private double grndLevel;

        public double getTemp(){
            return temp;
        }
        public void setTemp(double input){
            this.temp = input;
        }
        public double getPressure(){
            return pressure;
        }
        public void setPressure(double input){
            this.pressure = input;
        }
        public double getHumidity(){
            return humidity;
        }
        public void setHumidity(double input){
            this.humidity = input;
        }
        public double getTempMin(){
            return temp_min;
        }
        public void setTempMin(double input){
            this.temp_min = input;
        }
        public double getTempMax(){
            return temp_max;
        }
        public void setTempMax(double input){
            this.temp_max = input;
        }
        public double getSeaLevel(){
            return seaLevel;
        }
        public void setSeaLevel(double input){
            this.seaLevel = input;
        }
        public double getGrndLevel(){
            return grndLevel;
        }
        public void setGrndLevel(double input){
            this.grndLevel = input;
        }
    }
    public class Wind{
        private double speed;
        private double deg;

        public double getSpeed(){
            return speed;
        }
        public void setSpeed(double input){
            this.speed = input;
        }
        public double getDeg(){
            return deg;
        }
        public void setDeg(double input){
            this.deg = input;
        }
    }
    public class Clouds{
        private int all;

        public int getAll(){
            return all;
        }
        public void setAll(int input){
            this.all = input;
        }
    }
    public class Sys{
        private double message;
        private String country;
        private int sunrise;
        private int sunset;

        public double getMessage(){
            return message;
        }
        public void setMessage(int input){
            this.message = input;
        }
        public String getCountry(){
            return country;
        }
        public void setCountry(String input){
            this.country = input;
        }
        public int getSunrise(){
            return sunrise;
        }
        public void setSunrise(int input){
            this.sunrise = input;
        }
        public int getSunset(){
            return sunset;
        }
        public void setSunset(int input){
            this.sunset = input;
        }
    }
    public class WeatherInfo{
        private Coord coord;
        private List<Weather> weather;
        private String base;
        private Main main;
        private Wind wind;
        private Clouds clouds;
        private int dt;
        private Sys sys;
        private int id;
        private String name;
        private int cod;

        public Coord getCoord(){
            return coord;
        }
        public void setCoord(Coord input){
            this.coord = input;
        }
        public List<Weather> getWeather(){
            return weather;
        }
        public void setWeather(List<Weather> input){
            this.weather = input;
        }
        public String getBase(){
            return base;
        }
        public void setBase(String input){
            this.base = input;
        }
        public Main getMain(){
            return main;
        }
        public void setMain(Main input){
            this.main = input;
        }
        public Wind getWind(){
            return wind;
        }
        public void setWind(Wind input){
            this.wind = input;
        }
        public Clouds getClouds(){
            return clouds;
        }
        public void setClouds(Clouds input){
            this.clouds = input;
        }
        public int getDt(){
            return dt;
        }
        public void setDt(int input){
            this.dt = input;
        }
        public Sys getSys(){
            return sys;
        }
        public void setSys(Sys input){
            this.sys = input;
        }
        public int getId(){
            return id;
        }
        public void setId(int input){
            this.id = input;
        }
        public String getName(){
            return name;
        }
        public void setName(String input){
            this.name = input;
        }
        public int getCod(){
            return cod;
        }
        public void setCod(int input){
            this.cod = input;
        }
    }


}
