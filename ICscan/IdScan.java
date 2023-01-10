package com.zero1.idscan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 */
public class IdScan {

    private String idtype;
    private String filePath;

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "IdScan{" + "idtype=" + idtype + ", filePath=" + filePath + '}';
    }

    public IdScan idType() {
        Scanner fileIn = null;
        try {
            fileIn = new Scanner(new FileInputStream(filePath));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            System.exit(0);
        }

        boolean hasNextLine = fileIn.hasNextLine();
        String hasNext;
        while (hasNextLine) {
            hasNext = fileIn.nextLine();
            if (hasNext.contains("EMPLOYMENT PASS")) {
                return new IdEmployP(); // extract ID's data. ToDo
            } else if (hasNext.contains("DEPENDANT'S PASS")) {
                return new IdDependP(); // extract ID's data. ToDo
            } else if (hasNext.contains("STUDENT'S PASS")) {
                return new IdStudP(); // extract ID's data. ToDo
            } else if (hasNext.contains("S Pass No.")) {
                return new IdSPass(); // extract ID's data. ToDo
            } else if (hasNext.contains("WORK PERMIT")) {
                return new IdWorkP(); // extract ID's data. ToDo
            }
            hasNextLine = fileIn.hasNextLine();
        }
        fileIn.close(); //  ****

        return null;
    }

    public static void main(String[] args) {
        
        var s = new IdScan();

        var path = "..\\sample\\IDimagesText\\";
        var dirfile = new File(path);
        for (var str2 : dirfile.list()) {
            s.setFilePath(path + str2);
            
            // Todolist: to be save into CSV file

            System.out.println(">> : " + s.idType() + ", Path: " + path + str2);
        }
    }
}

/*
>> : IdScan{idtype=DependantPass, filePath=null}, Path: ..\sample\IDimagesText\dp_id2.txt
>> : IdScan{idtype=EmploymentPass, filePath=null}, Path: ..\sample\IDimagesText\ep_id1.txt  
>> : IdScan{idtype=SPass, filePath=null}, Path: ..\sample\IDimagesText\spass_id1.txt        
>> : IdScan{idtype=StudentPass, filePath=null}, Path: ..\sample\IDimagesText\student_id1.txt
>> : IdScan{idtype=WorkPermit, filePath=null}, Path: ..\sample\IDimagesText\wp_id3.txt     
*/
