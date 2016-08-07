package com.example.arthome.newexchangeworld;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileNotFoundException;

public class pictureActivity extends AppCompatActivity {

    private DisplayMetrics mPhone;
    private final static int CAMERA = 2;
    private final static int GALLERY = 4;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Button cameraButton = (Button) findViewById(R.id.camerabutton);
        Button galleryButton = (Button) findViewById(R.id.gallerybutton);

        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues value = new ContentValues();
                value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        value);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
                startActivityForResult(intent, CAMERA);
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GALLERY);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
           if ((requestCode == GALLERY)) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();

                try {
                    //讀取照片，型態為Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
                    if (bitmap.getWidth() > bitmap.getHeight()) ScalePic(bitmap,
                            mPhone.heightPixels);
                    else ScalePic(bitmap, mPhone.widthPixels);
                } catch (FileNotFoundException e) {
                }
            }
        else if ((requestCode == CAMERA)) {
               Bitmap mbmp = (Bitmap) data.getExtras().get("data");
               ImageView nImg = (ImageView) findViewById(R.id.itemPicture);
               nImg.setImageBitmap(mbmp);
           }
        }
    }

    private void ScalePic(Bitmap bitmap, int phone) {
        //縮放比例預設為1
        float mScale = 1;
        ImageView nImg = (ImageView) findViewById(R.id.itemPicture);
        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if (bitmap.getWidth() > phone) {
            //判斷縮放比例
            mScale = (float) phone / (float) bitmap.getWidth();

            Matrix mMat = new Matrix();
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMat, false);
            nImg.setImageBitmap(mScaleBitmap);
        } else nImg.setImageBitmap(bitmap);
    }

    public void onSubmit(View view) {
        // 確定按鈕
        if (view.getId() == R.id.submitbutton) {
            // 讀取使用者輸入的標題與內容
            EditText itemcontent = (EditText) findViewById(R.id.itemContent);
            EditText itemtitle =  (EditText) findViewById(R.id.itemTitle);
            String titleText = itemtitle.getText().toString();
            String contentText = itemcontent.getText().toString();

            // 取得回傳資料用的Intent物件
            Intent result = getIntent();
            // 設定標題與內容
            result.putExtra("titleText", titleText);
            result.putExtra("contentText", contentText);

            // 設定回應結果為確定
            setResult(Activity.RESULT_OK, result);
        }

        // 結束
        finish();
    }
}
