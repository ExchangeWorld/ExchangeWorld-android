package com.example.arthome.newexchangeworld.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.ChatRoomMessageModel;
import com.example.arthome.newexchangeworld.Models.ChatRoomModel;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.databinding.DialogMessageBinding;
import com.example.arthome.newexchangeworld.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/11/27.
 */

public class CustomMessageDialog extends Dialog {
    private DialogMessageBinding binding;
    private MessageAdapter messageAdapter;
    private User user;
    private ChatRoomModel chatRoomModel;
    private Context context;
    private List<ChatRoomMessageModel> messageModelList = new ArrayList<>();


    public CustomMessageDialog(Context context, User user, ChatRoomModel chatRoomModel) {
        super(context);
        this.context = context;
        this.user = user;
        this.chatRoomModel = chatRoomModel;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_message, null, false);
        setContentView(binding.getRoot());

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("訊息");

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.dialogMessageRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(messageModelList, user.getUid());
        binding.dialogMessageRecyclerView.setAdapter(messageAdapter);

        binding.dialogMessageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        downloadMessage();
    }

    private void downloadMessage(){
        Call<List<ChatRoomMessageModel>> call = new RestClient().getExchangeService().getMessage(
                chatRoomModel.getCid(), 30, 0, user.getExToken());
        call.enqueue(new Callback<List<ChatRoomMessageModel>>() {
            @Override
            public void onResponse(Call<List<ChatRoomMessageModel>> call, Response<List<ChatRoomMessageModel>> response) {
                if(response.code()==200){
                    messageModelList.clear();
                    messageModelList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "下載訊息 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoomMessageModel>> call, Throwable t) {
                Toast.makeText(context, "下載訊息 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void setMyGoodsDialogListener(MyGoodsDialogListener listener){
//        this.listener = listener;
//    }

//    public interface MyGoodsDialogListener{
//        void quequeClicked(int gid);
//    }
}
