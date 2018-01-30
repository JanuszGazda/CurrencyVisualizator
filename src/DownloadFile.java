import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadFile implements Runnable {

    /*
     *  This class Downloads one file from given link
     *  Saves data into buffer and writes it to a file
     */

    private String link;
    private File out;

    public DownloadFile(String link, File  out){
        this.link=link;
        this.out=out;
    }

    @Override
    public void run() {
        try{
            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(this.out);
            BufferedOutputStream bout = new BufferedOutputStream(fos);
            byte data[] = new byte[256];
            int count;
            while((count = in.read(data, 0, 256)) != -1){
                fos.write(data,0,count);
            }
            bout.close();
            in.close();
            System.out.println("Download of a  file complete");

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
