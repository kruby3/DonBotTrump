import java.io.IOException;
import java.util.Scanner;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DonaldAIApp {
	private static Twitter twitter;
	private static Scanner scan;
	private static Markov markov;
	private static final int MAX_CHAR = 140;
	private static final int PREFIX_LENGTH = 2;
	public static final String FILE_NAME = "TrumpTweets.txt";
	public static final String USER_NAME = "@realDonaldTrump";
	
	public static void main(String[] args) {
		twitter = TwitterFactory.getSingleton();
		scan = new Scanner(System.in);
		
		try {
			markov = new Markov(FILE_NAME,PREFIX_LENGTH,MAX_CHAR);
		} catch(IOException e){
			System.err.println("Error reading file: " + FILE_NAME);
		}
		
		boolean running = true;
		while(running) {
			printSelectScreen();
			int resp = scan.nextInt();
			switch (resp) {
			case 1:
				downloadTweets();
				break;
			case 2:
				postTweet();
				break;
			case 3:
				postTweets();
				break;
			case 4:
				localTweet();
				break;
			case 5:
				System.out.println("Exiting");
				running = false;
				break;
			default:
				System.out.println("Invalid choice");
				break;
			}
			System.out.println("");
		}
	}
	
	private static void printSelectScreen() {
		System.out.println("Welcome to the DonBot Trump Twitter Bot");
		System.out.println("1. Re-download Tweets");
		System.out.println("2. Post Single Tweet");
		System.out.println("3. Post Tweet every 'x' minutes");
		System.out.println("4. Create 'x' tweets locally");
		System.out.println("5. Exit");
		System.out.println("Enter number: ");
	}
	
	private static void downloadTweets() {
		System.out.println("Enter number of tweets to download");
		int numTweets = scan.nextInt();
		System.out.println("Downloading tweets");
		TwitterScraper twitterScraper = new TwitterScraper();
		try {
			twitterScraper.getTweets(numTweets);
			System.out.println("Complete!");
		} catch (TwitterException e) {
			e.printStackTrace();
			System.err.println("Error downloading tweets");
		}	
	}
	
	private static void postTweets(boolean oneTime, int minDelay) {
		
		System.out.println("Press any key to stop");
		boolean running = true;
		
		while(running){
			StatusUpdate statusUpdate = new StatusUpdate(markov.generateText());
			try {
				Status status = twitter.updateStatus(statusUpdate);
				System.out.println("Posting Tweet");
				if (oneTime || !scan.nextLine().isEmpty()) {
					break;
				}
				Thread.sleep(minDelay*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (TwitterException e) {
				System.err.println("Error posting tweet");
			}
		}
	}
	
	private static void postTweet() {
		postTweets(true, 1);
	}
	
	private static void postTweets() {
		System.out.println("Enter delay between tweets in minutes");
		int minDelay = scan.nextInt();
		postTweets(false, minDelay);
	}
	
	private static void localTweet() {
		System.out.println("Enter number of tweets");
		int numTweets = scan.nextInt();
		for(int i = 0; i < numTweets; i++) {
			System.out.println(markov.generateText());
		}
	}
}
