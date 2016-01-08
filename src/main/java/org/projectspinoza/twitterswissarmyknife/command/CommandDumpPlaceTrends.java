package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpPlaceTrends", commandDescription = "twitter place trends")
public class CommandDumpPlaceTrends extends BaseCommand {
    @Parameter(names = "-woeid", description = "where on earth ID")
    private int woeId;

    public int getWoeId() {
        return woeId;
    }

    public void setWoeId(int woeId) {
        this.woeId = woeId;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        Trends trends = twitter.getPlaceTrends(this.woeId);
        int remApiLimits = trends.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, trends);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        Trends trends = (Trends) tsakResponse.getResponseData();
        for (Trend trend : trends.getTrends()) {
            String jsonTrends = new Gson().toJson(trend);
            writer.append(jsonTrends);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpPlaceTrends [woeId=" + woeId + "]";
    }
}
