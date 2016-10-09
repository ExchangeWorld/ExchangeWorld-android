package com.example.arthome.newexchangeworld.ItemPage;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.pictureActivity;


public class PhotoAdapter extends BaseAdapter{
    private ViewGroup layout;
    private Context context;
    private List coll;
    private int picWidth;
    private boolean [] CheckedPic;
    private class Viewholder {
        ImageView imageView;
        CheckBox checkBox;
    }


    public PhotoAdapter(Context context, List coll) {

        super();
        this.context = context;
        this.coll = coll;
        CheckedPic = new boolean[coll.size()];
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        final Viewholder viewholder = new Viewholder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = inflater.inflate(R.layout.item_photo, parent, false);
        layout = (ViewGroup) rowview.findViewById(R.id.photo_layout);
        viewholder.imageView = (ImageView) rowview.findViewById(R.id.imageView1);
        viewholder.checkBox = (CheckBox) rowview.findViewById(R.id.checkBox);


        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        picWidth = (int) (screenWidth - px - 20*3) / 3; // 一行顯示四個縮圖

        layout.setLayoutParams(new GridView.LayoutParams(picWidth, picWidth));
        viewholder.imageView.setId(position);
        // Bitmap bm = BitmapFactory.decodeFile((String)coll.get(position));
        // Bitmap newBit = Bitmap.createScaledBitmap(bm, newWidth, newWidth,
        // true);

        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context
                        .getApplicationContext().getContentResolver(), Long
                        .parseLong((String) coll.get(position)),
                MediaStore.Images.Thumbnails.MICRO_KIND, null);

        viewholder.imageView.setImageBitmap(bm);
        viewholder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        if(CheckedPic[position])
            viewholder.checkBox.setChecked(true);
        else
            viewholder.checkBox.setChecked(false);

        //點擊照片
        viewholder.imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
        //        Toast.makeText(context, "index:" + position, Toast.LENGTH_SHORT)
        //                .show();

          //      ((pictureActivity)context).setImageView(position);
                if (viewholder.checkBox.isChecked()) {
                    viewholder.checkBox.setChecked(false);
                    CheckedPic[position] = false;
          //          checked.set(position, false);
                } else {
                    viewholder.checkBox.setChecked(true);
                    CheckedPic[position] = true;
         //           checked.set(position, true);
                }
            }

        });


        return rowview;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return coll.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return coll.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public boolean getCheckedPic(int position){
        return CheckedPic[position];
    }


 /*   public boolean getState(int position) {
        return checked.get(position);
    }*/
}
