package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpTweets", commandDescription = "Search tweets")
public class CommandDumpTweets extends BaseCommand {
    @Parameter(names = "-keywords", description = "keywords to be search for tweets", required = true)
    private String keywords;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    private int limit = 1;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<List<Status>> tweetsCollection = new ArrayList<List<Status>>();
        int userLimit = this.limit;
        int remApiLimits = 1;
        Query query = new Query(this.keywords);
        QueryResult queryResult;
        do {
            queryResult = twitter.search(query);
            tweetsCollection.add(queryResult.getTweets());
            remApiLimits = queryResult.getRateLimitStatus().getRemaining();
            query = queryResult.nextQuery();
        } while (query != null && remApiLimits != 0 && --userLimit > 0);
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, tweetsCollection);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<List<Status>> tweetsCollection = (List<List<Status>>) tsakResponse.getResponseData();
        for (List<Status> statuses : tweetsCollection) {
            for (Status status : statuses) {
                String statusJson = new Gson().toJson(status);
                writer.append(statusJson);
                writer.newLine();
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpTweets [keywords=" + keywords + ", limit=" + limit
                + "]";
    }
}