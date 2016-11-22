package com.example.arthome.newexchangeworld;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
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
import static android.Manifest.permission.*;

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
import java.util.jar.Manifest;

import static android.Manifest.permission.CAMERA;
import static android.provider.MediaStore.*;

public class pictureActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;
    private DisplayMetrics mPhone;
   // private final static int CAMERA = 2;
    private int poststate;
    private PhotoAdapter photoAdapter;
    private List<String> imagePaths;  //存放圖片的路徑
    private RecyclerView gallery;
    private Button cameraButton;
    private Button nextButton;
    private ArrayList<String> selectedPic;
    private ArrayList<String> postthumbs;
    final private  String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };

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
   //     setPic();
        nextButton.setEnabled(false);


     //   photoAdapter.notifyDataSetChanged();



  //     ContentResolver cr = getContentResolver();

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   ContentValues value = new ContentValues();
                value.put(Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI,value);*/
                int permission = ActivityCompat.checkSelfPermission(pictureActivity.this, CAMERA);
                if(permission != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(pictureActivity.this,new String[] {CAMERA},REQUEST_CAMERA);
                else
                    takePic();

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
                    if (photoAdapter.getCheckedPic(i))
                        selectedPic.add((String) photoAdapter.getItem(i));
                }
                bundle.putStringArrayList("imagePaths",  selectedPic);
                intent.putExtras(bundle);
                startActivity(intent);
                pictureActivity.this.finish();
            }
        });


    }

    public void takePic(){
        Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
        SimpleDateFormat tmpTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String filename = tmpTime.format(new Date())+".jpg";
        String filepath = Environment.getExternalStorageDirectory().toString();
        File tmpfile = new File(Environment.getExternalStorageDirectory(), filename);
        Uri uri = Uri.fromFile(tmpfile);
        intent.putExtra(EXTRA_OUTPUT, uri);
        intent.putExtra("Picturename",filepath+filename);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permission,int [] grantResult) {
        if(requestCode==REQUEST_CAMERA)
            takePic();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if (data != null) {
            if ((requestCode == 1)) {
                String picname = "";
                data.putExtra("Picturename",picname);
                imagePaths.add(picname);
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(pictureActivity.this,MainActivity.class);
        startActivity(i);
        pictureActivity.this.finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        Cursor cursor = managedQuery(Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        imagePaths = new ArrayList<String>();
        for (int i = cursor.getCount() - 1; i >= 0; i--) {
            cursor.moveToPosition(i);
            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑
            imagePaths.add(filepath);
        }
        photoAdapter = new PhotoAdapter(imagePaths);
        photoAdapter.setPictureClickListener(new PhotoAdapter.PictureClickListener() {
            @Override
            public void onPictureClick(View v) {
                if (!photoAdapter.isEmpty())
                    nextButton.setEnabled(true);
                else
                    nextButton.setEnabled(false);
            }
        });
        gallery.setAdapter(photoAdapter);
        photoAdapter.notifyItemInserted(photoAdapter.getItemCount());
    }
}



