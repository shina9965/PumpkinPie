package app;

public class Main {

	public static void main(String[] args) {


		BoolEx.ifTrueElse(true, () -> System.out.println("条件はtrueです"), () -> System.out.println("条件はfalseです"));
	}

}
