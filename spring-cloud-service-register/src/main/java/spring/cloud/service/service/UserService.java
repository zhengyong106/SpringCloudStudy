package spring.cloud.service.register.service;

import spring.cloud.service.register.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUser(String userId);
}
