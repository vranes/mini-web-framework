package testApp;

import framework.annotations.Controller;
import framework.annotations.http.Get;
import framework.annotations.http.Path;
import framework.response.JsonResponse;
import framework.response.Response;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Path("/")
    @Get
    public Response getRoot(HashMap<String){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", "/");
        responseMap.put("route_method", "get");
        Response response = new JsonResponse(responseMap);
        return response;
    }

    @Path("/test")
    @Get
    public Response getTest(HashMap<String){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", "/test");
        responseMap.put("route_method", "get");
        Response response = new JsonResponse(responseMap);
        return response;
    }
}
