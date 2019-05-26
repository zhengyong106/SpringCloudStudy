package spring.cloud.service.degradation.hystrix.controller;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Observer;
import spring.cloud.service.degradation.hystrix.entity.User;
import spring.cloud.service.degradation.hystrix.service.PayService;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class PayController {
    @Autowired
    private PayService payService;

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return payService.getUsers();
    }

    @GetMapping("/getUsersWithAsync")
    public List<User> getUsersWithAsync(){
        Future<List<User>> futureResponse = payService.getUsersWithAsync();

        List<User> users = null;
        try {
            users = futureResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return users;
    }

    @GetMapping("/getUsersWithReactive")
    public void getUsersWithReactive(){
        Observable<List<User>> observableResponse = payService.getUsersWithReactive();

        observableResponse.subscribe(new Observer<List<User>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<User> users) {
                System.out.println(users);
            }
        });
    }

    @GetMapping("/getUsersByCache")
    public List<User> getUsersByCache(){
        // 初始化Hystrix请求上下文
        HystrixRequestContext.initializeContext();
        // 调用服务
        List<User> usersA = payService.getUsersByCache("users");
        // 清除缓存
        //payService.flushCache("users");
        // 再次调用服务通过缓存直接返回
        List<User> usersB = payService.getUsersByCache("users");
        // 判断缓存是否命中
        System.out.println(usersA == usersB ? "cache hit!":"cache miss!");
        return usersA;
    }




    @GetMapping("/getUser")
    public User getUser(String userId){
        return payService.getUser(userId);
    }

    @GetMapping("/getUserWithAsync")
    public User getUserWithAsync(String userId){
        Future<User> futureResponse = payService.getUserWithAsync(userId);

        User user = null;
        try {
            user = futureResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

    @GetMapping("/getUserWithReactive")
    public void getUserWithReactive(String userId){
        Observable<User> observableResponse = payService.getUserWithReactive(userId);

        observableResponse.subscribe(new Observer<User>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(User user) {
                System.out.println(user);
            }
        });
    }

    @GetMapping("/getUserByCache")
    public User getUserByCache(String userId){
        // 初始化Hystrix请求上下文
        HystrixRequestContext.initializeContext();
        // 调用服务
        User userA = payService.getUserByCache("test", userId);
        // 清除缓存
        //CacheHystrixCommand.flushRequestCache("test", userId);
        // 再次调用服务通过缓存直接返回
        User userB = payService.getUserByCache("test", userId);
        // 判断缓存是否命中
        System.out.println(userA == userB ? "cache hit!":"cache miss!");
        return userA;
    }
}
