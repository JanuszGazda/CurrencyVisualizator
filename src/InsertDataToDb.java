import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class InsertDataToDb {

    private BufferedReader reader;
    private List<String> lines = new ArrayList<>();

    public void csvToList(File file) throws IOException, SQLException {

        //reading csv file into List
        reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        List<String> names = Arrays.asList(lines.get(0).split(";"));

        //Connection to Postgres
        ConnectToPostgres connection = new ConnectToPostgres();
        connection.Connect(false);

        //creating tables if they don't exist
        for (int i = 0; i < names.size()-2; i++) {

            String createTable = "CREATE TABLE IF NOT EXISTS " + cutNames(names.get(i)) + "(Value CHARACTER(14) NOT NULL)";
            PreparedStatement statement = connection.connection.prepareStatement(createTable);
            int tablesCreated = statement.executeUpdate();
            if(tablesCreated==1){System.out.println("created new table");}
        }

        //inserting data to tables
        for (int k = 2; k < lines.size()-4; k++) {
            List<String> values = Arrays.asList(lines.get(k).split(";"));
                for (int l = 0; l < names.size() - 3; l++) {
                    String insertValues = "Insert into " + cutNames(names.get(l)) + " values('" +
                            values.get(l).replace(",", ".").replace("/", "_") + "')";
                    PreparedStatement statement = connection.connection.prepareStatement(insertValues);
                    int rowsAdded = statement.executeUpdate();
                }
        }
        System.out.println("File " + file.getName() + " inserted to DB with "+(lines.size()-4)+" records and "+(names.size()-3)+" currencies");
        lines.clear();
        connection.Close(false);
    }

        //formatting table names
        private String cutNames(String name){
        String newName;
        if(name.contains("10000")){newName = name.substring(5);}
        else if(name.contains("1000")){newName = name.substring(4);}
        else if(name.contains("100")){newName = name.substring(3);}
        else if(name.contains("10")){newName = name.substring(2);}
        else if(name.contains("1")){newName = name.substring(1);}
        else{newName = name;}
        if(name.contains(" ")){newName = name.replace(" ", "_");}
            return newName;
        }
}