package framework.engine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Engine {

    private static Engine instance = null;

    private Scanner scanner = null;
    private AnnotationManager annotationManager = null;

    private final Set<Object> controllerInstances = new HashSet<>();
    private final Set<Class<?>> controllerClasses = new HashSet<>();
    private final HashMap<Method, Object> methodToInstance = new HashMap<>();

    public static Engine getInstance(){
        if (instance == null){
            instance = new Engine();
            instance.annotationManager = AnnotationManager.getInstance(instance);
            instance.scanner = Scanner.getInstance();
        }
        return instance;
    }

    public void initialize() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<Class> classes = scanner.scan();
        annotationManager.proccessClasses(classes);
    }

    public void addControlerInstance(Object controllerInstance){
        controllerInstances.add(controllerInstance);
    }

    public void addControllerClass(Class cl){
        controllerClasses.add(cl);
    }

    public void mapMethodToInstance(Method m, Object instance){
        methodToInstance.put(m, instance);
    }

    public Object getInstanceByMethod(Method m){
        return methodToInstance.get(m);
    }
}
