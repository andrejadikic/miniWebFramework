package test.component;

import domain.model.dependencyInjection.annotations.Component;

@Component
public class ComponentTest {

    private String message = "Hello from ComponentTest class";

    public ComponentTest() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
