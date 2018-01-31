import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

class Location extends JPanel  implements ActionListener {

    private JButton go;
    private JButton dwnld;
    private JButton insertToDb;
    private JButton visualize;
    private JLabel tekst;
    private static JFileChooser chooser;
    private String choosertitle;
    private static String locate;
    private File out;
    private BoxLayout boxLayout;


    //method containing behaviour of buttons
    public Location(List<String> link) {

        boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        tekst = new JLabel("Follow steps one by one");
        tekst.setAlignmentX( Component.CENTER_ALIGNMENT );
        add(tekst);

        //choosing of download location
        go = new JButton("Choose location");
        go.addActionListener(this);
        go.setAlignmentX( Component.CENTER_ALIGNMENT );
        add(go);

        //downloading files with shorter name
        dwnld = new JButton("Download");
        dwnld.addActionListener(e -> {
            for (String aLink : link) {
                out = new File(String.valueOf(chooser.getSelectedFile())
                        + "\\" + aLink.substring(aLink.lastIndexOf('_') + 1));
                new Thread(new DownloadFile(aLink, out)).start();
            }
        });
        dwnld.setAlignmentX( Component.CENTER_ALIGNMENT );
        add(dwnld);

        //inserting downloaded files to DB
        insertToDb = new JButton("Insert to DB");
        insertToDb.addActionListener(e -> {
            InsertDataToDb insert = new InsertDataToDb();
            try {
                for (String aLink : link) {
                    out = new File(String.valueOf(chooser.getSelectedFile())
                            + "\\" + aLink.substring(aLink.lastIndexOf('_') + 1));
                    insert.csvToList(out);
                }
            } catch (IOException | SQLException e1) {
                e1.printStackTrace();
            }
        });
        insertToDb.setAlignmentX( Component.CENTER_ALIGNMENT );
        add(insertToDb);

        //visualizing data
        visualize = new JButton("Visualize USD");
        visualize.addActionListener(e -> {
            XYLineChart_AWT chart = new XYLineChart_AWT("Currency");
            //chart.pack( );
            //RefineryUtilities.centerFrameOnScreen( chart );
            //chart.setVisible( true );
        });
        visualize.setAlignmentX( Component.CENTER_ALIGNMENT );
        add(visualize);
    }

    //JFileChooser
    public void actionPerformed(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): "
                    + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : "
                    + chooser.getSelectedFile());
            locate = String.valueOf(chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(280, 200);
    }
}