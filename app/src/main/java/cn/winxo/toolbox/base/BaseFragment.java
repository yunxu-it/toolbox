package cn.winxo.toolbox.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.winxo.toolbox.util.RxBus;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Author: Winxo
 * Date: 2016/8/26
 * Desc:
 */
public abstract class BaseFragment extends Fragment {

  protected View mContentView;
  private Disposable mDisposable;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (mContentView == null) {
      mContentView = inflater.inflate(setLayoutResourceID(), container, false);
    }
    initPresenter();
    mDisposable = RxBus.getDefault()
        .toObservable(Object.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::handleRxMsg, throwable -> Log.wtf("RxBus Error: ", throwable.getMessage()));
    init(savedInstanceState);
    initView();
    initData();
    return mContentView;
  }

  protected void handleRxMsg(Object object) {

  }

  protected abstract int setLayoutResourceID();

  protected void initPresenter() {

  }

  protected void init(Bundle savedInstanceState) {
  }

  protected abstract void initView();

  protected abstract void initData();

  @Override public void onDestroy() {
    super.onDestroy();
    RxBus.getDefault().unregister(mDisposable);
  }
}
