package com.example.gjaves;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gjaves.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class predict_then_save extends AppCompatActivity {


    TextView result,confidence;
    Button takepicture, map, weather;
    ImageView imagepic;

    int imageSize = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict_then_save);
        result = findViewById(R.id.prediction) ;
        confidence = findViewById(R.id.confidences) ;
        takepicture = findViewById(R.id.takeapicture);
        map = findViewById(R.id.googleMap);
        weather = findViewById(R.id.weather);
        imagepic = findViewById(R.id.image);



        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
    Intent intent =  new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent,1);

}else {
    requestPermissions(new String []{Manifest.permission.CAMERA}, 100);
}

            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),location_viewer.class);
                startActivity(intent);
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),myweather.class);
                startActivity(intent);
            }
        });






    }





public void classifyImage(Bitmap picture){
    try {
        Model model = Model.newInstance(getApplicationContext());


        // Creates inputs for reference.
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize*imageSize *3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int [] intvalues = new int [imageSize * imageSize];
picture.getPixels(intvalues, 0 ,picture.getWidth(),0,0,picture.getWidth(), picture.getHeight());
int pixel = 0;
for(int i = 0; i < imageSize; i++){
for(int j =0; j < imageSize; j++){
    int val = intvalues[pixel++];
    byteBuffer.putFloat(((val >> 16) &0xFF)* (1.f / 1));
    byteBuffer.putFloat(((val >> 8) &0xFF)* (1.f / 1));
    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
    }
        }

        inputFeature0.loadBuffer(byteBuffer);

        // Runs model inference and gets result.
        Model.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


        float[] confidences = outputFeature0.getFloatArray();
        int maxPos = 0;
       float maxConfidence =0;
       for(int i = 0 ; i < confidences.length; i++){
           if(confidences[i] > maxConfidence) {
               maxConfidence = confidences[i];
               maxPos = i;
           }
       }
       String[] classes = {"my picture","random people","random things"};

       result.setText(classes[maxPos]);

       String s =" ";
       for(int i = 0; i < classes.length;i++){
           s+= String.format("%s: %.1f%%\n", classes[i],confidences[i]*100);
       }
       confidence.setText(s);


       // Releases model resources if no longer used.
        model.close();
    } catch (IOException e) {
       e.printStackTrace();
    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imagepic.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }
}