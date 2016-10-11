package com.example.arthome.newexchangeworld.ItemPage;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
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


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
    private Context context;
    private List coll;
    private int picWidth;
    private List CheckedPic;
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        ImageView imageView;
        CheckBox checkBox;
        ViewGroup viewGroup;
        boolean isChecked;
        public ViewHolder(View v){
            super(v);
            viewGroup = (ViewGroup)v.findViewById(R.id.photo_layout);
            imageView = (ImageView) v.findViewById(R.id.imageView1);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            isChecked =false;
            imageView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            if(isChecked){
                checkBox.setChecked(false);
                isChecked = false;
            }
            else{
                checkBox.setChecked(true);
                isChecked = true;
            }
        }
    }


    public PhotoAdapter(List coll) {
        super();
        this.coll = coll;
        CheckedPic = new ArrayList();
        for(int i=0;i<coll.size();i++)
            CheckedPic.add(false);
    }

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context
                        .getApplicationContext().getContentResolver(), Long
                        .parseLong((String) coll.get(position)),
                MediaStore.Images.Thumbnails.MICRO_KIND, null);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        picWidth = (int) (screenWidth - px) / 3; // 一行顯示四個縮圖
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.height = picWidth;
        params.width = picWidth;
        holder.imageView.setId(position);
        holder.imageView.setImageBitmap(bm);
        holder.viewGroup.setLayoutParams(params);

    }

    public int getItemCount(){return coll.size();}

    public boolean isEmpty(){
        for(int i=0;i<CheckedPic.size();i++)
            if((boolean)CheckedPic.get(i))
                return false;
        return true;
    }


  /*  public View getView(final int position, View convertView, ViewGroup parent) {

        // Bitmap bm = BitmapFactory.decodeFile((String)coll.get(position));
        // Bitmap newBit = Bitmap.createScaledBitmap(bm, newWidth, newWidth,
        // true);

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
*/

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return coll.get(arg0);
    }
/*
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
*/
    public boolean getCheckedPic(int position){
        return (boolean)CheckedPic.get(position);
    }


 /*   public boolean getState(int position) {
        return checked.get(position);
    }*/
}
