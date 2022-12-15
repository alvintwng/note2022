/* www.Zero1.Sg 2022Dec */
package com.zero1.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
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
    private ArrayList<String> matchedArr, errPrint, matchedFilesOnly;
    private int primaryLineCount = 0, targetFileChkCount = 0, mode = 0;
    public int matchedNameCount = 0;
    public String info = "";

    /**
     * Initiate application.
     *
     * @param primaryPath
     * @param targetPath
     */
    public Main(String primaryPath, String targetPath) {
        hashmap = new HashMap<>();
        matchedArr = new ArrayList<>();
        errPrint = new ArrayList<>();
        addPrimarypathFilenameToHashmap(primaryPath);
        targetFilesVerifyByHash(targetPath);
    }

    /**
     * Initiate application with
     *
     * @see setMode()
     *
     * @param primaryPath
     * @param targetPath
     * @param modeType see mode
     */
    public Main(String primaryPath, String targetPath, int modeType) {
        mode = modeType;
        hashmap = new HashMap<>();
        matchedArr = new ArrayList<>();
        errPrint = new ArrayList<>();
        matchedFilesOnly = new ArrayList<>();
        addPrimarypathFilenameToHashmap(primaryPath);
        targetFilesVerifyByHash(targetPath);
    }

    public int getMode() {
        return mode;
    }

    /**
     * <li> mode = 1: filename as hashmap key. to compare folername togather
     * with filename to matchced.
     * <li> mode = 2: filename as hashmap key. However, only to compare
     * filename.
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @return hashmap size
     */
    public int getHashmapSize() {
        return hashmap.size();
    }

    /**
     * @return list of matching folder together with file of primaryPath and
     * targetPath.
     */
    public ArrayList<String> getMatchedArr() {
        return matchedArr;
    }

    /**
     * Use in mode 2 only.
     *
     * @return Array of list of matching files of primaryPath and targetPath.
     */
    public ArrayList<String> getMatchedFilesOnly() {
        return matchedFilesOnly;
    }

    /**
     *
     * @return list of error msg
     */
    public ArrayList<String> getErrPrint() {
        return errPrint;
    }

    /**
     * Add String, and System.out.println
     *
     * @param s String
     */
    public void setErrPrint(String s) {
        System.out.println(s);
        errPrint.add(s);
    }

    /**
     * @return no. of lines from path
     */
    public int getPrimaryLineCount() {
        return primaryLineCount;
    }

    /**
     * @return no. of lines from path
     */
    public int getTargetFileChkCount() {
        return targetFileChkCount;
    }

    /**
     * To get text lines from file, extract subString folder name and file
     * name.Sample format {@code CB718C312BA1B3622ECFDCBF727465F2\filename.png}.
     * Put key of 32 chars string, and filename as value to {@code hashmap}.
     *
     * @param primaryPath File of list of paths
     * @return
     */
    public boolean addPrimarypathFilenameToHashmap(String primaryPath) {
        info += """
                > primaryPath: """ + primaryPath;
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
                            errPrint.add(" < " + str2);
                        }
                        primaryLineCount++;
                        hasNextline = fileIn.hasNextLine();
                    }
                    fileIn.close();

                } catch (FileNotFoundException e) {
                    setErrPrint("> Error at FileNotFoundException " + e);
                    return false;
                }
            }
            return true;
        } else {
            errPrint.add("Primary directory NOT correct!");
            return false;
        }
    }

    private boolean subStringPutToHash(String s) {

        // remove the access char 0
        s = s.replace("\u0000", "");
//        var ss = "";
//        for (var a : s.toCharArray()) {
//            ss += (Integer.valueOf(a) != 0) ? (a) : ("");
//        }
//        s = ss;

        String filename, mkey, sub;
        try {
            sub = s.substring(0, s.lastIndexOf(DELIMITER));
            mkey = sub.substring(sub.lastIndexOf(DELIMITER) + 1); //eg. CB718C312BA1B3622ECFDCBF727465F2
            filename = s.substring(s.lastIndexOf(DELIMITER) + 1);
        } catch (StringIndexOutOfBoundsException e) {
            errPrint.add("> StringException: " + s);
            return false;
        }

        // check if right key lgth
        if (mkey.length() == 32) {
            switch (mode) {
                case 2 -> {
                    //  System.out.println("> filename as hashkey");
                    if (!hashmap.containsKey(filename)) {
                        hashmap.put(filename, new ArrayList<>());
                    }
                    hashmap.get(filename).add(s);
                }
                case 1 -> {
                    //  System.out.println("> filename as hashkey");
                    if (!hashmap.containsKey(filename)) {
                        hashmap.put(filename, new ArrayList<>());
                    }
                    hashmap.get(filename).add(mkey);
                }
                default -> {
                    // folder(mKey) as hash key
                    if (!hashmap.containsKey(mkey)) {
                        hashmap.put(mkey, new ArrayList<>());
                    }
                    hashmap.get(mkey).add(filename);
                }
            }

            /* * System.out.println(">> Primary >> mkey: " + mkey + " | filename: " + filename); // */
            return true;
        } else {
            errPrint.add("> key lgth err: " + s);
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
    public boolean targetFilesVerifyByHash(String targetPath) {
        info += """
                >> targetPath: """ + targetPath;
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
                        switch (mode) {
                            case 1 -> {
                                // System.out.println(">>  filename as hashkey");
                                if (hashmap.containsKey(tagFilename)) {
                                    ArrayList<String> arr = hashmap.get(tagFilename);
                                    arr.forEach(priFileName -> {
                                        if (priFileName.equalsIgnoreCase(tagKey)) {
                                            matchedArr.add((targetPath + DELIMITER + str + DELIMITER + tagFilename));
                                        }
                                    });
                                }
                            }
                            case 2 -> {
                                if (hashmap.containsKey(tagFilename)) {
                                    matchedNameCount++;
                                    if (tagFilename.length() > 20) { //ignore  image.jpg,...,nric_front.jpg,nric front small.jpg
                                        matchedFilesOnly.add(targetPath + DELIMITER + str + DELIMITER + tagFilename + " <> " + hashmap.get(tagFilename));
                                    }
                                }
                            }
                            default -> {
                                // folder(mKey) as hash key,
                                if (hashmap.containsKey(tagKey)) {
                                    ArrayList<String> arr = hashmap.get(tagKey);
                                    arr.forEach(priFileName -> {
                                        if (priFileName.equalsIgnoreCase(tagFilename)) {
                                            matchedArr.add((targetPath + DELIMITER + str + DELIMITER + tagFilename));
                                        }
                                    });
                                }
                            }
                        }

                        targetFileChkCount++;
                    }
                } else {
                    errPrint.add(">> " + dirfile.getName());
                }

                /* // monitoring, done at 50 'dots' KIV
                monitor = (monitor <= 0) ? dirLgth / 50 : monitor - 1;
                System.out.print((monitor <= 0) ? "." : ""); */
            }
            return true;
        } else {
            setErrPrint("Target directory NOT correct!");
            return false;
        }
    }

    /**
     * Print to file array of matchArr, matchedFileOnly, errPrint
     *
     * @param filename new filename to copy text to
     */
    public void printErrlogNMatchedToFile(String filename) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(
                    new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            setErrPrint("Error opening the file " + filename);
            System.exit(0);
        }

        outputStream.println("\nMatched 'folder file':");
        getMatchedArr().forEach(outputStream::println);

        if (getMode() == 2) {
            outputStream.println("\nPortal filename   <> against the txt files:\n"
                    + info + "\n"
                    + "only compare filename.length > 20, ie. ignore  image.jpg, ..."
                    + "\n");
            matchedFilesOnly.forEach(outputStream::println);
            outputStream.println(">>> matchedNameCount: " + matchedNameCount);
        } //else 
        {
            outputStream.println("\nError Messages:");
            getErrPrint().forEach(outputStream::println);
        }
        outputStream.close();
    }

    @Override
    public String toString() {
        return hashmap.toString();
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
        Main m = new Main(primaryPath, targetPath);
        assertEquals(5, m.getHashmapSize());
        assertEquals(1, m.getMatchedArr().size());
        assertEquals(9, m.getPrimaryLineCount());
        assertEquals(3, m.getTargetFileChkCount());
        
        m.addPrimarypathFilenameToHashmap(primaryPath);
        m.targetFilesVerifyByHash(targetPath);
        assertEquals(5, m.getHashmapSize());
        assertEquals(3, m.getMatchedArr().size());
        assertEquals(18, m.getPrimaryLineCount());
        assertEquals(6, m.getTargetFileChkCount());
    }

    @Test
    public void testMainHashSizeMode1() {
        Main m = new Main(primaryPath, targetPath, 1);
        assertEquals(1, m.getMode());
        assertEquals(7, m.getHashmapSize());
    }

    @Test
    public void testMode2() {
        Main m = new Main(primaryPath, targetPath, 2);
        assertEquals(2, m.getMode());
        assertEquals(7, m.getHashmapSize());
        assertEquals(0, m.getMatchedFilesOnly().size());
        assertEquals(1, m.matchedNameCount);
    }

    @Test
    public void testErrPrint() {
        Main m = new Main(primaryPath, targetPath);
        m.setErrPrint("test");
        m.getErrPrint();
//        m.getErrPrint().forEach(s -> {
//            System.out.println(">>MainTest>> "+s);
//        });
        assertEquals(6, m.getErrPrint().size());
    }
}
 */

 /*
--- exec-maven-plugin:3.0.0:exec (default-cli) @ app ---
SubString key and name, to hashmap.
> primaryPath:D:\temp2>> targetPath:C:\testdata\test1
> row count: 9
> Hashmap size: 5
>> files count: 4
>> matched found: 2
>> Error size: 5
Completed. Check logmessages.txt
Matched 'folder file':
C:\testdata\test1\CB718C312BA1B3622ECFDCBF727465F2\duke.png
C:\testdata\test1\CB718C312BA1B3622ECFDCBF727465F2\z01r002.png
Completed. Check logmessages.txt
 */

 /* App.java
public class App {

    public static void main(String args[]) {

        var logfile = "logmessages.txt";

        System.out.println("SubString key and name, to hashmap.");

//        String primaryPath = ("D:\\temp");
//        String targetPath = ("C:\\Users\\AlvinNg\\Zero1 Pte Ltd\\Portal - ToBeDeleted\\201808");
//        var m = new Main(primaryPath, targetPath);
//        m.addPrimarypathFilenameToHashmap("C:\\Users\\AlvinNg\\Zero1 Pte Ltd\\Portal - ToBeDeleted\\201809");
//
        String primaryPath = "D:\\temp2";
        String targetPath = "C:\\testdata\\test1";
        var m = new Main(primaryPath, targetPath);

        System.out.println(m.info + "\n"
                + "> row count: " + m.getPrimaryLineCount() + "\n"
                + "> Hashmap size: " + m.getHashmapSize() + "\n"
                + ">> files count: " + m.getTargetFileChkCount());

        if (m.getMode() != 2) {
            m.setErrPrint(">> matched found: " + m.getMatchedArr().size());
        }

        System.out.println(">> Error size: " +  m.getErrPrint().size());
//        m.getErrPrint().forEach(System.out::println);
//        System.out.println("\nMatched 'folder file':");
//        m.getMatchedArr().forEach(System.out::println);

//        m.printErrlogNMatchedToFile(logfile); // OR
        if (m.getMode() == 2) {
            System.out.println("""
                               Portal filename   <> against the txt files:
                               only compare filename.length > 20, ie. ignore  image.jpg, ...
                               """);
            m.getMatchedFilesOnly().forEach(s -> System.out.println(s));
            System.out.println(">>> matchedNameCount: " + m.matchedNameCount);
        }

        System.out.println("Completed. Check " + logfile);
    }
}
 */
