package spring.cloud.service.consume.hystrix.service;

import rx.Observable;
import spring.cloud.service.consume.hystrix.entity.User;

import java.util.List;
import java.util.concurrent.Future;

public interface PayService {
    User getUserWithSync(String userId);
    Future<User> getUserWithAsync(String userId);
    Observable<User> getUserWithReactive(String userId);
    User getUserByCache(String userId);


    List<User> getUsersWithSync();
    Future<List<User>> getUsersWithAsync();
    Observable<List<User>> getUsersWithReactive();
    List<User> getUsersByCache(String cacheKey);

    void flushUsersCache(String cacheKey);

    Future<User> getUserByCollapse(String userId);
    Future<User> getUserByCollapseAnnotation(String userId);
}
