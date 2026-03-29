package utcb.fii.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utcb.fii.model.Measurement;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.List;

public class GraphicManager {
    private static final Logger logger = LoggerFactory.getLogger(GraphicManager.class);

    public void generateGraphic(List<Measurement> measurementList, double slopeA, String output) {
        XYSeries series = new XYSeries("log R = f(log X)");
        for (Measurement m : measurementList) {
            series.add(m.getDecimalLogX(), m.getDecimalLogR());
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createScatterPlot(
                null, "log X [cm]", "log R [W/m2]",
                dataset, PlotOrientation.VERTICAL, false, true, false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setAxisOffset(new org.jfree.chart.ui.RectangleInsets(0, 0, 0, 0));
        Stroke solidStroke = new BasicStroke(1.0f);
        plot.setDomainGridlinePaint(Color.GREEN);
        plot.setDomainGridlineStroke(solidStroke);
        plot.setRangeGridlinePaint(Color.GREEN);
        plot.setRangeGridlineStroke(solidStroke);
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        xAxis.setRange(1.35, 2.05);
        yAxis.setRange(1.25, 2.40);
        xAxis.setNumberFormatOverride(new DecimalFormat("0.00") {
            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                if (number <= 1.35) return toAppendTo.append("0");
                return super.format(number, toAppendTo, pos);
            }
        });

        yAxis.setNumberFormatOverride(new DecimalFormat("0.00") {
            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                if (number <= 1.25) return toAppendTo.append("0");
                return super.format(number, toAppendTo, pos);
            }
        });

        xAxis.setTickUnit(new NumberTickUnit(0.05));
        yAxis.setTickUnit(new NumberTickUnit(0.10));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        String label = String.format("a = Δx/Δy = %.5f", slopeA);
        XYTextAnnotation annotation = new XYTextAnnotation(label, 1.85, 2.30);
        annotation.setFont(new Font("SansSerif", Font.BOLD, 14));
        annotation.setPaint(Color.RED);
        plot.addAnnotation(annotation);

        try {
            ChartUtils.saveChartAsPNG(new java.io.File(output), chart, 1000, 700);
        } catch (java.io.IOException e) {
            logger.error(e.getMessage());
        }
    }
}
