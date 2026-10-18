import javax.swing.*;


public class viewcalculator extends JFrame  {

    JLabel lbla, lblb, lblResult;
    JTextField txta, txtb, txtResult;
    JButton add1,sub1,div1,mul1;
        
        public viewcalculator() {
        setTitle("Calculator");
        
        setSize(450, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        lbla = new JLabel("Enter a :");
        lbla.setBounds(30, 30, 120, 30);
        add(lbla);
         
        txta = new JTextField();
        txta.setBounds(170, 30, 180, 30);
        add(txta);

        lblb = new JLabel("Enter b :");
        lblb.setBounds(30, 80, 120, 30);
        add(lblb);

        txtb = new JTextField();
        txtb.setBounds(170, 80, 180, 30);
        add(txtb);


        lblResult = new JLabel("Result : ");
        lblResult.setBounds(30, 200, 120, 30);
        add(lblResult);

        txtResult = new JTextField();
        txtResult.setBounds(170, 200, 180, 30);
        txtResult.setEditable(false);
        add(txtResult);

       
        add1 = new JButton("+");
        add1.setBounds(30, 270, 70, 40);
        add(add1);

        sub1 = new JButton("-");
        sub1.setBounds(110, 270, 70, 40);
        add(sub1);

        mul1 = new JButton("*");
        mul1.setBounds(190, 270, 70, 40);
        add(mul1);

        div1 = new JButton("/");
        div1.setBounds(270, 270, 70, 40);
        add(div1);

        

        setVisible(true);
    }
    


}
