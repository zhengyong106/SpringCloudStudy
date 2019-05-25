package spring.cloud.service.degradation.hystrix.controller;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.degradation.hystrix.PayHystrixCommand;
import spring.cloud.service.degradation.hystrix.entity.User;
import spring.cloud.service.degradation.hystrix.service.PayService;

import java.util.List;

@RestController
public class PayController {
    @Autowired
    private PayService payService;

    @GetMapping("/getUsers")
    @HystrixCommand(fallbackMethod = "hystrixFallback")
    public List<User> getUsers(){
        return payService.getUsers();
    }

    @GetMapping("/getUser")
    public User getUser(){
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("");
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionTimeoutInMilliseconds(5000);

        com.netflix.hystrix.HystrixCommand.Setter commandSetter = com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(groupKey)
                .andCommandPropertiesDefaults(commandProperties);

        //同步请求
        User syncUser = new PayHystrixCommand(commandSetter, new RestTemplate(), "0001").execute();
        return syncUser;
    }

    public List<User> hystrixFallback(){
        System.out.println("System error!");
        return null;
    }
}
