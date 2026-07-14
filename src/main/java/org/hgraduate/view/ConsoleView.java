package org.hgraduate.view;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;
import org.hgraduate.model.Status;
import org.hgraduate.service.CourseService;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final CourseService courseService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleView(CourseService courseService) {
        this.courseService = courseService;
    }

    public void start() {
        while (true) {
            try {
                printMenu();
                int choice = readInt("번호를 입력하세요: ");

                switch (choice) {
                    case 1 -> handleRegister();
                    case 2 -> handleFindAll();
                    case 3 -> handleSearchByCategory();
                    case 4 -> handleSearchByKeyword();
                    case 5 -> handleUpdate();
                    case 6 -> handleDelete();
                    case 0 -> {
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    }
                    default -> System.out.println("잘못된 번호입니다.");
                }
            } catch (RuntimeException e) {
                System.out.println("오류: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== 수강 과목 관리 시스템 ===");
        System.out.println("1. 과목 등록");
        System.out.println("2. 전체 조회");
        System.out.println("3. 구분별 검색");
        System.out.println("4. 과목명 키워드 검색");
        System.out.println("5. 과목 수정");
        System.out.println("6. 과목 삭제");
        System.out.println("0. 종료");
    }

    private void handleRegister() {
        String courseCode = readLine("과목코드: ");
        String courseName = readLine("과목명: ");
        Category category = readCategory();
        int credit = readInt("학점: ");
        String semester = readLine("수강학기 (예: 2024-1): ");
        Status status = readStatus();
        String grade = readLine("성적 (없으면 enter): ");
        if (grade.isBlank()) {
            grade = null;
        }

        Course course = new Course(courseCode, courseName, category, credit, semester, status, grade);
        courseService.register(course);
        System.out.println("등록 완료!");
    }

    private void handleFindAll() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("등록된 과목이 없습니다.");
            return;
        }
        courses.forEach(System.out::println);
    }

    private void handleSearchByCategory() {
        Category category = readCategory();
        List<Course> result = courseService.searchByCategory(category);
        if (result.isEmpty()) {
            System.out.println("해당 구분의 과목이 없습니다.");
            return;
        }
        result.forEach(System.out::println);
    }

    private void handleSearchByKeyword() {
        String keyword = readLine("검색할 키워드: ");
        List<Course> result = courseService.searchByKeyword(keyword);
        if (result.isEmpty()) {
            System.out.println("일치하는 과목이 없습니다.");
            return;
        }
        result.forEach(System.out::println);
    }

    private void handleUpdate() {
        Long id = readLong("수정할 과목 id: ");
        Course course = courseService.getCourse(id);

        String newName = readLine("새 과목명 (" + course.getCourseName() + "): ");
        if (!newName.isBlank()) {
            course.setCourseName(newName);
        }

        String newCreditInput = readLine("새 학점 (" + course.getCredit() + "): ");
        if (!newCreditInput.isBlank()) {
            course.setCredit(Integer.parseInt(newCreditInput));
        }

        course.setStatus(readStatus());

        courseService.updateCourse(course);
        System.out.println("수정 완료!");
    }

    private void handleDelete() {
        Long id = readLong("삭제할 과목 id: ");
        courseService.deleteCourse(id);
        System.out.println("삭제 완료!");
    }

    private Category readCategory() {
        System.out.println("구분 선택: 1.전공필수 2.전공선택 3.교양필수 4.교양선택 5.자유선택");
        int choice = readInt("번호: ");
        return switch (choice) {
            case 1 -> Category.MAJOR_REQUIRED;
            case 2 -> Category.MAJOR_ELECTIVE;
            case 3 -> Category.GENERAL_REQUIRED;
            case 4 -> Category.GENERAL_ELECTIVE;
            case 5 -> Category.FREE_ELECTIVE;
            default -> throw new IllegalArgumentException("잘못된 구분 선택입니다.");
        };
    }

    private Status readStatus() {
        System.out.println("상태 선택: 1.완료 2.예정 3.재이수");
        int choice = readInt("번호: ");
        return switch (choice) {
            case 1 -> Status.COMPLETED;
            case 2 -> Status.PLANNED;
            case 3 -> Status.RETAKE;
            default -> throw new IllegalArgumentException("잘못된 상태 선택입니다.");
        };
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        return Integer.parseInt(readLine(prompt));
    }

    private Long readLong(String prompt) {
        return Long.parseLong(readLine(prompt));
    }
}