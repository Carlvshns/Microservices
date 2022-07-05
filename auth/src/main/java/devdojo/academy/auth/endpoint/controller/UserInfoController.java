package devdojo.academy.auth.endpoint.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devdojo.academy.core.model.ApplicationUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user")
@Api(value = "Endpoints to manage auth")
public class UserInfoController {
 
    @ApiOperation(value = "Will retireve the information from the user available in the token", response = ApplicationUser.class)
    @GetMapping(path = "info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApplicationUser> getUserInfo(Principal principal){
        ApplicationUser applicationUser = 
        (ApplicationUser)((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return new ResponseEntity<>(applicationUser, HttpStatus.OK);
    }
}
