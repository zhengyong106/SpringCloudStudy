package spring.cloud.service.degradation.hystrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;
import spring.cloud.service.degradation.hystrix.entity.User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CacheHystrixCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private String userId;

    public CacheHystrixCommand(Setter setter, RestTemplate restTemplate, String userId){
        super(setter);
        this.restTemplate = restTemplate;
        this.userId = userId;
    }

    @Override
    protected User run() {
        return restTemplate.getForEntity("http://localhost:8011/getUser?userId={UserId}", User.class, this.userId).getBody();
    }

    @Override
    protected User getFallback() {
        System.out.println("System error!");
        return null;
    }

    @Override
    protected String getCacheKey() {
        return this.userId;
    }

    public static void flushRequestCache(String commandKey, String cacheKey){
        HystrixRequestCache.getInstance(
                HystrixCommandKey.Factory.asKey(commandKey), HystrixConcurrencyStrategyDefault.getInstance())
                .clear(String.valueOf(cacheKey));
    }
}
