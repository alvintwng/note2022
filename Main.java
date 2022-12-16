/* www.Zero1.Sg 2022Dec
https://github.com/alvinzero1/verify/tree/api */
package com.zero1.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * <li> mode = 1: filename as hashmap key. to compare folername togather
     * with filename to matchced.
     * <li> mode = 2: filename as hashmap key. However, only to compare
     * filename.
     */
    public int getMode() {
        return mode;
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
    public List<String> getMatchedArr() {
        return matchedArr;
    }

    /**
     * Use in mode 2 only.
     *
     * @return Array of list of matching files of primaryPath and targetPath.
     */
    public List<String> getMatchedFilesOnly() {
        return matchedFilesOnly;
    }

    /**
     *
     * @return list of error msg
     */
    public List<String> getErrPrint() {
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
        info += "\n> primaryPath: " + primaryPath;
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

                        textLine = utf16ToUtf8(textLine);

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

    /**
     * if first or second chars in s contain null \u0000, remove all
     *
     * @param s
     * @return s
     */
    public static String utf16ToUtf8(String s) {
        if ((s.codePointAt(1) == 0) || (s.codePointAt(3) == 0)) {

            // String s in utf16, converting to utf8
            // remove the access char 0
            return s.replace("\u0000", "");
        }
        return s;
    }

    private boolean subStringPutToHash(String s) {

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

                    /* */ assert (mode == 0) : "mode error"; // test only  */
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
        info += "\n>> targetPath: " + targetPath;
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
        } //else {
        outputStream.println("\nError Messages:");
        getErrPrint().forEach(outputStream::println);
        //}
        outputStream.close();
    }

    @Override
    public String toString() {
        return hashmap.toString();
    }

}

/* MainTest.java
// To create this; Projects/com.zero1.app > right-click `New` > `Test for Existing Class`
package com.zero1.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MainTest {

    String primaryPath = "..\\..\\verify\\app\\testSample";
    String targetPath = "..\\..\\verify\\app\\testSample";

    @Test
    public void testMain() {
        var m = new Main(primaryPath, targetPath);
        assertEquals(5, m.getHashmapSize());
        assertEquals(1, m.getMatchedArr().size());
        assertEquals(9, m.getPrimaryLineCount());
        assertEquals(3, m.getTargetFileChkCount());

        assertTrue(m.addPrimarypathFilenameToHashmap(primaryPath));
        assertTrue(m.targetFilesVerifyByHash(targetPath));
        assertEquals(5, m.getHashmapSize());
        assertEquals(3, m.getMatchedArr().size());
        assertEquals(18, m.getPrimaryLineCount());
        assertEquals(6, m.getTargetFileChkCount());
    }

    @Test
    public void testMainMode1() {
        var m = new Main(primaryPath, targetPath, 1);
        assertEquals(1, m.getMode());
        assertEquals(7, m.getHashmapSize());
    }

    @Test
    public void testMainMode2() {
        var m = new Main(primaryPath, targetPath, 2);
        assertEquals(2, m.getMode());
        assertEquals(7, m.getHashmapSize());
        assertEquals(0, m.getMatchedFilesOnly().size());
        assertEquals(1, m.matchedNameCount);
    }

    @Test
    public void testErrPrint() {
        var m = new Main(primaryPath, targetPath);
        m.setErrPrint("test");
        m.getErrPrint();
//        m.getErrPrint().forEach(s -> {
//            System.out.println(">>MainTest>> "+s);
//        });
        assertEquals(6, m.getErrPrint().size());
    }

    @Test
    public void testUtf16ToUtf8() {
        String s = Main.utf16ToUtf8("t\u0000e\u0000s\u0000t\u0000");
        assertEquals("test", s);
    }

    // https://www.javadoc.io/doc/org.testng/testng/6.11/org/testng/Assert.html
    @Test
    public void testAssert() {

        // using class assert, on the main code
        int value = 22;
        assert value >= 20 : " Underweight"; 

        // void assertEquals(boolean expected,boolean actual): checks that two
        // primitives/objects are equal. It is overloaded.
        String s = "s";
        assertEquals("s", s);

        // void assertTrue(boolean condition): checks that a condition is true.
        boolean t = true;
        assertTrue(t);

        // void assertFalse(boolean condition): checks that a condition is
        // false.
        boolean f = false;
        assertFalse(f);

        // void assertNull(Object obj): checks that object is null.
        String o = null;
        assertNull(o);

        // void assertNotNull(Object obj): checks that object is not null.
        assertNotNull(f);

        System.out.println("""
            C:\\...\\testSample>more md5chkUtf16.txt
            C:\\...\\testSample\\C5094E4C507910CFBE9974D1C97CE73D
                           \\zero1.pngec270631b6\\20180512_214614.jpg596.jpg
            C:\\...\\testSample>tree /F
            Folder PATH listing for volume Windows-SSD
            Volume serial number is 6E2A-67EF
            C:.
            │   md5chkUtf16.txt
            │
            ├───C5094E4C507910CFBE9974D1C97CE73D
            │       zero1.png
            │       zero1QR.png
            │
            └───C877E4C399F8442990ADFD0DF0681B53
                    qr01.png """);
    }
}
 */

 /* Run Project (App)
--- exec-maven-plugin:3.0.0:exec (default-cli) @ app ---
SubString key and name, to hashmap.
> primaryPath:D:\temp2>> targetPath:C:\testdata\test1
> row count: 9
> Hashmap size: 5
>> files count: 4
>> matched found: 2
>> Error size: 5
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
