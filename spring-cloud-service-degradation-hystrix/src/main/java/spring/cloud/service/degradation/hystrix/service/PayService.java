package spring.cloud.service.degradation.hystrix.service;

import rx.Observable;
import spring.cloud.service.degradation.hystrix.entity.User;

import java.util.List;
import java.util.concurrent.Future;

public interface PayService {
    List<User> getUsers();
    Future<List<User>> getUsersWithAsync();
    Observable<List<User>> getUsersWithReactive();
    List<User> getUsersByCache(String cacheKey);
    void flushCache(String cacheKey);

    User getUser(String userId);
    Future<User> getUserWithAsync(String userId);
    Observable<User> getUserWithReactive(String userId);
    User getUserByCache(String commandKey, String userId);
}
