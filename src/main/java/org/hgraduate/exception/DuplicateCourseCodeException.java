package org.hgraduate.exception;

public class DuplicateCourseCodeException extends RuntimeException {
    public DuplicateCourseCodeException(String courseCode) {
        super("이미 존재하는 과목코드입니다: " + courseCode);
    }
}