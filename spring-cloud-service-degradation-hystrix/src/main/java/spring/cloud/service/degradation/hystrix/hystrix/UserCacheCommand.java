package spring.cloud.service.degradation.hystrix.hystrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.degradation.hystrix.entity.User;

public class UserCacheCommand extends HystrixCommand<User> {
    private RestTemplate restTemplate;
    private String userId;

    public UserCacheCommand(RestTemplate restTemplate, String userId){
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserCommandGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(5000)));
        this.restTemplate = restTemplate;
        this.userId = userId;
    }

    /**
     * 调用execute()或queue()前执行此代码方法。
     */
    @Override
    protected User run() {
        return restTemplate.getForEntity("http://spring-cloud-service-register/getUser?userId={UserId}", User.class, this.userId).getBody();
    }

    /**
     *  调用服务失败后，返回的服务降级方法
     *  返回值为与正常服务调用方法返回值相同
     */
    @Override
    protected User getFallback() {
        System.out.println("System error!");
        return null;
    }

    /**
     *  默认情况下，返回空值，即“不缓存”
     *  返回用于请求缓存的键
     */
    @Override
    protected String getCacheKey() {
        return this.userId;
    }

    /**
     *  刷新缓存
     */
    public static void flushRequestCache(String commandKey, String cacheKey){
        HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey(commandKey), HystrixConcurrencyStrategyDefault.getInstance())
                .clear(String.valueOf(cacheKey));
    }
}
