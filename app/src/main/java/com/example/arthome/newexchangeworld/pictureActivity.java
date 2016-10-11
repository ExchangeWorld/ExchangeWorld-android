package com.example.arthome.newexchangeworld;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ItemPage.PhotoAdapter;
import com.example.arthome.newexchangeworld.ItemPage.PostAdapter;
import com.google.android.gms.wearable.internal.StorageInfoResponse;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.*;

public class pictureActivity extends AppCompatActivity {

    private DisplayMetrics mPhone;
    private final static int CAMERA = 2;
    private int poststate;
    private PhotoAdapter photoAdapter;
    private List<String> thumbs;  //存放縮圖的id
    private List<String> imagePaths;  //存放圖片的路徑
    private RecyclerView gallery;
    private Button cameraButton;
    private Button nextButton;
    private ArrayList<String> selectedPic;
    private ArrayList<String> postthumbs;



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        gallery = (RecyclerView) findViewById(R.id.GalleryView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        gallery.setLayoutManager(layoutManager);
        setPic();
        nextButton.setEnabled(false);


  //     ContentResolver cr = getContentResolver();

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   ContentValues value = new ContentValues();
                value.put(Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI,value);*/
                Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
                SimpleDateFormat tmpTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
                File tmpfile = new File("/sdcard/DCIM", tmpTime.format(new Date()) + ".jpg");
                Uri uri = Uri.fromFile(tmpfile);
                intent.putExtra(EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postthumbs = new ArrayList<String>();
                selectedPic = new ArrayList<String>();
                Intent intent = new Intent();
                intent.setClass(pictureActivity.this,PostActivity.class);
                Bundle bundle = new Bundle();
                for(int i = 0;i<photoAdapter.getItemCount();i++) {
                    if(photoAdapter.getCheckedPic(i)) {
                        postthumbs.add((String)photoAdapter.getItem(i));
                        selectedPic.add(imagePaths.get(i));
                    }
                }
                bundle.putStringArrayList("thumb",  postthumbs);
                bundle.putStringArrayList("imagePaths",  selectedPic);
                intent.putExtras(bundle);
                startActivity(intent);
                pictureActivity.this.finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if (data != null) {
            if ((requestCode == CAMERA)) {
          /*      Bitmap mbmp = (Bitmap) data.getExtras().getParcelable("data");
                CameraV.setImageBitmap(mbmp);
                CameraV.setScaleType(ImageView.ScaleType.FIT_XY);
                postText.setVisibility(View.VISIBLE);
                CameraV.setVisibility(View.VISIBLE);
                cameraButton.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);*/
            }
        }
    }

 /*   public void setImageView(int position){
        Bitmap bm = BitmapFactory.decodeFile(imagePaths.get(position));
        CameraV.setImageBitmap(bm);
        CameraV.setScaleType(ImageView.ScaleType.FIT_XY);
        postText.setVisibility(View.VISIBLE);
        CameraV.setVisibility(View.VISIBLE);
        cameraButton.setVisibility(View.GONE);
        gallery.setVisibility(View.GONE);
    }*/
    protected void setPic() {
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        thumbs = new ArrayList<String>();
        imagePaths = new ArrayList<String>();
        for (int i = cursor.getCount() - 1; i >= 0; i--) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));// ID
            thumbs.add(id + "");
            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑
            imagePaths.add(filepath);
        }
        cursor.close();
        photoAdapter = new PhotoAdapter(thumbs);
        gallery.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(pictureActivity.this,MainActivity.class);
        startActivity(i);
        pictureActivity.this.finish();
    }
}



