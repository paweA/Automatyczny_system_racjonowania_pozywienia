package sample;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;

public class Time_buttons
{

    private Spinner t1,t2,t3,t4;
    private CheckBox k1;


    public String get_result()
    {
        String tnext1 =t3.getValue().toString() ,tnext2 = t4.getValue().toString();

        if(!k1.isSelected())
        {
            tnext1="222";
            tnext2="222";
        }

        return t1.getValue().toString() + "\n" + t2.getValue().toString() + "\n" + tnext1 + "\n" + tnext2;
    }


    Time_buttons(Spinner t1, Spinner t2,Spinner t3,Spinner t4,CheckBox k1)
    {
        this.t1=t1;
        this.t2=t2;
        this.t3=t3;
        this.t4=t4;
        this.k1=k1;

        SpinnerValueFactory<Integer> th =new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,1);
        SpinnerValueFactory<Integer> tm =new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,1);
        SpinnerValueFactory<Integer> th2 =new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,1);
        SpinnerValueFactory<Integer> tm2 =new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,1);
        t1.setValueFactory(th);
        t2.setValueFactory(tm);
        t3.setValueFactory(th2);
        t4.setValueFactory(tm2);

        TextFormatter<Integer> formater1 = new TextFormatter<>(th.getConverter(), th.getValue());
        TextFormatter<Integer> formater2 = new TextFormatter<>(tm.getConverter(), tm.getValue());
        TextFormatter<Integer> formater3 = new TextFormatter<>(th.getConverter(), th.getValue());
        TextFormatter<Integer> formater4 = new TextFormatter<>(tm.getConverter(), tm.getValue());
        t1.getEditor().setTextFormatter(formater1);
        t2.getEditor().setTextFormatter(formater2);
        t3.getEditor().setTextFormatter(formater3);
        t4.getEditor().setTextFormatter(formater4);
    }

    public void select()
    {
        t3.setDisable(!k1.isSelected());
        t4.setDisable(!k1.isSelected());
    }
}
