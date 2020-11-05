package hncc.face_detection_trail;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

import id.zelory.compressor.Compressor;
//import id.zelory.compressor.FileUtil;
//import rx.android.schedulers.AndroidSchedulers;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;

public class MainActivity3 extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView actualImageView;
    private ImageView compressedImageView;
    private TextView actualSizeTextView;
    private TextView compressedSizeTextView;
    private File actualImage;
    private File compressedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actualImageView = (ImageView) findViewById(R.id.actual_image);
        compressedImageView = (ImageView) findViewById(R.id.compressed_image);
        actualSizeTextView = (TextView) findViewById(R.id.actual_size);
        compressedSizeTextView = (TextView) findViewById(R.id.compressed_size);

        actualImageView.setBackgroundColor(getRandomColor());
        clearImage();
    }

    public void chooseImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void compressImage(View view) {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {

            // Compress image in main thread
            //compressedImage = Compressor.getDefault(this).compressToFile(actualImage);
            //setCompressedImage();

            // Compress image to bitmap in main thread
            /*compressedImageView.setImageBitmap(Compressor.getDefault(this).compressToBitmap(actualImage));*/

            // Compress image using RxJava in background thread
//            File ccc=Compressor.com
        }
    }

    public void customCompressImage(View view) {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {
            // Compress image in main thread using custom Compressor
//            compressedImage = new Compressor.Builder(this)
//                    .setMaxWidth(640)
//                    .setMaxHeight(480)
//                    .setQuality(75)
//                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                    .build()
//                    .compressToFile(actualImage);
//            setCompressedImage();

            // Compress image using RxJava in background thread with custom Compressor
           /* new Compressor.Builder(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFileAsObservable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            compressedImage = file;
                            setCompressedImage();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            showError(throwable.getMessage());
                        }
                    });*/
        }
    }

    private void setCompressedImage() {
        compressedImageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
        compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));

        Toast.makeText(this, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());
    }

    private void clearImage() {
        actualImageView.setBackgroundColor(getRandomColor());
        compressedImageView.setImageDrawable(null);
        compressedImageView.setBackgroundColor(getRandomColor());
        compressedSizeTextView.setText("Size : -");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!");
                return;
            }
            try {
                actualImage = FileUtil.from(this, data.getData());
                actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
                actualSizeTextView.setText(String.format("Size : %s", getReadableFileSize(actualImage.length())));
                clearImage();
            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}

































//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.util.Consumer;
//
//import android.os.Bundle;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import java.io.File;
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.util.Random;
//
//import id.zelory.compressor.Compressor;
//
//public class MainActivity3 extends AppCompatActivity {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private File actualImage;
//    private ImageView actualImageView;
//    private TextView actualSizeTextView;
//    /* access modifiers changed from: private */
//    public File compressedImage;
//    private ImageView compressedImageView;
//    private TextView compressedSizeTextView;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//        this.actualImageView = (ImageView) findViewById(R.id.actualImageView);
//        this.compressedImageView = (ImageView) findViewById(R.id.compressedImageView);
//        this.actualSizeTextView = (TextView) findViewById(R.id.actualSizeTextView);
//        this.compressedSizeTextView = (TextView) findViewById(R.id.compressedSizeTextView);
//        this.actualImageView.setBackgroundColor(getRandomColor());
//        clearImage();
//    }
//
//    public void chooseImage(View view) {
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent, 1);
//    }
//
//    public void compressImage(View view) {
//        if (this.actualImage == null) {
//            showError("Please choose an image!");
//        } else {
//            new Compressor(this).compressToFileAsFlowable(this.actualImage).subscribeOn(Schedulers.m25io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
//                public void accept(File file) {
//                    File unused = MainActivity3.this.compressedImage = file;
//                    MainActivity3.this.setCompressedImage();
//                }
//            }, new Consumer<Throwable>() {
//                public void accept(Throwable throwable) {
//                    throwable.printStackTrace();
//                    MainActivity3.this.showError(throwable.getMessage());
//                }
//            });
//        }
//    }
//
//    public void customCompressImage(View view) {
//        if (this.actualImage == null) {
//            showError("Please choose an image!");
//            return;
//        }
//        try {
//            this.compressedImage = new Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(75).setCompressFormat(Bitmap.CompressFormat.WEBP).setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()).compressToFile(this.actualImage);
//            setCompressedImage();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showError(e.getMessage());
//        }
//    }
//
//    /* access modifiers changed from: private */
//    public void setCompressedImage() {
//        this.compressedImageView.setImageBitmap(BitmapFactory.decodeFile(this.compressedImage.getAbsolutePath()));
//        this.compressedSizeTextView.setText(String.format("Size : %s", new Object[]{getReadableFileSize(this.compressedImage.length())}));
//        Toast.makeText(this, "Compressed image save in " + this.compressedImage.getPath(), 1).show();
//        Log.d("Compressor", "Compressed image save in " + this.compressedImage.getPath());
//    }
//
//    private void clearImage() {
//        this.actualImageView.setBackgroundColor(getRandomColor());
//        this.compressedImageView.setImageDrawable((Drawable) null);
//        this.compressedImageView.setBackgroundColor(getRandomColor());
//        this.compressedSizeTextView.setText("Size : -");
//    }
//
//    /* access modifiers changed from: protected */
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode != 1 || resultCode != -1) {
//            return;
//        }
//        if (data == null) {
//            showError("Failed to open picture!");
//            return;
//        }
//        try {
//            this.actualImage = FileUtil.from(this, data.getData());
//            this.actualImageView.setImageBitmap(BitmapFactory.decodeFile(this.actualImage.getAbsolutePath()));
//            this.actualSizeTextView.setText(String.format("Size : %s", new Object[]{getReadableFileSize(this.actualImage.length())}));
//            clearImage();
//        } catch (IOException e) {
//            showError("Failed to read picture data!");
//            e.printStackTrace();
//        }
//    }
//
//    public void showError(String errorMessage) {
//        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
////        Toast.makeText(this, errorMessage, 0).show();
//    }
//
//    private int getRandomColor() {
//        Random rand = new Random();
//        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
//    }
//
//    public String getReadableFileSize(long size) {
//        if (size <= 0) {
//            return "0";
//        }
//        String[] units = {"B", "KB", "MB", "GB", "TB"};
//        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
//        return new DecimalFormat("#,##0.#").format(((double) size) / Math.pow(1024.0d, (double) digitGroups)) + " " + units[digitGroups];
//    }
//}