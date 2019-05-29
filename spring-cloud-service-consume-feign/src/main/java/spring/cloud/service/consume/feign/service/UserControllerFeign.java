package spring.cloud.service.consume.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spring.cloud.service.consume.feign.entity.User;

import java.util.List;

@Service
@FeignClient(value = "spring-cloud-service-register")
public interface UserControllerFeign extends UserController {
    @Override
    @RequestMapping (value = "/getUsers", method = RequestMethod.GET)
    List<User> getUsers();

    @Override
    @RequestMapping(value = "/getUser/{userId}", method = RequestMethod.GET)
    User getUser(@PathVariable("userId") String userId);
}
