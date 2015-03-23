package com.dptsolutions.pathbuttondemoapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dptsolutions.pathbutton.PathButton;


public class MainActivity extends ActionBarActivity {

    ViewGroup buttonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonsContainer = (ViewGroup) findViewById(R.id.buttons_container);
        final int horizontalMargin = getResources().getDimensionPixelSize(R.dimen.default_path_button_horizontal_margin);
        final int verticalPadding = getResources().getDimensionPixelSize(R.dimen.default_path_button_vertical_padding);
        final int horizontalPadding = getResources().getDimensionPixelSize(R.dimen.default_path_button_horizontal_padding);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, horizontalMargin);

        //Custom Border Width example
        final float customBorderPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        PathButton customBorderWidthButton = new PathButton(this);
        customBorderWidthButton.setOnClickListener(clickListener);
        customBorderWidthButton.setLayoutParams(params);
        customBorderWidthButton.setPadding(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding);
        customBorderWidthButton.setText(R.string.label_custom_border_width);
        customBorderWidthButton.setBorderWidth((int) customBorderPixels);
        buttonsContainer.addView(customBorderWidthButton);

        //Alternate Colors example
        PathButton alternateColorsButton = new PathButton(this);
        alternateColorsButton.setOnClickListener(clickListener);
        alternateColorsButton.setLayoutParams(params);
        alternateColorsButton.setPadding(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding);
        alternateColorsButton.setText(R.string.label_alternate_colors);
        alternateColorsButton.setTextColor(getResources().getColorStateList(R.color.alternate_text_colors));
        alternateColorsButton.setFillColors(getResources().getColorStateList(R.color.alternate_fill_colors));
        buttonsContainer.addView(alternateColorsButton);

        //Alternate Colors example
        PathButton solidColorButton = new PathButton(this);
        solidColorButton.setOnClickListener(clickListener);
        solidColorButton.setLayoutParams(params);
        solidColorButton.setPadding(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding);
        solidColorButton.setText(R.string.label_solid_color);
        solidColorButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        solidColorButton.setFillColor(getResources().getColor(android.R.color.holo_red_light));
        buttonsContainer.addView(solidColorButton);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonClicked(v);
        }
    };

    public void buttonClicked(View view) {
        PathButton button = (PathButton)view;
        Toast.makeText(this, String.format("%s clicked!", button.getText()), Toast.LENGTH_SHORT).show();
    }
}
