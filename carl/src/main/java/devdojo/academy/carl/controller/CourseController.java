package devdojo.academy.carl.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devdojo.academy.core.model.Course;
import devdojo.academy.carl.service.CourseService;

@RestController
@RequestMapping("v1/admin/course")
public class CourseController {

    private CourseService courseService;
    
    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Iterable<Course>> list(Pageable pageable){
        return new ResponseEntity<>(courseService.list(pageable), HttpStatus.OK);
    }
}
