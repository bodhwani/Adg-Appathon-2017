package com.example.tanmay.womensecurity.Entity.IntroSlider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tanmay.womensecurity.Entity.Login.LoginActivity;
import com.example.tanmay.womensecurity.Entity.TopLevel.TopLevelActivity;
import com.example.tanmay.womensecurity.R;

public class IntroSliderActivity extends Activity {
    Button next;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        preferenceManager=new PreferenceManager(this);
        if(!preferenceManager.ifFirstTimeLaunch())
        {
            launchLoginScreen();
            finish();
        }
        if(Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_intro_slider);

        viewPager=(ViewPager)findViewById(R.id.view_pager_intro_slider);
        dotsLayout=(LinearLayout)findViewById(R.id.layout_dots_intro_slider);
        btnSkip=(Button)findViewById(R.id.intro_slider_button_skip);
        btnNext=(Button)findViewById(R.id.intro_slider_button_next);

        //layouts of all intro slides
        layouts=new int[]{
                R.layout.intro_slider_one,
                R.layout.intro_slider_two,
                R.layout.intro_slider_three
        };

        //adding bottom dots for first slide
        addBottomDots(0);

        //making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter=new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLoginScreen();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current= getItem(+1);
                if(current<layouts.length){
                    viewPager.setCurrentItem(current);
                }else{
                    launchLoginScreen();
                }

            }
        });

    }

    @Override
    public void onStart()
    {
        super.onStart();
        //next=(Button)findViewById(R.id.next);
        //next.setOnClickListener(new View.OnClickListener() {
            //@Override
          //  public void onClick(View v) {
          //      startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        //    }
        //});
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void addBottomDots(int currentPage){
        dots = new TextView[layouts.length];

        int[] colorsActive=getResources().getIntArray(R.array.array_dot_active);
        int[] colorInactive=getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for(int i=0;i< dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if(dots.length>0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            //changing the next button 'NEXT'/'GOT IT'
            if(position==layouts.length-1){
                btnNext.setText("GOT IT");
                btnSkip.setVisibility(View.GONE);
            }else{
                btnNext.setText("NEXT");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void launchLoginScreen(){
        preferenceManager.setFirstTimeLaunch(true);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //Making notification bar transparent
    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //View Pager Adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter(){}

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view=(View) object;
            container.removeView(view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(layouts[position],container,false);
            container.addView(view);
            return view;
        }

    }

}
