package framework.engine;

import framework.annotations.di.*;
import framework.annotations.http.Get;
import framework.annotations.http.Path;
import framework.annotations.http.Post;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static framework.request.enums.HttpMethod.GET;
import static framework.request.enums.HttpMethod.POST;

public class DIEngine {

    private static DIEngine instance = null;

    private Scanner scanner = null;

    private final Set<Object> controllerInstances = new HashSet<>();
    private final Set<Class<?>> controllerClasses = new HashSet<>();
    private final HashMap<Method, Object> methodToInstance = new HashMap<>();
    private final HashMap<Class, Object> singletonClassToInstance = new HashMap<>();

    public static DIEngine getInstance(){
        if (instance == null){
            instance = new DIEngine();
            instance.scanner = Scanner.getInstance();
        }
        return instance;
    }

    public void initialize() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<Class> classes = scanner.scan();
        proccessClasses(classes);
    }

    public void proccessClasses(ArrayList<Class> classes) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        for (Class cl: classes){
            if (!cl.isInterface() && !cl.isEnum()) {
                if(cl.isAnnotationPresent(Controller.class)){
                    controllerClasses.add(cl);
                }
                if (cl.isAnnotationPresent(Qualifier.class)){
                    Qualifier qualifier = (Qualifier)cl.getAnnotation(Qualifier.class);
                    for (Class interfaceClass: cl.getInterfaces()){
                        DependencyContainer.getInstance().mapImpl(interfaceClass, qualifier.value(), cl);
                    }
                }
                Bean bean  = (Bean)cl.getAnnotation(Bean.class);
                if (cl.isAnnotationPresent(Service.class) || (bean != null && bean.scope().equals(Scope.SINGLETON))){
                    Object instance = cl.getDeclaredConstructor().newInstance();
                    singletonClassToInstance.put(cl, instance);
                }
            }
        }
        processControlers();
    }

    private void processControlers() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class cl: controllerClasses){
            Object instance = cl.getDeclaredConstructor().newInstance();
            controllerInstances.add(instance);
            processMethods(cl, instance);
            processFields(cl, instance);
        }
    }

    private void processMethods(Class cl, Object instance){
        for (Method m: cl.getDeclaredMethods()){

            Path path = m.getAnnotation(Path.class);
            if (path != null){

                if (m.isAnnotationPresent(Get.class)){
                    Router.getInstance().registerRoute(m, path.value(), GET);
                }
                else if (m.isAnnotationPresent(Post.class)){
                    Router.getInstance().registerRoute(m, path.value(), POST);
                }
                else{
                    throw new RuntimeException("Method annotated with Path must also be annotated with either POST or GET annotation.");
                }

                methodToInstance.put(m, instance);
            }
        }
    }

    private void processFields(Class cl, Object instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Field f: cl.getFields()){
            if (f.isAnnotationPresent(Autowired.class)){

                Object fieldInstance = null;
                Class fieldClass = f.getClass();

                if (f.getType().isInterface()){
                    Qualifier qualifier = f.getAnnotation(Qualifier.class);
                    if (qualifier == null) {
                        throw new RuntimeException("Interface annotated with Autowired must also have the Qualifier annotation.");
                    }
                    fieldClass = DependencyContainer.getInstance().getImpl(f.getClass(), qualifier.value());
                }

                Bean bean  = (Bean)fieldClass.getAnnotation(Bean.class);
                Service service = (Service) fieldClass.getAnnotation(Service.class);
                Component component = (Component)fieldClass.getAnnotation(Component.class);

                if(component != null || (bean != null && bean.scope().equals(Scope.PROTOTYPE))){
                    fieldInstance = fieldClass.getDeclaredConstructor().newInstance();
                }
                else if (service != null || (bean != null && bean.scope().equals(Scope.SINGLETON))){
                    fieldInstance = singletonClassToInstance.get(fieldClass);
                }
                else{
                    throw new RuntimeException("Field annotated with Autowired must be of class with a Bean/Service/Component annotation.");
                }

                processFields(fieldClass, fieldInstance);
                f.setAccessible(true);
                f.set(instance, fieldInstance);

                if (f.getAnnotation(Autowired.class).verbose()){
                    System.out.println("Initialized " + f.getType() + " " + f.getName() + " in " + cl.getName()
                            + " on " + java.util.Calendar.getInstance().getTime() + " with " + fieldInstance);
                }
            }
        }
    }

    public Object getInstanceByMethod(Method m){
        return methodToInstance.get(m);
    }
}
