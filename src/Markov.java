import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Markov {
	private String fileName;
	private int prefixLength;
	private int maxChars;
	private Scanner input;
	private HashMap<String, ArrayList<String>> hashMap;
	private CurrentPrefix currentPrefix;
	private String suffix;
	
	public Markov(String fileName, int prefixLength, int maxChars) throws IOException{
		this.fileName = fileName;
		this.prefixLength = prefixLength;
		this.maxChars = maxChars;
		hashMap = new HashMap<>();
		currentPrefix = new CurrentPrefix(prefixLength);
		readFile(fileName);
		parseText();
	}
	
	private void readFile(String fileName) throws IOException{
		File file = new File(fileName);
		input = new Scanner(file);
	}
	
	private void parseText() {
		String thisWord;
		String nextWord;
		thisWord = input.next();
		while (input.hasNext()) {
			nextWord = input.next();
			currentPrefix.add(thisWord);
			addToHashMap(nextWord);
			thisWord = nextWord;
		}
	}
	
	private void addToHashMap(String nextWord) {
		String prefixString = currentPrefix.toString();
		ArrayList<String> currentValues;
		if (hashMap.containsKey(prefixString)) {
			currentValues = hashMap.get(prefixString);
			currentValues.add(nextWord);
		} else {
			currentValues = new ArrayList<>();
			currentValues.add(nextWord);
			hashMap.put(prefixString, currentValues);
		}	
	}
	
	public String generateText() {
		String totalString;
		currentPrefix = new CurrentPrefix(prefixLength, getStartingWords());
		totalString = currentPrefix.toString().trim();
		boolean running = true;
		while(running){
			suffix = getSuffix();
			currentPrefix.add(suffix);
			if (totalString.length() + suffix.length() + 1  < maxChars){
				totalString += (" " + suffix);
			} else {
				running = false;
			}
		}
		totalString = fixString(totalString);
		if(totalString.isEmpty()){
			totalString = generateText();
		}
		return totalString;
	}

	private String fixString(String s) {
		s = trimString(s);
		s = capitalizeString(s);
		s = s.replaceAll("\"", "");;
		return s;
		
	}
	private String trimString(String s) {
		int lastCharIndex = Math.max(s.lastIndexOf("."),s.lastIndexOf("!"));
		return s.substring(0,lastCharIndex + 1);
	}
	
	private String capitalizeString(String s) {
		String newString;
		if (s.length() > 2) {
			newString = s.substring(0,1).toUpperCase() + s.substring(1);
		} else {
			newString = "";
		}
		return newString;
	}
	
	private String getStartingWords() {
		int size = hashMap.size();
		int randIndex = new Random().nextInt(size);
		List<String> keySet = new ArrayList<String>(hashMap.keySet());
		String key = keySet.get(randIndex);
		return key;
	}
	
	private String getSuffix() {
		ArrayList<String> values = hashMap.get(currentPrefix.toString());
		int size = values.size();
		int randIndex = new Random().nextInt(size);
		return values.get(randIndex);
	}
	
	public int getNumEntries() {
		return hashMap.size();
	}
}
