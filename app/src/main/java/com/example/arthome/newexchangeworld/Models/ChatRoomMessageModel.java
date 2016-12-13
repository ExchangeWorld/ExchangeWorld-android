package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/12/8.
 */

public class ChatRoomMessageModel {
    private int mid;
    private int chatroom_cid;
    private int sender_uid;
    private String created_at;
    private String content;

    public ChatRoomMessageModel(int chatroom_cid, String content) {
        this.chatroom_cid = chatroom_cid;
        this.content = content;
    }


    public int getChatroom_cid() {
        return chatroom_cid;
    }

    public void setChatroom_cid(int chatroom_cid) {
        this.chatroom_cid = chatroom_cid;
    }

    public int getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(int sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
