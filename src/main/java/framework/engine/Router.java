package framework.engine;

import framework.request.Request;
import framework.request.enums.HttpMethod;
import framework.response.JsonResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private static Router instance = null;

    private HashMap<String, Method> routes = null;

    public static Router getInstance(){
        if (instance == null){
            instance = new Router();
            instance.routes = new HashMap<>();
        }

        return instance;
    }

    public JsonResponse processRequest(Request request) throws InvocationTargetException, IllegalAccessException {
//        Map<String, Object> responseMap = new HashMap<>();
//        return new JsonResponse(responseMap);
        Method m = routes.get(request.getLocation() + request.getMethod());
        Object instance = Engine.getInstance().getInstanceByMethod(m);
        return (JsonResponse) m.invoke(instance);
    }

    public void registerRoute(Method m, String path, HttpMethod httpMethod){
        routes.put(path + httpMethod, m);
    }
}
