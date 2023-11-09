package application;

import domain.model.Calculate;

public class Main {
    public static void main(String[] args) {
        Calculate c = new Calculate();
        c.addInteger(3);
        c.addInteger(5);
        c.subtractInteger(1);
    }
}
