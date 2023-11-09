package test.bean;

import domain.model.dependencyInjection.annotations.Autowired;
import domain.model.dependencyInjection.annotations.Bean;

@Bean
public class Person {
    private String name = "aleksa";

    @Autowired(verbose = true)
    private Address address;

    public Person() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
