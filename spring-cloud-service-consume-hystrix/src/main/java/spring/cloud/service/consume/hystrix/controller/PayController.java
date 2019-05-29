package spring.cloud.service.consume.hystrix.controller;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;
import spring.cloud.service.consume.hystrix.entity.User;
import spring.cloud.service.consume.hystrix.service.PayService;

import java.util.*;
import java.util.concurrent.Future;

@RestController
public class PayController {
    @Autowired
    PayService payService;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/getUser")
    public User getUserWithSync(String userId){
        return this.payService.getUserWithSync(userId);
    }

    @GetMapping("/getUserWithAsync")
    public void getUserWithAsync(String userId) throws Exception {
        long startTime = System.currentTimeMillis();
        Future<User> future1 = this.payService.getUserWithAsync(userId);
        Future<User> future2 = this.payService.getUserWithAsync(userId);

        System.out.println("user1 == user2: " + (future1.get() == future2.get()));
        System.out.println("use time: " + (System.currentTimeMillis() - startTime));
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
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try{
            User userA = this.payService.getUserByCache(userId);
            //UserCacheCommand.flushRequestCache(userId);
            User userB = this.payService.getUserByCache(userId);
            System.out.println(userA == userB ? "cache hit!":"cache miss!");
            return userA;
        } finally {
            context.close();
        }
    }

    @GetMapping("/collapseTest")
    public void collapseTest(){
        int requestCount = 100000;
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
                // 不知道为什么使用注解形式生成的请求合并要慢很多
                futures.add(this.payService.getUserByCollapse(userId));
                //futures.add(this.payService.getUserByCollapseAnnotation(userId));
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




    @GetMapping("/getUsers")
    public List<User> getUsersWithSync(){
        return this.payService.getUsersWithSync();
    }

    @GetMapping("/getUsersWithAsync")
    public void getUsersWithAsync() throws Exception {
        long startTime = System.currentTimeMillis();
        Future<List<User>> future1 = this.payService.getUsersWithAsync();
        Future<List<User>> future2 = this.payService.getUsersWithAsync();

        System.out.println("users1 == users2: " + (future1.get() == future2.get()));
        System.out.println("use time: " + (System.currentTimeMillis() - startTime));
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
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try{
            List<User> usersA = this.payService.getUsersByCache("users");
            //this.payService.flushUsersCache("users");
            List<User> usersB = this.payService.getUsersByCache("users");
            System.out.println(usersA == usersB ? "cache hit!":"cache miss!");
            return usersA;
        } finally {
            context.close();
        }
    }
}
