package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Parameters(commandNames = "dumpAvailableTrends", commandDescription = "twitter available trends")
public class CommandDumpAvailableTrends extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<Location> locations = twitter.getAvailableTrends();
        int remApiLimits = locations.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, locations);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, FileWriter writer) throws IOException {
        ResponseList<Location> locations = (ResponseList<Location>) tsakResponse.getResponseData();
        for (Location location : locations) {
            String jsonLocation = new Gson().toJson(location);
            writer.append(jsonLocation);
        }
    }

    @Override
    public String toString() {
        return "CommandDumpAvailableTrends []";
    }
}