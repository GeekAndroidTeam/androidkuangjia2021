/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frameutils.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fosung.frameutils.permission.PermissionsManager;
import com.fosung.frameutils.util.ActivityUtil;


/**
 * 所有Acitivity的基类，实现公共操作
 * 1 沉浸式状态栏的封装
 * 2 Activity实例集合的加入和移除
 * 3 权限回调处理
 * 4 onActivityResult回调处理
 * 5 继承此类之后可使用getView（int resId）替代findViewById。
 */
public class BaseFrameActivity extends AppCompatActivity {
    private final SparseArray<View> mViews = new SparseArray<>();
    private ResultActivityHelper resultActivityHelper;
    protected Activity mActivity;
    protected Bundle mBundle; //Activity 销毁/恢复 时 保存/获取 数据
    private boolean isDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;
        mActivity = this;
        ActivityUtil.addActivityToList(this);
        putExtra(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
        mActivity = null;
        ActivityUtil.removeActivityFromList(this);
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void putExtra(Bundle savedInstanceState) {
        mBundle = new Bundle();
        if (savedInstanceState != null) {
            mBundle.putAll(savedInstanceState);
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            mBundle.putAll(getIntent().getExtras());
        }
    }

    public <T extends View> T getView(int resId) {
        T view = (T) mViews.get(resId);
        if (view == null) {
            view = (T) findViewById(resId);
            mViews.put(resId, view);
        }
        return view;
    }

    /**
     * 因为核心库中需要使用ProgressDialog, 如果应用模块自定义ProgressDialog， 则需要在应用模块重写此函数返回自定义的ProgressDialog
     */
    public ProgressDialog getProgressDialog() {
        return null;
    }

    /**
     * 启动带有回调的Activity
     */
    public void startActivityWithCallback(Intent intent, ResultActivityHelper.ResultActivityListener listener) {
        if (resultActivityHelper == null) {
            resultActivityHelper = new ResultActivityHelper(this);
        }
        resultActivityHelper.startActivityForResult(intent, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultActivityHelper != null) {
            resultActivityHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionsManager.getInstance()
                .notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putAll(mBundle);
        }
    }
}