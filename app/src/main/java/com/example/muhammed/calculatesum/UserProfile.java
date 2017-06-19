package com.example.muhammed.calculatesum;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.Manifest;

public class UserProfile extends AppCompatActivity {
    private static int IMG_RESULT = 1;
    String ImageDecode;
    ImageView imageViewLoad;
    Button LoadImage;
    Intent intent;
    String[] FILE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {



            }
        } else {
            // continue with your code
        }


        imageViewLoad = (ImageView) findViewById(R.id.imageView2);
        LoadImage = (Button)findViewById(R.id.button2);

        LoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, IMG_RESULT);

            }
        });
        readFromFile();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(URI);

                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size

                imageViewLoad.setImageBitmap(selectedImage);


              try {
                  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                  selectedImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                  File f = new File(Environment.getExternalStorageDirectory()
                          + File.separator + "profilePic.jpg");

                  FileOutputStream fo = new FileOutputStream(f);
                  fo.write(bytes.toByteArray());
                  fo.close();
              }
              catch (Exception e)
              {
                  System.out.println(e);
              }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void saveUserName(View view) {
        // Do something in response to button

        EditText userName = (EditText) findViewById(R.id.editText3);
        String uName = userName.getText().toString();


        try {

            writeToFile(uName,view.getContext());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(getString(R.string.name_text_file), Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void readFromFile() {
        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        View view=this.findViewById(android.R.id.content);
        Context context=view.getContext();
        File imgFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "profilePic.jpg");
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView2);

            myImage.setImageBitmap(myBitmap);

        }
        else{



            ImageView myImage = (ImageView) findViewById(R.id.imageView2);

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

                    EditText userName = (EditText) findViewById(R.id.editText3);
                    String name=receiveString;
                    userName.setText(name);
                   // File imgFile = new  File(imagepath);

                }

                inputStream.close();
                //  ret = stringBuilder.toString()+"\n";
                //   ret=ret.replaceAll("\\n","\n");
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


    }
}
