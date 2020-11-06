package hncc.face_detection_trail;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.repsly.library.timelineview.LineType;
import com.repsly.library.timelineview.TimelineView;

import java.util.List;

/**
 * Adapter for RecyclerView with TimelineView
 */
//com.repsly.library.timeline.TimelineAdapter.ViewHolder
class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private final int orientation;
    private final List<ListItem> items;
    MyInterface myInterface;

    TimelineAdapter(int orientation, List<ListItem> items,MyInterface myInterface) {
        this.orientation = orientation;
        this.items = items;
        this.myInterface=myInterface;
    }

    @Override
    public int getItemViewType(int position) {
            return R.layout.item_vertical;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(items.get(position).getName());
        holder.button.setText(items.get(position).getAddress());
        holder.timelineView.setLineType(getLineType(position));
        holder.timelineView.setNumber(position);
        holder.timelineView.setFillMarker(false);

        // Make first and last markers stroked, others filled
//        if (position == 0 || position + 1 == getItemCount()) {
//            holder.timelineView.setFillMarker(false);
//        } else {
//            holder.timelineView.setFillMarker(true);
//        }
//
//        if (position == 4) {
//            holder.timelineView.setDrawable(AppCompatResources
//                    .getDrawable(holder.timelineView.getContext(),
//                            R.drawable.ic_checked));
//        } else {
//            holder.timelineView.setDrawable(null);
//        }
//
//        // Set every third item active
//        holder.timelineView.setActive(position % 3 == 2);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private LineType getLineType(int position) {
        if (getItemCount() == 1) {
            return LineType.ONLYONE;

        } else {
            if (position == 0) {
                return LineType.BEGIN;

            } else if (position == getItemCount() - 1) {
                return LineType.END;

            } else {
                return LineType.NORMAL;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TimelineView timelineView;
        TextView text;
        TextView button;

        ViewHolder(View view) {
            super(view);
            timelineView = (TimelineView) view.findViewById(R.id.timeline);
            text = (TextView) view.findViewById(R.id.tv_text);
            button = (TextView) view.findViewById(R.id.bt_button);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            myInterface.interfaceInvoked(position);
        }
    }
    public interface MyInterface{

        void interfaceInvoked(int position);
    }

}
