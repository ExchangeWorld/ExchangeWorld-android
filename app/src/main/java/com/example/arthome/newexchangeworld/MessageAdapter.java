package com.example.arthome.newexchangeworld;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.Models.ChatRoomMessageModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.QueueOfGoodsModel;
import com.example.arthome.newexchangeworld.databinding.ItemChooseMyGoodsBinding;
import com.example.arthome.newexchangeworld.databinding.ItemMyMessageBinding;
import com.example.arthome.newexchangeworld.databinding.ItemOtherMessageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthome on 2016/11/27.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatRoomMessageModel> messageModelList;
    private int myUID;

    private enum ITEM_TYPE {
        ITEM_TYPE_MY_MESSAGE,
        ITEM_TYPE_OTHER_MESSAGE
    }

    public MessageAdapter(List<ChatRoomMessageModel> messageModelList, int myUID){
        this.messageModelList= messageModelList;
        this.myUID = myUID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE.ITEM_TYPE_MY_MESSAGE.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message,parent,false);
            return new MyMessageViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_message,parent,false);
            return new OtherMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyMessageViewHolder){
            ((MyMessageViewHolder) holder).getBinding().setMessage(messageModelList.get(position));
            ((MyMessageViewHolder) holder).getBinding().executePendingBindings();
        }else if(holder instanceof  OtherMessageViewHolder){
            ((OtherMessageViewHolder) holder).getBinding().setMessage(messageModelList.get(position));
            ((OtherMessageViewHolder) holder).getBinding().executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MyMessageViewHolder extends RecyclerView.ViewHolder{
        private ItemMyMessageBinding binding;
        public MyMessageViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemMyMessageBinding getBinding(){
            return binding;
        }
    }

    public class OtherMessageViewHolder extends RecyclerView.ViewHolder{
        private ItemOtherMessageBinding binding;
        public OtherMessageViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemOtherMessageBinding getBinding(){
            return binding;
        }
    }

    @Override
    public int getItemViewType(int position) { //start from 0
        if (messageModelList.get(position).getSender_uid() ==  myUID) {
            return ITEM_TYPE.ITEM_TYPE_MY_MESSAGE.ordinal();
        } else {
            return ITEM_TYPE.ITEM_TYPE_OTHER_MESSAGE.ordinal();
        }
    }
}
