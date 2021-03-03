package sample;

import javafx.scene.control.CheckBox;

public class Days_buttons
{
    private CheckBox d1,d2,d3,d4,d5,d6,d7,da;


    public void select()
    {
        d1.setSelected(da.isSelected());
        d2.setSelected(da.isSelected());
        d3.setSelected(da.isSelected());
        d4.setSelected(da.isSelected());
        d5.setSelected(da.isSelected());
        d6.setSelected(da.isSelected());
        d7.setSelected(da.isSelected());
    }

    private String to_str(CheckBox c)
    {
        int b = c.isSelected() ? 1 : 0;
        return Integer.toString(b);
    }

    public String get_result()
    {
        String t=to_str(d1)+to_str(d2)+to_str(d3)+to_str(d4)+to_str(d5)+to_str(d6)+to_str(d7);
        return  Integer.toString(Integer.parseInt(t, 2));
    }

    public Days_buttons(CheckBox d1, CheckBox d2, CheckBox d3, CheckBox d4, CheckBox d5, CheckBox d6, CheckBox d7, CheckBox da)
    {
        this.d1=d1;
        this.d2=d2;
        this.d3=d3;
        this.d4=d4;
        this.d5=d5;
        this.d6=d6;
        this.d7=d7;
        this.da=da;

    }
}
