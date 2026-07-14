package org.hgraduate;

import org.hgraduate.repository.CourseFileRepository;
import org.hgraduate.repository.CourseRepository;
import org.hgraduate.service.CourseService;
import org.hgraduate.view.ConsoleView;

public class HGraduateApplication {

    public static void main(String[] args) {
        CourseRepository repository = new CourseFileRepository(); // Step 5에서 이 한 줄만 교체
        CourseService service = new CourseService(repository);
        ConsoleView view = new ConsoleView(service);
        view.start();
    }
}