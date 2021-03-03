package sample;

import gnu.io.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.*;


public class Serial_comm {
    private ComboBox<String> c1;
    private TextField ssid,passwd;
    Vector<Integer> v = new Vector<>();
    TextArea serial_recv;
    public static ThreadGroup tg=new ThreadGroup("serial");
    public static SerialPort serialPort;

    public Serial_comm(ComboBox<String> c1, TextField ssid, TextField passwd, TextArea serial_recv)
    {
        this.c1=c1;
        this.ssid=ssid;
        this.passwd=passwd;
        this.serial_recv=serial_recv;
    }



    private static class SerialReader implements Runnable
    {
        InputStream in;
        TextArea serial_recv;

        public SerialReader ( InputStream in,TextArea serial_recv )
        {

            this.in = in;
            this.serial_recv=serial_recv;
        }

        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                String text="",tmp;
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    tmp = new String(buffer,0,len);
                    System.out.print("q");

                    if(!tmp.isEmpty())
                        text+= tmp;

                    serial_recv.setText(text);
                }

            }
            catch ( IOException ignored)
            {


            }
        }
    }


    /** */
    private static class SerialWriter implements Runnable
    {
        OutputStream out;
        Vector<Integer> v ;

        public SerialWriter ( OutputStream out,Vector<Integer> v  )
        {
            this.out = out;
            this.v=v;
        }

        public void run ()
        {
            try
            {
                Thread.sleep(2000);
                int i=0;
                while ( i<v.capacity()-1 )
                {
                    this.out.write(v.get(i));
                    i++;

                }
            }
            catch (Exception ignored )
            {

            }
            Thread.currentThread().stop();
        }
    }


    private ArrayList listPorts()
    {
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> arr= new ArrayList<>();

        while ( portEnum.hasMoreElements() )
        {
            CommPortIdentifier portIdentifier = (CommPortIdentifier) portEnum.nextElement();
            arr.add(portIdentifier.getName());
        }
        return arr;
    }

    public void ref_ports()
    {
        c1.getItems().clear();
        c1.getItems().addAll( listPorts());
    }


    public String send_serial(TextField serial_info) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException
    {

        InetAddress ip;
        ip=InetAddress.getLocalHost();

        String n="Cfg" + '\n' + ssid.getText() + "\n" +passwd.getText() + "\n" + ip.getHostAddress()+"\n"+',';


        int i=0;
        while(i<n.length())
        {
           v.add((int) n.charAt(i));
           i++;
        }

        String info;
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(c1.getSelectionModel().getSelectedItem());
        if ( portIdentifier.isCurrentlyOwned() )
        {
            info="Błąd: Port jest zajęty";
            //System.out.println(info);
            serial_info.setStyle("-fx-control-inner-background: red;");

            Serial_comm.tg.stop();
            if (Serial_comm.serialPort != null)
                Serial_comm.serialPort.close();

            return info;
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort)
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();



                Thread t1 = new Thread(tg,new Serial_comm.SerialReader(in,serial_recv));
                Thread t2=new Thread(tg,new Serial_comm.SerialWriter(out,v));

                t1.start();
                t2.start();


                info="Wysłano dane";
                serial_info.setStyle("-fx-control-inner-background: green;");
                return info;

            }
            else
            {
                Serial_comm.tg.stop();
                if (Serial_comm.serialPort != null)
                    Serial_comm.serialPort.close();
                
                info="Błąd: nieobsługiwany typ portu";
                serial_info.setStyle("-fx-control-inner-background: red;");
                //System.out.println(info);
                return info;

            }
        }
    }

}
