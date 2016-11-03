package com.example.arthome.newexchangeworld.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.R;

/**
 * Created by art on 2016/11/2.
 */


public class CustomCategoryView extends LinearLayout {

    private ImageView categoryImageView;
    private TextView categoryTextView;

    public CustomCategoryView(Context context) {
        super(context);
        init(context);
    }

    public CustomCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomCategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.custom_category_view, this, true);
        categoryImageView = (ImageView) findViewById(R.id.custom_category_image_view);
        categoryTextView = (TextView) findViewById(R.id.custom_category_text_view);
    }
}
