package WindowElements;

import Components.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class DisplayFrame extends JFrame {
    Interface inter = new Interface();
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
        inter.setMenu(menuInterfaces);
        add(menuInterfaces,BorderLayout.NORTH);
        add(buttonsInterfaces, BorderLayout.LINE_START);
        add(inter, BorderLayout.CENTER);
       // setUndecorated(true);
        pack();
        setMinimumSize(new Dimension( 300,530));//TODO definire a fine lavoro
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }


    private class MyDispatcher implements KeyEventDispatcher {//TODO implementing every button
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
                buttonsInterfaces.setFocus(buttonsInterfaces.select);
                inter.select();
            }else
            if (e.getKeyCode() == KeyEvent.VK_1) {
                buttonsInterfaces.setFocus(buttonsInterfaces.npnT);
                inter.addTransistorNpn();
            }else
            if (e.getKeyCode() == KeyEvent.VK_2) {
                buttonsInterfaces.setFocus(buttonsInterfaces.pnpT);
                inter.addTransistorPnp();
            }else
            if (e.getKeyCode() == KeyEvent.VK_3) {
                buttonsInterfaces.setFocus(buttonsInterfaces.vcc);
                inter.addVcc();
            }else
            if (e.getKeyCode() == KeyEvent.VK_4) {
                buttonsInterfaces.setFocus(buttonsInterfaces.gnd);
                inter.addGnd();
            }else
            if (e.getKeyCode() == KeyEvent.VK_5) {
                buttonsInterfaces.setFocus(buttonsInterfaces.switchButton);
                inter.addSwitch();
            }else
            if (e.getKeyCode() == KeyEvent.VK_6) {
                buttonsInterfaces.setFocus(buttonsInterfaces.bitDisplay);
                inter.addBitDisplay();
            }else
            if (e.getKeyCode() == KeyEvent.VK_7) {
                buttonsInterfaces.setFocus(buttonsInterfaces.text);
                inter.addText();
            }else
            if (e.getKeyCode() == KeyEvent.VK_8) {
                buttonsInterfaces.setFocus(buttonsInterfaces.delete);
                inter.delete();
            }
            return false;
        }
    }
}
