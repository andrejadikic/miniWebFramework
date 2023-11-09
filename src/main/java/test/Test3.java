package test;

import test.bean.*;
import domain.model.dependencyInjection.annotations.*;
import domain.model.routesRegistration.*;
import domain.model.webframework.request.Request;
import domain.model.webframework.response.JsonResponse;
import domain.model.webframework.response.Response;

@Controller
public class Test3 {

    @Autowired(verbose = true)
//    @Qualifier(value = "opel")
    private Car emptyObject;

    @Path(endpoint = "/getError")
    @GET
    public Response getError(Request request) {
        Response response = new JsonResponse(emptyObject);
        return response;
    }
}
