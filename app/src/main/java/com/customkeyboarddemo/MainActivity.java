package com.customkeyboarddemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CAPTURE_IMAGE = 2;
    static MainActivity mainActivity;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        if(getIntent().getExtras() != null){
            if(getIntent().getStringExtra("request").equals("camera"))
                openCamera();
            else
                openGallery();
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(),"image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
}
    public void openGallery(){

        Intent intent;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        }
        intent.putExtra("return-data", true);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        if (resultCode == RESULT_OK) {


            if (requestCode == RESULT_CAPTURE_IMAGE ) {
                File f = new File(Environment.getExternalStorageDirectory().toString()+File.separator+"image.jpg");
                Uri imgUri = Uri.fromFile(f);
                selectedImageUri = imgUri;
            }else if (requestCode == RESULT_LOAD_IMAGE) {
                // Get the url from data
                selectedImageUri = data.getData();
            }
            if(null != selectedImageUri) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "testing");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
                intent.setType("image/jpeg");
                intent.setPackage("com.whatsapp");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
            finish();
        }

    }
}
