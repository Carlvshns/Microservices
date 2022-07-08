package devdojo.academy.carl.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devdojo.academy.core.model.Course;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import devdojo.academy.carl.service.CourseService;

@RestController
@RequestMapping("v1/admin/course")
@Api(value = "Endpoints to manage course")
public class CourseController {

    private CourseService courseService;
    
    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @ApiOperation(value = "List all available courses", response = Course[].class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Iterable<Course>> list(Pageable pageable){
        return new ResponseEntity<>(courseService.list(pageable), HttpStatus.OK);
    }
}
