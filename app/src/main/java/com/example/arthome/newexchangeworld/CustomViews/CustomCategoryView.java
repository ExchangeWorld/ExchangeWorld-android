package com.example.arthome.newexchangeworld.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
    private Drawable drawable;
    private String categoryName;

    public CustomCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomCategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.custom_category_view, this, true);
        categoryImageView = (ImageView) findViewById(R.id.custom_category_image_view);
        categoryTextView = (TextView) findViewById(R.id.custom_category_text_view);


        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCategoryView, 0, 0);
        try {
            drawable = a.getDrawable(R.styleable.CustomCategoryView_categoryImageID);
            categoryName = a.getString(R.styleable.CustomCategoryView_categoryName);
        }
        finally {
            a.recycle();
        }
        categoryTextView.setText(categoryName);
        categoryImageView.setImageDrawable(drawable);
    }
}
