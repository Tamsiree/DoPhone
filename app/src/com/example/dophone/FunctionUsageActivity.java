package com.example.dophone;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.animation.AnimationEasing;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.example.dophone.R;
import com.example.dophone.activity.MainActivity;
import com.example.dophone.activity.ScannerActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FunctionUsageActivity extends Activity implements OnChartGestureListener {
    private PieChart mChart;
    private Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_function_usage);
        findView();

        mChart.setUsePercentValues(true);
        // change the color of the center-hole
        // mChart.setHoleColor(Color.rgb(235, 235, 235));
        //mChart.setHoleColorTransparent(true);

        /*tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));*/
        mChart.setUsePercentValues(true);
        // 设置中间圆盘的颜�??
        mChart.setHoleColor(Color.parseColor("#ffffff"));
        mChart.setHoleRadius(50f);  //半径    
        mChart.setTransparentCircleRadius(58f); // 半�?�明�??   


        // mChart.setHoleRadius(60f);

        mChart.setDescription("");

        mChart.setDrawCenterText(true);

        mChart.setDrawHoleEnabled(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);


        // add a selection listener
        mChart.setOnChartGestureListener(this);
        // mChart.setTouchEnabled(false);

        mChart.setCenterText("Do Phone\n\nFunction");
        mChart.setCenterTextSize(15f);
        mChart.setCenterTextColor(Color.parseColor("#ff33b5e5"));

        setData(7, 1000);

        mChart.animateXY(1500, 1500, AnimationEasing.EasingOption.EaseOutBack);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (MainActivity.appUseCount[i]* mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(MainActivity.name[i % MainActivity.name.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "分布�??");
        dataSet.setSliceSpace(2f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.DKGRAY);
        // data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private void findView() {
        // TODO Auto-generated method stub
        mChart = (PieChart) findViewById(R.id.chart1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.function_usage, menu);
        return true;
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX,
                             float velocityY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        // TODO Auto-generated method stub

    }

    public void fun_ok(View v){
        Intent intent = new Intent();
        intent.setClass(FunctionUsageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
