
// See chkFileId
//
// To create this test; right-click on 'Main.java', and select Tools > Create Tests
package com.zero1.chkfileid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChkFileIdTest {

    String primaryPath = "..\\..\\verify\\testSample";
    String targetPath = "..\\..\\verify\\testSample";

    @Test
    public void testMain() {
        var m = new ChkFileId(primaryPath, targetPath);
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
        var m = new ChkFileId(primaryPath, targetPath, 1);
        assertEquals(1, m.getMode());
        assertEquals(7, m.getHashmapSize());
    }

    @Test
    public void testMainMode2() {
        var m = new ChkFileId(primaryPath, targetPath, 2);
        assertEquals(2, m.getMode());
        assertEquals(7, m.getHashmapSize());
        assertEquals(0, m.getMatchedFilesOnly().size());
        assertEquals(1, m.getMatchedNameCount());
    }

    @Test
    public void testErrPrint() {
        var m = new ChkFileId(primaryPath, targetPath);
        m.setErrPrint("test");
        m.getErrPrint();
//        m.getErrPrint().forEach(s -> {
//            System.out.println(">>MainTest>> "+s);
//        });
        assertEquals(6, m.getErrPrint().size());
    }

    @Test
    public void testUtf16ToUtf8() {
        String s = ChkFileId.utf16ToUtf8("t\u0000e\u0000s\u0000t\u0000");
        assertEquals("test", s);
    }
    
    // ^^^^^^^^^^^^^  TEST ^^^^^^^^^^^^^
    // https://www.javadoc.io/doc/org.testng/testng/6.11/org/testng/Assert.html
    @Test
    public void testAssert() {

        // using class assert, on the main code  // <<<<<<
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
