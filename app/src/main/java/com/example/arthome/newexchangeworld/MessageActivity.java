package com.example.arthome.newexchangeworld;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.ChatRoomMessageModel;
import com.example.arthome.newexchangeworld.Models.ChatRoomModel;
import com.example.arthome.newexchangeworld.databinding.ActivityMessageBinding;
import com.example.arthome.newexchangeworld.databinding.ActivityUserPageBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private ActivityMessageBinding binding;
    private MessageAdapter messageAdapter;
    private List<ChatRoomMessageModel> messageModelList = new ArrayList<>();
    private User user;
    private ChatRoomModel chatRoomModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);

        user = RealmManager.INSTANCE.retrieveUser().get(0);
        chatRoomModel = new Gson().fromJson(getIntent().getStringExtra(Constant.INTENT_CHATROOM_MODEL), ChatRoomModel.class);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        binding.messageRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(messageModelList, user.getUid());
        binding.messageRecyclerView.setAdapter(messageAdapter);

        MainActivity.getMyWebSocketClient().setListener(new MyWebSocketClient.MyWebSocketListener() {
            @Override
            public void onMessage(String message) {
                System.out.println(">>>>on Message = " + message);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(">>>not json");
                }
                String type = null;
                try {
                    type = jsonObject.getString("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (type) {
                    case "message":
                        ChatRoomMessageModel messageModel = new Gson().fromJson(message, ChatRoomMessageModel.class);
                        if (messageModel.getChatroom_cid() == chatRoomModel.getCid()) {  //確認是目前聊天對象傳來的訊息才顯示
                            messageModelList.add(0, messageModel);
                            messageAdapter.notifyItemInserted(0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((LinearLayoutManager) binding.messageRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0,300);
                                }
                            });
                            //TODO offset++
                        }
                        break;
                    case "notification":
                        break;
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println(">>>>on Close");
            }

            @Override
            public void onError(Exception ex) {
                System.out.println(">>>>on Error");
            }
        });

        binding.messageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myMessage = binding.messageEditText.getText().toString();
                MainActivity.getMyWebSocketClient().writeMessage(chatRoomModel.getCid(), myMessage);
                binding.messageEditText.setText("");
            }
        });
        downloadMessage();
    }

    private void downloadMessage() {
        Call<List<ChatRoomMessageModel>> call = new RestClient().getExchangeService().getMessage(
                chatRoomModel.getCid(), 30, 0, user.getExToken());
        call.enqueue(new Callback<List<ChatRoomMessageModel>>() {
            @Override
            public void onResponse(Call<List<ChatRoomMessageModel>> call, Response<List<ChatRoomMessageModel>> response) {
                if (response.code() == 200) {
                    messageModelList.clear();
                    messageModelList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "下載訊息 status code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoomMessageModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "下載訊息 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
