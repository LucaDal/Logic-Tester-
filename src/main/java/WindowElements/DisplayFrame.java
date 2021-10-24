package WindowElements;

import Components.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class DisplayFrame extends JFrame {
    ActionBar actionBar = new ActionBar();
    Interface inter = new Interface(actionBar);
    Buttons buttonsInterfaces = new Buttons(inter);
    Menu menuInterfaces = new Menu(inter,inter);
    public DisplayFrame() {
        super("Logic Tester");
        setLayout(new BorderLayout());
        Image icon = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "\\src\\main\\resources\\npn.png");
        setIconImage(icon);
        /*key bindigs*/
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());

        add(menuInterfaces,BorderLayout.NORTH);
        add(buttonsInterfaces, BorderLayout.LINE_START);
        add(inter, BorderLayout.CENTER);
        add(actionBar,BorderLayout.SOUTH);
       // setUndecorated(true);
        pack();
        setMinimumSize(new Dimension( 200,280));//TODO definire a fine lavoro
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);

    }


    private class MyDispatcher implements KeyEventDispatcher {//TODO implementing every button
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                inter.resetAll();
            }
            if (e.getKeyCode() == KeyEvent.VK_1) {
                inter.select();
            }
            if (e.getKeyCode() == KeyEvent.VK_2) {
                inter.addTransistorNpn();
            }
            if (e.getKeyCode() == KeyEvent.VK_3) {
                inter.addTransistorPnp();
            }
            if (e.getKeyCode() == KeyEvent.VK_4) {
                inter.addVcc();
            }
            if (e.getKeyCode() == KeyEvent.VK_5) {
                inter.addGnd();
            }
            if (e.getKeyCode() == KeyEvent.VK_6) {
                inter.delete();
            }
            return false;
        }
    }
}
