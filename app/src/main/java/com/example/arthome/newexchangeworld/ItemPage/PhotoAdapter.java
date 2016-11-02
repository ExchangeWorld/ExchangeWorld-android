package com.example.arthome.newexchangeworld.ItemPage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.pictureActivity;
import com.squareup.picasso.Picasso;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
    public interface PictureClickListener{void onPictureClick(View v);};
    private PictureClickListener pictureClickListener;
    private Context context;
    private List coll;
    private int picWidth;
    private List CheckedPic;
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        ImageView imageView;
        CheckBox checkBox;
        ViewGroup viewGroup;
        public ViewHolder(View itemView){
            super(itemView);
            viewGroup = (ViewGroup)itemView.findViewById(R.id.photo_layout);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            imageView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            if((boolean)CheckedPic.get((int)imageView.getTag(R.string.app_name)))
                CheckedPic.set((int)imageView.getTag(R.string.app_name),false);
            else
                CheckedPic.set((int)imageView.getTag(R.string.app_name),true);
            pictureClickListener.onPictureClick(itemView);
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
   /*BitmapFactory.Options option = new BitmapFactory.Options();
        option.inPurgeable = true;
        option.inSampleSize = 5;*/
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        picWidth = (int) (screenWidth - px) / 3; // 一行顯示四個縮圖
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.height = picWidth;
        params.width = picWidth;
     /*   Bitmap bm = BitmapFactory.decodeFile(coll.get(position).toString(), option);*/
        holder.imageView.setId(position);
  /*      holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.imageView.setImageBitmap(bm);*/
        holder.viewGroup.setLayoutParams(params);
        holder.imageView.setTag(R.string.app_name,position);
        Glide.with(context).load(new File(coll.get(position).toString())).centerCrop().into(holder.imageView);
        if((boolean)CheckedPic.get(position))
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);

    }

    public int getItemCount(){return coll.size();}

    public boolean isEmpty(){
        for(int i=0;i<CheckedPic.size();i++)
            if((boolean)CheckedPic.get(i))
                return false;
        return true;
    }



    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return coll.get(arg0);
    }

    public boolean getCheckedPic(int position){
        return (boolean)CheckedPic.get(position);
    }

    public void addThumb(String s){
        coll.add(s);
    }

    public void setPictureClickListener(PictureClickListener p){
        pictureClickListener = p;
    }


}
