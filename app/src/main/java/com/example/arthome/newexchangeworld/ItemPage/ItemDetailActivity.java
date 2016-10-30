package com.example.arthome.newexchangeworld.ItemPage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ItemDetailActivity extends AppCompatActivity {

    ImageView goodsImage;
    TextView ownerName;
    TextView goodsDescription;
    TextView goodsName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Showing and Enabling clicks on the Home/Up button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // recovering data from MainActivity, sent via intent
        Bundle bundle = getIntent().getExtras();
            if(bundle != null) {
                String json = bundle.getString("goodModel");
                GoodsModel goodsModel = new Gson().fromJson(json, GoodsModel.class);

                setUpUIView();

                Picasso.with(this).load(goodsModel.getPhoto_path()).into(goodsImage);
                ownerName.setText(goodsModel.getOwner().getName());
                goodsName.setText(goodsModel.getName());
                goodsDescription.setText(goodsModel.getDescription());
            }
        }

    public void setUpUIView(){
        goodsImage = (ImageView ) findViewById(R.id.goods_image_detail);
        ownerName = (TextView) findViewById(R.id.owner_name_detail);
        goodsDescription = (TextView) findViewById(R.id.goods_description_detail);
        goodsName = (TextView) findViewById(R.id.goods_name_detail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //go back arrow
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
