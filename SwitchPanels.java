package com.zero1.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SwitchPanels extends JFrame implements ActionListener {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 200;

    private JFrame j1 = new JFrame("SW J1"), j2 = new JFrame("SW J2");
    private JPanel panel1, panel2;

    private JTextField name;

    public static void main(String args[]) {
        SwitchPanels sw1 = new SwitchPanels();
    }

    public SwitchPanels() {

        j1.setSize(WIDTH, HEIGHT);
        j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j2.setSize(WIDTH, HEIGHT);
        j2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel1 = new JPanel();
        panel1.add(new JLabel("Panel 1"));

        JButton button1 = new JButton("callJ2");
        button1.addActionListener(this);
        panel1.add(button1);

        panel2 = new JPanel();
        panel2.add(new JLabel("Panel 2"));

        JButton button2 = new JButton("callJ1");
        button2.addActionListener(this);
        panel2.add(button2);

        name = new JTextField(30);
        panel2.add(name);

        j1.add(panel1);
        j1.setVisible(true);
        j2.add(panel2);
        //j2.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonString = e.getActionCommand();
        if (buttonString.equals("callJ2")) {
            j2.setVisible(true);
            j1.setVisible(false);
        } else if (buttonString.equals("callJ1")) {
            j1.setVisible(true);
            j2.setVisible(false);
            name.setText("j2 " + name.getText());
        } else {
            System.out.println("Unexpected error.");
        }
    }
}
