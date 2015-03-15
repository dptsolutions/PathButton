package com.dptsolutions.pathbuttondemoapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dptsolutions.pathbutton.PathButton;


public class MainActivity extends ActionBarActivity {

    PathButton programmaticTestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set width of this button to 10dp programatically
        programmaticTestButton = (PathButton)findViewById(R.id.programmatic_borderwidth_example);
        final float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        programmaticTestButton.setBorderWidth((int) pixels);
    }

    public void buttonClicked(View view) {
        PathButton button = (PathButton)view;
        Toast.makeText(this, String.format("%s clicked!", button.getText()), Toast.LENGTH_SHORT).show();
    }
}
