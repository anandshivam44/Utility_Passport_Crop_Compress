package hncc.face_detection_trail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        int orientation=LinearLayoutManager.VERTICAL;
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TimelineAdapter adapter = new TimelineAdapter(orientation, getItems(), new TimelineAdapter.MyInterface() {
            @Override
            public void interfaceInvoked(int position) {
                if (position==0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(TimelineActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(
                                    new String[]{
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                    102);
                        } else {

                        }
                    }
                }
                else if (position == 1){

                }
                else if (position == 2){

                }
                else if (position == 3){

                }
                else if (position == 4){

                }
            }
        });
        recycler.setAdapter(adapter);
    }
    private List<ListItem> getItems() {
        List<ListItem> items = new ArrayList<>();
        Random random = new Random();

        ListItem item1 = new ListItem();
        item1.setName("Give your phone necessary permission");
        item1.setAddress("Give Permission");
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
        item4.setName("Strong compression");
        item4.setAddress("Compress");
        items.add(item4);

        ListItem item5 = new ListItem();
        item5.setName("Save Image to Pictures");
        item5.setAddress("Save");
        items.add(item5);



        return items;
    }
}