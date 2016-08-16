import java.util.Scanner;
public class CurrentPrefix {
	private int prefLength;
	private String[] strArray;
	
	
	public CurrentPrefix(int prefLength){
		strArray = new String[prefLength];
		this.prefLength = prefLength;
	}
	
	public CurrentPrefix(int prefLength, String words) {
		this(prefLength);
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(words);
		while (scan.hasNext()) {
			add(scan.next());
		}
	}
	
	public void add(String word) {
		String[] tempArray = new String[prefLength];
		for (int i = 0; i < prefLength - 1; i++) {
			tempArray[i] = strArray[i+1];
		}
		tempArray[prefLength - 1] = word;
		strArray = tempArray;
		//System.out.println(Arrays.toString(strArray));
	}
	
	@Override 
	public String toString() {
		String s = "";
		for (int i = 0; i < prefLength; i++) {
			if (strArray[i] != null) {
				s += strArray[i] + " ";
			} 
		}
		return s;	
	}
}
