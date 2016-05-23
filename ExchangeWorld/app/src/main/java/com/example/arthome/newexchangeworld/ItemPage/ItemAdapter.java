package com.example.arthome.newexchangeworld.ItemPage;

import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by arthome on 2016/5/15.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.myViewHolder> {
    private static MyViewHolderClicks myViewHolderClicks;
    public interface MyViewHolderClicks{
        void onGoodsClick(View itemView, int position);
    }
    public void setMyViewHolderClicks(MyViewHolderClicks ViewHolderClicks){
        myViewHolderClicks = ViewHolderClicks;
    }

    //private ArrayList<String> items = new ArrayList<>();
    private List<GoodsModel> goodsModel ;
    public ItemAdapter(List<GoodsModel> goodsModel) {
        this.goodsModel = goodsModel;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        //for  MyViewHolderClicks
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(myViewHolder viewHolder, int position) { //change list_item name here
        //  String info = items.get(position);
            Log.i("onBing!!!!!!!!", Integer.toString(position));
            viewHolder.goods_textView.setText(goodsModel.get(position).getName());
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(goodsModel.get(position).getPhoto_path(), viewHolder.goods_image);


            switch (goodsModel.get(position).getCategory()) {
                case "Books":
                    viewHolder.category_image.setImageResource(R.drawable.ic_book);
                    break;
                default:
                    break;
            }
            viewHolder.user_textView.setText(goodsModel.get(position).getOwner().getName());
    }

    @Override
    public int getItemCount() {
        return goodsModel.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView goods_textView;
        TextView user_textView;
        ImageView category_image;
        ImageView goods_image;
        MyViewHolderClicks mListener;
        CardView mCardView;
        public myViewHolder(View itemView) {
            super(itemView);
            goods_textView = (TextView) itemView.findViewById(R.id.id_goods_name);
            user_textView = (TextView) itemView.findViewById(R.id.id_user_name);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);
            goods_image = (ImageView) itemView.findViewById(R.id.goods_image);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            mCardView.setOnClickListener(this); // onClick
        }
        @Override
        public void onClick(View v) {
            // Triggers click upwards to the adapter on click
            if (myViewHolderClicks != null)
                myViewHolderClicks.onGoodsClick(itemView,getLayoutPosition());
        }
    }
}
