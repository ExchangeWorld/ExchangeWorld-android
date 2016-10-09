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
    private final static int EDIT = 0;
    private final static int POST = 1;
    private int poststate;
    private PhotoAdapter photoAdapter;
    private PostAdapter postAdapter;
    private List<String> thumbs;  //存放縮圖的id
    private List<String> imagePaths;  //存放圖片的路徑
    private List<String> selectedPic;
    private List<String> postthumbs;
    private GridView gallery;
    private Button cameraButton;
    private Button nextButton;
    private Button postButton;
    private GridView postgallery;
    private EditText nameText;
    private EditText describeText;
    private Spinner classSpinner;
    private TextView nameTitle;
    private TextView classTitle;
    private TextView describeTitle;
    private ArrayAdapter<String> classList;
    private String[] classType = {"書籍","3C產品","教科書","流行服飾","美妝用品","其他"};

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
        gallery = (GridView) findViewById(R.id.GalleryView);
        postgallery = (GridView) findViewById(R.id.postView);
        nameTitle = (TextView)findViewById(R.id.nameTitle);
        classTitle = (TextView)findViewById(R.id.classTitle);
        describeTitle = (TextView)findViewById(R.id.describeTitle);
        nameText = (EditText)findViewById(R.id.nameText);
        classSpinner = (Spinner)findViewById(R.id.classSpinner);
        describeText = (EditText)findViewById(R.id.describeText);
        postButton = (Button)findViewById(R.id.postButton);
        postgallery.setVisibility(View.GONE);
        nameTitle.setVisibility(View.GONE);
        nameText.setVisibility(View.GONE);
        classTitle.setVisibility(View.GONE);
        classSpinner.setVisibility(View.GONE);
        describeTitle.setVisibility(View.GONE);
        describeText.setVisibility(View.GONE);
        postButton.setVisibility(View.GONE);
        classList = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,classType);
        classSpinner.setAdapter(classList);
        poststate = EDIT;
        setPic();
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
                postgallery.setVisibility(View.VISIBLE);
                nameTitle.setVisibility(View.VISIBLE);
                nameText.setVisibility(View.VISIBLE);
                classTitle.setVisibility(View.VISIBLE);
                classSpinner.setVisibility(View.VISIBLE);
                describeTitle.setVisibility(View.VISIBLE);
                describeText.setVisibility(View.VISIBLE);
                postButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
                cameraButton.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
                poststate = POST;

                postthumbs = new ArrayList<String>();
                selectedPic = new ArrayList<String>();
                for(int i = 0;i<photoAdapter.getCount();i++) {
                    if(photoAdapter.getCheckedPic(i)) {
                        postthumbs.add((String) photoAdapter.getItem(i));
                        selectedPic.add( imagePaths.get(i));
                    }
                }
                postAdapter = new PostAdapter(pictureActivity.this, postthumbs);
                postgallery.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();

            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                File mFile = new File("/sdcard/ExchangeWorld");
                if(!mFile.exists())
                    mFile.mkdirs();
                try{
                    FileWriter fw = new FileWriter("/sdcard/ExchangeWorld/PostData.txt", false);
                    BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                    bw.write("Name = " + nameText.getText().toString());
                    bw.newLine();
                    bw.write("Class = " + classSpinner.getSelectedItem().toString());
                    bw.newLine();
                    bw.write("Description = " + describeText.getText().toString());
                    bw.newLine();
                    bw.write("Picture = ");
                    bw.newLine();
                    for(int i = 0;i<selectedPic.size();i++) {
                        bw.write(selectedPic.get(i));
                        bw.newLine();
                    }
                    bw.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(poststate==POST) {
            postgallery.setVisibility(View.GONE);
            nameTitle.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            classTitle.setVisibility(View.GONE);
            classSpinner.setVisibility(View.GONE);
            describeTitle.setVisibility(View.GONE);
            describeText.setVisibility(View.GONE);
            postButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
            cameraButton.setVisibility(View.VISIBLE);
            gallery.setVisibility(View.VISIBLE);
            poststate = EDIT;
        }
        else
            super.onBackPressed();
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
        photoAdapter = new PhotoAdapter(pictureActivity.this, thumbs);
        gallery.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
    }
}



