package Memory;

import java.util.Random;

public class Verktyg {
	public static void slumpOrdning(Object[] items){
		Random slump = new Random();
		for (int j = 0; j<150;j++){
			for (int i = 0; i < items.length;i++){
				moveObject(items, i, slump.nextInt(items.length-1));
			}
		}
	}
	private static void moveObject(Object[] arr, int from, int to){
		Object temp = arr[from];
		arr[from]=arr[to];
		arr[to]=temp;
	}
}
