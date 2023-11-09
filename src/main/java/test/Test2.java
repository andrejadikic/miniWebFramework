package test;

import test.bean.*;
import domain.model.dependencyInjection.annotations.*;
import domain.model.routesRegistration.*;
import domain.model.webframework.request.Request;
import domain.model.webframework.response.JsonResponse;
import domain.model.webframework.response.Response;
import test.component.ComponentTest;
import test.service.ServiceTest;

@Controller
public class Test2 {
    @Autowired(verbose = true)
    private Person person;

    @Autowired(verbose = true)
    private ServiceTest serviceTest;

    @Autowired(verbose = false)
    private ComponentTest componentTest;

    @Path(endpoint = "/getPerson")
    @GET
    public Response getPerson(Request req) {
        Response response = new JsonResponse(person);
        return response;
    }

    @Path(endpoint = "/getComponent")
    @GET
    public Response getComponent(Request req) {
        Response response = new JsonResponse(componentTest);
        return response;
    }

    @Path(endpoint = "/postService")
    @POST
    public Response postService(Request req) {
        serviceTest.setRepository(req.getParameter("repository"));
        Response response = new JsonResponse(serviceTest);
        return response;
    }

    @Path(endpoint = "/postPerson")
    @POST
    public Response postPerson(Request req) {
        person.setName("nikola");
        Response response = new JsonResponse(person);
        return response;
    }
}
