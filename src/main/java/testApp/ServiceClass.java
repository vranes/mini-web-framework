package testApp;

import framework.annotations.di.Service;

@Service
public class ServiceClass {
    public String value;

    public ServiceClass(){
        value = "Service Class Initialized";
        System.out.println(value);
    }
}
