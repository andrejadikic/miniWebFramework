package domain.model;

public class Calculate {

	private int sum = 0;

	public void addInteger(Integer i){
		sum += i;
		System.out.println("Suma nakon sabiranja je: " + sum);
	}

	public void subtractInteger(Integer i){
		sum -= i;
		System.out.println("Suma nakon oduzimanja je: " + sum);
	}

}
