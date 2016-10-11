package com.example.arthome.newexchangeworld.ItemPage;

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
        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context
                        .getApplicationContext().getContentResolver(), Long
                        .parseLong((String) coll.get(position)),
                MediaStore.Images.Thumbnails.MICRO_KIND, null);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        int picWidth = (int) (screenWidth - px) / 2; // 一行顯示四個縮圖
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.height = picWidth;
        params.width = picWidth;
        holder.imageView.setId(position);
        holder.imageView.setImageBitmap(bm);
        holder.viewGroup.setLayoutParams(params);
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
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public int getItemCount(){return coll.size();}

}
