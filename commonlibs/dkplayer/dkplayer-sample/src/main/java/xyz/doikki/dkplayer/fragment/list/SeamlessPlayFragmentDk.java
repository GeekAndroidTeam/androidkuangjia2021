package xyz.doikki.dkplayer.fragment.list;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import xyz.doikki.dkplayer.R;
import xyz.doikki.dkplayer.activity.list.DetailActivityDk;
import xyz.doikki.dkplayer.adapter.VideoRecyclerViewAdapterDk;
import xyz.doikki.dkplayer.bean.VideoBeanDk;
import xyz.doikki.dkplayer.util.IntentKeysDk;
import xyz.doikki.dkplayer.util.TagDk;
import xyz.doikki.dkplayer.util.UtilsDk;
import xyz.doikki.videoplayer.player.VideoView;

/**
 * 无缝播放
 */
public class SeamlessPlayFragmentDk extends RecyclerViewAutoPlayFragmentDk {

    private boolean mSkipToDetail;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recycler_viewdk;
    }

    @Override
    protected void initView() {
        super.initView();

        //提前添加到VideoViewManager，供详情使用
        getVideoViewManager().add(mVideoView, TagDk.SEAMLESS);

        mAdapter.setOnItemClickListener(position -> {
            mSkipToDetail = true;
            Intent intent = new Intent(getActivity(), DetailActivityDk.class);
            Bundle bundle = new Bundle();
            VideoBeanDk videoBean = mVideos.get(position);
            if (mCurPos == position) {
                //需要无缝播放
                bundle.putBoolean(IntentKeysDk.SEAMLESS_PLAY, true);
                bundle.putString(IntentKeysDk.TITLE, videoBean.getTitle());
            } else {
                //无需无缝播放，把相应数据传到详情页
                mVideoView.release();
                //需要把控制器还原
                mController.setPlayState(VideoView.STATE_IDLE);
                bundle.putBoolean(IntentKeysDk.SEAMLESS_PLAY, false);
                bundle.putString(IntentKeysDk.URL, videoBean.getUrl());
                bundle.putString(IntentKeysDk.TITLE, videoBean.getTitle());
                mCurPos = position;
            }
            intent.putExtras(bundle);
            View sharedView = mLinearLayoutManager.findViewByPosition(position).findViewById(R.id.player_container);
            //使用共享元素动画
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), sharedView, DetailActivityDk.VIEW_NAME_PLAYER_CONTAINER);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        });
    }

    @Override
    protected void startPlay(int position) {
        mVideoView.setVideoController(mController);
        super.startPlay(position);
    }

    @Override
    protected void pause() {
        if (!mSkipToDetail) {
            super.pause();
        }
    }

    @Override
    protected void resume() {
        if (mSkipToDetail) {
            mSkipToDetail = false;
        } else {
            super.resume();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || !addTransitionListener()) {
            restoreVideoView();
        }
    }

    @RequiresApi(21)
    private boolean addTransitionListener() {
        final Transition transition = getActivity().getWindow().getSharedElementExitTransition();
        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    restoreVideoView();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
            return true;
        }
        return false;
    }

    private void restoreVideoView() {
        //还原播放器
        View itemView = mLinearLayoutManager.findViewByPosition(mCurPos);
        VideoRecyclerViewAdapterDk.VideoHolder viewHolder = (VideoRecyclerViewAdapterDk.VideoHolder) itemView.getTag();
        mVideoView = getVideoViewManager().get(TagDk.SEAMLESS);
        UtilsDk.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);

        mController.addControlComponent(viewHolder.mPrepareView, true);
        mController.setPlayState(mVideoView.getCurrentPlayState());
        mController.setPlayerState(mVideoView.getCurrentPlayerState());

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mVideoView.setVideoController(mController);
            }
        }, 100);
    }
}