package com.zero1.idscan;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class ScanFrontBack {

    /**
     * ToDoList : To be automate from folder. ToDo..
     */
    public static void main(String[] args) {
        System.out.println("detectext");
        String filePathBack = "..\\sample\\IDImages\\WP\\ID3\\93CD7EAC-C780-4075-B1E3-091BA5D51D21_back.jpeg";
        String filePathFront = "..\\sample\\IDImages\\WP\\ID3\\DFC42D4E-4646-4537-8F20-6FB67B2DAE2E_front.jpeg";

        String dataPath = "wp_id3.txt";

        try {
            PrintStream dataStream = new PrintStream(
                    new FileOutputStream(dataPath));
            DetectText.detectText(filePathBack, dataStream);
            DetectText.detectText(filePathFront, dataStream);
            dataStream.close(); // 

        } catch (Exception ex) {
            System.out.println(">>>> Exception: " + ex);
        }

    }
}
