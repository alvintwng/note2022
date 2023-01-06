package com.zero1.idscan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AlvinNg
 */
public class DetectTextTest {

    public DetectTextTest() {
    }

    @Test
    public void testDetectText() {
        System.out.println("detectText");
        String filePath = "..\\..\\idScan\\sample\\zero1.png";
        String dataPath = "dataStream.txt";
        PrintStream dataStream = null;
        String s = "";
        Scanner fileIn = null;
        try {
            dataStream = new PrintStream(
                    new FileOutputStream(dataPath));
            DetectText.detectText(filePath, dataStream);
            dataStream.close(); // 

            try {
                fileIn = new Scanner(new FileInputStream(dataPath));
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                System.exit(0);
            }
            fileIn.next();
            s = fileIn.next();
            
            fileIn.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error opening file with FileOutputStream.");
            System.exit(0);
        } catch (Exception ex) {
            System.out.println(">>>> Exception: " + ex);
        }

        assertEquals("ZERO", s);
    }

}
/*
--- exec-maven-plugin:3.0.0:exec (default-cli) @ IdScan ---
Jan 06, 2023 4:15:10 PM com.google.auth.oauth2.DefaultCredentialsProvider warnAboutProblematicCredentials
WARNING: Your application has authenticated using end user credentials from Google Cloud SDK. We recommend that most server applications use service accounts instead. If your application continues to use end user credentials from Cloud SDK, you might receive a "quota exceeded" or "API not enabled" error. For more information about service accounts, see https://cloud.google.com/docs/authentication/.
>> dataStream wordcount >> 97
*/
