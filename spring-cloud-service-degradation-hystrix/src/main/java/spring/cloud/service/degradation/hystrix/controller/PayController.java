package spring.cloud.service.degradation.hystrix.controller;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;
import spring.cloud.service.degradation.hystrix.entity.User;
import spring.cloud.service.degradation.hystrix.service.PayService;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class PayController {
    @Autowired
    PayService payService;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return this.payService.getUsers();
    }

    @GetMapping("/getUsersWithAsync")
    public List<User> getUsersWithAsync() throws Exception {
        Future<List<User>> futureResponse = this.payService.getUsersWithAsync();
        return futureResponse.get();
    }

    @GetMapping("/getUsersWithReactive")
    public void getUsersWithReactive(){
        Observable<List<User>> observableResponse = this.payService.getUsersWithReactive();
        observableResponse.subscribe(new Observer<List<User>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<User> users) {
                System.out.println("This is reactor request's response:" + users);
            }
        });
    }

    @GetMapping("/getUsersByCache")
    public List<User> getUsersByCache(){
        // 初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try{
            // 调用服务
            List<User> usersA = this.payService.getUsersByCache("users");
            // 清除缓存
            //payService.flushCache("users");
            // 再次调用服务通过缓存直接返回
            List<User> usersB = this.payService.getUsersByCache("users");
            // 判断缓存是否命中
            System.out.println(usersA == usersB ? "cache hit!":"cache miss!");
            return usersA;
        } finally {
            context.close();
        }
    }




    @GetMapping("/getUser")
    public User getUser(String userId){
        return this.payService.getUser(userId);
    }

    @GetMapping("/getUserWithAsync")
    public User getUserWithAsync(String userId) throws ExecutionException, InterruptedException {
        Future<User> futureResponse = this.payService.getUserWithAsync(userId);
        return futureResponse.get();
    }

    @GetMapping("/getUserWithReactive")
    public void getUserWithReactive(String userId){
        Observable<User> observableResponse = this.payService.getUserWithReactive(userId);
        observableResponse.subscribe(new Observer<User>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(User user) {
                System.out.println("This is reactor request's response:" + user);
            }
        });
    }

    @GetMapping("/getUserByCache")
    public User getUserByCache(String userId){
        // 初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try{
            // 调用服务
            User userA = this.payService.getUserByCache("test", userId);
            // 清除缓存
            //CacheHystrixCommand.flushRequestCache("test", userId);
            // 再次调用服务通过缓存直接返回
            User userB = this.payService.getUserByCache("test", userId);
            // 判断缓存是否命中
            System.out.println(userA == userB ? "cache hit!":"cache miss!");
            return userA;
        } finally {
            context.close();
        }
    }




    @GetMapping("/getUserByCollapse")
    public User getUserByCollapse(String userId){
        // 初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        User user = null;
        try {
            user = this.payService.getUserByCollapse(userId).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
        return user;
    }

    @GetMapping("/collapseTest")
    public void collapseTest(){
        int requestCount = 10000000;
        List<User> users = new ArrayList<>(requestCount);
        List<String> userIds = new ArrayList<>(requestCount);
        List<Future<User>> futures = new ArrayList<>(requestCount);

        // 初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            // 初始化批量请求参数UserIds
            for(int i=0; i < requestCount; i++){
                String userId = "000" + (int)(1 + Math.random() * 3);
                userIds.add(userId);
            }

            long startTime = System.currentTimeMillis();

            // 发起批量请求
            for (String userId: userIds){
                futures.add(this.payService.getUserByCollapse(userId));
            }

            // 获取批量请求结果
            for (Future<User> future: futures){
                users.add(future.get());
            }

            System.out.printf("The test completed! use time: %sms\n", (System.currentTimeMillis() - startTime));
            users.removeAll(Collections.singleton(null));
            System.out.printf("Request success count: %s\n", users.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
