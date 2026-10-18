import java.awt.event.*;

public class controllercalculator implements ActionListener {

    private viewcalculator vc;
    private modelscalculator mc;

    public controllercalculator(viewcalculator vc, modelscalculator mc)
    {
        this.vc = vc;
        this.mc = mc;

        vc.add1.addActionListener(this);
        vc.sub1.addActionListener(this);
        vc.mul1.addActionListener(this);
        vc.div1.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        double a = Double.parseDouble(vc.txta.getText());
        double b = Double.parseDouble(vc.txtb.getText());

        double res = 0;

        if(e.getSource() == vc.add1)
            {
                res = mc.add(a,b);
                
            }
        else if(e.getSource() == vc.sub1)
            {
                res = mc.sub(a,b);
            }

        else if(e.getSource() == vc.mul1)
            {
                res = mc.mul(a , b);

            }
        else if(e.getSource() == vc.div1)
            {
                res = mc.div(a , b);
            }

            vc.txtResult.setText(String.valueOf(res));


    }
    
}
