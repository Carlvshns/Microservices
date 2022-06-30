package devdojo.academy.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import devdojo.academy.core.model.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long>{
    
}
