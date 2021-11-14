package testApp;

import framework.annotations.di.Bean;

@Bean
public class SingletonBeanClass {
    public String value;

    public SingletonBeanClass(){
        value = "Singleton Bean Class Initialized";
        System.out.println(value);
    }
}
