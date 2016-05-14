package com.example.arthome.newexchangeworld.ItemPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.R;

import java.util.ArrayList;

/**
 * Created by Rod on 2016/3/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {
    private ArrayList<String> items = new ArrayList<>();

    public ItemAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
      //  String info = items.get(position);
        View view = viewHolder.itemView;
        TextView goods_textView = (TextView) view.findViewById(R.id.id_goods_name);
        goods_textView.setText("Computers");
        TextView user_textView = (TextView) view.findViewById(R.id.id_goods_name);
        user_textView.setText("Art");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        public viewHolder(View itemView) {
            super(itemView);
        }
    }
}