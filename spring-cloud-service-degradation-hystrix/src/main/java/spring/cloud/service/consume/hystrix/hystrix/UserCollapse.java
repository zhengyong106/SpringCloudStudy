package spring.cloud.service.consume.hystrix.hystrix;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.consume.hystrix.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserCollapse extends HystrixCollapser<List<User>, User, String> {
    private String userId;
    private RestTemplate restTemplate;

    public UserCollapse(RestTemplate restTemplate, String userId) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("PayCollapse"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter()
                        .withTimerDelayInMilliseconds(100)));
        this.userId = userId;
        this.restTemplate = restTemplate;
    }

    @Override
    public String getRequestArgument() {
        return this.userId;
    }

    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, String>> collapsedRequests) {
        //按请求数声名UserId的集合
        List<String> userIds = new ArrayList<>(collapsedRequests.size());

        for(CollapsedRequest<User, String> collapsedRequest : collapsedRequests) {
            userIds.add(collapsedRequest.getArgument());
        }
        return new UserBatchCommand(this.restTemplate, userIds);
    }

    @Override
    protected void mapResponseToRequests(List<User> batchResponse, Collection<CollapsedRequest<User, String>> collapsedRequests) {
        int count = 0 ;
        for(CollapsedRequest<User, String> collapsedRequest : collapsedRequests){
            //从批响应集合中按顺序取出结果
            User user = batchResponse.get(count++);
            //将结果放回原Request的响应体内
            collapsedRequest.setResponse(user);
        }
    }
}
