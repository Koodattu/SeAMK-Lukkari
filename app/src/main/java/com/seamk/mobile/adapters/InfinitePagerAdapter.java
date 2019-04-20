package com.seamk.mobile.adapters;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mikepenz.fastadapter.IItem;
import com.seamk.mobile.R;
import com.seamk.mobile.objects.WinhaCourse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InfinitePagerAdapter extends LoopingPagerAdapter<Integer> {

    List<IItem> dataList;

    public InfinitePagerAdapter(Context context, ArrayList<Integer> itemList, boolean isInfinite, List<IItem> dataList) {
        super(context, itemList, isInfinite);
        this.dataList = dataList;
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        if (viewType == 0)
            return LayoutInflater.from(context).inflate(R.layout.item_winha_bargraph, container, false);
        if (viewType == 1)
            return LayoutInflater.from(context).inflate(R.layout.item_winha_linegraph, container, false);
        if (viewType == 2)
            return LayoutInflater.from(context).inflate(R.layout.item_winha_linegraph, container, false);

        return null;
    }

    @Override
    protected int getItemViewType(int listPosition) {
        return listPosition;
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        if (viewType == 0){
            ((TextView)convertView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.winha_graph_grades));
            BarChart graph = convertView.findViewById(R.id.graph);

            graph.setDrawGridBackground(false);
            graph.setDrawBorders(false);
            graph.getLegend().setEnabled(false);
            Description description = new Description();
            description.setText("");
            graph.setDescription(description);
            graph.setTouchEnabled(false);
            graph.getAxisLeft().setAxisMinimum(0f);
            graph.getAxisRight().setAxisMinimum(0f);
            graph.getAxisLeft().setDrawAxisLine(false);
            graph.getAxisRight().setDrawAxisLine(false);
            graph.getAxisLeft().setDrawLabels(false);
            graph.getAxisRight().setDrawLabels(false);
            graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            graph.getXAxis().setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6, context.getResources().getDisplayMetrics()));
            graph.setExtraBottomOffset(4f);
            graph.getXAxis().setDrawGridLines(false);
            graph.getAxisLeft().setDrawGridLines(false);
            graph.getAxisRight().setDrawGridLines(false);
            graph.getXAxis().setAxisLineWidth(2f);
            graph.getXAxis().setAxisLineColor(R.color.cardview_dark_background);
            graph.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            graph.getRenderer().getPaintRender().setShadowLayer(3, 5, 3, Color.GRAY);

            int grade0 = 0;
            int grade1 = 0;
            int grade2 = 0;
            int grade3 = 0;
            int grade4 = 0;
            int grade5 = 0;

            for (IItem iItem : dataList) {
                WinhaCourse winhaCourse = (WinhaCourse) iItem;
                if (!winhaCourse.getCredits().startsWith("0")) { // in case course gives no credits, we wont use it for calculating gpa
                    if (winhaCourse.getGrade().matches("[0-9]+")) {
                        switch (Integer.parseInt(winhaCourse.getGrade())) {
                            case 0:
                                grade0++;
                                break;
                            case 1:
                                grade1++;
                                break;
                            case 2:
                                grade2++;
                                break;
                            case 3:
                                grade3++;
                                break;
                            case 4:
                                grade4++;
                                break;
                            case 5:
                                grade5++;
                                break;
                        }
                    }
                }
            }

            List<Integer> grades = new ArrayList<>();
            grades.add(grade0);
            grades.add(grade1);
            grades.add(grade2);
            grades.add(grade3);
            grades.add(grade4);
            grades.add(grade5);

            List<BarEntry> entries = new ArrayList<>();

            for (int i = 0; i < grades.size(); i++) {
                entries.add(new BarEntry(i, grades.get(i)));
            }

            BarDataSet dataSet = new BarDataSet(entries, "");

            dataSet.setColors(ColorTemplate.MATERIAL_COLORS[2], ColorTemplate.COLORFUL_COLORS[1], ColorTemplate.COLORFUL_COLORS[2], ColorTemplate.MATERIAL_COLORS[3], ColorTemplate.MATERIAL_COLORS[0], ColorTemplate.JOYFUL_COLORS[0]);
            dataSet.setValueTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6, context.getResources().getDisplayMetrics()));
            BarData barData = new BarData(dataSet);
            barData.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return new DecimalFormat("#").format(value);
                }
            });

            graph.setData(barData);
            graph.invalidate();
        }
        if (viewType == 1){
            ((TextView)convertView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.winha_graph_grade_average_over_time));
            LineChart graph = convertView.findViewById(R.id.graph);

            graph.setDrawGridBackground(false);
            graph.setDrawBorders(false);
            graph.getLegend().setEnabled(false);
            Description description = new Description();
            description.setText("");
            graph.setDescription(description);
            graph.setTouchEnabled(false);
            graph.getAxisLeft().setAxisMinimum(0f);
            graph.getAxisRight().setAxisMinimum(0f);
            graph.getAxisLeft().setAxisMaximum(5f);
            graph.getAxisRight().setAxisMaximum(5f);
            graph.getAxisLeft().setDrawAxisLine(false);
            graph.getAxisRight().setDrawAxisLine(false);
            graph.getAxisLeft().setDrawLabels(true);
            graph.getAxisRight().setDrawLabels(false);
            graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            graph.getXAxis().setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6, context.getResources().getDisplayMetrics()));
            graph.setExtraBottomOffset(4f);
            graph.getXAxis().setDrawGridLines(false);
            graph.getAxisLeft().setDrawGridLines(false);
            graph.getAxisRight().setDrawGridLines(false);
            graph.getXAxis().setAxisLineWidth(2f);
            graph.getXAxis().setAxisLineColor(R.color.cardview_dark_background);
            graph.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            graph.getRenderer().getPaintRender().setShadowLayer(3, 5, 3, Color.GRAY);

            Collections.sort(dataList, new Comparator<IItem>() {
                public int compare(IItem obj1, IItem obj2) {
                    if (((WinhaCourse) obj1).getDateLong() == ((WinhaCourse) obj2).getDateLong()) {
                        return ((WinhaCourse) obj1).getName().compareToIgnoreCase(((WinhaCourse) obj2).getName());
                    } else {
                        return ((WinhaCourse) obj1).getDateLong() < ((WinhaCourse) obj2).getDateLong() ? -1 : 1;
                    }
                }
            });

            List<Entry> entries = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++){
                WinhaCourse winhaCourse = (WinhaCourse) dataList.get(i);
                if (!winhaCourse.getCredits().startsWith("0")) { // in case course gives no credits, we wont use it for calculating gpa
                    if (winhaCourse.getGrade().matches("[0-9]+")) {
                        float value = (float)Integer.parseInt(winhaCourse.getGrade());

                        if (entries.size() > 0){
                            float first = entries.get(entries.size() - 1).getY();
                            float second = Integer.parseInt(winhaCourse.getGrade());
                            float total = first + second;
                            float avg = total / 2;
                            value = avg;
                        }

                        entries.add(new Entry(winhaCourse.getDateLong(), value));
                    }
                }
            }

            LineDataSet dataSet = new LineDataSet(entries, "");

            LineData data = new LineData(dataSet);
            graph.setData(data);
            graph.invalidate(); // refresh

        }
        if (viewType == 2){
            ((TextView)convertView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.winha_graph_cumulative_credits));
            LineChart graph = convertView.findViewById(R.id.graph);

            graph.setDrawGridBackground(false);
            graph.setDrawBorders(false);
            graph.getLegend().setEnabled(false);
            Description description = new Description();
            description.setText("");
            graph.setDescription(description);
            graph.setTouchEnabled(false);
            graph.getAxisLeft().setAxisMinimum(0f);
            graph.getAxisRight().setAxisMinimum(0f);
            graph.getAxisLeft().setDrawAxisLine(false);
            graph.getAxisRight().setDrawAxisLine(false);
            graph.getAxisLeft().setDrawLabels(true);
            graph.getAxisRight().setDrawLabels(false);
            graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            graph.getXAxis().setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6, context.getResources().getDisplayMetrics()));
            graph.setExtraBottomOffset(4f);
            graph.getXAxis().setDrawGridLines(false);
            graph.getAxisLeft().setDrawGridLines(false);
            graph.getAxisRight().setDrawGridLines(false);
            graph.getXAxis().setAxisLineWidth(2f);
            graph.getXAxis().setAxisLineColor(R.color.cardview_dark_background);
            graph.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            graph.getRenderer().getPaintRender().setShadowLayer(3, 5, 3, Color.GRAY);

            Collections.sort(dataList, new Comparator<IItem>() {
                public int compare(IItem obj1, IItem obj2) {
                    if (((WinhaCourse) obj1).getDateLong() == ((WinhaCourse) obj2).getDateLong()) {
                        return ((WinhaCourse) obj1).getName().compareToIgnoreCase(((WinhaCourse) obj2).getName());
                    } else {
                        return ((WinhaCourse) obj1).getDateLong() < ((WinhaCourse) obj2).getDateLong() ? -1 : 1;
                    }
                }
            });

            List<Entry> entries = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++){
                WinhaCourse winhaCourse = (WinhaCourse) dataList.get(i);
                if (!winhaCourse.getCredits().startsWith("0")) { // in case course gives no credits, we wont use it for calculating gpa
                    if (winhaCourse.getGrade().equals("H") || winhaCourse.getGrade().equals("S") || Integer.parseInt(winhaCourse.getGrade()) != 0) {
                        int previous = 0;
                        if (entries.size() > 0)
                            previous = (int)entries.get(entries.size() - 1).getY();
                        entries.add(new Entry(winhaCourse.getDateLong(), Integer.parseInt(winhaCourse.getCredits().substring(0, winhaCourse.getCredits().indexOf(","))) + previous));
                    }
                }
            }

            LineDataSet dataSet = new LineDataSet(entries, "");

            LineData data = new LineData(dataSet);
            graph.setData(data);
            graph.invalidate(); // refresh
        }
    }
}
