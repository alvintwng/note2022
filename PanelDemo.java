package com.zero1.test;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;

public class PanelDemo extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;

    public static void main(String[] args) {
        PanelDemo gui = new PanelDemo();
        gui.setVisible(true);
    }

    public PanelDemo() {
        super("Panel Demonstration");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        NewJPanel testNew = new NewJPanel();
        testNew.setBackground(Color.CYAN);
        add(testNew);

        ExitPanel ex = new ExitPanel();
        add(ex, BorderLayout.EAST);
    }
}
