package com.example.arthome.newexchangeworld.ItemPage;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.pictureActivity;

public class PostAdapter extends BaseAdapter{
    private ViewGroup layout;
    private Context context;
    private List coll;

    public PostAdapter(Context context, List coll) {

        super();
        this.context = context;
        this.coll = coll;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = inflater.inflate(R.layout.item_post, parent, false);
        layout = (ViewGroup) rowview.findViewById(R.id.post_layout);
        ImageView imageView = (ImageView) rowview.findViewById(R.id.imageView2);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        int newWidth = (int) (screenWidth - px ) / 2; // 一行顯示四個縮圖

        layout.setLayoutParams(new GridView.LayoutParams(newWidth, newWidth));
        imageView.setId(position);
       // Bitmap bm = BitmapFactory.decodeFile((String) coll.get(position));
        // Bitmap newBit = Bitmap.createScaledBitmap(bm, newWidth, newWidth,
        // true);

       Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context
                        .getApplicationContext().getContentResolver(), Long
                        .parseLong((String) coll.get(position)),
                MediaStore.Images.Thumbnails.MICRO_KIND, null);

        imageView.setImageBitmap(bm);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

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

}
