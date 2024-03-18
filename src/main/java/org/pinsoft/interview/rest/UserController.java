package org.pinsoft.interview.rest;

import org.pinsoft.interview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/user")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        UserDto userDtoRes = userService.saveUser(userDto);

        return ResponseEntity.ok(userDtoRes);
    }

}
