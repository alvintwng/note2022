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

}
