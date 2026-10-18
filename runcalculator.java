

public class runcalculator {

    void main()
    {


        viewcalculator view = new viewcalculator();
        modelscalculator model = new modelscalculator();
        new controllercalculator(view,model);
        System.out.println("Calculator");
    }
    
}
