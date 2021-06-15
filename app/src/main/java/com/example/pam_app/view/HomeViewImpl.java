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
import com.example.pam_app.adapter.BucketEntryAdapter;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.utils.DecimalPercentageFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

    private final String legendTitle;
    private final String others;
    private final ArrayList<Integer> graphColors;
    private BucketEntryAdapter<BucketEntry> adapter;
    private List<BucketEntry> entryList;

    public HomeViewImpl(Context context) {
        this(context, null);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HomeViewImpl(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        graphColors = new ArrayList<>();

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
        this.entryList = entryList;
        renderGraph(entryList);
        adapter.setData(entryList);
    }

    private void renderGraph(final List<BucketEntry> entryList) {
        final PieChart chart = findViewById(R.id.chart);

        final ArrayList<PieEntry> entries = getBucketData(entryList);

        final PieDataSet dataSet = new PieDataSet(entries, legendTitle);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(graphColors);

        final PieData data = new PieData(dataSet);
        data.setValueFormatter(new DecimalPercentageFormatter(new DecimalFormat("###,###,###")));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();

        final TextView welcome = findViewById(R.id.welcome);
        final TextView no_entries = findViewById(R.id.no_entries);
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
            entryList.add(bucketEntry);
            renderGraph(entryList);
        }
    }

    private void setUpList() {
        final RecyclerView listView = findViewById(R.id.activity);
        adapter = new BucketEntryAdapter<>();
        listView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(listView, false);
    }

    private void setUpGraph(final Context context) {
        final PieChart chart = findViewById(R.id.chart);
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

        final Legend l = chart.getLegend();
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

        final TextView welcome = findViewById(R.id.welcome);
        final TextView no_entries = findViewById(R.id.no_entries);
        chart.setVisibility(View.GONE);
        no_entries.setVisibility(View.VISIBLE);
        welcome.setVisibility(View.VISIBLE);

        graphColors.add(context.getColor(R.color.graph1));
        graphColors.add(context.getColor(R.color.graph2));
        graphColors.add(context.getColor(R.color.graph3));
        graphColors.add(context.getColor(R.color.graph4));
        graphColors.add(context.getColor(R.color.graph5));
        graphColors.add(context.getColor(R.color.graph6));
    }

    private ArrayList<PieEntry> getBucketData(final List<BucketEntry> entryList) {
        final List<BucketEntry> reduceEntryList = entryList.stream()
                .filter((e) -> e.date.after(getFirstDayOfMonth()))
                .collect(Collectors.collectingAndThen(
                    Collectors.toMap(BucketEntry::getBucketTitle,
                            Function.identity(),
                            (left, right) -> new BucketEntry(left.amount + right.amount, left.date, left.comment, left.bucketTitle)),
                    m -> new ArrayList<>(m.values())
                ));
        reduceEntryList.sort((e1, e2) -> (int) (e2.amount - e1.amount));
        final ArrayList<PieEntry> entries = new ArrayList<>();
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
