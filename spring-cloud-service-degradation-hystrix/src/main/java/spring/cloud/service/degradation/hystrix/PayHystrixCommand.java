package spring.cloud.service.degradation.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.degradation.hystrix.entity.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PayHystrixCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private String userId;

    public PayHystrixCommand(Setter setter, RestTemplate restTemplate, String userId){
        super(setter);
        this.restTemplate = restTemplate;
        this.userId = userId;
    }

    @Override
    protected User run() {
        // 本地请求
        return restTemplate.getForEntity("http://localhost:8011/getUser?userId={UserId}", User.class, this.userId).getBody();
        // 连注册中心请求
        //return restTemplate.getForEntity("http://spring-cloud-service-register/getUser?userId={UserId}", User.class, this.userId).getBody();
    }

    @Override
    protected User getFallback() {
        System.out.println("System error!");
        return null;
    }

    public static void main(String[] args) {
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("userGroup");
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionTimeoutInMilliseconds(5000);

        com.netflix.hystrix.HystrixCommand.Setter commandSetter = com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(groupKey)
                .andCommandPropertiesDefaults(commandProperties);

        //同步请求
        User syncUser = new PayHystrixCommand(commandSetter, new RestTemplate(), "0001").execute();
        System.out.println("This is sync request's response:" + syncUser);

        //异步请求
        Future<User> responseFuture = new PayHystrixCommand(commandSetter, new RestTemplate(), "0001").queue();

        User asyncUser = null;
        try {
            asyncUser = responseFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("This is async request's response:" + asyncUser);
    }
}
