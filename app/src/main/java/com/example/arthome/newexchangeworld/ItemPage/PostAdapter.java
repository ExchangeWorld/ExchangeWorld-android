package com.example.arthome.newexchangeworld.ItemPage;

import java.io.File;
import java.util.List;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.pictureActivity;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private ViewGroup layout;
    private Context context;
    private List coll;

    public PostAdapter( List coll) {

        super();
        this.coll = coll;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ViewGroup viewGroup;
        public ViewHolder(View itemView){
            super(itemView);
            viewGroup = (ViewGroup)itemView.findViewById(R.id.post_layout);
            imageView = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
   /*     BitmapFactory.Options option = new BitmapFactory.Options();
        option.inPurgeable = true;
        option.inSampleSize = 5;*/
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        int picWidth = (int) (screenWidth - px) / 2; // 一行顯示四個縮圖
/*        Bitmap bm = BitmapFactory.decodeFile(coll.get(position).toString(), option);*/
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.height = picWidth;
        params.width = picWidth;
        holder.imageView.setId(position);
    /*    holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.imageView.setImageBitmap(bm);*/
        holder.viewGroup.setLayoutParams(params);
        Glide.with(context).load(new File(coll.get(position).toString())).centerCrop().into(holder.imageView);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public int getItemCount(){return coll.size();}

}
