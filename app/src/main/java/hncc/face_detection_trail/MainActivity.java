package hncc.face_detection_trail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
//import hncc.face_detection_trail.MainActivity2;


public class MainActivity extends AppCompatActivity {
    final String TAG = "MyTag";
    final  int PICK_IMAGE=101;
    ImageView imageView;
    Button pick;
    Button convert;
    Button saveUncompressed;
    Button compress;
    Bitmap resultBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        pick = findViewById(R.id.pick);
        convert =findViewById(R.id.crop);
        saveUncompressed=findViewById(R.id.save_uncompressed);
        compress=findViewById(R.id.compress);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        saveUncompressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImage(resultBmp);
            }
        });
        compress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);

            }
        });

        Bitmap mBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test_image);
        imageView.setImageBitmap(mBitmap);

        final Paint boxPaint = new Paint();
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);

        final Bitmap tempBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, null);


        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if (!faceDetector.isOperational()) {
                    Toast.makeText(MainActivity.this, "Face Detector Not Working", Toast.LENGTH_SHORT).show();
                }

                Frame frame = new Frame.Builder().setBitmap(tempBitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);
                int a = 1, b = 1;
                double sum = a + b;
                float centre_x, centre_y;
                int start_x, start_y, end_x, end_y;
                for (int i = 0; i < sparseArray.size(); i++) {
                    Face face = sparseArray.valueAt(i);
                    float x = face.getPosition().x;
                    float y = face.getPosition().y;
                    float w = face.getWidth();
                    float h = face.getHeight();
                    centre_x = x + (w / 2);
                    centre_y = y + (h / 2);
                    start_x = (int) (centre_x - 1.5 * (w / 2));
                    start_y = (int) (centre_y - (21 * a) / (10 * (sum)) * (w));
                    end_x = (int) (centre_x + 1.5 * (w / 2));
                    end_y = (int) (centre_y + (21 * b) / (10 * (sum)) * (w));

//                    RectF rectF = new RectF(start_x, start_y, end_x, end_y);
//                    canvas.drawRoundRect(rectF, 2, 2, boxPaint);


                    Rect rect = new Rect(start_x, start_y, end_x, end_y);
                    assert (rect.left < rect.right && rect.top < rect.bottom);
                    resultBmp = Bitmap.createBitmap(rect.right - rect.left, rect.bottom - rect.top, Bitmap.Config.ARGB_8888);
//                    canvas.drawBitmap(tempBitmap, -rect.left, -rect.top, null);
                    new Canvas(resultBmp).drawBitmap(tempBitmap, -rect.left, -rect.top, null);

                }
                imageView.setImageDrawable(new BitmapDrawable(getResources(), resultBmp));
//                SaveImage(resultBmp);
            }
        });
    }

    ///////////////////////////////////////
    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();

        int n = 10000;
        n = generator.nextInt(n);
        String uFname = "Image-" + n + "UNCOMPRESSED.jpg";
//        String cFname = "Image-" + n + "COMPRESSED.jpg";
        File ufile = new File(myDir, uFname);
//        File cfile = new File(myDir, cFname);


        ////////////////////UNCOMPRESSED
        if (ufile.exists()) ufile.delete();
        try {
            FileOutputStream out = new FileOutputStream(ufile);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            ImageInRAM.storeImage(ufile);

            // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Image not saved", Toast.LENGTH_LONG).show();
        }
        ///////////////////COMPRESSED
//        if (cfile.exists()) cfile.delete();
//        try {
//            MainActivity2 obj=new MainActivity2();
//            Object oooo = obj.compressImage(MainActivity.this, ufile, cfile);
////            obj.getReadableFileSize()
////            FileOutputStream out = new FileOutputStream(cfile);
////            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
////
////            // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
////            //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
////            out.flush();
////            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show();
//        }
        //scan
        MediaScannerConnection.scanFile(this, new String[]{ufile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }

}