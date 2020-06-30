package com.example.lakecircle.ui.home.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.NetUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ActivitiesListFragment extends Fragment{

    private int TYPE; // 1-unfinished ; 0-finished

    private RecyclerView mActivitiesRv;
    private ActivitiesAdapter mAdapter;
    private NavController mNavController;

    private static int unfinishedPage = 1;
    private static int finishedPage = 1;

    private ActivitiesAdapter.OnItemClickListener mOnItemClickListener = new ActivitiesAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int id) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            mNavController.navigate(R.id.action_activities_detail, bundle);
        }

        @Override
        public void onMoreClick() {
            if( TYPE == 1) getUnfinished(false);
            else getFinished(false);
        }

        @Override
        public void onRefreshClick() {
            if( TYPE == 1) getUnfinished(true);
            else getFinished(true);
        }
    };

    public ActivitiesListFragment (int type) {
        TYPE = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities_list, container, false);
        mNavController = NavHostFragment.findNavController(this);

        mActivitiesRv = view.findViewById(R.id.rv_activities_list);
        mAdapter = new ActivitiesAdapter(new ArrayList<>(), getContext(), mOnItemClickListener);
        mActivitiesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mActivitiesRv.setAdapter(mAdapter);

        if (TYPE == 1) getUnfinished(true);
        else getFinished(true);

        return view;
    }

    private void getUnfinished(boolean ifRefresh) {
        if (ifRefresh) unfinishedPage = 1;
        NetUtil.getInstance().getApi().getUnfinishedActivities(unfinishedPage+"", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<Activities>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponseModel<Activities> activitiesBaseResponseModel) {
                        List<Activities> activities =  activitiesBaseResponseModel.getData();
                        if ( unfinishedPage == 1 )
                            mAdapter.refresh(activities);
                        else {
                            mAdapter.add(activities);
                            unfinishedPage++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("ActivitiesListFragment", "get unfinished activities "+unfinishedPage+" fail");
                        showError("加载失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void getFinished(boolean ifRefresh) {
        if (ifRefresh) finishedPage = 1;
        NetUtil.getInstance().getApi().getFinishedActivities(finishedPage+"", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<Activities>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponseModel<Activities> activitiesBaseResponseModel) {
                        List<Activities> activities =  activitiesBaseResponseModel.getData();
                        if ( finishedPage == 1 )
                            mAdapter.refresh(activities);
                        else {
                            mAdapter.add(activities);
                            finishedPage++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("ActivitiesListFragment", "get finished activities "+finishedPage+" fail");
                        showError("加载失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }


    private void showError(String error) {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(error)
                .create();
        tipDialog.show();
        this.getView().postDelayed(() -> {
            tipDialog.dismiss();
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
        }, 1500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
    }
}
