package spring.cloud.service.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.service.register.entity.User;
import spring.cloud.service.register.service.UserService;

import java.util.List;

@RestController
public class UserControllerImpl implements UserController{
    @Autowired
    private UserService userService; //注入发现客户端

    @Override
    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @Override
    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @Override
    @GetMapping("/retry")
    public String retry(){
        System.out.println("feignRetry方法调用成功");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
