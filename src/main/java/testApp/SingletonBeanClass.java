package testApp;

import framework.annotations.di.Bean;

@Bean
public class SingletonBeanClass {
    public String value;

    public SingletonBeanClass(){
        value = "This is a Singleton Bean Class";
//        System.out.println(value);
    }
}
