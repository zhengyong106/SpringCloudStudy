package spring.cloud.zuul.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ZuulClientApplication {
    public static void main(String args[]){
        SpringApplication.run(ZuulClientApplication.class, args);
    }

    @RequestMapping("/")
    public String index(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello everyone";
    }
}