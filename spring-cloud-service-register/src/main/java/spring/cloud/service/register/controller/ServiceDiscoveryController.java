package spring.cloud.service.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.service.register.entity.EurekaService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ServiceDiscoveryController {
    @Autowired
    private DiscoveryClient client; //注入发现客户端

    @RequestMapping(value = "/serviceDiscovery", method = RequestMethod.GET)
    public List<EurekaService> serviceDiscovery(String serviceId){
        List<EurekaService> eurekaServices = new ArrayList<>();

        List<String> serviceIds = serviceId == null ? client.getServices() : Arrays.asList(new String[]{serviceId});
        for(String _serviceId: serviceIds){
            //获取服务实例，作用为之后console显示效果
            List<ServiceInstance> serviceInstances = client.getInstances(_serviceId);
            for(ServiceInstance serviceInstance: serviceInstances){
                eurekaServices.add(new EurekaService(serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort()));
            }
        }
        return eurekaServices;
    }
}
