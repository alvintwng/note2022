
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

    public Main() {
        hashmap = new HashMap<>();
    }

    /**
     * To get text lines from file, extract subString folder name and file name.
     * Sample format {@code CB718C312BA1B3622ECFDCBF727465F2\filename.png}.
     *
     * Put key of 32 chars string, and filename as value to {@code hashmap}.
     *
     * @param primaryPath File of list of paths
     */
    public void putFilenameToHashmap(String primaryPath) {
        String textLine;
        int priCount = 0;
        Scanner fileIn;
        System.out.println("");
        System.out.println("\nSubString key and name, to hashmap.");
        System.err.println("\n> primaryPath: " + primaryPath);
        var dirfile = new File(primaryPath + DELIMITER);
        if (dirfile.isDirectory()) {
            for (var str2 : dirfile.list()) {
                System.out.println("> " + str2);
                var filePath = (primaryPath + DELIMITER + str2);

                try {
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
                        priCount++;
                        hasNextline = fileIn.hasNextLine();
                    }
                    fileIn.close();

                } catch (FileNotFoundException e) {
                    System.out.println("File not found.");
                    System.exit(0);
                }
            }
        } else {
            System.out.println("Primary directory NOT correct!");
            System.exit(0);
        }
        System.out.println("> row count: " + priCount);
        System.out.println("> Hashmap size: " + hashmap.size());
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
     * To get the full path name of directory individual files. And compare with
     * {@code hashmap} for key as folder, value as filename.
     *
     * @param targetPath Path from local drive, or local OneDrive.
     * @return True for process done, or false for wrong directory
     */
    public boolean targetFilesVerifyByHash(String targetPath) {
        System.err.println("\n>> targetPath: " + targetPath);
        var mainfile = new File(targetPath);

        if (mainfile.isDirectory()) {
            int monitor = 0;
            var dirLgth = mainfile.list().length;
            System.out.println("\nScanning thru " + dirLgth + " directories:");

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
                                    System.err.println(">> matched: " + targetPath + DELIMITER + str
                                            + DELIMITER + tagFilename);
                                }
                            }
                        }
                    }
                } else {
                    System.out.println(">> " + dirfile.getName());
                }

                // monitoring, done at 50 'dots'
                monitor = (monitor <= 0) ? dirLgth / 50 : monitor - 1;
                System.out.print((monitor <= 0) ? "." : "");
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

    /**
     * Test run
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        var m = new Main();

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

        m.putFilenameToHashmap("D:\\temp2");
        m.targetFilesVerifyByHash("C:\\Users\\AlvinNg\\verify\\test\\test1");

//        m.putFilenameToHashmap("D:\\temp");
//        m.targetFilesVerifyByHash("C:\\Users\\AlvinNg\\Zero1 Pte Ltd\\Portal - ToBeDeleted\\201808");
//        m.targetFilesVerifyByHash("C:\\Users\\AlvinNg\\Zero1 Pte Ltd\\Portal - ToBeDeleted\\201809");
        
        System.out.println("\nCompleted, check " + logfile + " for error msg.");
        errStream.close();
    }
}

/*
run:


SubString key and name, to hashmap.
> md5chksum.txt
> row count: 9
> Hashmap size: 5

Will scan thru 3 directories:
...
Completed, check logmessages.txt for error msg.
BUILD SUCCESSFUL (total time: 0 seconds)
 */

 /* eg "C:\\Users\\AlvinNg\\verify\\test\\test1\\CB718C312BA1B3622ECFDCBF727465F2\\Duke.png"; */

 /* logmessages.txt
> primaryPath: D:\temp2
> key lgth err: c:\\users\alvinng\verify\test\test1\z01r002\zero1.png < md5chksum.txt
> StringException: photoid_20181118.zip md5 c8139bf1e2aff9f95c5a238a2a0656c6 < md5chksum.txt

>> targetPath: C:\Users\AlvinNg\verify\test\test1
>> matched: C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Duke.png
>> matched: C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Z01R002.png
 */

 /* md5chksum.txt
C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Z01R002.png
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201809\fe5e99114138db8a57c888ec270631b6\20180512_214629.jpg
C:\Users\AlvinNg\verify\test\test1\Z01R002\zero1.png
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201808\0a7efcee6ef0761a2e8dea1c17684074\1535777048812682408596.jpg
photoid_20181118.zip md5 c8139bf1e2aff9f95c5a238a2a0656c6
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201809\1ce8f8bf682135c2e44b0414cb8572e2\1536034574342-565107118.jpg
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201808\d3c8965cadd2b12123d469480709ba53\image.jpg
C:\Users\AlvinNg\verify\test\test1\cb718c312ba1b3622ecfdcbf727465f2\Duke.png
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201809\fe5e99114138db8a57c888ec270631b6\20180512_214614.jpg
 */

 /* tree path for target dir C:\Users\AlvinNg\verify\test\test1
C:\Users\AlvinNg\verify\test\test1>tree /f
Folder PATH listing for volume Windows-SSD
Volume serial number is 6E2A-67EF
C:.
├───C5094E4C507910CFBE9974D1C97CE73D
│       zero1.png
│
├───CB718C312BA1B3622ECFDCBF727465F2
│       Duke.png
│       Z01R002.png
│       zero1QR.png
│
└───fe5e99114138db8a57c888ec270631b6

C:\Users\AlvinNg\verify\test\test1>
 */
