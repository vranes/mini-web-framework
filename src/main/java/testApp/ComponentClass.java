package testApp;

import framework.annotations.di.Component;

@Component
public class ComponentClass {
    public String value;

    public ComponentClass(){
        value = "Component Class Initialized";
        System.out.println(value);
    }
}
