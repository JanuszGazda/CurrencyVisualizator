import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
 *  Class for displaying data with JFreeChart library
 */


class XYLineChart_AWT extends ApplicationFrame {

    public XYLineChart_AWT(String applicationTitle) {
        super(applicationTitle);
        JFreeChart timelineChart = ChartFactory.createTimeSeriesChart(
                "USD",
                "Date",
                "Value",
                createDataset()
        );

        ChartPanel chartPanel = new ChartPanel(timelineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(660, 467));
        final XYPlot plot = timelineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setBaseShapesVisible(false);
        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset(){
        final TimeSeries usd = new TimeSeries("usd");

        ConnectToPostgres conect = new ConnectToPostgres();

        String select = "SELECT * FROM usd";
        String select2 = "SELECT * FROM data";
        Statement statement = null, statement2=null;
        ResultSet rs, rs2 = null;
        Date data = null;

        try (Connection connection = DriverManager.getConnection(conect.url,conect.user, conect.password)){
            statement = connection.createStatement();
            statement2 = connection.createStatement();
            rs = statement.executeQuery(select);
            rs2 = statement2.executeQuery(select2);
            while(rs.next() && rs2.next()){
                double wartosc = Double.parseDouble(rs.getString("value").trim());
                String dataString = rs2.getString("value");
                DateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
                try {
                    data = dataFormat.parse(dataString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                usd.add(new Day(data), wartosc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(usd);
        return dataset;
    }
}