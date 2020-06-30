package com.example.lakecircle.ui.home.realtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LakeDetailFragment extends Fragment {

    private int id;
    private SimpleDraweeView image;
    private TextView name;
    private TextView description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lakedetail, container, false);

        Bundle arguments = getArguments();
        id = arguments.getInt("id");

        image = root.findViewById(R.id.lakedetail_image);
        name = root.findViewById(R.id.lakedetail_name);
        description = root.findViewById(R.id.lakedetail_description);

        initView();
        return root;
    }

    private void initView() {
        NetUtil.getInstance().getApi().getLakeInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RealtimeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RealtimeResponse realtimeResponse) {
                        name.setText(realtimeResponse.getData().getName());
                        image.setImageURI(realtimeResponse.getData().getSight_url());
                        description.setText(realtimeResponse.getData().getIntroduction());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(LakeDetailFragment.this.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
