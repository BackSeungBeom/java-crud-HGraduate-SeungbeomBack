package org.hgraduate.model;

import java.time.LocalDateTime;

public class Course {
    private Long id;
    private String courseCode;
    private String courseName;
    private Category category;
    private int credit;
    private String semester;
    private Status status;
    private String grade;          // nullable — 예정 상태면 null
    private LocalDateTime createdAt;

    public Course() {
    }

    public Course(String courseCode, String courseName, Category category,
                  int credit, String semester, Status status, String grade) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.category = category;
        this.credit = credit;
        this.semester = semester;
        this.status = status;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", category=" + category +
                ", credit=" + credit +
                ", semester='" + semester + '\'' +
                ", status=" + status +
                ", grade='" + grade + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

}
