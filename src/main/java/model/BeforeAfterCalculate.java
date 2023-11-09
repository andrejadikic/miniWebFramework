package model;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class BeforeAfterCalculate {
    //call - poziv metode
    //imeKlase.imeMetode(tip)

    @Pointcut("call(public void domain.model.Calculate.addInteger(Integer))")
//    @Pointcut("call(public void Calculate.*(..))")
    void sabiranje(){
    }

    @Around("sabiranje()")
    public Object preSabiranja(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("-> Aspekt pre sabiranja");
        Object returnValue = jp.proceed();
        System.out.println("sabrao");

        return returnValue;
    }

    @Pointcut("call(public void domain.model.Calculate.subtractInteger(Integer))")
    void oduzimanje(){

    }

    @After("oduzimanje()")
    public void posleOduzimanja() {
        System.out.println("<- Aspekt pozvan posle oduzimanja");
    }

}
