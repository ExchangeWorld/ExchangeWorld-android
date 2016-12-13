package com.example.arthome.newexchangeworld;

import android.widget.Toast;

import com.example.arthome.newexchangeworld.Models.ChatRoomMessageModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by arthome on 2016/12/8.
 */

public class MyWebSocketClient extends WebSocketClient {
    private MyWebSocketListener listener;
    //TODO change to singleton
    public MyWebSocketClient(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(">>>socket open");
    }

    @Override
    public void onMessage(String message) {

        listener.onMessage(message);
        System.out.println(">>>socket message");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        listener.onClose(code, reason, remote);
        System.out.println(">>>socket close");
    }

    @Override
    public void onError(Exception ex) {
//        listener.onError(ex);
        System.out.println(">>>socket error");
    }

    public void writeMessage(int cid, String msg) {
        ChatRoomMessageModel chatRoomMessageModel = new ChatRoomMessageModel(cid, msg);
        this.send(new Gson().toJson(chatRoomMessageModel));
    }

    public void setListener(MyWebSocketListener listener) {
        this.listener = listener;
    }

    public interface MyWebSocketListener {
        void onMessage(String message);

        void onClose(int code, String reason, boolean remote);

        void onError(Exception ex);
    }
}
