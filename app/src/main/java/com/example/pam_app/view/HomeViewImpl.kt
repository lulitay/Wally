package com.example.pam_app.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_app.R
import com.example.pam_app.adapter.BucketEntryAdapter
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.utils.DecimalPercentageFormatter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import java.text.DecimalFormat
import java.util.*

class HomeViewImpl @kotlin.jvm.JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr), HomeView {
    private val legendTitle: String
    private val others: String
    private val graphColors: ArrayList<Int> = ArrayList()
    private var adapter: BucketEntryAdapter<BucketEntry>? = null
    private var entryList: MutableList<BucketEntry?>? = null
    override fun bind(entryList: List<BucketEntry>?) {
        this.entryList = entryList?.toMutableList()
        renderGraph(entryList)
        adapter!!.setData(entryList)
    }

    private fun renderGraph(entryList: List<BucketEntry?>?) {
        val chart: PieChart = findViewById(R.id.chart)
        val entries = getBucketData(entryList)
        val dataSet = PieDataSet(entries, legendTitle)
        val data = PieData(dataSet)
        val welcome = findViewById<TextView>(R.id.welcome)
        val noEntries = findViewById<TextView>(R.id.no_entries)
        configureDataSet(dataSet)
        configureChart(chart, data)
        if (entries!!.isEmpty()) {
            chart.visibility = View.GONE
            noEntries.visibility = View.VISIBLE
            welcome.visibility = View.VISIBLE
        } else {
            chart.visibility = View.VISIBLE
            welcome.visibility = View.GONE
            noEntries.visibility = View.GONE
        }
    }

    override fun onBucketEntryAdded(bucketEntry: BucketEntry?) {
        if (bucketEntry != null) {
            adapter!!.showNewBucket(bucketEntry)
            entryList!!.add(bucketEntry)
            renderGraph(entryList)
        }
    }

    private fun setUpList() {
        val listView: RecyclerView = findViewById(R.id.activity)
        adapter = BucketEntryAdapter()
        listView.adapter = adapter
        ViewCompat.setNestedScrollingEnabled(listView, false)
    }

    private fun setUpGraph(context: Context) {
        val chart: PieChart = findViewById(R.id.chart)
        val legend = chart.legend
        val welcome = findViewById<TextView>(R.id.welcome)
        val noEntries = findViewById<TextView>(R.id.no_entries)
        noEntries.visibility = View.VISIBLE
        welcome.visibility = View.VISIBLE
        setUpChart(context, chart)
        setUpLegend(legend)
        setUpChartColors(context)
    }

    private fun setUpLegend(legend: Legend) {
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f
    }

    private fun setUpChart(context: Context, chart: PieChart) {
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.dragDecelerationFrictionCoef = 0.95f
        chart.centerText = context.getString(R.string.metrics)
        chart.setCenterTextSize(20f)
        chart.setCenterTextColor(context.getColor(R.color.colorPrimary))
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f
        chart.setDrawCenterText(true)
        chart.rotationAngle = 0f
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true
        chart.animateY(1400, Easing.EaseInOutQuad)
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTextSize(12f)
        chart.setDrawEntryLabels(false)
        chart.visibility = View.GONE
    }

    private fun setUpChartColors(context: Context) {
        graphColors.add(context.getColor(R.color.graph1))
        graphColors.add(context.getColor(R.color.graph2))
        graphColors.add(context.getColor(R.color.graph3))
        graphColors.add(context.getColor(R.color.graph4))
        graphColors.add(context.getColor(R.color.graph5))
        graphColors.add(context.getColor(R.color.graph6))
    }

    private fun configureChart(chart: PieChart, data: PieData) {
        data.setValueFormatter(DecimalPercentageFormatter(DecimalFormat("###,###,###")))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        chart.data = data
        chart.highlightValues(null)
        chart.invalidate()
    }

    private fun configureDataSet(dataSet: PieDataSet) {
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f
        dataSet.colors = graphColors
    }

    private fun getBucketData(entryList: List<BucketEntry?>?): ArrayList<PieEntry> {
        val reduceEntryList: List<BucketEntry> = entryList!!
                .filter { it!!.date!!.after(firstDayOfMonth) }
                .groupBy { it!!.bucketTitle }
                .mapValues { it -> BucketEntry(it.value.sumOf { it!!.amount }, it.value[0]!!.date, it.value[0]!!.comment, it.value[0]!!.bucketTitle, it.value[0]!!.bucketId) }
                .values
                .toList()
                .sortedBy { it.amount }
        val entries = ArrayList<PieEntry>()
        var otherAmount = 0.0
        for (i in reduceEntryList.indices) {
            val e = reduceEntryList[i]
            if (i < 5) {
                entries.add(PieEntry(e.amount.toFloat(), e.bucketTitle))
            } else {
                otherAmount += e.amount
            }
        }
        if (reduceEntryList.size > 5) {
            entries.add(PieEntry(otherAmount.toFloat(), others))
        }
        return entries
    }

    private val firstDayOfMonth: Date
        get() {
            val today = Calendar.getInstance()
            val next = Calendar.getInstance()
            next.clear()
            next[Calendar.YEAR] = today[Calendar.YEAR]
            next[Calendar.MONTH] = today[Calendar.MONTH]
            next[Calendar.DAY_OF_MONTH] = 1
            return next.time
        }

    init {
        View.inflate(context, R.layout.view_home, this)
        gravity = Gravity.CENTER
        orientation = VERTICAL
        setUpList()
        setUpGraph(context)
        legendTitle = context.getString(R.string.bucket)
        others = context.getString(R.string.others)
    }
}