package framework.engine;

import framework.annotations.http.Get;
import framework.annotations.http.Path;
import framework.annotations.http.Post;
import framework.annotations.Controller;
import framework.engine.Router;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static framework.request.enums.HttpMethod.GET;
import static framework.request.enums.HttpMethod.POST;

public class AnnotationManager {

    private Engine engine = null;

    private static AnnotationManager instance = null;
    public static AnnotationManager getInstance(Engine engine){
        if (instance == null){
            instance = new AnnotationManager();
            instance.engine = engine;
        }
        return instance;
    }

    public void proccessClasses(ArrayList<Class> classes) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        for (Class cl: classes)
            processClass(cl);
    }

    private void processClass(Class cl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!cl.isInterface() && cl.isEnum()) {
            if(cl.isAnnotationPresent(Controller.class)){
                engine.addControllerClass(cl);                                              // todo possibly return instead
                Object instance = cl.getDeclaredConstructor().newInstance();
                engine.addControlerInstance(instance);
                processMethods(cl, instance);
            }
        }
    }

    private void processMethods(Class cl, Object instance){                 // TODO controllermethods rename
        for (Method m: cl.getDeclaredMethods()){
            Path path = m.getAnnotation(Path.class);
            if (path != null){
                engine.mapMethodToInstance(m, instance);
                if (m.isAnnotationPresent(Get.class)){
                    Router.getInstance().registerRoute(m, path.value(), GET);
                }
                else if (m.isAnnotationPresent(Post.class)){
                    Router.getInstance().registerRoute(m, path.value(), POST);
                }

                // TODO
            }
        }
    }

}
