package com.example.adaptivefault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class localError_Activity extends AppCompatActivity {

    private TextView errorTextView;
    private TextView solutionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localsolution);
        errorTextView = findViewById(R.id.error);
        solutionTextView = findViewById(R.id.solution);
        Intent intent = getIntent();
        Error error = (Error) intent.getSerializableExtra("error");
        errorTextView.setText(error.getError());
        solutionTextView.setText(error.getSolution());
        solutionTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
