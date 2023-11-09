package test;

import domain.model.dependencyInjection.annotations.*;
import domain.model.routesRegistration.*;
import domain.model.webframework.request.Request;
import domain.model.webframework.response.JsonResponse;
import domain.model.webframework.response.Response;
import test.bean.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Test1 {

    @Autowired(verbose = true)
    @Qualifier("audi")
    private Car audi;

    @Autowired(verbose = true)
    @Qualifier("opel")
    private Car opel;

    @GET
    @Path(endpoint = "")
    public Response getCars(Request request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("ClassName: ", "test.Test1");
        responseMap.put("route_method", request.getMethod());
        responseMap.put("route_location", request.getLocation());
        responseMap.put("return_value", audi);
        Response response = new JsonResponse(responseMap);
        return response;
    }

    @Path(endpoint = "/getCar")
    @GET
    public Response getCar(Request request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("ClassName: ", "test.Test1");
        responseMap.put("route_method", request.getMethod());
        responseMap.put("route_location", request.getLocation());
        responseMap.put("return_value", audi);
        Response response = new JsonResponse(responseMap);
        return response;
    }

    @Path(endpoint = "/postCar")
    @POST
    public Response postCar(Request request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("ClassName: ", "test.Test1");
        responseMap.put("route_method", request.getMethod());
        responseMap.put("route_location", request.getLocation());
        responseMap.put("parameters", request.getParameters());
        Response response = new JsonResponse(responseMap);
        return response;
    }
}
