package testApp;

import framework.annotations.di.Bean;
import framework.annotations.di.Qualifier;

@Qualifier("test")
@Bean
public class ImplementationClass implements TestInterface{
    public ImplementationClass(){
//        System.out.println("Implementation Class here!");
    }
}
