import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/*
 *  Class for displaying data with JFreeChart library
 */


class XYLineChart_AWT extends ApplicationFrame {


    public XYLineChart_AWT(String applicationTitle) {
        super(applicationTitle);
        JFrame f = new JFrame("Chart");
        JPanel p = new JPanel();
        JScrollPane s;
        JCheckBox[] lista;
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        JFreeChart timelineChart = ChartFactory.createTimeSeriesChart(
                "JG",
                "Date",
                "Value",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(timelineChart);
        final XYPlot plot = timelineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setBaseShapesVisible(false);
        plot.setRenderer(renderer);
        setContentPane(chartPanel);
        try {
            lista = whichToShow();
            for(int i=0; i<lista.length; i++){
                p.add(lista[i]);
                int finalI = i;
                lista[i].addItemListener(e -> {
                    if(lista[finalI].isSelected()==true){
                        System.out.println(lista[finalI].getName());
                        dataset.addSeries(newSeries(lista[finalI].getName()));
                    }else if(lista[finalI].isSelected()==false){
                        for (int v = 0; v < dataset.getSeriesCount(); v++) {
                            String name = (String) dataset.getSeriesKey(v);
                            if(name==lista[finalI].getName()){
                                dataset.removeSeries(v);
                            }
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        f.setLayout(new BorderLayout(0, 5));
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.add(chartPanel, BorderLayout.CENTER);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        s = new JScrollPane(p);
        s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        f.add(s, BorderLayout.SOUTH);
        f.setLocation(300,100);
        f.setVisible(true);
        f.setPreferredSize(new java.awt.Dimension(660, 467));
        f.pack();
    }

    private JCheckBox[] whichToShow() throws SQLException {

        ConnectToPostgres.Connect(false);
        Statement r = ConnectToPostgres.connection.createStatement();
        ResultSet rss = r.executeQuery("SELECT table_name" +
                "  FROM information_schema.tables" +
                " WHERE table_schema='public'" +
                "   AND table_type='BASE TABLE';");
        ArrayList<String> currencies = new ArrayList<String>();
        while(rss.next()){
            currencies.add(rss.getString(1));
        }

        JCheckBox[] boxes = new JCheckBox[currencies.size()]; //  Each checkbox will get a name of food from food array.
        for(int i=0; i<boxes.length; i++){
            boxes[i] = new JCheckBox(currencies.get(i));
            boxes[i].setName(currencies.get(i));
        }
        return boxes;
    }

    private TimeSeries newSeries(String name){
        final TimeSeries seria = new TimeSeries(name);
        ConnectToPostgres conect = new ConnectToPostgres();
        String select = "SELECT * FROM "+name;
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

                seria.add(new Day(data), wartosc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seria;
    }

}