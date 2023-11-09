package test.bean;

import domain.model.dependencyInjection.ScopeType;
import domain.model.dependencyInjection.annotations.Bean;

@Bean(scope = ScopeType.PROTOTYPE)
public class Address {
    private String street = "Decanska 23";

    public Address() {}
}
