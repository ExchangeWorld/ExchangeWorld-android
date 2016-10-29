package com.example.arthome.newexchangeworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.ItemPage.PostAdapter;
import com.example.arthome.newexchangeworld.Models.PostModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    private RecyclerView postgallery;
    private EditText nameText;
    private EditText describeText;
    private TextView classText;
    private TextView nameTitle;
    private TextView classTitle;
    private TextView describeTitle;
    private ArrayList<String> postPic;
    private ArrayList<String> postthumbs;
    private ArrayAdapter<String> classList;
    private Button postButton;
    private PostAdapter postAdapter;
    private String[] classType = {"書籍","3C產品","教科書","流行服飾","美妝用品","其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postPic = new ArrayList<String>();
        postthumbs = new ArrayList<String>();
        postgallery = (RecyclerView)findViewById(R.id.postGallery);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        postgallery.setLayoutManager(layoutManager);
        nameText = (EditText)findViewById(R.id.nameText);
        describeText = (EditText)findViewById(R.id.describText);
        classText = (TextView)findViewById(R.id.classText);
        nameTitle = (TextView)findViewById(R.id.nameTitle);
        classTitle = (TextView)findViewById(R.id.classTitle);
        describeTitle = (TextView)findViewById(R.id.describTitle);
        postButton = (Button)findViewById(R.id.postButton);
        Bundle bundle = getIntent().getExtras();
        postPic = bundle.getStringArrayList("imagePaths");
        postAdapter = new PostAdapter(postPic);
        postgallery.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        classText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder classList = new AlertDialog.Builder(PostActivity.this);
                classList.setTitle("選擇類別");
                classList.setItems(classType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        classText.setText(classType[which]);
                    }
                });
                classList.show();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mFile = new File("/sdcard/ExchangeWorld");
                if (!mFile.exists())
                    mFile.mkdirs();
                try {
                    FileWriter fw = new FileWriter("/sdcard/ExchangeWorld/PostData.txt", false);
                    BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                    bw.write("Name = " + nameText.getText().toString());
                    bw.newLine();
                    bw.write("Class = " + classText.getText().toString());
                    bw.newLine();
                    bw.write("Description = " + describeText.getText().toString());
                    bw.newLine();
                    bw.write("Picture = ");
                    bw.newLine();
                    for (int i = 0; i < postPic.size(); i++) {
                        bw.write(postPic.get(i));
                        bw.newLine();
                    }
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PostModel postModel = new PostModel(nameText.getText().toString(),
                        postPic.get(0),
                        describeText.getText().toString(),
                        classText.getText().toString()
                );
                Intent i = new Intent(PostActivity.this, MainActivity.class);
                i.putExtra("postInfo", postModel);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
    @Override

    public void onBackPressed(){
        Intent i = new Intent(PostActivity.this,pictureActivity.class);
        startActivity(i);
        PostActivity.this.finish();
    }
}
