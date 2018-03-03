package com.example.asus.androidadvance_assignment;

/**
 * Created by asus on 10/02/2017.
 */

public class students {
    private int ID;
    private String studentsID, studentName, Class;

    public students(int ID, String studentsID, String studentName,String Class) {
        this.ID = ID;
        this.studentsID = studentsID;
        this.studentName = studentName;
        this.Class = Class;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentsID() {
        return studentsID;
    }

    public void setStudentsID(String studentsID) {
        this.studentsID = studentsID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getaClass() {
        return Class;
    }

    public void setaClass(String aClass) {
        Class = aClass;
    }
}
