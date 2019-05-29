package spring.cloud.service.degradation.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;
import spring.cloud.service.degradation.hystrix.entity.User;
import spring.cloud.service.degradation.hystrix.hystrix.UserCacheCommand;
import spring.cloud.service.degradation.hystrix.hystrix.UserCollapse;
import spring.cloud.service.degradation.hystrix.hystrix.UserCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

@Service
public class PayServiceImpl implements PayService{
    //注入restTemplate
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "hystrixFallback", commandProperties = @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000"))
    public List<User> getUsers() {
        // 使用restTemplate调用微服务接口
        ResponseEntity<User[]> responseEntity= restTemplate.getForEntity("http://spring-cloud-service-register/getUsers", User[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    @HystrixCommand(fallbackMethod = "hystrixFallback", commandProperties = @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000"))
    public Future<List<User>> getUsersWithAsync() {
        return new AsyncResult<List<User>>() {
            @Override
            public List<User> invoke() {
                ResponseEntity<User[]> responseEntity= restTemplate.getForEntity("http://spring-cloud-service-register/getUsers", User[].class);
                return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
            }
        };
    }

    @Override
    @HystrixCommand(fallbackMethod = "hystrixFallback", commandProperties = @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000"))
    public Observable<List<User>> getUsersWithReactive() {
        return Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                new Thread(() -> {
                    User[] users = restTemplate.getForObject("http://spring-cloud-service-register/getUsers", User[].class);

                    subscriber.onNext(Arrays.asList(users));
                    subscriber.onCompleted();
                }).start();
            }
        });
    }

    @Override
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "hystrixFallback", commandProperties = @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000"), commandKey = "usersCache")
    public List<User> getUsersByCache(String cacheKey) {
        ResponseEntity<User[]> responseEntity= restTemplate.getForEntity("http://spring-cloud-service-register/getUsers", User[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    /**
     * 这里有两点要特别注意：
     * 1、这个方法的入参的类型必须与缓存方法的入参类型相同，如果不同被调用会报这个方法找不到的异常
     * 2、这个方法的返回值一定是String类型
     */
    public String getCacheKey(String cacheKey){
        return cacheKey;
    }

    /**
     * 使用注解清除缓存
     * CacheRemove 必须指定commandKey才能进行清除指定缓存
     */
    @Override
    @CacheRemove(commandKey = "usersCache")
    @HystrixCommand
    public void flushCache(String cacheKey){
        System.out.println("请求缓存已清空！");
    }

    @Override
    @HystrixCollapser(batchMethod = "batchUserByCollapseAnnotation",collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds",value = "100")})
    public Future<User> getUserByCollapseAnnotation(String userId) {
        throw new RuntimeException("This method body should not be executed");
    }

    @HystrixCommand(fallbackMethod = "hystrixFallback", commandProperties = @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000"))
    public List<User> batchUserByCollapseAnnotation(List<String> userIds) {
        ResponseEntity<User[]> responseEntity= this.restTemplate.getForEntity("http://spring-cloud-service-register/getUsers", User[].class);

        List<User> users = new ArrayList<>(userIds.size());
        for(String id: userIds){
            User user = null;
            for(User _user: Objects.requireNonNull(responseEntity.getBody())){
                if(_user.getUserId().equals(id)){
                    user = _user;
                    break;
                }
            }
            users.add(user);
        }

        return users;
    }

    public List<User> hystrixFallback(){
        System.out.println("System error!");
        return null;
    }

    public List<User> hystrixFallback(String cacheKey){
        System.out.println("System error!");
        return null;
    }

    public List<User> hystrixFallback(List<String> userIds){
        System.out.println("System error!");
        User[] users = new User[userIds.size()];
        Arrays.fill(users, null);
        return Arrays.asList(users);
    }





    @Override
    public User getUser(String userId) {
        return new UserCommand(restTemplate, userId).execute();
    }

    @Override
    public Future<User> getUserWithAsync(String userId) {
        return new UserCommand(restTemplate, userId).queue();
    }

    @Override
    public Observable<User> getUserWithReactive(String userId) {
        return new UserCommand(restTemplate, userId).observe();
    }

    @Override
    public User getUserByCache(String commandKey, String userId) {
        return new UserCacheCommand(restTemplate, userId).execute();
    }

    @Override
    public Future<User> getUserByCollapse(String userId) {
        return new UserCollapse(restTemplate, userId).queue();
    }
}
