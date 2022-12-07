
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    /* s = "C:\\Users\\AlvinNg\\verify\\test\\test1\\CB718C312BA1B3622ECFDCBF727465F2\\Duke.png"; */
    private static final PrintStream OUT = System.out;
    private static final int HASH_INITIAL_CAP = 70000;
    public String primaryPath;
    public String targetPath;
    public HashMap<String, ArrayList<String>> hashmap;

    public void inputPath() {
        var keyboard = new Scanner(System.in);
        OUT.print(" Enter primary path name: ");
        /* * primaryPath = "D:\\temp";  // */ //
        primaryPath = keyboard.nextLine();
        OUT.print(" Enter target path name: ");
        /* * targetPath = "C:\\Users\\AlvinNg\\verify\\test\\test1"; // */ //        targetPath = keyboard.nextLine();
        /* * targetPath = "C:\\Users\\AlvinNg\\Zero1 Pte Ltd\\Portal - ToBeDeleted\\201808"; // */ //
        targetPath = keyboard.nextLine();
    }

    public void putFilesToMem() {
        String str, textLine;
        int count = 0;
        Scanner fileIn = null;

        var dirfile = new File(primaryPath + "\\");
        if (dirfile.isDirectory()) {
            str = dirfile.getName();
            for (var str2 : dirfile.list()) {
                OUT.println("> " + str2);
                var filePath = (primaryPath + "\\" + str2);

                try {
                    fileIn = new Scanner(
                            new FileInputStream(filePath));
                } catch (FileNotFoundException e) {
                    OUT.println("File not found.");
                    System.exit(0);
                }

                boolean hasNextline;
                hasNextline = fileIn.hasNextLine();
                toContinue:
                while (hasNextline) {
                    textLine = fileIn.nextLine().trim().toLowerCase();
                    if (textLine.length() <= 1) {
                        hasNextline = fileIn.hasNextLine();
                        continue toContinue;
                    }

                    if (!subStringPutToHash(textLine)) {
                        System.err.println(" < " + str2);
                    }
                    count++;
                    hasNextline = fileIn.hasNextLine();
                }
                fileIn.close();
            }
        } else {
            OUT.println("Primary directory NOT correct!");
            System.exit(0);
        }
        OUT.println("> row count: " + count);
    }

    public boolean subStringPutToHash(String s) {
        String filename, mkey, sub;
        try {
            sub = s.substring(0, s.lastIndexOf("\\"));
            mkey = sub.substring(sub.lastIndexOf("\\") + 1); // CB718C312BA1B3622ECFDCBF727465F2
            filename = s.substring(s.lastIndexOf("\\") + 1); // Duke.png
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

            /* * OUT.println(">> Primary >> mkey: " + mkey + " | filename: " + filename); // */
            return true;
        } else {
            System.err.print("> key lgth err: " + s);
            return false;
        }
    }

    public void targetFilesVerifyByHash() {
        var mainfile = new File(targetPath);
        int lgth = 0, count = 0;

        if (mainfile.isDirectory()) {
            lgth = mainfile.list().length;
            OUT.println("\nWill scan thru " + lgth + " directories:");
        } else {
            OUT.println("Target directory NOT correct!");
            System.exit(0);
        }

        for (var str : mainfile.list()) {
            String tagKey = "", tagFilename = "";
            // monitoring
            count = (count <= 0) ? count = lgth / 20 : (count -= 1);
            OUT.print((count <= 0) ? "." : "");

            var dirfile = new File(mainfile + "\\" + str);
            if (dirfile.isDirectory()) {
                tagKey = dirfile.getName().toLowerCase();
                for (var str2 : dirfile.list()) {
                    var subfile = new File(dirfile + "\\" + str2);
                    tagFilename = subfile.getName();
                    /* * OUT.println(">> target key: " + tagKey + " | name: " + tagFilename); // */

                    // if there IS match from hashmap, will log error message.
                    if (hashmap.containsKey(tagKey)) {

                        ArrayList<String> arr = hashmap.get(tagKey);
                        for (String priFileName : arr) {
                            if (priFileName.equalsIgnoreCase(tagFilename)) {
                                System.err.println(">> matched: " + targetPath + "\\" + str
                                        + "\\" + tagFilename);
                            }
                        }
                    }
                }
            } else {
                OUT.println(">> " + dirfile.getName());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        PrintStream errStream = null;
        var logfile = "logmessages.txt";
        try {
            errStream = new PrintStream(
                    new FileOutputStream(logfile));
        } catch (FileNotFoundException e) {
            OUT.println("Error opening file with FileOutputStream.");
            System.exit(0);
        }
        System.setErr(errStream);

        var m = new Main();
        m.inputPath();

        OUT.println("\nSubString key and name, to hashmap.");
        m.hashmap = new HashMap<>(HASH_INITIAL_CAP);

        System.err.println("\n> primaryPath: " + m.primaryPath);
        m.putFilesToMem();
        OUT.println("Hashmap size: " + m.hashmap.size());

        System.err.println("\n>> targetPath: " + m.targetPath);
        m.targetFilesVerifyByHash();
        OUT.println("\nCompleted, check on " + logfile + " for error msg.");

        errStream.close();
    }
}

/*
run:
 Enter primary path name: D:\temp2
 Enter target path name: C:\Users\AlvinNg\verify\test\test1

SubString key and name, to hashmap.
> md5chksum.txt
> row count: 6
Hashmap size: 3

Will scan thru 2 directories:
..
Completed, check on logmessages.txt for error msg.
BUILD SUCCESSFUL (total time: 16 seconds)
 */

/* logmessages.txt
> primaryPath: D:\temp2
> key lgth err: c:\\users\alvinng\verify\test\test1\z01r002\zero1.png < md5chksum.txt
> StringException: photoid_20181118.zip md5 c8139bf1e2aff9f95c5a238a2a0656c6 < md5chksum.txt

>> targetPath: C:\Users\AlvinNg\verify\test\test1
>> matched: C:\Users\AlvinNg\verify\test\test1\C5094E4C507910CFBE9974D1C97CE73D\zero1.png
>> matched: C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Duke.png
>> matched: C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Z01R002.png
*/

/* md5chksum.txt
C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Z01R002.png
C:\Users\AlvinNg\verify\test\test1\C5094E4C507910CFBE9974D1C97CE73D\zero1.png
C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Duke.png
C:\Users\AlvinNg\verify\test\test1\Z01R002\zero1.png
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201808\0a7efcee6ef0761a2e8dea1c17684074\1535777048812682408596.jpg
photoid_20181118.zip md5 c8139bf1e2aff9f95c5a238a2a0656c6
*/

/* tree path for target dir C:\Users\AlvinNg\verify\test\test1
Folder PATH listing for volume Windows-SSD
Volume serial number is 6E2A-67EF
C:.
├───C5094E4C507910CFBE9974D1C97CE73D
│       zero1.png
│
└───CB718C312BA1B3622ECFDCBF727465F2
        Duke.png
        Z01R002.png


C:\Users\AlvinNg\verify\test\test1>
*/