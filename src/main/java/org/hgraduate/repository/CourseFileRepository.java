package org.hgraduate.repository;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;
import org.hgraduate.model.Status;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseFileRepository implements CourseRepository {

    private static final String DEFAULT_FILE_PATH = "data/courses.csv";
    private static final String DELIMITER = ",";

    private final String filePath;
    private final List<Course> courses = new ArrayList<>();

    public CourseFileRepository() {
        this(DEFAULT_FILE_PATH);
    }

    public CourseFileRepository(String filePath) {
        this.filePath = filePath;
        load();
    }

    @Override
    public void save(Course course) {
        long maxId = courses.stream()
                .mapToLong(Course::getId)
                .max()
                .orElse(0L);

        course.setId(maxId + 1);
        course.setCreatedAt(LocalDateTime.now());
        courses.add(course);
        saveToFile();
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void update(Course course) {
        Course existing = findById(course.getId())
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을 수 없습니다. id=" + course.getId()));

        existing.setCourseCode(course.getCourseCode());
        existing.setCourseName(course.getCourseName());
        existing.setCategory(course.getCategory());
        existing.setCredit(course.getCredit());
        existing.setSemester(course.getSemester());
        existing.setStatus(course.getStatus());
        existing.setGrade(course.getGrade());
        saveToFile();
    }

    @Override
    public void delete(Long id) {
        courses.removeIf(c -> c.getId().equals(id));
        saveToFile();
    }

    @Override
    public List<Course> findByCategory(Category category) {
        return courses.stream()
                .filter(c -> c.getCategory() == category)
                .toList();
    }

    @Override
    public List<Course> findByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return courses.stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        return courses.stream()
                .anyMatch(c -> c.getCourseCode().equals(courseCode));
    }

    private void load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                courses.add(fromLine(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("과목 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (Course course : courses) {
                writer.write(toLine(course));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("과목 파일을 저장하는 중 오류가 발생했습니다.", e);
        }
    }

    private String toLine(Course course) {
        return String.join(DELIMITER,
                String.valueOf(course.getId()),
                course.getCourseCode(),
                course.getCourseName(),
                course.getCategory().name(),
                String.valueOf(course.getCredit()),
                course.getSemester(),
                course.getStatus().name(),
                course.getGrade() == null ? "" : course.getGrade(),
                course.getCreatedAt().toString()
        );
    }

    private Course fromLine(String line) {
        String[] tokens = line.split(DELIMITER, -1);

        Course course = new Course();
        course.setId(Long.parseLong(tokens[0]));
        course.setCourseCode(tokens[1]);
        course.setCourseName(tokens[2]);
        course.setCategory(Category.valueOf(tokens[3]));
        course.setCredit(Integer.parseInt(tokens[4]));
        course.setSemester(tokens[5]);
        course.setStatus(Status.valueOf(tokens[6]));
        course.setGrade(tokens[7].isEmpty() ? null : tokens[7]);
        course.setCreatedAt(LocalDateTime.parse(tokens[8]));
        return course;
    }
}
