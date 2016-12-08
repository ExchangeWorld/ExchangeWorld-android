package com.example.arthome.newexchangeworld.Models;

/**
 * Created by arthome on 2016/12/8.
 */

public class ChatRoomModel {
    // {"cid":10,"members":[2,15],"read_members":[2],"last_message":"","last_message_time":"2016-12-08T13:51:34.000Z","created_at":"2016-12-08T13:51:34.193Z","updated_at":"2016-12-08T13:51:38.694Z","members_info":{"2":{"name":"鄭哲亞","photo_path":"https://scontent.xx.fbcdn.net/v/t1.0-1/c0.0.320.320/p320x320/14184343_1112986765447440_4506204186751420883_n.jpg?oh=eaec03c520898dd7f2a73e4c89a75fae&oe=5887311D"},"15":{"name":"Ming-Feng Tsai","photo_path":"https://scontent.xx.fbcdn.net/v/t1.0-1/c1.0.320.320/p320x320/10246722_10203888351276246_6252262075745186799_n.jpg?oh=8b9785c4d012189c91242a0976ff455a&oe=58874C83"}}}
    private int cid;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
