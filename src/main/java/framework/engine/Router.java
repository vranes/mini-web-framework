package framework.engine;

import framework.request.Request;
import framework.request.enums.HttpMethod;
import framework.response.JsonResponse;
import framework.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private static Router instance = null;
    private static int cnt = 0;

    private HashMap<String, Method> routes = null;

    public static Router getInstance(){
        if (instance == null){
            instance = new Router();
            instance.routes = new HashMap<>();
        }
        return instance;
    }

    public Response processRequest(Request request) throws InvocationTargetException, IllegalAccessException {
//        Map<String, Object> responseMap = new HashMap<>();
//        return new JsonResponse(responseMap);
        if (request.getLocation().endsWith("favicon.ico"))
            return null;

        System.out.println(request.getLocation() + request.getMethod());
        Method m = routes.get(request.getLocation() + request.getMethod());

        if (m == null)
            return null;

        Object instance = Engine.getInstance().getInstanceByMethod(m);
            // TODO parameters?

        if (m.getParameterCount() > 0)
            return (Response) m.invoke(instance, request.getParameters());

        return (Response) m.invoke(instance);
    }

    public void registerRoute(Method m, String path, HttpMethod httpMethod){
        routes.put(path + httpMethod, m);
    }
}
