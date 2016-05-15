package com.example.arthome.newexchangeworld.ItemPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;

import java.util.List;

/**
 * Created by arthome on 2016/5/15.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.myViewHolder> {
    //private ArrayList<String> items = new ArrayList<>();
    private List<GoodsModel> goodsModel ;

    public ItemAdapter(List<GoodsModel> goodsModel) {
        this.goodsModel = goodsModel;
    }




    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(myViewHolder viewHolder, int position) {
        //  String info = items.get(position);

        viewHolder.goods_textView.setText(goodsModel.get(0).getName()); //TODO change to array

        switch(goodsModel.get(0).getCategory()){
            case "Books":
                viewHolder.category_image.setImageResource(R.drawable.ic_book);
                break;
            default:
                break;
        }
        viewHolder.user_textView.setText("Art");
    }

    @Override
    public int getItemCount() {
        return goodsModel.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView goods_textView;
        TextView user_textView;
        ImageView category_image;
        public myViewHolder(View itemView) {
            super(itemView);
            goods_textView = (TextView) itemView.findViewById(R.id.id_goods_name);
            user_textView = (TextView) itemView.findViewById(R.id.id_user_name);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);

        }
    }
}
