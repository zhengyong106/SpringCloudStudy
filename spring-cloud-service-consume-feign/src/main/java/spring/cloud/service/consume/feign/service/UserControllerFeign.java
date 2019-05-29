package spring.cloud.service.consume.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@Service
// value为消费的服务id，fallback包含服务降级方法
@FeignClient(value = "spring-cloud-service-register", fallback = UserControllerFeignFallback.class)
public interface UserControllerFeign extends UserController {
}
