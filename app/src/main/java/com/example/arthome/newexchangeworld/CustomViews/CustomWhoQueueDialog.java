package com.example.arthome.newexchangeworld.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.QueueOfGoodsModel;
import com.example.arthome.newexchangeworld.MyPage.WhoQueueAdapter;
import com.example.arthome.newexchangeworld.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthome on 2016/11/17.
 */

public class CustomWhoQueueDialog extends Dialog {
    RecyclerView recyclerView;
    Button acceptButton, cancelButton;

    public CustomWhoQueueDialog(Context context, List<QueueOfGoodsModel> queueOfGoodsModelList) {
        super(context);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("看誰排");
        setContentView(R.layout.dialog_who_queue);

        List<GoodsModel> goodsModelList = new ArrayList<>();
        for(int i = 0 ;i<queueOfGoodsModelList.size();i++){
            goodsModelList.add(queueOfGoodsModelList.get(i).getQueuer_goods());
        }

        recyclerView = (RecyclerView) findViewById(R.id.dialog_who_queue_recycler_view);
        cancelButton = (Button) findViewById(R.id.dialog_who_queue_cancel_button);
        acceptButton = (Button) findViewById(R.id.dialog_who_queue_accept_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new WhoQueueAdapter(goodsModelList));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
