package sample;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class download_data {
    private String ip,feed, time,days;
    private TextField ip_id,feed_id,time_id;
    private TextArea days_id;
    private TextField cz,dt;

    private ServerSocket ss= null;
    private Socket s = null;

    String dni_tygodnia[]={"Poniedziałek","Wtorek","Środa","Czwartek" ,"Piątek","Sobota","Niedziela"};


    public boolean listen() throws IOException
    {
        ss = new ServerSocket(1123);
        ss.setSoTimeout(100);

        s=null;
        ip = "nie połączono";


        int i=0;
        while (s==null && i<=50) {
            try {

                s = ss.accept();
            } catch (IOException ignored) {
                System.out.println("nie połączono"+i++);
                ip = "nie połączono";
                ip_id.setStyle("-fx-control-inner-background: red;");

                detele_text();
            }
        }
        if(s==null)
        {
            ss.close();
            return false;
        }

        ip_id.setStyle("-fx-control-inner-background: white;");
        System.out.println("adres"+ s.getInetAddress());
        ip=s.getInetAddress().toString();
        ip=ip.substring(1);


        return true;
    }

    public void detele_text()
    {
        feed="brak";
        time="brak";
        days="brak";
    }

    public String set_data(String d,TextField send_info) throws IOException
    {
        String answer = establish_connection("set data\n" + d);

        System.out.println(answer);
        if(answer==null)
        {
            send_info.setStyle("-fx-control-inner-background: red;");
            return "przerwano połączenie";

        }


        s.close();
        ss.close();

        send_info.setStyle("-fx-control-inner-background: green;");
        return "ustawiono harmonogram";
    }

    public void get_data() throws IOException
    {


        String answer = establish_connection("get data");
        if(answer==null)
            return;

        System.out.println("odp: ");
        System.out.println(answer);
        read_stream(answer);

        s.close();
        ss.close();
    }

    private String establish_connection(String reqest) throws IOException {

        DataOutputStream dOut = new DataOutputStream(s.getOutputStream());


        dOut.writeUTF("XXX" + reqest);
        dOut.flush(); // Send off the data

        DataInputStream dIn = new DataInputStream((new BufferedInputStream(s.getInputStream())));

        char a = ' ';
        String answer="";

        read_timeout(dIn);
        if(dIn.available()>0)
        {
            dIn.readByte(); //trash
            dIn.readByte(); //trash


            while(a!='\r' && dIn.available()>0)
            {
                a=(char) dIn.readByte();
                answer+= a;
            }
        }
        else
        {
            ip = "przerwano połączenie";
            ip_id.setStyle("-fx-control-inner-background: red;");

            dOut.close();
            dIn.close();
            s.close();
            ss.close();

            detele_text();
            return null;
        }

        dOut.close();
        dIn.close();

        return answer;
    }




    private void read_stream(String answer)
    {
        Scanner scanner= new Scanner(answer);

        String d =scanner.nextLine();
        int amount = Integer.parseInt(scanner.nextLine())*10;
        String t=scanner.nextLine()+":"+scanner.nextLine() +" " + scanner.nextLine()+scanner.nextLine();

        cz.setText(scanner.nextLine());
        dt.setText(dni_tygodnia[Integer.parseInt(scanner.nextLine())-1]);

        scanner.close();
        String tmp="";

        for (int i=0;i<7;i++)
        {
            if(d.charAt(i)== '1')
                tmp += dni_tygodnia[i]+"\n";
        }

        feed=String.valueOf(amount);
        days=tmp;
        time=t;
    }


    private void read_timeout(DataInputStream dIn) throws IOException {
        long start = System.currentTimeMillis();
        long current = start;
        int check;

        while((current - start) < 10000)
        {
            current = System.currentTimeMillis();
            check=dIn.available();
            if (check!=0)
            {
                System.out.println(dIn.available());
                break;
            }
        }
    }

    public void update_data()
    {
        ip_id.setText(ip);
        feed_id.setText(feed);
        time_id.setText(time);
        days_id.setText(days);
    }


    public download_data(TextField ip_id,TextField feed_id, TextField time_id,TextArea days_id,TextField cz, TextField dt) {
        ip="brak";
        detele_text();

        this.ip_id=ip_id;
        this.feed_id=feed_id;
        this.time_id=time_id;
        this.days_id=days_id;
        this.cz=cz;
        this.dt=dt;

        update_data();
    }
}