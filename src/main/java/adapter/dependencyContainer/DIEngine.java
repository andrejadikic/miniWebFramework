package adapter.dependencyContainer;


import adapter.classDiscovery.ClassLocatorForDirectory;
import domain.model.classDiscovery.ClassLocator;
import domain.model.dependencyInjection.ScopeType;
import domain.model.dependencyInjection.annotations.*;
import domain.model.routesRegistration.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DIEngine {

    private static DIEngine instance = null;
    private HashMap<String, Object> initializedClasses = new HashMap<>();
    public HashMap<String, Object> controllerClasses = new HashMap<>();
    private DIContainer dependencyContainer = DIContainer.getInstance();

    public static DIEngine getInstance() {
        if (instance == null) {
            instance = new DIEngine();
        }
        return instance;
    }

    public void initClasses() throws Exception {
        //Set<Class<?>> allClasses = new ClassLocatorForDirectory().locateClasses("src");

        List<Class> allClasses = ClassLocatorForDirectory.getAllClasses("");
        System.out.println(allClasses.size());
        for (Class<?> cl : allClasses) {
            if (cl.isAnnotationPresent(Controller.class)) {
                Router.getInstance().mapRoutes(cl);
                initializedClasses.put(cl.getName(), cl.getConstructor().newInstance());
                controllerClasses.put(cl.getName(), initializedClasses.get(cl.getName()));
            }

            if ((cl.isAnnotationPresent(Bean.class)
                    && cl.getAnnotation(Bean.class).scope().equals(ScopeType.SINGLETON))
                    || cl.isAnnotationPresent(Service.class)) {
                initializedClasses.put(cl.getName(), cl.getConstructor().newInstance());
            }

            if (cl.isAnnotationPresent(Qualifier.class) &&
                ( cl.isAnnotationPresent(Bean.class)
                    || cl.isAnnotationPresent(Service.class)
                    || cl.isAnnotationPresent(Component.class) )
            ) {
                String value = cl.getDeclaredAnnotation(Qualifier.class).value();
                if (dependencyContainer.getQualifiers().get(value) == null) {
                    dependencyContainer.getQualifiers().put(value, cl);
                }
                else throw new Exception("Class with the same qualifier already exist, error in class: " + cl.getName());
            }
        }

        for (Object value : controllerClasses.values()) {
            dependencyInjection(value);
        }
        System.out.println("-----------All classes are initialized-----------\n");
    }

    public void dependencyInjection(Object object) throws Exception {
        Class cl = object.getClass();
        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                if (!field.getType().isPrimitive() && !field.getType().isInterface()) {
                    setField(field, object, false);
                }
                else if (field.getType().isInterface() && field.isAnnotationPresent(Qualifier.class)) {
                    setField(field, object, true);
                }
            }
        }
    }

    public void setField(Field field, Object object, boolean isInterface) throws Exception {
        boolean isVerbose = field.getAnnotation(Autowired.class).verbose();
        Class classType;
        if (isInterface) {
            String value = field.getAnnotation(Qualifier.class).value();
            if (dependencyContainer.getQualifiers().get(value) == null) {
                throw new Exception("No bean for qualifier");
            }
            classType = dependencyContainer.getQualifiers().get(value);
        }
        else classType = field.getType();

        Object instance = getInstance(classType);
        if (instance != null) {
            field.setAccessible(true);
            field.set(object, instance);
            if (isVerbose) {
                System.out.println("Initialized " + classType.getName() + " " + field.getName() + " " +
                        "in " + object.getClass().getName() + " on " + LocalDateTime.now() +
                        " with " + field.hashCode());
            }
            dependencyInjection(instance);
        }
    }

    public Object getInstance(Class<?> classType) throws Exception {
        if (classType.isAnnotationPresent(Bean.class)) {
            ScopeType scope = (classType.getAnnotation(Bean.class)).scope();
            return (scope.equals(ScopeType.SINGLETON)) ?
                initializedClasses.get(classType.getName())
                    : classType.getConstructor().newInstance();
        }
        else if (classType.isAnnotationPresent(Service.class)) {
            return initializedClasses.get(classType.getName());
        }
        else if (classType.isAnnotationPresent(Component.class)) {
            return classType.getConstructor().newInstance();
        }
        else {
            throw new Exception("Autowired attribute is not a Bean, Service or Component");
        }
    }

}
