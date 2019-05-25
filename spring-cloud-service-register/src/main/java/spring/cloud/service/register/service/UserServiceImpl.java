package spring.cloud.service.register.service;

import org.springframework.stereotype.Service;
import spring.cloud.service.register.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private static final List<User> users;

    static{
        users = new ArrayList<>();
        users.add(new User("0001", "张三", 20));
        users.add(new User("0002", "李四", 21));
        users.add(new User("0003", "王五", 22));
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUser(String userId) {
        for(User user: users){
            if(user.getUserId().equals(userId)){
                return user;
            }
        }
        return null;
    }
}
