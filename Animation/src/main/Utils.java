package main;

public class Utils {
	
	private static boolean quietMode = false;

	public static void Log(String message) {
		
    	if (!quietMode) {
    		System.out.println(message);
    	}
    }
	
public static void Log(String message, char c) {
	
}

}
