/* www.Zero1.Sg 2022Dec
https://github.com/alvinzero1/verify/tree/api */
package com.zero1.chkfileid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * To check and compare from log files and directories of folder with files.
 *
 * Folder for files should be 32 chars String.
 */
public class ChkFileId {

    private static final String DELIMITER = "\\";
    private final HashMap<String, ArrayList<String>> hashmap;
    private ArrayList<String> matchedArr, errPrint, matchedFilesOnly;
    private int primaryLineCount = 0, targetFileChkCount = 0, mode = 0, matchedNameCount = 0;
    private String primaryPath, targetPath, info = "";

    /**
     * Initiate application.
     *
     * @param primaryPathName
     * @param targetPathName
     */
    public ChkFileId(String primaryPathName, String targetPathName) {
        hashmap = new HashMap<>();
        matchedArr = new ArrayList<>();
        errPrint = new ArrayList<>();
        primaryPath = primaryPathName;
        targetPath = targetPathName;
        addPrimarypathFilenameToHashmap();
        targetFilesVerifyByHash();
    }

    /**
     * Initiate application with
     *
     * @param primaryPathName
     * @param targetPathName
     * @param modeType see mode
     * @see setMode()
     */
    public ChkFileId(String primaryPathName, String targetPathName, int modeType) {
        mode = modeType;
        hashmap = new HashMap<>();
        matchedArr = new ArrayList<>();
        errPrint = new ArrayList<>();
        matchedFilesOnly = new ArrayList<>();
        primaryPath = primaryPathName;
        targetPath = targetPathName;
        addPrimarypathFilenameToHashmap();
        targetFilesVerifyByHash();
    }

    /**
     * <li> mode = 1: filename as hashmap key.to compare folername togather with
     * filename to matchced.
     * <li> mode = 2: filename as hashmap key. However, only to compare
     * filename.
     *
     * @return mode
     */
    public int getMode() {
        return mode;
    }

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

    public List<String> getErrPrint() {
        return errPrint;
    }

    public int getPrimaryLineCount() {
        return primaryLineCount;
    }

    public int getTargetFileChkCount() {
        return targetFileChkCount;
    }

    public int getMatchedNameCount() {
        return matchedNameCount;
    }

    public String getInfo() {
        return info;
    }

    public void setPrimaryPath(String primaryPath) {
        this.primaryPath = primaryPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setErrPrint(String s) {
        errPrint.add(s);
    }

    /**
     * To get text lines from file, extract subString folder name and file
     * name.Sample format {@code CB718C312BA1B3622ECFDCBF727465F2\filename.png}.
     * Put key of 32 chars string, and filename as value to {@code hashmap}.
     *
     * @return
     */
    public final boolean addPrimarypathFilenameToHashmap() {
        info += "\n> primaryPath: " + primaryPath;
        String textLine;
        Scanner fileIn;

        var dirfile = new File(primaryPath + DELIMITER);
        if (dirfile.isDirectory()) {
            for (var str2 : dirfile.list()) {
                var filePath = primaryPath + DELIMITER + str2;

                try {
                    if ((new File(filePath)).isDirectory()) {
                        continue;
                    }

                    fileIn = new Scanner(new FileInputStream(filePath));
                    boolean hasNextline = fileIn.hasNextLine();
                    while (hasNextline) {
                        textLine = fileIn.nextLine().trim().toLowerCase();
                        textLine = utf8ToUtf16(textLine);

                        if (textLine.length() <= 1) {
                            hasNextline = fileIn.hasNextLine();
                            continue;
                        } else if (!subStringPutToHash(textLine)) {
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
     * String s in `utf8, convert to `utf16`
     *
     * @param s
     * @return s
     */
    public static String utf8ToUtf16(String s) {

        //return s.replace("\u0000", ""); // alternative
        //
        s += "\u0000"; // add null for the last char 
        byte arr[];
        arr = s.getBytes(StandardCharsets.UTF_8);
        return new String(arr, StandardCharsets.UTF_16LE);
    }

    private boolean subStringPutToHash(String s) {

        String filename, mkey;

        String[] fields = s.split(DELIMITER + DELIMITER); // eg fields.length: 4
        if (fields.length < 3) {
            errPrint.add("> splited sentences less than 3.");
            return false;
        }
        filename = fields[fields.length - 1].trim().toLowerCase();
        mkey = fields[fields.length - 2].trim().toLowerCase();
        // eg. CB718C312BA1B3622ECFDCBF727465F2

        // check if right key lgth
        if (mkey.length() != 32) {
            errPrint.add("> key lgth err: " + s);
            return false;
        }

        switch (mode) {
            case 2 -> {
                //  filename as hashkey, full String as hashvalue
                if (!hashmap.containsKey(filename)) {
                    hashmap.put(filename, new ArrayList<>());
                }
                hashmap.get(filename).add(s);
            }
            case 1 -> {
                //  filename as hashkey, folder as hashvalue
                if (!hashmap.containsKey(filename)) {
                    hashmap.put(filename, new ArrayList<>());
                }
                hashmap.get(filename).add(mkey);
            }
            default -> {
                // folder(mKey) as hash key, filename as hashvalue
                if (!hashmap.containsKey(mkey)) {
                    hashmap.put(mkey, new ArrayList<>());
                }
                hashmap.get(mkey).add(filename);
            }
        }
        return true;
    }

    /**
     * Add additional path. To get the full path name of directory individual
     * files. And compare with {@code hashmap} for key as folder, value as
     * filename.
     *
     * @return True for process done, or false for wrong directory
     */
    public final boolean targetFilesVerifyByHash() {
        info += "\n>> targetPath: " + targetPath;
        var mainfile = new File(targetPath);

        if (mainfile.isDirectory()) {
            for (var str : mainfile.list()) {
                var dirfile = new File(mainfile + DELIMITER + str);
                if (dirfile.isDirectory()) {

                    dirfileCompareHash(dirfile, str);

                } else {
                    errPrint.add(">> " + dirfile.getName());
                }
            }
            return true;
        } else {
            setErrPrint("Target directory NOT correct!");
            return false;
        }
    }

    private void dirfileCompareHash(File dirfile, String str) {
        var tagKey = dirfile.getName().toLowerCase();
        for (var str2 : dirfile.list()) {
            var subfile = new File(dirfile + DELIMITER + str2);
            var tagFilename = subfile.getName();
            var filepath = targetPath + DELIMITER + str + DELIMITER + tagFilename;

            switch (mode) {
                case 1 -> {
                    // filename as hashkey
                    if (hashmap.containsKey(tagFilename)) {
                        ArrayList<String> arr = hashmap.get(tagFilename);
                        arr.forEach(priFileName -> {
                            if (priFileName.equalsIgnoreCase(tagKey)) {
                                matchedArr.add(filepath);
                            }
                        });
                    }
                }
                case 2 -> {
                    if (hashmap.containsKey(tagFilename)) {
                        matchedNameCount++;
                        if (tagFilename.length() > 20) { //ignore  image.jpg,...,nric_front.jpg,nric front small.jpg
                            matchedFilesOnly.add(filepath + " <> " + hashmap.get(tagFilename));
                        }
                    }
                }
                default -> {
                    // folder(mKey) as hash key,
                    if (hashmap.containsKey(tagKey)) {
                        ArrayList<String> arr = hashmap.get(tagKey);
                        arr.forEach(priFileName -> {
                            if (priFileName.equalsIgnoreCase(tagFilename)) {
                                matchedArr.add(filepath);
                            }
                        });
                    }
                }
            }
            targetFileChkCount++;
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
        }
        outputStream.println("\nError Messages:");
        getErrPrint().forEach(outputStream::println);

        outputStream.close();
    }

    @Override
    public String toString() {
        return hashmap.toString();
    }

}
