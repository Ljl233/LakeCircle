package com.example.lakecircle.ui.home.realtime;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.lakecircle.R;
import com.example.lakecircle.ui.home.journey.lake.LakeIntroBean;

import java.util.List;

public class RealtimeAdapter extends RecyclerView.Adapter<RealtimeAdapter.VH> {

    List<LakeIntroBean> lakes;
    LatLng current;
    RealTimeFragment fragment;

    public RealtimeAdapter(List<LakeIntroBean> lakeIntros, LatLng currentLatLng, RealTimeFragment fragment) {
        lakes = lakeIntros;
        this.current = currentLatLng;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realtime, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        float distance = AMapUtils.calculateLineDistance(lakes.get(position).getLatLng(), current);
        holder.bindView(lakes.get(position), distance);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.jump2Retail(lakes.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lakes.size();
    }

    public void changeDataset(List<LakeIntroBean> data) {
        lakes.clear();
        lakes = data;
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tvLakeName, tvDistance;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvDistance = itemView.findViewById(R.id.item_realtime_distance);
            tvLakeName = itemView.findViewById(R.id.item_realtime_lake_name);

        }

        @SuppressLint("DefaultLocale")
        void bindView(LakeIntroBean lakeIntro, float distance) {
            tvLakeName.setText(lakeIntro.getName());

            String dis;
            if (distance > 1000) {
                dis = "999+";
            } else {
                dis = String.format("%.1f", distance) + " Km";
            }
            tvDistance.setText(dis);
        }

    }
}
