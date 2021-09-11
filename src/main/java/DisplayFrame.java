import javax.swing.*;

public class DisplayFrame extends JFrame {

    public DisplayFrame(){
        super("Logic Tester");
        Interface I = new Interface();
        add(I);
        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
