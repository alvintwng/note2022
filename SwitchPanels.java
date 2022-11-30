package com.zero1.panelTwo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SwitchPanels {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 200;

    private JFrame j1 = new JFrame("SW J1");
    private JFrame j2 = new JFrame("SW J2");
    private Panel2left panel1;
    private JPanel panel2;
    private JPanel panelButton1;
    private JPanel panelButton2;

    private JTextField name;

    public static String textA2B;

    // To pass the var from j1 to j2
    public static void getTextA2B(String text) {
        System.out.println(">>>>>> textA2B:: " + text);
        textA2B = text;
    }

    public static void main(String args[]) {
        SwitchPanels sw1 = new SwitchPanels();
    }

    public SwitchPanels() {

        j1.setSize(WIDTH, HEIGHT);
        j1.setLayout(new BorderLayout());
        j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j2.setSize(WIDTH, HEIGHT);
        j2.setLayout(new BorderLayout());
        j2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel1 = new Panel2left();
        panel1.add(new JLabel("Panel 1"));

        panel2 = new JPanel();
        panel2.add(new JLabel("Panel 2"));
        name = new JTextField(30);
        panel2.add(name);

        class PanelButton extends JPanel implements ActionListener {
            PanelButton(String s) {
                super();
                setLayout(new FlowLayout());
                JButton b = new JButton(s);
                b.addActionListener(this);
                add(b);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonString = e.getActionCommand();
                switch (buttonString) {
                    case "callJ2" -> {
                        j2.setVisible(true);
                        j1.setVisible(false);
                    }
                    case "callJ1" -> {
                        j1.setVisible(true);
                        j2.setVisible(false);
                        name.setText("j2 " + name.getText());
                    }
                    default -> System.out.println("Unexpected error.");
                }
            }
        }

        PanelButton panelButton1 = new PanelButton("callJ2");
        PanelButton panelButton2 = new PanelButton("callJ1");

        j1.add(panel1, BorderLayout.CENTER);
        j1.setVisible(true);
        j1.add(panelButton1, BorderLayout.SOUTH);
        j2.add(panel2, BorderLayout.CENTER);
        j2.add(panelButton2, BorderLayout.SOUTH);
        //j2.setVisible(true);
    }
}
