package com.timper.emptyview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.timper.emptyview.EmptyLayout.Status.NO_NETWORK;

public class EmptyLayout extends FrameLayout {

  public enum Status {
    NO_NETWORK,
    EMPTY,
    TRANSPARENT_REFRESH,
    REFRESH,
    NONE,
    ERROR
  }

  private final Context        context;
  private       LayoutInflater inflater;

  private OnRefreshListener onRefreshListener;

  private Status status;

  private String emptyInfo;

  private float    textSize;
  private int      textColor;
  private Drawable res;

  private FrameLayout flRoot;

  private int offHeight = -1;

  public EmptyLayout(Context context) {
    this(context, null);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public EmptyLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyLayout);
    emptyInfo = typedArray.getString(R.styleable.EmptyLayout_el_info);
    textSize = typedArray.getDimension(R.styleable.EmptyLayout_el_textSize, dip2px(8));
    textColor = typedArray.getColor(R.styleable.EmptyLayout_el_textColor, Color.parseColor("#91959A"));
    if (typedArray.hasValue(R.styleable.EmptyLayout_el_res) && typedArray.getDrawable(R.styleable.EmptyLayout_el_res) != null) {
      res = typedArray.getDrawable(R.styleable.EmptyLayout_el_res);
    } else {
      res = context.getDrawable(R.mipmap.icon_empty_nodata);
    }
    typedArray.recycle();
    init();
  }

  private void init() {
    View view = LayoutInflater.from(getContext())
                              .inflate(R.layout.widget_empty, this, false);
    flRoot = view.findViewById(R.id.fl_root);
    this.setOnClickListener(view1 -> {
    });
    addView(view);
    if (this.getVisibility() == GONE) {
      return;
    }
    if (isNetworkAvailable(context)) {
      this.setVisibility(GONE);
    } else {
      this.setStatus(NO_NETWORK);
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  public OnRefreshListener getOnRefreshListener() {
    return onRefreshListener;
  }

  public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
    this.onRefreshListener = onRefreshListener;
  }

  public void setStatus(Status status) {
    this.status = status;
    if (!isNetworkAvailable(context)) {
      this.status = NO_NETWORK;
    }
    flRoot.removeAllViews();
    switch (this.status) {
      case NO_NETWORK:
        View view = inflater.inflate(R.layout.widget_empty_nonet, this, false);
        view.findViewById(R.id.tv_refresh)
            .setOnClickListener(v -> {
              if (onRefreshListener != null && isNetworkAvailable(context)) {
                onRefreshListener.onRefresh();
              }
            });
        flRoot.addView(view);
        setVisibility(View.VISIBLE);
        break;
      case EMPTY:
        LinearLayout noDataView = (LinearLayout) inflater.inflate(R.layout.widget_empty_nodata, null);
        ImageView ivEmpty = noDataView.findViewById(R.id.iv_empty);
        TextView tvEmpty = noDataView.findViewById(R.id.tv_no_data);
        tvEmpty.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvEmpty.setTextColor(textColor);
        ivEmpty.setImageDrawable(res);
        flRoot.addView(noDataView);
        if (!TextUtils.isEmpty(emptyInfo)) {
          tvEmpty.setText(emptyInfo);
        }
        if (offHeight > 0) {
          ivEmpty.setPadding(0, offHeight, 0, 0);
          noDataView.setGravity(Gravity.TOP);
        } else {
          noDataView.setGravity(Gravity.CENTER);
        }
        setVisibility(View.VISIBLE);
        break;
      case REFRESH:
        flRoot.addView(inflater.inflate(R.layout.widget_empty_refresh, null));
        setVisibility(View.VISIBLE);
        break;
      case TRANSPARENT_REFRESH:
        flRoot.addView(inflater.inflate(R.layout.widget_empty_refresh_transparent, null));
        setVisibility(View.VISIBLE);
        break;
      case ERROR:
        View viewError = inflater.inflate(R.layout.widget_empty_error, this, false);
        viewError.findViewById(R.id.tv_refresh)
                 .setOnClickListener(v -> {
                   if (onRefreshListener != null && isNetworkAvailable(context)) {
                     onRefreshListener.onRefresh();
                   }
                 });
        flRoot.addView(viewError);
        setVisibility(View.VISIBLE);
        break;
      case NONE:
        setVisibility(View.GONE);
        break;
      default:
        break;
    }
  }

  public void setInfo(String info) {
    emptyInfo = info;
  }

  public void setOffHeight(int height) {
    offHeight = height;
  }

  public interface OnRefreshListener {
    void onRefresh();
  }

  public int dip2px(float dpValue) {
    final float scale = context.getResources()
                               .getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  public boolean isNetworkAvailable(Context context) {
    ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] info = mgr.getAllNetworkInfo();
    if (info != null) {
      for (int i = 0; i < info.length; i++) {
        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
          return true;
        }
      }
    }
    return false;
  }
}
