package com.neocean.app.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2018/10/26.
 */

public class MultiStateView extends FrameLayout {

  public static final int VIEW_STATE_UNKNOWN = -1;

  public static final int VIEW_STATE_CONTENT = 0;

  public static final int VIEW_STATE_ERROR = 1;

  public static final int VIEW_STATE_EMPTY = 2;

  public static final int VIEW_STATE_LOADING = 3;



  private final int errorViewId=R.layout.view_error;
  private final int emptyiewId=R.layout.view_empty;
  private final int loadingViewId=R.layout.view_loading;


  private Animation loadingAnimation= AnimationUtils.loadAnimation(getContext(),R.anim.loading);
  private ImageView loadingImg;

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({VIEW_STATE_UNKNOWN, VIEW_STATE_CONTENT, VIEW_STATE_ERROR, VIEW_STATE_EMPTY, VIEW_STATE_LOADING})
  public @interface ViewState {
  }

  private LayoutInflater mInflater;

  private View mContentView;

  private View mLoadingView;

  private View mErrorView;

  private View mEmptyView;

  private boolean mAnimateViewChanges = false;


  @Nullable
  private StateListener mListener;

  @ViewState
  private int mViewState = VIEW_STATE_UNKNOWN;

  public MultiStateView(Context context) {
    this(context, null);
  }

  public MultiStateView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public MultiStateView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    mInflater = LayoutInflater.from(getContext());
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView);

    int loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1);
    if (loadingViewResId > -1) {
      mLoadingView = mInflater.inflate(loadingViewResId, this, false);
      loadingImg=null;
      addView(mLoadingView, mLoadingView.getLayoutParams());
    }else{
      mLoadingView = mInflater.inflate(loadingViewId, this, false);
      loadingImg=(ImageView)mLoadingView.findViewById(R.id.img_loading);
      addView(mLoadingView, mLoadingView.getLayoutParams());

    }

    int emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1);
    if (emptyViewResId > -1) {
      mEmptyView = mInflater.inflate(emptyViewResId, this, false);
      addView(mEmptyView, mEmptyView.getLayoutParams());
    }else{
      mEmptyView = mInflater.inflate(emptyiewId, this, false);
      addView(mEmptyView, mEmptyView.getLayoutParams());
    }

    int errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1);
    if (errorViewResId > -1) {
      mErrorView = mInflater.inflate(errorViewResId, this, false);
      addView(mErrorView, mErrorView.getLayoutParams());
    }else{
      mErrorView = mInflater.inflate(errorViewId, this, false);
      addView(mErrorView, mErrorView.getLayoutParams());
    }

    int viewState = a.getInt(R.styleable.MultiStateView_msv_viewState, VIEW_STATE_CONTENT);
    mAnimateViewChanges = a.getBoolean(R.styleable.MultiStateView_msv_animateViewChanges, false);

    switch (viewState) {
      case VIEW_STATE_CONTENT:
        mViewState = VIEW_STATE_CONTENT;
        break;

      case VIEW_STATE_ERROR:
        mViewState = VIEW_STATE_ERROR;
        break;

      case VIEW_STATE_EMPTY:
        mViewState = VIEW_STATE_EMPTY;
        break;

      case VIEW_STATE_LOADING:
        mViewState = VIEW_STATE_LOADING;
        break;

      case VIEW_STATE_UNKNOWN:
      default:
        mViewState = VIEW_STATE_UNKNOWN;
        break;
    }

    a.recycle();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (mContentView == null) throw new IllegalArgumentException("Content view is not defined");
    setView(VIEW_STATE_UNKNOWN);

  }


    public void setView(View view,int state){
    if(view==null)
      return;
    View localView=null;
    switch (state){
      case VIEW_STATE_CONTENT:
      removeView(mContentView);
        mContentView=view;
        localView=view;
        break;
      case VIEW_STATE_ERROR:
        removeView(mErrorView);
        mErrorView=view;
        localView=view;
        break;
      case VIEW_STATE_EMPTY:
        removeView(mEmptyView);
        mEmptyView=view;
        localView=view;
        break;
      case VIEW_STATE_LOADING:
        loadingImg=null;
        removeView(mLoadingView);
        mLoadingView=view;
        localView=view;
        break;
    }
    addView(localView);

    }


  /* All of the addView methods have been overridden so that it can obtain the content view via XML
   It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
   any of the setViewForState methods to set views for their given ViewState accordingly */
  @Override
  public void addView(View child) {
    if (isValidContentView(child)) mContentView = child;
    super.addView(child);
  }

  @Override
  public void addView(View child, int index) {
    if (isValidContentView(child)) mContentView = child;
    super.addView(child, index);
  }

  @Override
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    if (isValidContentView(child)) mContentView = child;
    super.addView(child, index, params);
  }

  @Override
  public void addView(View child, ViewGroup.LayoutParams params) {
    if (isValidContentView(child)) mContentView = child;
    super.addView(child, params);
  }

  @Override
  public void addView(View child, int width, int height) {
    if (isValidContentView(child)) mContentView = child;
    super.addView(child, width, height);
  }

  @Override
  protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
    if (isValidContentView(child)) mContentView = child;
    return super.addViewInLayout(child, index, params);
  }

  @Override
  protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
    if (isValidContentView(child)) mContentView = child;
    return super.addViewInLayout(child, index, params, preventRequestLayout);
  }

  /**
   *
   * @param state
   * @return
   */
  @Nullable
  public View getView(@ViewState int state) {
    switch (state) {
      case VIEW_STATE_LOADING:
        return mLoadingView;

      case VIEW_STATE_CONTENT:
        return mContentView;

      case VIEW_STATE_EMPTY:
        return mEmptyView;

      case VIEW_STATE_ERROR:
        return mErrorView;

      default:
        return null;
    }
  }

  /**
   *
   *
   * @return
   */
  @ViewState
  public int getViewState() {
    return mViewState;
  }

  /**
   * 设置状态
   *
   *
   */
  public void setViewState(@ViewState int state) {
    if (state != mViewState) {
      int previous = mViewState;
      mViewState = state;
      setView(previous);
      if (mListener != null) mListener.onStateChanged(mViewState);
    }
  }

  /**
   *
   */
  private void setView(@ViewState int previousState) {
    if(loadingAnimation!=null){
      loadingAnimation.cancel();
    }

    switch (mViewState) {
      case VIEW_STATE_LOADING:
        if (mLoadingView == null) {
          throw new NullPointerException("Loading View");
        }

        if (mContentView != null) mContentView.setVisibility(View.GONE);
        if (mErrorView != null) mErrorView.setVisibility(View.GONE);
        if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);

        if (mAnimateViewChanges) {
          animateLayoutChange(getView(previousState));
        } else {
          mLoadingView.setVisibility(View.VISIBLE);
        }
       if(loadingImg!=null){
         loadingAnimation.setInterpolator(new LinearInterpolator());
         loadingImg.setAnimation(loadingAnimation);
         loadingAnimation.start();
       }


        break;

      case VIEW_STATE_EMPTY:
        if (mEmptyView == null) {
          throw new NullPointerException("Empty View");
        }


        if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
        if (mErrorView != null) mErrorView.setVisibility(View.GONE);
        if (mContentView != null) mContentView.setVisibility(View.GONE);

        if (mAnimateViewChanges) {
          animateLayoutChange(getView(previousState));
        } else {
          mEmptyView.setVisibility(View.VISIBLE);
        }
        break;

      case VIEW_STATE_ERROR:
        if (mErrorView == null) {
          throw new NullPointerException("Error View");
        }


        if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
        if (mContentView != null) mContentView.setVisibility(View.GONE);
        if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);

        if (mAnimateViewChanges) {
          animateLayoutChange(getView(previousState));
        } else {
          mErrorView.setVisibility(View.VISIBLE);
        }
        break;

      case VIEW_STATE_CONTENT:
      default:
        if (mContentView == null) {
          // Should never happen, the view should throw an exception if no content view is present upon creation
          throw new NullPointerException("Content View");
        }


        if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
        if (mErrorView != null) mErrorView.setVisibility(View.GONE);
        if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);

        if (mAnimateViewChanges) {
          animateLayoutChange(getView(previousState));
        } else {
          mContentView.setVisibility(View.VISIBLE);
        }
        break;
    }
  }

  /**
   * Checks if the given {@link View} is valid for the Content View
   *
   * @param view The {@link View} to check
   * @return
   */
  private boolean isValidContentView(View view) {
    if (mContentView != null && mContentView != view) {
      return false;
    }

    return view != mLoadingView && view != mErrorView && view != mEmptyView;
  }

  /**
   * Sets the view for the given view state
   *
   * @param view          The {@link View} to use
   * @param state
   * @param switchToState
   */
  public void setViewForState(View view, @ViewState int state, boolean switchToState) {
    switch (state) {
      case VIEW_STATE_LOADING:
        if (mLoadingView != null) removeView(mLoadingView);
        mLoadingView = view;
        addView(mLoadingView);
        break;

      case VIEW_STATE_EMPTY:
        if (mEmptyView != null) removeView(mEmptyView);
        mEmptyView = view;
        addView(mEmptyView);
        break;

      case VIEW_STATE_ERROR:
        if (mErrorView != null) removeView(mErrorView);
        mErrorView = view;
        addView(mErrorView);
        break;

      case VIEW_STATE_CONTENT:
        if (mContentView != null) removeView(mContentView);
        mContentView = view;
        addView(mContentView);
        break;
    }

    setView(VIEW_STATE_UNKNOWN);
    if (switchToState) setViewState(state);
  }

  /**
   * Sets the
   *
   * @param view  The {@link View} to use
   * @param state
   */
  public void setViewForState(View view, @ViewState int state) {
    setViewForState(view, state, false);
  }

  /**
   * Sets the {@link View} for the given
   *
   * @param layoutRes     Layout resource id
   * @param state
   * @param switchToState
   */
  public void setViewForState(@LayoutRes int layoutRes, @ViewState int state, boolean switchToState) {
    if (mInflater == null) mInflater = LayoutInflater.from(getContext());
    View view = mInflater.inflate(layoutRes, this, false);
    setViewForState(view, state, switchToState);
  }

  /**
   *
   *
   * @param layoutRes Layout resource id
   * @param state     The {@link View} state to set
   */
  public void setViewForState(@LayoutRes int layoutRes, @ViewState int state) {
    setViewForState(layoutRes, state, false);
  }

  /**
   * Sets whether an animate will occur when changing between {@link ViewState}
   *
   * @param animate
   */
  public void setAnimateLayoutChanges(boolean animate) {
    mAnimateViewChanges = animate;
  }

  /**
   * Sets the {@link StateListener} for the view
   *
   * @param listener The {@link StateListener} that will receive callbacks
   */
  public void setStateListener(StateListener listener) {
    mListener = listener;
  }

  /**
   * Animates the layout changes between {@link ViewState}
   *
   * @param previousView The view that it was currently on
   */
  private void animateLayoutChange(@Nullable final View previousView) {
    if (previousView == null) {
      getView(mViewState).setVisibility(View.VISIBLE);
      return;
    }

    previousView.setVisibility(View.VISIBLE);
    ObjectAnimator anim = ObjectAnimator.ofFloat(previousView, "alpha", 1.0f, 0.0f).setDuration(250L);
    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        previousView.setVisibility(View.GONE);
        getView(mViewState).setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(getView(mViewState), "alpha", 0.0f, 1.0f).setDuration(250L).start();
      }
    });
    anim.start();
  }

  public interface StateListener {
    /**
     * Callback for when the {@link ViewState} has changed
     *
     * @param viewState The {@link ViewState} that was switched to
     */
    void onStateChanged(@ViewState int viewState);
  }
}
