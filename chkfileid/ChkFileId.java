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

    private static final String DELIMITER = "\\"; // Window system
    private final HashMap<String, ArrayList<String>> hashmap;
    private ArrayList<String> matchedArr, errPrint;
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
        primaryPath = primaryPathName;
        targetPath = targetPathName;
        addPrimarypathFilenameToHashmap();
        targetFilesVerifyByHash();
    }

    /**
     * <li> default, mode = 0: foldername as hashmap key, to compare foldername
     * together with filename to matchced.
     * <li> mode = 1: filename as hashmap key, to compare foldername together
     * with filename to matchced.
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

    public List<String> getErrPrint() {
        return errPrint;
    }

    public int getPrimaryLineCount() {
        return primaryLineCount;
    }

    public int getTargetFileChkCount() {
        return targetFileChkCount;
    }

    // mode 2, all filename sizes that matached
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
        if (!dirfile.isDirectory()) {
            errPrint.add("Primary directory NOT correct!");
            return false;
        }

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
                        errPrint.add("< not put to Hashmap: " + str2 + ">" + textLine);
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
    }

    /**
     * String s encoded to `utf16`. Remove chars that not Cmd text.
     *
     * @param s
     * @return s
     */
    public static String utf8ToUtf16(String s) {

        // uses surrogate pairs, however
        // if there are any unpaired surrogate, will be assume utf8 in String.
        if (s.contains("\u0000")) { // check for extra null in utf8
            s += "\u0000"; // add null at the last char 

            // to replaces malformed-input and unmappable-character sequences.
            byte[] arr = s.getBytes(StandardCharsets.UTF_8);
            // Java default UTF-16
            s = new String(arr, StandardCharsets.UTF_16LE);
        }

        // remove chars that not Cmd text
        String regexNotThisCha = "[^a-zA-Z0-9:_. [" + DELIMITER + DELIMITER + "-]]";
        s = s.replaceAll(regexNotThisCha, "");
        return s;
    }

    /**
     * Splitted string with delimiter. Folder should be 32chars. Split by
     * backlash need to be 3 or more parts. Put into hashmap.
     *
     * @param String of full path.
     * @return true if ok
     */
    private boolean subStringPutToHash(String s) {
        String filename, mkey;
        int mkeyLength = 32; // folder name length 

        String[] fields = s.split(DELIMITER + DELIMITER); // eg fields.length: 4
        if (fields.length < 3) {
            errPrint.add("> splited sentences less than 3.");
            return false;
        }
        filename = fields[fields.length - 1].trim().toLowerCase();
        mkey = fields[fields.length - 2].trim().toLowerCase();
        // eg. CB718C312BA1B3622ECFDCBF727465F2

        // check if right key lgth
        if (mkey.length() != mkeyLength) {
            errPrint.add("> key lgth " + mkeyLength + " chars " + " err: " + s);
            return false;
        }

        switch (mode) {
            case 2 -> {
                //  filename as hashkey, full String as hashvalue
                hashmap.computeIfAbsent(filename,
                        k -> new ArrayList<>()).add(s);
            }
            case 1 -> {
                //  filename as hashkey, folder as hashvalue
                hashmap.computeIfAbsent(filename,
                        k -> new ArrayList<>()).add(mkey);
            }
            default -> {
                // folder(mKey) as hash key, filename as hashvalue
                hashmap.computeIfAbsent(mkey,
                        k -> new ArrayList<>()).add(filename);

//                if (!hashmap.containsKey(mkey)) {
//                    hashmap.put(mkey, new ArrayList<>());
//                }
//                hashmap.get(mkey).add(filename);
            }
        }
        return true;
    }

    /**
     * Add additional path. To get the full path name of directory individual
     * files. And compare with {@code hashmap} for key as folder, value as
     * filename.
     *
     * Mode 2, minimum filename to accept = mode2MinimumFileLength 9 chars.
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
                    errPrint.add(">> not directory: " + dirfile.getName());
                }
            }
            return true;
        } else {
            setErrPrint("Target directory NOT correct!");
            return false;
        }
    }

    /**
     * compare path with hashmap.
     *
     * @param dirfile
     * @param str
     */
    private void dirfileCompareHash(File dirfile, String str) {
        var mode2MinimumFileLength = 9;
        var tagKey = dirfile.getName().toLowerCase();

        for (var str2 : dirfile.list()) {
            var subfile = new File(dirfile + DELIMITER + str2);
            var tagFilename = subfile.getName();
            var filepath = targetPath + DELIMITER + str + DELIMITER + tagFilename;

            if (mode == 1 && hashmap.containsKey(tagFilename)) {
                // filename as hashkey
                addIfMatched(tagFilename, tagKey, filepath);
            } else if (mode == 2 && hashmap.containsKey(tagFilename)) {
                matchedNameCount++; // all filename sizes that matched
                //add only if filename is mode2MinimumFileLength or more
                if (tagFilename.length() >= mode2MinimumFileLength) {
                    matchedArr.add(filepath + " <> " + hashmap.get(tagFilename));
                }
            } else if (mode == 0 && hashmap.containsKey(tagKey)) {
                // folder(mKey) as hash key,
                addIfMatched(tagKey, tagFilename, filepath);
            }

            targetFileChkCount++;
        }
    }

    /**
     * add texts if found matched keyvalue in the hashmap array value
     *
     * @param hashkey
     * @param target
     * @param texts
     */
    private void addIfMatched(String hashkey, String target, String texts) {

        ArrayList<String> arr = hashmap.get(hashkey);

        arr.forEach(primaryFileName -> {
            if (primaryFileName.equalsIgnoreCase(target)) {
                matchedArr.add(texts);
            }
        });
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
            matchedArr.forEach(outputStream::println);
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
