/* www.Zero1.Sg 2022Dec */
package com.zero1.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * To check if there is match from GoogleDrive and ToBeDelete_Portal. If match
 * found LOG pathname as error message.
 *
 * Folder for files should be 32 chars String.
 */
public class Main {

    private static final String DELIMITER = "\\";
    private final HashMap<String, ArrayList<String>> hashmap;
    private ArrayList<String> matchedArr;
    public int primaryLineCount = 0;
    public int targetFileChkCount = 0;

    public Main() {
        hashmap = new HashMap<>();
        matchedArr = new ArrayList<>();
    }

    /**
     * Initiate application.
     *
     * @param primaryPath
     * @param targetPath
     */
    public Main(String primaryPath, String targetPath) {
        hashmap = new HashMap<>();
        matchedArr = new ArrayList<>();
        putFilenameToHashmap(primaryPath);
        targetFilesVerifyByHash(targetPath);
    }

    public HashMap<String, ArrayList<String>> getHashmap() {
        return hashmap;
    }

    public ArrayList<String> getMatchedArr() {
        return matchedArr;
    }

    /**
     * To get text lines from file, extract subString folder name and file name.Sample format {@code CB718C312BA1B3622ECFDCBF727465F2\filename.png}.
     * Put key of 32 chars string, and filename as value to {@code hashmap}.
     *
     * @param primaryPath File of list of paths
     * @return 
     */
    public boolean addPrimaryPath(String primaryPath) {
        return putFilenameToHashmap(primaryPath);
    }

    private boolean putFilenameToHashmap(String primaryPath) {
        String textLine;
        Scanner fileIn;

        var dirfile = new File(primaryPath + DELIMITER);
        if (dirfile.isDirectory()) {
            for (var str2 : dirfile.list()) {
                var filePath = (primaryPath + DELIMITER + str2);

                try {
                    if ((new File(primaryPath + DELIMITER + str2)).isDirectory()) {
                        continue;
                    }

                    fileIn = new Scanner(
                            new FileInputStream(filePath));

                    boolean hasNextline = fileIn.hasNextLine();
                    while (hasNextline) {
                        textLine = fileIn.nextLine().trim().toLowerCase();
                        if (textLine.length() <= 1) {
                            hasNextline = fileIn.hasNextLine();
                            continue;
                        }

                        if (!subStringPutToHash(textLine)) {
                            System.err.println(" < " + str2);
                        }
                        primaryLineCount++;
                        hasNextline = fileIn.hasNextLine();
                    }
                    fileIn.close();

                } catch (FileNotFoundException e) {
                    System.out.println("File not found.");
                    return false;
                }
            }
            return true;
        } else {
            System.out.println("Primary directory NOT correct!");
            return false;
        }
    }

    private boolean subStringPutToHash(String s) {
        String filename, mkey, sub;
        try {
            sub = s.substring(0, s.lastIndexOf(DELIMITER));
            mkey = sub.substring(sub.lastIndexOf(DELIMITER) + 1); //eg. CB718C312BA1B3622ECFDCBF727465F2
            filename = s.substring(s.lastIndexOf(DELIMITER) + 1);
        } catch (StringIndexOutOfBoundsException e) {
            System.err.print("> StringException: " + s);
            return false;
        }

        // check if right key lgth
        var keylgth = mkey.length();

        //  modified double char to single
        if (keylgth > 64) {
            var ss = "";
            for (var a : mkey.toCharArray()) {
                ss += (Integer.valueOf(a) != 0) ? (a) : ("");
            }
            mkey = ss;
            keylgth = ss.length();
        }

        if (keylgth == 32) {

            // if hashmap NOT contain key
            if (!hashmap.containsKey(mkey)) {
                hashmap.put(mkey, new ArrayList<>());
            }
            hashmap.get(mkey).add(filename.toLowerCase());

            /* * System.out.println(">> Primary >> mkey: " + mkey + " | filename: " + filename); // */
            return true;
        } else {
            System.err.print("> key lgth err: " + s);
            return false;
        }
    }

    /**
     * Add additional path. To get the full path name of directory individual
     * files. And compare with {@code hashmap} for key as folder, value as
     * filename.
     *
     * @param targetPath Path from local drive, or local sharepoint.
     * @return True for process done, or false for wrong directory
     */
    public boolean addTargetPath(String targetPath) {
        return targetFilesVerifyByHash(targetPath);
    }

    private boolean targetFilesVerifyByHash(String targetPath) {

        var mainfile = new File(targetPath);

        if (mainfile.isDirectory()) {
            for (var str : mainfile.list()) {
                var dirfile = new File(mainfile + DELIMITER + str);
                if (dirfile.isDirectory()) {
                    var tagKey = dirfile.getName().toLowerCase();
                    for (var str2 : dirfile.list()) {
                        var subfile = new File(dirfile + DELIMITER + str2);
                        var tagFilename = subfile.getName();
                        /* * System.out.println(">> target key: " + tagKey + " | name: " + tagFilename); // */

                        // if there IS match from hashmap, will log error message.
                        if (hashmap.containsKey(tagKey)) {

                            ArrayList<String> arr = hashmap.get(tagKey);
                            for (String priFileName : arr) {
                                if (priFileName.equalsIgnoreCase(tagFilename)) {
                                    matchedArr.add((targetPath + DELIMITER + str + DELIMITER + tagFilename));
                                }
                            }
                        }
                        targetFileChkCount++;
                    }
                } else {
                    System.out.println(">> " + dirfile.getName());
                }

                /* // monitoring, done at 50 'dots' KIV
                monitor = (monitor <= 0) ? dirLgth / 50 : monitor - 1;
                System.out.print((monitor <= 0) ? "." : ""); */
            }
            return true;
        } else {
            System.err.println("Target directory NOT correct!");
            return false;
        }
    }

    @Override
    public String toString() {

        return hashmap.toString();
    }

    public static void main(String args[]) {
        PrintStream errStream = null;
        var logfile = "logmessages.txt";
        try {
            errStream = new PrintStream(
                    new FileOutputStream(logfile));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file with FileOutputStream.");
            System.exit(0);
        }
//        System.setErr(errStream);

        System.out.println("SubString key and name, to hashmap.");

//        String primaryPath = ("D:\\temp");
//        String targetPath = ("C:\\Users\\AlvinNg\\Zero1 Pte Ltd\\Portal - ToBeDeleted\\201808");
//        var m = new Main(primaryPath, targetPath);
//        System.err.println("> primaryPath: " + primaryPath);
//        m.targetFilesVerifyByHash("C:\\Users\\AlvinNg\\Zero1 Pte Ltd\\Portal - ToBeDeleted\\201809");
//        System.err.println("> primaryPath: " + primaryPath);
        String primaryPath = "D:\\temp2";
        String targetPath = "C:\\testdata\\test1";
        var m = new Main(primaryPath, targetPath);
        System.err.println("> primaryPath: " + primaryPath);

        System.out.println("> row count: " + m.primaryLineCount);
        System.out.println("> Hashmap size: " + m.getHashmap().size());

        System.err.println(">> targetPath: " + targetPath);
        System.out.println(">> files count: " + m.targetFileChkCount);

        System.err.println(">> matched found: " + m.getMatchedArr().size());
        for (String s : m.getMatchedArr()) {
            System.err.println(s);
        }

        System.out.println("Completed. Check " + logfile + " for "
                + m.getMatchedArr().size() + " matched data.");
        errStream.close();
    }
}

/* MainTest.java
package com.zero1.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    String primaryPath = "..\\..\\verify\\app\\testSample";
    String targetPath = "..\\..\\verify\\app\\testSample";

    public MainTest() {
    }

    @Test
    public void testMainHashSize() {
        var m = new Main(primaryPath, targetPath);
        assertEquals(5, m.getHashmap().size());
    }

    @Test
    public void testMainMatchedQty() {
        var m = new Main(primaryPath, targetPath);
        assertEquals(1, m.getMatchedArr().size());
    }
}
*/

/*
--- exec-maven-plugin:3.0.0:exec (default-cli) @ app ---
SubString key and name, to hashmap.
> row count: 9
> Hashmap size: 5
>> files count: 2
Completed. Check logmessages.txt for 2 matched data.
 */
/* logmessages.txt
> key lgth err: c:\\users\alvinng\verify\test\test1\z01r002\zero1.png < md5chksum.txt
> StringException: photoid_20181118.zip md5 c8139bf1e2aff9f95c5a238a2a0656c6 < md5chksum.txt
> primaryPath: D:\temp2
>> targetPath: C:\testdata\test1
>> matched found: 2
C:\testdata\test1\CB718C312BA1B3622ECFDCBF727465F2\Duke.png
C:\testdata\test1\CB718C312BA1B3622ECFDCBF727465F2\Z01R002.png
*/
