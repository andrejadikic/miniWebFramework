package test.bean;

import domain.model.dependencyInjection.annotations.Qualifier;
import domain.model.dependencyInjection.annotations.Bean;

@Bean
@Qualifier(value = "opel")
public class Opel implements Car {

    private String model = "astra";
    @Override
    public String getModel() {
        return model;
    }
    @Override
    public void setModel(String model) {
        this.model = model;
    }

    public Opel() {}
}
