import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {

        //List containing urls of files with currency values
        List<String> pliki = new ArrayList<>();
        pliki.add("http://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2013.csv");
        pliki.add("http://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2014.csv");
        pliki.add("http://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2015.csv");
        pliki.add("http://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2016.csv");
        pliki.add("http://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2017.csv");
        pliki.add("http://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2018.csv");

        //Window with user interface
        JFrame frame = new JFrame("Janusz Gazda");
        Location panel = new Location(pliki);
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        frame.getContentPane().add(panel,"Center");
        frame.setSize(panel.getPreferredSize());
        frame.setVisible(true);

        ConnectToPostgres connection = new ConnectToPostgres();
        connection.Connect(true);

    }
}
