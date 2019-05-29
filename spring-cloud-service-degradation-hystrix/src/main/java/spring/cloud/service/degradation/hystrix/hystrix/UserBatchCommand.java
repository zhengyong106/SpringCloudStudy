package spring.cloud.service.degradation.hystrix.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.degradation.hystrix.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserBatchCommand extends HystrixCommand<List<User>> {
    private RestTemplate restTemplate;
    private List<String> ids;

    UserBatchCommand(RestTemplate restTemplate, List<String> ids) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserBatchCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(5000)));
        this.restTemplate = restTemplate;
        this.ids = ids;
    }

    /**
     * 调用execute()或queue()前执行此代码方法。
     */
    @Override
    protected List<User> run() {
        ResponseEntity<User[]> responseEntity= this.restTemplate.getForEntity("http://spring-cloud-service-register/getUsers", User[].class);

        List<User> users = new ArrayList<>(this.ids.size());
        for(String id: this.ids){
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

    /**
     *  调用服务失败后，返回的服务降级方法
     *  返回值为与正常服务调用方法返回值相同
     */
    @Override
    protected List<User> getFallback() {
        System.out.println("System error!");
        User[] users = new User[this.ids.size()];
        Arrays.fill(users, null);
        return Arrays.asList(users);
    }
}
