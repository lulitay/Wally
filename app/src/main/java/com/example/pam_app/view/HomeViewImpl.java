package com.example.pam_app.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.adapter.BucketEntryHomeAdapter;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.utils.DecimalPercentageFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static android.view.Gravity.CENTER;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class HomeViewImpl extends LinearLayout implements HomeView {

    private BucketEntryHomeAdapter adapter;
    private final String legendTitle;
    private final String others;

    public HomeViewImpl(Context context) {
        this(context, null);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        inflate(context, R.layout.view_home, this);
        setGravity(CENTER);
        setOrientation(VERTICAL);
        setUpList();
        setUpGraph(context);
        legendTitle = context.getString(R.string.bucket);
        others = context.getString(R.string.others);
    }

    @Override
    public void bind(final List<BucketEntry> entryList) {
        adapter.update(entryList);
        PieChart chart = findViewById(R.id.chart);

        ArrayList<PieEntry> entries = getBucketData(entryList);

        PieDataSet dataSet = new PieDataSet(entries, legendTitle);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();//TODO check

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new DecimalPercentageFormatter(new DecimalFormat("###,###,###")));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();

        TextView welcome = findViewById(R.id.welcome);
        TextView no_entries = findViewById(R.id.no_entries);
        if (entryList.size() == 0) {
            chart.setVisibility(View.GONE);
            no_entries.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.VISIBLE);
        }
        else {
            chart.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.GONE);
            no_entries.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBucketEntryAdded(final BucketEntry bucketEntry) {
        if (bucketEntry != null) {
            adapter.showNewBucket(bucketEntry);
        }
    }

    private void setUpList() {
        final RecyclerView listView = findViewById(R.id.activity);
        adapter = new BucketEntryHomeAdapter();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }

    private void setUpGraph(final Context context) {
        PieChart chart = findViewById(R.id.chart);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterText(context.getString(R.string.metrics));
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(context.getColor(R.color.colorPrimary));

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
        chart.setDrawEntryLabels(false);

        TextView welcome = findViewById(R.id.welcome);
        TextView no_entries = findViewById(R.id.no_entries);
        chart.setVisibility(View.GONE);
        no_entries.setVisibility(View.VISIBLE);
        welcome.setVisibility(View.VISIBLE);
    }

    private ArrayList<PieEntry> getBucketData(final List<BucketEntry> entryList) {
        List<BucketEntry> reduceEntryList = entryList.stream()
                .filter((e) -> e.date.after(getFirstDayOfMonth()))
                .collect(Collectors.collectingAndThen(
                    Collectors.toMap(BucketEntry::getBucketTitle,
                            Function.identity(),
                            (left, right) -> new BucketEntry(left.amount + right.amount, left.date, left.comment, left.bucketTitle)),
                    m -> new ArrayList<>(m.values())
                ));
        reduceEntryList.sort((e1, e2) -> (int) (e2.amount - e1.amount));
        ArrayList<PieEntry> entries = new ArrayList<>();
        double otherAmount = 0;
        for (int i = 0; i < reduceEntryList.size(); i++) {
            BucketEntry e = reduceEntryList.get(i);
            if (i < 5) {
                entries.add(new PieEntry((float) e.amount, e.bucketTitle));
            }
            else {
                otherAmount += e.amount;
            }
        }
        if (reduceEntryList.size() > 5) {
            entries.add(new PieEntry((float) otherAmount, others));
        }
        return entries;
    }

    private Date getFirstDayOfMonth() {
        final Calendar today = getInstance();
        final Calendar next = getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH));
        next.set(DAY_OF_MONTH, 1);
        return next.getTime();
    }
}
