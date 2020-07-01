package com.example.lakecircle.ui.home.merchant;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.example.lakecircle.R;
import com.example.lakecircle.data.Merchant.Merchant;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

public class MerchantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Merchant> merchants;
    private Context mContext;
    private OnMerchantClickListener mOnMerchantClickListener;

    private int sortMode = 1;  //0-按兑换次数， 1-按距离
    private LatLng location = null;

    public MerchantAdapter(List<Merchant> merchants, Context mContext,
                           OnMerchantClickListener mOnMerchantClickListener, int sortMode,
                           LatLng location) {
        this.merchants = merchants;
        this.mContext = mContext;
        this.mOnMerchantClickListener = mOnMerchantClickListener;
        this.sortMode = sortMode;
        this.location = location;
    }

    public void setSortMode(int sortMode) {
        if ( sortMode != this.sortMode )
            sort();
        this.sortMode = sortMode;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public interface OnMerchantClickListener {
        void onItemClick(Merchant merchant);
        void onRefreshClick();
    }

    public void setData(List<Merchant> merchantList) {
        Log.e("TAG", "set data");
        merchants = merchantList;
        sort();
    }

    private void sort() {
        if ( sortMode == 0)
            Collections.sort(merchants, new MerchantChangeNumComparator());
        else
            Collections.sort(merchants, new MerchantDistanceComparator(location));
        refresh();
    }

    public void refresh() {
        Log.e("TAG", "refresh");
        int size = merchants.size();
        notifyItemRangeRemoved(0,size);
        notifyItemRangeInserted(0, size);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new MerchantViewHolder(LayoutInflater.from(mContext).inflate(
                    R.layout.item_merchant_list, parent, false));
        else
            return new NoneViewHolder(LayoutInflater.from(mContext).inflate(
                    android.R.layout.simple_list_item_1, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof MerchantViewHolder ) {
            MerchantViewHolder h = (MerchantViewHolder) holder;
            h.mPicSdv.setImageURI(merchants.get(position).getAvatar_url());
            h.mNameTv.setText(merchants.get(position).getName());
            h.mIntroTv.setText(merchants.get(position).getIntro());
            h.itemView.setOnClickListener(v -> mOnMerchantClickListener.onItemClick(merchants.get(position)));
        } else if ( holder instanceof NoneViewHolder) {
            NoneViewHolder mHolder = (NoneViewHolder) holder;
            mHolder.textView.setText("找不到商家,点击尝试刷新");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mOnMerchantClickListener.onRefreshClick());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ( merchants.size() == 0 ) return 1;
        else return 0;
    }

    @Override
    public int getItemCount() {
        if ( merchants.size() == 0 )
            return 1;
        return merchants.size();
    }

    class MerchantViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView mPicSdv;
        private TextView mNameTv;
        private TextView mIntroTv;

        public MerchantViewHolder(@NonNull View itemView) {
            super(itemView);
            mPicSdv = itemView.findViewById(R.id.sdv_item_merchant_list);
            mNameTv = itemView.findViewById(R.id.tv_name_item_merchant_list);
            mIntroTv = itemView.findViewById(R.id.tv_intro_item_merchant_list);
        }
    }

    public class NoneViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public NoneViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
