package testApp;

import framework.annotations.di.Autowired;
import framework.annotations.di.Controller;
import framework.annotations.di.Qualifier;
import framework.annotations.http.Get;
import framework.annotations.http.Path;
import framework.annotations.http.Post;
import framework.response.JsonResponse;
import framework.response.Response;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController2 {

    @Autowired
    private SingletonBeanClass singletonBeanClass;
    @Autowired
    private PrototypeBeanClass prototypeBeanClass;
    @Autowired
    private ServiceClass serviceClass;
    @Autowired
    private ComponentClass componentClass;
    @Autowired
    @Qualifier("test")
    private TestInterface implementationClass;

    @Path("/test")
    @Get
    public Response getTest(){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("controller", "2");
        responseMap.put("route_location", "/test");
        responseMap.put("route_method", "get");
        Response response = new JsonResponse(responseMap);
        return response;
    }

    @Path("/test")
    @Post
    public Response postTest(HashMap<String, String> parameters){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("controller", "2");
        responseMap.put("route_location", "/test");
        responseMap.put("route_method", "get");
        responseMap.put("parameters", parameters);
        Response response = new JsonResponse(responseMap);
        return response;
    }
}
