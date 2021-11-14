package testApp;

import framework.annotations.di.Component;

@Component
public class ComponentClass {
    public String value;

    public ComponentClass(){
        value = "Component Class here!";
//        System.out.println(value);
    }
}
