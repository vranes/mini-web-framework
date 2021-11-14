package framework.engine;

import framework.request.Request;
import framework.request.enums.HttpMethod;
import framework.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

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
        if (request.getLocation().endsWith("favicon.ico"))
            return null;

        String path = request.getLocation();
        int params = path.indexOf('?');
        if (params > -1)
            path = path.substring(0, params);

        Method m = routes.get(path + request.getMethod());
        if (m == null)
            return null;

        Object instance = DIEngine.getInstance().getInstanceByMethod(m);
            // TODO parameters?

        if (m.getParameterCount() > 0)
            return (Response) m.invoke(instance, request.getParameters());

        return (Response) m.invoke(instance);
    }

    public void registerRoute(Method m, String path, HttpMethod httpMethod){
        routes.put(path + httpMethod, m);
    }
}
