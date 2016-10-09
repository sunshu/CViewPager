package nus.me.cviewpager;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nus.me.cviewpager.transformer.AccordionTransformer;
import nus.me.cviewpager.transformer.BackgroundToForegroundTransformer;
import nus.me.cviewpager.transformer.FlipHorizontalTransformer;
import nus.me.cviewpager.transformer.PageTransformerOne;
import nus.me.cviewpager.transformer.PageTransformerThree;
import nus.me.cviewpager.transformer.PageTransformerTwo;
import nus.me.cviewpager.transformer.RotateDownTransformer;
import nus.me.cviewpager.transformer.ZoomInTransformer;
import nus.me.cviewpager.transformer.ZoomOutPageTransformer;


public class MainActivity extends AppCompatActivity {


    private Banner banner1;
    private Banner banner2;
    private Banner banner3;
    private Banner banner4;
    private ArrayList<BannerInfo> list;
    private String[] urls;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner1 = (Banner) findViewById(R.id.banner1);
        banner2 = (Banner) findViewById(R.id.banner2);
        banner3 = (Banner) findViewById(R.id.banner3);
        banner4 = (Banner) findViewById(R.id.banner4);


        urls = new String[]{
                "http://nuuneoi.com/uploads/source/playstore/cover.jpg",
                "http://pic105.nipic.com/file/20160725/13948737_152610217000_2.jpg",
                "http://pic106.nipic.com/file/20160812/13948737_155734476000_2.jpg",
                "http://pic104.nipic.com/file/20160719/21897230_230149646000_2.jpg",};

        list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerInfo info = new BannerInfo();
            info.title = "position" + i;
            info.url_iv = urls[i];
            list.add(info);
        }
        banner1.setData(list, new PageTransformerThree());
        banner2.setData(list, new PageTransformerTwo());
        banner3.setData(list, new PageTransformerOne());
        banner4.setData(list, new RotateDownTransformer(), new Banner.ImageCycleViewListener() {
            @Override
            public void onImageClick(BannerInfo info, int position, View imageView) {
                Toast.makeText(MainActivity.this,"click"+position,Toast.LENGTH_SHORT).show();
            }
        });


    }

}
