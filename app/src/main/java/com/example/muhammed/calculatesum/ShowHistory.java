package com.example.muhammed.calculatesum;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShowHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        readFromFile();


    }



    private void readFromFile() {

        String ret = "";
View view=this.findViewById(android.R.id.content);
        Context context=view.getContext();
        try {
            InputStream inputStream = context.openFileInput(getString(R.string.history_save_file));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {

                    TextView txtView=(TextView)findViewById(R.id.textView6);
                    txtView.append(receiveString+"\n");
                   // stringBuilder.append(receiveString);
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
