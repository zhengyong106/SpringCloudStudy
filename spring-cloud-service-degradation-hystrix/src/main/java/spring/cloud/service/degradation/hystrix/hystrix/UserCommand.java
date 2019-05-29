package spring.cloud.service.degradation.hystrix.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;
import spring.cloud.service.degradation.hystrix.entity.User;

import java.util.concurrent.Future;

public class UserCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private String userId;

    public UserCommand(RestTemplate restTemplate, String userId){
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
        this.restTemplate = new RestTemplate();
        return restTemplate.getForEntity("http://localhost:8011/getUser?userId={UserId}", User.class, this.userId).getBody();
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

    public static void main(String[] args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();


        //同步请求
        System.out.println("This is sync request's response:" + new UserCommand(restTemplate, "0001").execute());


        //异步请求
        Future<User> userFuture = new UserCommand(restTemplate, "0001").queue();
        System.out.println("This is async request's response:" + userFuture.get());


        // 响应式请求，通过subscribe()注册观察者（异步非阻塞）
        Observable<User> hotObservable = new UserCommand(restTemplate, "0001").observe();
        hotObservable.subscribe(new Observer<User>() {
            // 先执行onNext再执行onCompleted
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(User user) {
                System.out.println("This is reactor request's response:" + user);
            }
        });


        // 响应式请求，转换为同步步请求
        System.out.println("This is reactive request's response:" + new UserCommand(restTemplate, "0001").toObservable().toBlocking().single());


        // 响应式请求，转换为异步请求
        Future<User> reactiveFuture = new UserCommand(restTemplate, "0001").toObservable().toBlocking().toFuture();
        System.out.println("This is reactive->future request's response:" + reactiveFuture.get());
    }
}
