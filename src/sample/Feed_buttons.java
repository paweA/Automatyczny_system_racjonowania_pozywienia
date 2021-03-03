package sample;

import javafx.scene.control.*;

public class Feed_buttons {

    private RadioButton r1,r2,r3,r4;
    private Spinner s5;
    private ToggleGroup tg;

    public void set_choice()
    {
        RadioButton b= (RadioButton) tg.getSelectedToggle();
        s5.setDisable(!b.getId().equals("r4"));

        if(!b.getId().equals("r4"))
        s5.getValueFactory().setValue(Integer.parseInt(b.getUserData().toString()));
    }

    private void set_group()
    {
        tg = new ToggleGroup();
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);
        r3.setToggleGroup(tg);
        r4.setToggleGroup(tg);
    }

    public Feed_buttons(RadioButton r1,RadioButton r2,RadioButton r3,RadioButton r4,Spinner s5)
    {
        SpinnerValueFactory<Integer> f =new SpinnerValueFactory.IntegerSpinnerValueFactory(10,2000,1);
        s5.setValueFactory(f);

        TextFormatter<Integer> formater = new TextFormatter<>(f.getConverter(), f.getValue());
        s5.getEditor().setTextFormatter(formater);

        r1.setUserData("50");
        r2.setUserData("300");
        r3.setUserData("700");
        this.r1=r1;
        this.r2=r2;
        this.r3=r3;
        this.r4=r4;
        set_group();

        this.s5=s5;
    }

    public String get_result() throws NullPointerException
    {
        RadioButton b= (RadioButton) tg.getSelectedToggle();
        if(b.getId().equals("r4"))
        {
            int v= (int) s5.getValue();
            return Integer.toString(v/10);
        }
        return Integer.toString(Integer.parseInt(b.getUserData().toString())/10);
    }
}
