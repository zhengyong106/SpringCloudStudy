package spring.cloud.service.consume.ribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.consume.ribbon.entity.User;
import spring.cloud.service.consume.ribbon.service.OrderService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/getUsers")
    public List<User> getService(){
        return orderService.getUsers();
    }

    @GetMapping("/getUser/{userId}")
    public User getServiceById(@PathVariable("userId") String userId){
        return orderService.getUser(userId);
    }
}
