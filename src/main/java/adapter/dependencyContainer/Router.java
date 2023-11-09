package adapter.dependencyContainer;

import domain.model.routesRegistration.*;
import domain.model.webframework.request.Request;
import domain.model.webframework.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Router {
    public HashMap<String, Method> methodsWithGetAndPostAnnotations = new HashMap<>();
    private static Router instance = null;
    public static Router getInstance(){
        if (instance == null){
            instance = new Router();
        }
        return instance;
    }

    public void mapRoutes(Class cl) {
        Method[] methods = cl.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
            if (method.isAnnotationPresent(Path.class)) {
                System.out.println(method.getName());
                String path = method.getAnnotation(Path.class).endpoint();
                if (method.isAnnotationPresent(GET.class) || method.isAnnotationPresent(POST.class))
                    methodsWithGetAndPostAnnotations.put(path, method);
            }
        }
    }

    public Response getResponse(Request request) throws InvocationTargetException, IllegalAccessException {

        if (request.getLocation().endsWith("favicon.ico")) return null;

        if (request.getMethod().toString().equals("GET")) {
            Method methodGet = methodsWithGetAndPostAnnotations.get(request.getLocation());
            System.out.println(methodsWithGetAndPostAnnotations);
            return (Response) methodGet.invoke(DIEngine.getInstance().controllerClasses.get(methodGet.getDeclaringClass().getName()), request);
        }
        else if (request.getMethod().toString().equals("POST")) {
            String path = request.getLocation();
            int params = path.indexOf('?');
            if (params > -1)
                path = path.substring(0, params);
            Method methodPost = methodsWithGetAndPostAnnotations.get(path);
            return (Response) methodPost.invoke(DIEngine.getInstance().controllerClasses.get(methodPost.getDeclaringClass().getName()), request);
        }

        return null;
    }
}
