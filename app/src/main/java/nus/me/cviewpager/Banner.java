package nus.me.cviewpager;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nus.me.cviewpager.transformer.BackgroundToForegroundTransformer;
import nus.me.cviewpager.transformer.DepthPageTransformer;
import nus.me.cviewpager.transformer.PageTransformerThree;
import nus.me.cviewpager.transformer.PageTransformerTwo;
import nus.me.cviewpager.transformer.ZoomOutPageTransformer;
import nus.me.cviewpager.transformer.ZoomOutTranformer;

import static android.R.attr.cacheColorHint;
import static android.R.attr.delay;
import static android.R.attr.ignoreGravity;
import static android.R.id.list;

/**
 * Created by nus on 16-10-8.
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {

    private static  final String TAG = "Banner";
    private Context mContext;

    private ViewPager vp; // viewpager
    private TextView tv_title; // title
    private LinearLayout ll_indicatorLayout; // indicator;

    private Handler handler;
    // STATE
    private static int VP_RUN = 1;
    private static int VP_WAIT= 0;
    int flag = 0;

    private ImageCycleViewListener mImageCycleViewListener;

    private List<ImageView> list_iv = new ArrayList<ImageView>();

    private ImageView[] Points ; // point

    private boolean isScrolling = false;

    private boolean isCycle = true;

    private int vp_delay = 3000; //
    private int mCurremtPosition = 0;

    private long releaseTime ; //



    private List<BannerInfo> list_info; //
    private int mIndicatorSelected; //
    private int mIndicatorUnselected; //
    private int realPosition;


    public Banner(Context context) {
        this(context,null);
    }
    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.banner, this, true);
        vp = (ViewPager) findViewById(R.id.vp);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_indicatorLayout= (LinearLayout) findViewById(R.id.ll_indicator);

    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == VP_RUN){
                   vp.setCurrentItem(vp.getCurrentItem()+1);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
//                isScrolling = true;
                while (isScrolling){
                    SystemClock.sleep(vp_delay);
                    Log.e("isTOuch","is"+flag);
                    if (flag == 0)
                    handler.sendEmptyMessage(VP_RUN);
                    else if(flag == 1)
                    {
                        handler.sendEmptyMessage(VP_WAIT);
                    }
                }

            }
        }.start();

    }

    public void setData(final List<BannerInfo> list_info){
        this.setData(list_info,null);
    }


    public void  setData(final List<BannerInfo> list_info,ViewPager.PageTransformer transformer){
        this.setData(list_info,transformer,null);
    }

    public void setData(final List<BannerInfo> list_info, ViewPager.PageTransformer transformer, final ImageCycleViewListener listener){
        for (int i = 0; i < list_info.size(); i++) {
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            params.setMargins(5,0,0,0);
            iv .setLayoutParams(params);
            ll_indicatorLayout.setGravity(Gravity.CENTER|Gravity.RIGHT);
            ll_indicatorLayout.addView(iv );
        }
        vp.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                realPosition = position%list_info.size();

                ImageView iv = new ImageView(mContext);
                iv.setId(position);
                Glide.with(mContext).load(list_info.get(realPosition).url_iv).centerCrop().into(iv);
                container.addView(iv);
                tv_title.setText(list_info.get(realPosition).title);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){

                         listener.onImageClick( list_info.get(realPosition),realPosition,v);
                        }
                    }
                });

                return iv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
        vp.setCurrentItem(Integer.MAX_VALUE/2);
        setPointSelectColor(Integer.MAX_VALUE/2);
        vp.addOnPageChangeListener(this);
        if (transformer!=null){
          vp.setPageTransformer(true,transformer);
        }
        isScrolling =true;

        vp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flag = 1;
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        flag = 0;
                    }
                }

                return false;
            }
        });

    }


    private void setPointSelectColor(int selectPosition){

        for (int i = 0; i < ll_indicatorLayout.getChildCount(); i++) {
            ll_indicatorLayout.getChildAt(i).setBackgroundResource(R.drawable.point_normal);
        }


      ll_indicatorLayout.getChildAt((selectPosition+1)%ll_indicatorLayout.getChildCount()).setBackgroundResource(R.drawable.point_selected);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setPointSelectColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public static interface ImageCycleViewListener {

        /**
         * 单击图片事件
         */
        public void onImageClick(BannerInfo info, int position, View imageView);
    }


}
