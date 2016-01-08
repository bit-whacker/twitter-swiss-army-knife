package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpOwnRetweets", commandDescription = "Authenticated user retweets")
public class CommandDumpOwnRetweets extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<Status> retweets = twitter.getRetweetsOfMe();
        int remApiLimits = retweets.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, retweets);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<Status> retweets = (ResponseList<Status>) tsakResponse.getResponseData();
        for (Status tweet : retweets) {
            String tweetJson = new Gson().toJson(tweet);
            writer.append(tweetJson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpOwnRetweets []";
    }
}
