package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.UserService;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/users")
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest user) {
        log.info("Request for user {} creation", user.getEmail());
        return userService.createUser(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive @PathVariable int userId) {
        log.info("Request for user {} deletion", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) int[] ids,
                               @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                               @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request for get users with ids={} from={} size={}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }
}