package ch17;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Proj1701. Design and code a Swing GUI to translate text that is input in
 * English into Pig Latin. You can assume that the sentence contains no
 * punctuation. The rules for Pig Latin are as follows: ...
 */
public class PigLatin extends JFrame implements ActionListener {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 200;
    public static final int NUMBER_OF_LINES = 3;
    public static final int NUMBER_OF_CHAR = 30;

    private JTextArea entryText, resultText;

    public static void main(String[] args) {
        PigLatin gui = new PigLatin();
        gui.setVisible(true);
    }

    public PigLatin() {
        this.setTitle("Pig Latin");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new FlowLayout());
        entryPanel.setBackground(Color.GRAY);

        add(entryPanel);

        entryText = new JTextArea(NUMBER_OF_LINES, NUMBER_OF_CHAR);
        entryPanel.add(entryText);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new FlowLayout());
        resultPanel.setBackground(Color.LIGHT_GRAY);

        add(resultPanel);

        resultText = new JTextArea(NUMBER_OF_LINES, NUMBER_OF_CHAR);
        resultText.setEditable(false);
        resultPanel.add(resultText);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.BLUE);
        JButton actionButton = new JButton("Pig!");
        actionButton.addActionListener(this);
        buttonPanel.add(actionButton);

        add(buttonPanel);
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("Pig!")) {
            resultText.setText(
                    scantext(entryText.getText()));
        } else {
            resultText.setText("Unexpected error.");
        }
    }

    public String scantext(String entry) {
        Scanner scanLine = new Scanner(entry);

        String result = "";

        while (scanLine.hasNextLine()) {
            String nextLine = scanLine.nextLine();

            Scanner scan = new Scanner(nextLine);
            while (scan.hasNext()) {
                result += pigLatin(scan.next()) + " ";
            }

            // Uppercase the first letter
            String letter = (result.charAt(0) + "").toUpperCase();
            result = letter + result.substring(1) + "\n";

        }

        return result;
    }

    public String pigLatin(String pretext) {
        pretext = pretext.toLowerCase();

        // For words that begin with vowels,
        char ch = pretext.charAt(0);
        if ((ch == 'a') || (ch == 'e') || (ch == 'i') || (ch == 'o') || (ch == 'u')) {
            pretext = pretext + "way";
        } else {

            // For words that begin with consonants
            //     **** .charAt(1) .substring(2) ****
            String letter = (pretext.charAt(1) + "");
            pretext = letter + pretext.substring(2) + pretext.charAt(0) + "ay";
        }

        return pretext;
    }
}

/*
A quick brown fox
jums over the lazy dog.
Away uickqay rownbay oxfay 
umsjay overway hetay azylay og.day 
 */
