package testApp;

import framework.annotations.di.Bean;
import framework.annotations.di.Scope;

@Bean(scope = Scope.PROTOTYPE)
public class PrototypeBeanClass {
    public String value;

    public PrototypeBeanClass(){
        value = "Prototype Bean Class Initialized";
        System.out.println(value);
    }
}
