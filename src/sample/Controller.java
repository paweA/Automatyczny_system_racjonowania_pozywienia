package sample;

import javafx.scene.control.*;

import java.io.IOException;
import java.util.Calendar;

public class Controller {

    private download_data data;
    private Feed_buttons feed_buttons;
    private Time_buttons time_buttons;
    private Days_buttons days_buttons;
    private Serial_comm serial;


    public TextField deviceip_id,feed_id,time_id;
    public TextArea days_id;
    public Button reflesh;

    public RadioButton r1,r2,r3,r4;
    public Spinner<Integer> s5;

    public Spinner<Integer> t1,t2,t3,t4;
    public CheckBox k1;

    public CheckBox d1,d2,d3,d4,d5,d6,d7,da;

    public ComboBox<String> c1;

    public TextField ssid,passwd;

    public TextField cz,dt;

    public TextField serial_info;
    public TextField send_info;

    public TextArea serial_recv;





    public void initialize()
    {

        data = new download_data(deviceip_id,feed_id,time_id,days_id,cz,dt);
        feed_buttons= new Feed_buttons(r1,r2,r3,r4,s5);
        time_buttons= new Time_buttons(t1,t2,t3,t4,k1);
        days_buttons= new Days_buttons(d1,d2,d3,d4,d5,d6,d7,da);
        serial = new Serial_comm(c1,ssid,passwd,serial_recv);


    }

    public void radio()
    {
        feed_buttons.set_choice();
    }

    public void select()
    {
        days_buttons.select();
    }
    public void time_checkBox()
    {
        time_buttons.select();
    }

    public void ref()
    {
        Serial_comm.tg.stop();
        if (Serial_comm.serialPort != null)
            Serial_comm.serialPort.close();

        send_info.setStyle("-fx-control-inner-background: white;");

        boolean can_get_data;
        try { can_get_data = data.listen(); } catch (IOException e) { e.printStackTrace(); return;}

        if(can_get_data)
        {
            try { data.get_data(); } catch (IOException  e) { e.printStackTrace(); return;}
        }

        data.update_data();
    }

    public void send()
    {
        Serial_comm.tg.stop();
        if (Serial_comm.serialPort != null)
            Serial_comm.serialPort.close();

        String d;
        try
        {
            Calendar rightNow = Calendar.getInstance();
            rightNow.getCalendarType();



            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int minutes = rightNow.get(Calendar.MINUTE);
            int day = rightNow.get(Calendar.DAY_OF_WEEK);
            day--;
            if (day==0)
                day=7;


            d = days_buttons.get_result() + "\n" + feed_buttons.get_result() + "\n" + time_buttons.get_result() + "\n" + hour + "\n" + minutes+ "\n" +day+ "\n" ;

        } catch (NullPointerException e)
        {
            send_info.setText("nie wybrano ilości pokarmu");
            send_info.setStyle("-fx-control-inner-background: red;");
            return;
        }

        boolean can_get_data;
        try { can_get_data = data.listen(); } catch (IOException e) { e.printStackTrace();return;}

        if(can_get_data)
        {
            try { send_info.setText(data.set_data(d,send_info)); } catch (IOException e) { e.printStackTrace(); return;}
        }
        else
        {
            send_info.setText("nie połączono");
            send_info.setStyle("-fx-control-inner-background: red;");
        }

        data.update_data();


    }

    public void serial_reflesh()
    {
        serial.ref_ports();
    }

    public void serial_connect()
    {

        try
        {
            serial_info.setText(serial.send_serial(serial_info));
        } catch (Exception e)
        {
           // e.printStackTrace();
            serial_info.setText("nie wybrano portu");
            serial_info.setStyle("-fx-control-inner-background: red;");
            System.out.println("dsdsdsdwewwwwwww");
            return;
        }
    }
}