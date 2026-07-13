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
}
