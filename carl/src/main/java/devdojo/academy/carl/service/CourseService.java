package devdojo.academy.carl.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import devdojo.academy.core.model.Course;
import devdojo.academy.core.repository.CourseRepository;

@Service
public class CourseService {
    
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public Iterable<Course> list(Pageable pageable){
        return courseRepository.findAll(pageable);
    }
}
