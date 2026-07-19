package org.hgraduate.repository;

import org.hgraduate.model.Category;
import org.hgraduate.model.Course;
import org.hgraduate.model.Status;
import org.hgraduate.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDbRepository implements CourseRepository{

    @Override
    public void save(Course course) {
        String sql = "INSERT INTO course(course_code, course_name, category, credit, semester, status, grade, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDateTime now = LocalDateTime.now();
            course.setCreatedAt(now);

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getCategory().name());
            pstmt.setInt(4, course.getCredit());
            pstmt.setString(5, course.getSemester());
            pstmt.setString(6, course.getStatus().name());
            pstmt.setString(7, course.getGrade());
            pstmt.setTimestamp(8, Timestamp.valueOf(now));

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("과목 저장 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<Course> findAll() {
        String sql = "SELECT * FROM course";
        List<Course> courses = new ArrayList<>();

        Connection conn = DBConnection.getInstance().getConnection();

        try(PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    courses.add(mapRowToCourse(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("과목 조회 중 오류가 발생했습니다.", e);
        }
        return courses;
    }

    @Override
    public Optional<Course> findById(Long id) {
        String sql = "SELECT * FROM course where id = ?";
        Course course = null;

        Connection conn = DBConnection.getInstance().getConnection();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    course = mapRowToCourse(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("과목 목록 조회 중 오류가 발생했습니다.", e);
        }
        return Optional.ofNullable(course);
    }

    @Override
    public void update(Course course) {
        String sql = "UPDATE course SET course_code = ?, course_name = ?, category = ?, credit = ?, semester = ?, status = ?, grade = ? WHERE id = ?";

        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getCategory().name());
            pstmt.setInt(4, course.getCredit());
            pstmt.setString(5, course.getSemester());
            pstmt.setString(6, course.getStatus().name());
            pstmt.setString(7, course.getGrade());
            pstmt.setLong(8, course.getId());

            pstmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("과목 수정 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM course WHERE id = ?";

        Connection conn = DBConnection.getInstance().getConnection();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("과목 삭제 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<Course> findByCategory(Category category) {
        String sql = "SELECT * FROM course WHERE category = ?";
        List<Course> courses = new ArrayList<>();

        Connection conn = DBConnection.getInstance().getConnection();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.name());

            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapRowToCourse(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("과목 조회 중 오류가 발생했습니다.", e);
        }
        return courses;
    }

    @Override
    public List<Course> findByKeyword(String keyword) {
        String sql = "SELECT * FROM course WHERE course_name LIKE ?";
        List<Course> courses = new ArrayList<>();

        Connection conn = DBConnection.getInstance().getConnection();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");

            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapRowToCourse(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("과목 조회 중 오류가 발생했습니다.", e);
        }
        return courses;
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        String sql = "SELECT 1 FROM course WHERE course_code = ? LIMIT 1";

        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);

            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("과목 조회 중 오류가 발생했습니다.", e);
        }
        return false;
    }

    private Course mapRowToCourse(ResultSet rs) throws SQLException{
        Course course = new Course();
        course.setId(rs.getLong("id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setCategory(Category.valueOf((rs.getString("category"))));
        course.setCredit(rs.getInt("credit"));
        course.setSemester(rs.getString("semester"));
        course.setStatus(Status.valueOf((rs.getString("status"))));
        course.setGrade(rs.getString("grade"));
        course.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return course;
    }
}
