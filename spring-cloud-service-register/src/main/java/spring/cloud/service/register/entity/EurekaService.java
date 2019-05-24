package spring.cloud.service.register.entity;

import java.io.Serializable;

public class EurekaService implements Serializable {
    private String serviceId;
    private String serviceHost;
    private int servicePort;

    public EurekaService() {
    }

    public EurekaService(String serviceId, String serviceHost, int servicePort) {
        this.serviceId = serviceId;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }
}
