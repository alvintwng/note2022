
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CheckPhotoId {

    void checkPhotoId() throws IOException {

        File dir = new File("D:\\temp");// "D:\\temp3"); ////"Temp");
        File[] files = dir.listFiles();

        HashMap<String, String> hmPhotoIDs = new HashMap<>();
        int count, numFiles = 0;

        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }

            if (!file.getName().toLowerCase().startsWith("photo")) {
                continue;
            }

            numFiles++;
            count = 0;
            final String photoIDFile = file.getName(); // eg PhotoID_201808_01.txt

            Scanner scanner = new Scanner(new FileInputStream(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.contains("\\")) {
                    continue;
                }

                String[] fields = line.split("\\\\"); // eg fields.length: 4
                String folder = fields[fields.length - 2].trim().toUpperCase();
                String filename = fields[fields.length - 1].trim().toUpperCase();
                String key = folder + "_" + filename;
                // eg 0 0 3 5 8 C 0 A E C 5 4 3 6 F 4 8 F 7 8 4 0 C 9 2 2 7 7 9 A 8 D_2 0 1 8 1 0 2 9 _ 1 2 1 0 4 7 . J P G

                if (hmPhotoIDs.containsKey(key)) {
                    System.out.println( //Log.errWriteLogln(
                            "Duplicate: " + photoIDFile + ", " + key);
                } else {
                    hmPhotoIDs.put(key, photoIDFile);
                }
                count++;
            }
            scanner.close();
            System.out.println(//  Log.writeLogln(
                    file.getAbsolutePath() + " rows : " + count);
        }
        System.out.println( // Log.writeLogln(
                "Total files " + numFiles);

        //////// Target directory
        int total = 0;
        Set<String> tbdPhotoIDs = new HashSet<>();
        Scanner scanner = new Scanner(new FileInputStream("D:\\files.txt")); //"D:\\testdatatest1.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.contains("\\")) {
                continue;
            }

            String[] fields = line.split("\\\\"); // eg fields.length: 8
            String folder = fields[fields.length - 2].trim().toUpperCase();
            String filename = fields[fields.length - 1].trim().toUpperCase();
            String key = folder + "_" + filename;
            // eg key: 0 0 0 1 0 1 B 8 6 A C 2 5 2 B 1 1 0 5 D 9 1 8 9 7 B 4 3 7 1 C 8_I M G _ 2 0 1 8 0 1 0 5 _ 1 1 3 4 2 2 _ 2 1 5 . J P G

            if (tbdPhotoIDs.contains(key)) {
                System.out.println( //Log.errWriteLogln(
                        "files - Duplicate: " + key);
            } else {
                tbdPhotoIDs.add(key);
            }
            total++;
        }
        scanner.close();

        System.out.println( //Log.writeLogln(
                "files rows : " + total);

        ////// comparing the dir "D:\\temp" with the target "D:\\files.txt"
        int found = 0, notFound = 0;
        for (String se : tbdPhotoIDs) {
            if (hmPhotoIDs.containsKey(se)) {
                found++;
            } else {
                System.out.println( //Log.errWriteLogln(
                        se);
                notFound++;
            }
        }

        System.out.println(//Log.writeLogln(
                "total " + total + ", found " + found + ", not found " + notFound);
    }

    public static void main(String args[]) throws IOException {
        var m = new CheckPhotoId();
        m.checkPhotoId();
    }
}
/* Sample Dialogue 2
--- exec-maven-plugin:3.0.0:exec (default-cli) @ app ---
D:\temp3\PhotoID_md5chksum.txt rows : 8
Total files 1
files rows : 4
C 5 0 9 4 E 4 C 5 0 7 9 1 0 C F B E 9 9 7 4 D 1 C 9 7 C E 7 3 D_Z E R O 1 . P N G
C B 7 1 8 C 3 1 2 B A 1 B 3 6 2 2 E C F D C B F 7 2 7 4 6 5 F 2_D U K E . P N G
C B 7 1 8 C 3 1 2 B A 1 B 3 6 2 2 E C F D C B F 7 2 7 4 6 5 F 2_Z E R O 1 Q R . P N G
C B 7 1 8 C 3 1 2 B A 1 B 3 6 2 2 E C F D C B F 7 2 7 4 6 5 F 2_Z 0 1 R 0 0 2 . P N G
total 4, found 0, not found 4
*/
/* Sample Dialogue
--- exec-maven-plugin:3.0.0:exec (default-cli) @ app ---
D:\temp\PhotoID-20181201.txt rows : 1883
...
D:\temp\PhotoID_20190722.txt rows : 1800
Total files 69
files rows : 37494
5 B 2 A 4 2 B B 9 B 6 A 8 0 7 3 9 D 6 D 2 C 1 2 E B E 6 C 4 7 D_S C R E E N S H O T _ 2 0 1 8 - 0 3 - 1 2 - 1 8 - 1 1 - 2 0 - 1 3 2 _ C O M . D R O P B O X . A N D R O I D . P N G
...
F 5 1 C 4 A 9 F 7 F E A 3 A F 1 B 7 F 5 9 1 C E 8 4 2 0 9 5 0 5_S C R E E N S H O T _ 2 0 1 8 - 0 3 - 2 8 - 0 9 - 5 1 - 0 4 - 0 6 2 _ C O M . A D O B E . R E A D E R . P N G
total 37494, found 37474, not found 20
*/

/*
Get-ChildItem -Recurse | Select-String "dummy" -List | Select Path
another powershell command you can use to search for string in files recursively.

Get-ChildItem -Path "C:\testdata\test1" -File -Recurse | select fullname > .\testdatatest1.txt
```
FullName
--------
C:\testdata\test1\C5094E4C507910CFBE9974D1C97CE73D\zero1.png
C:\testdata\test1\CB718C312BA1B3622ECFDCBF727465F2\Duke.png
C:\testdata\test1\CB718C312BA1B3622ECFDCBF727465F2\Z01R002.png
C:\testdata\test1\CB718C312BA1B3622ECFDCBF727465F2\zero1QR.png
```
PhotoID_md5chksum.txt
```
C:\Users\AlvinNg\verify\test\test1\CB718C312BA1B3622ECFDCBF727465F2\Z01R002.png
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201809\fe5e99114138db8a57c888ec270631b6\20180512_214629.jpg
C:\Users\AlvinNg\verify\test\test1\Z01R002\zero1.png
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201808\0a7efcee6ef0761a2e8dea1c17684074\1535777048812682408596.jpg
photoid_20181118.zip md5 c8139bf1e2aff9f95c5a238a2a0656c6
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201809\1ce8f8bf682135c2e44b0414cb8572e2\1536034574342-565107118.jpg
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201808\d3c8965cadd2b12123d469480709ba53\image.jpg
C:\Users\AlvinNg\verify\test\test1\cb718c312ba1b3622ecfdcbf727465f2\Duke.png
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201809\fe5e99114138db8a57c888ec270631b6\20180512_214614.jpg
```
*/
