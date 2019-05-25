package spring.cloud.service.consume.ribbon.service;

import spring.cloud.service.consume.ribbon.entity.User;

import java.util.List;

public interface OrderService {
    List<User> getUsers();
    User getUser(String userId);
}
