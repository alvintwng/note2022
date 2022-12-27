/* www.Zero1.Sg 2022Dec */
package com.zero1.chkfileid;

public class App {

    public static void main(String args[]) {

        var logfile = "logmessages.txt";

        System.out.println("SubString key and name, to hashmap.");

        String primaryPath = "D:\\temp2";
        String targetPath = "C:\\testdata\\test1";
        var m = new ChkFileId(primaryPath, targetPath);

        System.out.println(m.getInfo() + "\n"
                + "> row count: " + m.getPrimaryLineCount() + "\n"
                + "> Hashmap size: " + m.getHashmapSize() + "\n"
                + ">> files count: " + m.getTargetFileChkCount());

        if (m.getMode() != 2) {
            System.out.println(">> matched found: " + m.getMatchedArr().size());
        }

        System.out.println(">> Error size: " + m.getErrPrint().size());
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
            System.out.println(">>> matchedNameCount: " + m.getMatchedNameCount());
        }

        System.out.println("Completed. Check " + logfile);
    }
}

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
 */
 /*
C:\testdata\test1>tree /f
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

C:\testdata\test1>D:
D:\temp2>more md5chksum.txt
C:\Users\AlvinNg\Zero1 Pte Ltd\Portal - ToBeDeleted\201809\fe5e99114138db8a57c888ec270631b6\20180512_214614.jpg7118.jpg

D:\temp2>
 */
