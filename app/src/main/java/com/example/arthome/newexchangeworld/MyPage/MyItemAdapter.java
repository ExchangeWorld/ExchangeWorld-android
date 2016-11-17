package com.example.arthome.newexchangeworld.MyPage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.util.CategoryTool;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arthome on 2016/5/15.
 */

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.myViewHolder> {
    private static MyViewHolderClicks myViewHolderClicks;
    private static List<GoodsModel> goodsModel;

    public interface MyViewHolderClicks {
        void onGoodsClick(GoodsModel goodsModel);
    }

    public void setMyViewHolderClicks(MyViewHolderClicks ViewHolderClicks) {
        myViewHolderClicks = ViewHolderClicks;
    }


    public MyItemAdapter(List<GoodsModel> goodsModel) {
        this.goodsModel = goodsModel;
    }

    private Context context;

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_goods, parent, false);
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
        viewHolder.goods_textView.setText(goodsModel.get(position).getName());
        Picasso.with(context).load(StringTool.INSTANCE.getFirstPhotoURL(goodsModel.get(position).getPhoto_path()))
                .into(viewHolder.goods_image);
        viewHolder.category_image.setImageResource(
                CategoryTool.INSTANCE.getCategoryDrawableID(goodsModel.get(position).getCategory()));
        viewHolder.goodsDescription.setText(goodsModel.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return goodsModel.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView goods_textView;
        private TextView goodsDescription;
        private ImageView category_image;
        private ImageView goods_image;
        private CardView mCardView;

        public myViewHolder(View itemView) {
            super(itemView);
            SetUpUIViews();
            mCardView.setOnClickListener(this); // onClick
        }

        @Override
        public void onClick(View v) {
            // Triggers click upwards to the adapter on click
            if (myViewHolderClicks != null)
                switch (v.getId()) {
//                    case R.id.item_my_good_category_image:
//                    case R.id.item_my_goods_context_text_view:
//                        Log.i("oscart", "user clicked");  //TODO go to user page
//                        break;
                    default:
                        myViewHolderClicks.onGoodsClick(goodsModel.get(getAdapterPosition()));
                        break;
                }
        }

        public void SetUpUIViews() {
            goods_textView = (TextView) itemView.findViewById(R.id.my_id_goods_name);
            goodsDescription = (TextView) itemView.findViewById(R.id.item_my_goods_context_text_view);
            category_image = (ImageView) itemView.findViewById(R.id.my_category_image);
            goods_image = (ImageView) itemView.findViewById(R.id.my_goods_image);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}
