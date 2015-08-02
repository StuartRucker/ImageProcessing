package Util;

public class Util {
	public static boolean inRange(int x, int y, int[][] array){
		if(x < array.length && y < array[0].length && x >=0 && y >= 0){
			return true;
		}
		return false;
	}
}	
