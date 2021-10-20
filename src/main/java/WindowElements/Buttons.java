package WindowElements;

import Components.Interface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Buttons extends JPanel implements ActionListener {
    JButton npnT, vcc, select, gnd, delete, switchButton,debug, bitDisplay,pnpT;
    Components.Interface Interface;
    int width = 40, height = 40;
    private Image imgSwitch,imgBitDisplay,imgGnd,imgVcc,imgPnp,imgNpn,imgSelect,imgDelete;

    public Buttons(Interface inter) {
        setBackground(Color.GRAY);//TODO remove comments when implewmentinh images
        setLayout(new GridBagLayout());
        this.Interface = inter;
        initialize();

        npnT = new JButton();//TODO: implementig the selection of npn or pnp npnT
        npnT.setPreferredSize(new Dimension(width, height));
        npnT.setContentAreaFilled(false);
        npnT.setIcon(new ImageIcon(imgNpn));

        pnpT = new JButton();//TODO: implementig the selection of npn or pnp npnT
        pnpT.setPreferredSize(new Dimension(width, height));
        pnpT.setContentAreaFilled(false);
        pnpT.setIcon(new ImageIcon(imgPnp));

        vcc = new JButton();
        vcc.setPreferredSize(new Dimension(width, height));
        vcc.setContentAreaFilled(false);
        vcc.setIcon(new ImageIcon(imgVcc));

        select = new JButton();
        select.setPreferredSize(new Dimension(width, height));
        select.setContentAreaFilled(false);
        select.setIcon(new ImageIcon(imgSelect));

        gnd = new JButton();
        gnd.setPreferredSize(new Dimension(width, height));
        gnd.setContentAreaFilled(false);
        gnd.setIcon(new ImageIcon(imgGnd));

        delete = new JButton();
        delete.setPreferredSize(new Dimension(width, height));
        delete.setContentAreaFilled(false);
        delete.setIcon(new ImageIcon(imgDelete));


        switchButton = new JButton();
        switchButton.setPreferredSize(new Dimension(width, height));
        switchButton.setContentAreaFilled(false);
        switchButton.setIcon(new ImageIcon(imgSwitch));


        bitDisplay = new JButton();
        bitDisplay.setPreferredSize(new Dimension(width, height));
        bitDisplay.setContentAreaFilled(false);
        bitDisplay.setIcon(new ImageIcon(imgBitDisplay));


        debug = new JButton("D");
        debug.setPreferredSize(new Dimension(width, height));
        debug.setContentAreaFilled(false);

        //===================================================
        npnT.addActionListener(this);
        pnpT.addActionListener(this);
        vcc.addActionListener(this);
        select.addActionListener(this);
        gnd.addActionListener(this);
        delete.addActionListener(this);
        switchButton.addActionListener(this);
        bitDisplay.addActionListener(this);
        debug.addActionListener(this);

        /* inserirsco Interface bottoni nel Jpanel */
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        add(select, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        add(npnT, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 2;
        add(pnpT, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 3;
        add(vcc, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 4;
        add(gnd, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 5;
        add(delete, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 6;
        add(switchButton, c);
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 7;
        //c.weighty = 1;//TODO riattiva dopo che elimini il debug
        add(bitDisplay, c);
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 8;
        c.weighty = 1;
        add(debug, c);
    }
    private void initialize(){
        BufferedImage imgSel,imgDel,imgBit,imgSwi,imgNp,imgPn,imgVc,imgGn;
        int scaleAdjust = 15;
        try {
            String path = System.getProperty("user.dir");
            imgSel = ImageIO.read(new File(path + "\\src\\main\\resources\\select.png"));
            imgSelect = imgSel.getScaledInstance(width-scaleAdjust-5, height-scaleAdjust-5, Image.SCALE_SMOOTH);
            imgDel = ImageIO.read(new File(path + "\\src\\main\\resources\\delete.png"));
            imgDelete = imgDel.getScaledInstance(width-scaleAdjust, height-scaleAdjust, Image.SCALE_SMOOTH);
            imgBit = ImageIO.read(new File(path + "\\src\\main\\resources\\bitDisplay.png"));
            imgBitDisplay = imgBit.getScaledInstance(width-scaleAdjust+5, height-scaleAdjust+5, Image.SCALE_SMOOTH);
            imgSwi = ImageIO.read(new File(path + "\\src\\main\\resources\\switch.png"));
            imgSwitch = imgSwi.getScaledInstance(width-scaleAdjust, height-scaleAdjust, Image.SCALE_SMOOTH);
            imgNp = ImageIO.read(new File(path + "\\src\\main\\resources\\npn.png"));
            imgNpn = imgNp.getScaledInstance(width-scaleAdjust, height-scaleAdjust, Image.SCALE_SMOOTH);
            imgPn = ImageIO.read(new File(path + "\\src\\main\\resources\\pnp.png"));
            imgPnp = imgPn.getScaledInstance(width-scaleAdjust, height-scaleAdjust, Image.SCALE_SMOOTH);
            imgVc = ImageIO.read(new File(path + "\\src\\main\\resources\\vcc.png"));
            imgVcc = imgVc.getScaledInstance(width-scaleAdjust-5, height-scaleAdjust-5, Image.SCALE_SMOOTH);
            imgGn = ImageIO.read(new File(path + "\\src\\main\\resources\\gnd.png"));
            imgGnd = imgGn.getScaledInstance(width-scaleAdjust, height-scaleAdjust, Image.SCALE_SMOOTH);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (npnT == source) {
            Interface.addTransistorNpn();
        } else if (pnpT == source) {
            Interface.addTransistorPnp();
        } else if (vcc == source) {
            Interface.addVcc();
        } else if (select == source) {
            Interface.select();
        } else if (gnd == source) {
            Interface.addGnd();
        } else if (delete == source) {
            Interface.delete();
        } else if (switchButton == source) {
            Interface.addSwitch();
        } else if (bitDisplay == source) {
            Interface.addBitDisplay();
        } else if (debug == source) {
            Interface.debug();
        }
    }

}
