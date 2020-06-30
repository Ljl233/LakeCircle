package com.example.lakecircle.ui.mine.govern.problemview;

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
import com.example.lakecircle.commonUtils.NetUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class ProblemListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private int mType; //列表种类 1-待解决 0-已解决
    private static int solvedPage = 1;
    private static int unsolvedPage = 1;
    private ProblemListAdapter mAdapter;
    private NavController navController;
    private ProblemListAdapter.ProblemItemListener problemItemListener = new ProblemListAdapter.ProblemItemListener() {
        @Override
        public void onItemClickListener(int id) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            navController.navigate(R.id.action_solve_problem,bundle);
        }

        @Override
        public void onMoreClickListener() {
            if (mType == 1) getUnsolvedProblemList(false);
            else getSolvedProblemList(false);
        }

        @Override
        public void onRefreshClickListener() {
            if (mType == 1) getUnsolvedProblemList(true);
            else getSolvedProblemList(true);
        }
    };

    public ProblemListFragment () { }

    public static ProblemListFragment newInstance(int type) {
        ProblemListFragment fragment = new ProblemListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = findNavController(this);
        if ( getArguments() != null )
            mType = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_list, container, false);

        mRecyclerView = view.findViewById(R.id.rv_problem_list);

        mAdapter = new ProblemListAdapter(new ArrayList<>(), mType, problemItemListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        if ( mType == 1 )
            getUnsolvedProblemList(true);
        else
            getSolvedProblemList(true);

        return view;
    }

    private void getUnsolvedProblemList(boolean ifRefresh) {
        if ( ifRefresh ) unsolvedPage = 1;
        NetUtil.getInstance().getApi().getUnsolvedProblems(unsolvedPage+"", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProblemListResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ProblemListResponseBean problemListResponseBeanResponse) {
                        List<ProblemListResponseBean.DataBean.Problem> t = problemListResponseBeanResponse.getData().getData();
                            List<Problem> problemList = new ArrayList<>();
                            for (ProblemListResponseBean.DataBean.Problem data : t)
                                problemList.add(new Problem(data.getId(), data.isIs_solved(), data.getLocation(), data.getPicture_url()));
                            if (unsolvedPage == 1)
                                mAdapter.refresh(problemList);
                            else {
                                mAdapter.add(problemList);
                                unsolvedPage++;
                            }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("ProblemListFragment", "get unsolved problem "+unsolvedPage+"page list fail");
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void getSolvedProblemList(boolean ifRefresh) {
        if ( ifRefresh ) solvedPage = 1;
        NetUtil.getInstance().getApi().getSolvedProblems(solvedPage+"", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProblemListResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ProblemListResponseBean problemListResponseBeanResponse) {
                            List<ProblemListResponseBean.DataBean.Problem> t = problemListResponseBeanResponse.getData().getData();
                            List<Problem> problemList = new ArrayList<>();
                            for (ProblemListResponseBean.DataBean.Problem data : t)
                                problemList.add(new Problem(data.getId(), data.isIs_solved(), data.getLocation(), data.getPicture_url()));
                            if (solvedPage == 1)
                                mAdapter.refresh(problemList);
                            else {
                                mAdapter.add(problemList);
                                solvedPage++;
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("ProblemListFragment", "get solved problem "+solvedPage+"page list fail");
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

}
