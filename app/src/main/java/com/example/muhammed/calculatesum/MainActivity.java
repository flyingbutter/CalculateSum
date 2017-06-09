package com.example.muhammed.calculatesum;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showHistory(View view) {
        Intent intent = new Intent(this, ShowHistory.class);
        startActivity(intent);
    }

    public void showGpsLocation(View view) {
        Intent intent = new Intent(this, ShowLocation.class);
        startActivity(intent);
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

}
