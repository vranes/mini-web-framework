package framework.engine;

import java.lang.reflect.Field;
import java.util.HashMap;

public class DependencyContainer {

    HashMap<Class, HashMap<String, Class>> interfaceToImpls = new HashMap<>();

    private static DependencyContainer instance = null;
    public static DependencyContainer getInstance(){
        if (instance == null)
            instance = new DependencyContainer();
        return instance;
    }

    public void mapImpl(Class interface_, String qualifier, Class implementation){
        HashMap<String, Class> qualifierToImpl = interfaceToImpls.getOrDefault(interface_, new HashMap<>());

        if (qualifierToImpl.containsKey(qualifier)){
            throw new RuntimeException("Qualifier value " + qualifier + " already exists for interface " + interface_.getName());
        }

        qualifierToImpl.put(qualifier, implementation);
      //  interfaceToImpls.put(interface_, qualifierToImpl);  // TODO  redundant?
    }

    public Class getImpl(Class interface_, String qualifier){
        HashMap<String, Class> qualifierToImpl = interfaceToImpls.get(interface_);

        if (qualifierToImpl == null){
            throw new RuntimeException("No class implementation of " + interface_.getName() + " interface found for injection.");
        }

        Class cl = qualifierToImpl.get(qualifier);

        if(cl == null){
            throw new RuntimeException("No class with qualifier " + qualifier + " found for injection.");
        }

        return cl;
    }

}
