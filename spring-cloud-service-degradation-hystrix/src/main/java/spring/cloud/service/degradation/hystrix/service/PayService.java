package spring.cloud.service.degradation.hystrix.service;

import spring.cloud.service.degradation.hystrix.entity.User;

import java.util.List;

public interface PayService {
    List<User> getUsers();
    User getUser(String userId);
}
