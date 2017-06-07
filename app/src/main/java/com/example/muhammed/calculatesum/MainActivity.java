package com.example.muhammed.calculatesum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
