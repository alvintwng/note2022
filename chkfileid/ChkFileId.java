/* www.Zero1.Sg 2022Dec
https://github.com/alvinzero1/verify/tree/api */
package com.zero1.chkfileid;

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
 * To check and compare from log files and directories of folder with files.
 *
 * Folder for files should be 32 chars String.
 */
public class ChkFileId {

    private static final String DELIMITER = "\\";
    private final HashMap<String, ArrayList<String>> hashmap;
    private ArrayList<String> matchedArr, errPrint, matchedFilesOnly;
    private int primaryLineCount = 0, targetFileChkCount = 0, mode = 0;
    private int matchedNameCount = 0;
    private String info = "";

    /**
     * Initiate application.
     *
     * @param primaryPath
     * @param targetPath
     */
    public ChkFileId(String primaryPath, String targetPath) {
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
    public ChkFileId(String primaryPath, String targetPath, int modeType) {
        mode = modeType;
        hashmap = new HashMap<>();
        matchedArr = new ArrayList<>();
        errPrint = new ArrayList<>();
        matchedFilesOnly = new ArrayList<>();
        addPrimarypathFilenameToHashmap(primaryPath);
        targetFilesVerifyByHash(targetPath);
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

    public void setErrPrint(String s) {
        errPrint.add(s);
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

    /**
     * To get text lines from file, extract subString folder name and file
     * name.Sample format {@code CB718C312BA1B3622ECFDCBF727465F2\filename.png}.
     * Put key of 32 chars string, and filename as value to {@code hashmap}.
     *
     * @param primaryPath File of list of paths
     * @return
     */
    public final boolean addPrimarypathFilenameToHashmap(String primaryPath) {
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
     * if first or second chars in s contain null \u0000, remove all.
     *
     * String s in `utf16`, converting to `utf8`, remove the access char 0
     *
     * @param s
     * @return s
     */
    public static String utf16ToUtf8(String s) {
        if ((s.codePointAt(1) == 0) || (s.codePointAt(3) == 0)) {
            return s.replace("\u0000", "");
        }
        return s;
    }

    private boolean subStringPutToHash(String s) {

        String filename, mkey;

        String[] fields = s.split(DELIMITER + DELIMITER); // eg fields.length: 4
        if (fields.length < 3) {
            errPrint.add("> splited sentences less than 3.");
            return false;
        }
        mkey = fields[fields.length - 2].trim().toLowerCase();
        // eg. CB718C312BA1B3622ECFDCBF727465F2
        filename = fields[fields.length - 1].trim().toLowerCase();

        // check if right key lgth
        if (mkey.length() == 32) {
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
    public final boolean targetFilesVerifyByHash(String targetPath) {
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
