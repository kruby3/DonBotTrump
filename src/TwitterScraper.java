import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterScraper{
	private static Twitter twitter;
	private static File trumpTweetFile;
	private static PrintWriter output;
	
    public TwitterScraper() {
    	twitter = TwitterFactory.getSingleton();
    	try {
	    	trumpTweetFile = new File(DonaldAIApp.FILE_NAME);
	    	output = new PrintWriter(trumpTweetFile);
    	} catch(IOException e){
    		System.err.println("File not found");
    	} 
    }
    
    public void getTweets(int numTweets) throws TwitterException{
    	String user = DonaldAIApp.USER_NAME;
    	List<Status> statuses = twitter.getUserTimeline(user,new Paging(1, 200));
    	for(int i = 2; i < numTweets / 200; i++) {
    		statuses.addAll(twitter.getUserTimeline(user,new Paging(i, 200)));
    	}
    	for(Status status: statuses){
    		output.println(cleanUp(status));
    	}
    	output.close();
    }
   
    //Removes links and twitter handles from tweet
    private String cleanUp(Status tweet) {
    	String cleanString = tweet.getText();
    	cleanString = cleanString.replaceAll("https?://\\S+\\s?", "");
    	cleanString = cleanString.replaceAll("@\\s*(\\w+)", "");
    	return cleanString;
    }
    
}