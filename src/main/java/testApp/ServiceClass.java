package testApp;

import framework.annotations.di.Service;

@Service
public class ServiceClass {
    public String value;

    public ServiceClass(){
        value = "This is a Service Class";
//        System.out.println(value);
    }
}
