package spring.cloud.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RefreshScope
public class ConfigClinetApplication {
    public static void main(String args[]){
        SpringApplication.run(ConfigClinetApplication.class, args);
    }

    @Value("${profile}")
    private String profile;

    @Value("${password}")
    private String password;

    @RequestMapping("getProfile")
    public String getProfile(){
        return profile;
    }

    @RequestMapping("getPassword")
    public String getPassword(){
        return password;
    }
}
