package com.example.arthome.newexchangeworld;

import com.example.arthome.newexchangeworld.Models.ChatRoomMessageModel;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by arthome on 2016/12/8.
 */

public class MyWebSocketClient extends WebSocketClient {

//TODO change to singleton
    public MyWebSocketClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(">>>here");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(">>>here");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println(">>>here");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println(">>>here");
    }

    public void writeMessage(int cid, String msg){
        ChatRoomMessageModel chatRoomMessageModel = new ChatRoomMessageModel(cid, msg);
        this.send(new Gson().toJson(chatRoomMessageModel));
    }
}
