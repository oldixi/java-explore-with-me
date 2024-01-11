package ru.practicum.user;

import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    UserDto createUser(NewUserRequest user);

    void deleteUser(int userId);

    List<UserDto> getUsers(int[] ids, int from, int size);

    User getUserById(int userId);
}