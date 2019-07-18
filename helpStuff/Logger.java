package testProg.helpStuff;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger implements AutoCloseable{
    BufferedWriter bw;
    private static String path ="D:\\TestLog\\log.txt";

    public static String getPath(){return path;}

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public Logger(){
        try {
            bw = new BufferedWriter(new FileWriter(path,true));
        }catch (IOException e){e.printStackTrace();}
    }

    public void startLog()throws IOException{
        bw.newLine();
        bw.newLine();
        bw.newLine();
        bw.write("######## Start test " + Logger.getCurrentTimeStamp()+" ########");
        bw.newLine();
    }
    public void logLine(String s) throws IOException{
            bw.newLine();
            bw.write(s);
            }
    public void logAndPrintLine(String s) throws IOException{
        bw.newLine();
        bw.write(s);
        System.out.println(s);
    }
    public void logSucces()throws IOException{
        bw.newLine();
        bw.write("#Succes.");
    }
    public void finishLog() throws IOException{
        bw.newLine();
        bw.write("######## FINISH ########");
        bw.close();
    }
    public void finishWithoutLog() throws IOException{
        bw.close();
    }

    @Override
    public void close() throws Exception {
        finishLog();
    }
}
