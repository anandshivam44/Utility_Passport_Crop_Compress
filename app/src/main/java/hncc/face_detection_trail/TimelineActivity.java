package hncc.face_detection_trail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.repsly.library.timelineview.TimelineView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

public class TimelineActivity extends AppCompatActivity {
    final String TAG = "MyTag";
    final int PICK_IMAGE = 101;
    final int RW_PERMISSION = 102;
    ImageView imageView;
    Bitmap bitmapFromGallery = null;
    RecyclerView recycler;
    Bitmap croppedBmp;
    File ufile;
    File cfile;
    Context context;
    File myDir;
    String root = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        context = this;

        imageView = findViewById(R.id.image_v);

        int orientation = LinearLayoutManager.VERTICAL;
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TimelineAdapter adapter = new TimelineAdapter(orientation, getItems(), new TimelineAdapter.MyInterface() {
            @Override
            public void interfaceInvoked(int position) {
                if (position == 0) {
                    checkPermission();
                } else if (position == 1) {
                    openGallery();
                } else if (position == 2) {
                    applyML();
                    SaveUncompressedImage(croppedBmp);
                } else if (position == 3) {
                    compressSingleListener(3);
                } else if (position == 4) {
                    saveToPictures();

                }
            }
        });
        recycler.setAdapter(adapter);
//        recycler.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.timeline);


        Bitmap mBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test_image);
        if (bitmapFromGallery == null) {
            bitmapFromGallery = mBitmap;
        }
        final Paint boxPaint = new Paint();
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);

//        final Bitmap tempBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.RGB_565);
//        final Canvas canvas = new Canvas(tempBitmap);
//        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private List<ListItem> getItems() {
        List<ListItem> items = new ArrayList<>();
        Random random = new Random();

        ListItem item1 = new ListItem();
        item1.setName("Give your phone necessary permission");
        item1.setAddress("Allow");
        items.add(item1);

        ListItem item2 = new ListItem();
        item2.setName("Choose Image to work with");
        item2.setAddress("Pick Image");
        items.add(item2);

        ListItem item3 = new ListItem();
        item3.setName("Apply Machine Learning to Crop Image");
        item3.setAddress("Apply");
        items.add(item3);

        ListItem item4 = new ListItem();
        item4.setName("Apply strong compression");
        item4.setAddress("Compress");
        items.add(item4);

        ListItem item5 = new ListItem();
        item5.setName("Save Image to Pictures");
        item5.setAddress("Save");
        items.add(item5);


        return items;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(TimelineActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        RW_PERMISSION);
            } else {
                TimelineView timelineView = recycler.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.timeline);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(TimelineActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        timelineView.setFillMarker(true);
                    }
                }
            }
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            if (data!=null){
                TimelineView timelineView = recycler.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.timeline);
                timelineView.setFillMarker(true);
            }
            Uri imageUri = data.getData();
            try {
                bitmapFromGallery = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmapFromGallery);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //  it is not work here  onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RW_PERMISSION) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context.getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TimelineView timelineView = recycler.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.timeline);
                timelineView.setFillMarker(true);
            }

        }
    }

    private void applyML() {

        final Bitmap tempBitmap = Bitmap.createBitmap(bitmapFromGallery.getWidth(), bitmapFromGallery.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmapFromGallery, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if (!faceDetector.isOperational()) {
            Toast.makeText(TimelineActivity.this, "Face Detector Not Working", Toast.LENGTH_SHORT).show();
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
            croppedBmp = Bitmap.createBitmap(rect.right - rect.left, rect.bottom - rect.top, Bitmap.Config.ARGB_8888);
//                    canvas.drawBitmap(tempBitmap, -rect.left, -rect.top, null);
            new Canvas(croppedBmp).drawBitmap(tempBitmap, -rect.left, -rect.top, null);

        }
        imageView.setImageDrawable(new BitmapDrawable(getResources(), croppedBmp));
        Toast.makeText(this, "ML Applied", Toast.LENGTH_SHORT).show();
    }

    private void SaveUncompressedImage(Bitmap finalBitmap) {

        root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        myDir = new File(root + "/");
        myDir.mkdirs();
        Random generator = new Random();

        SharedPreferences sharedPreferences = getSharedPreferences("fileName", MODE_PRIVATE);
        int index = sharedPreferences.getInt("index", 0);

        String uFname = "UNCOMPRESSED_" + String.valueOf(index) + ".jpg";

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("index", index + 1);
        editor.apply();

        ufile = new File(myDir, uFname);
        //UNCOMPRESSED IMAGE
        if (ufile.exists()) ufile.delete();
        try {
            FileOutputStream out = new FileOutputStream(ufile);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            TimelineView timelineView = recycler.findViewHolderForAdapterPosition(2).itemView.findViewById(R.id.timeline);
            timelineView.setFillMarker(true);
            Toast.makeText(this, "Saved to /Pictures", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Image not saved", Toast.LENGTH_LONG).show();
        }
        //scan gallery
        MediaScannerConnection.scanFile(this, new String[]{ufile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    void compressSingleListener(int gear) {
//        if (mFileList.isEmpty()) {
//            return;
//        }
        Luban.compress(ufile, getFilesDir())
                .putGear(gear)
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Log.i(TAG, "start");
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i("TAG", file.getAbsolutePath());
//                        mImageViews.get(0).setImageURI(Uri.fromFile(file));
                        Toast.makeText(TimelineActivity.this, "Compressed", Toast.LENGTH_SHORT).show();
                        TimelineView timelineView = recycler.findViewHolderForAdapterPosition(3).itemView.findViewById(R.id.timeline);
                        timelineView.setFillMarker(true);
                        Log.d(TAG, "onSuccess: " + Uri.fromFile(file) + " " + file.getAbsolutePath() + " " + file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

    private void saveToPictures() {
        File dir = new File(context.getFilesDir().getAbsolutePath());
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                Log.d(TAG, "onClick: " + f.getName().toString());
                //copyFile(String inputPath, String inputFile, String outputPath)
                moveFile(dir.toString() + "/", f.getName(), root + "/");
            }
        }
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            SharedPreferences sharedPreferences = getSharedPreferences("fileName", MODE_PRIVATE);
            int index = sharedPreferences.getInt("index", 0);

            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + "COMPRESSED_" + String.valueOf(index) + ".jpg");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("index", index + 1);
            editor.apply();

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            TimelineView timelineView = recycler.findViewHolderForAdapterPosition(4).itemView.findViewById(R.id.timeline);
            timelineView.setFillMarker(true);
            new File(inputPath + inputFile).delete();
            Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException fnfe1) {
            Log.e(TAG, fnfe1.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }
}