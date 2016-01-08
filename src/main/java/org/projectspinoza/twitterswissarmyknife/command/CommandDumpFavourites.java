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

@Parameters(commandNames = "dumpFavourites", commandDescription = "Authenticated user's favourite tweets")
public class CommandDumpFavourites extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<Status> favourites = twitter.getFavorites();
        int remApiLimits = favourites.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, favourites);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<Status> statuses = (ResponseList<Status>) tsakResponse.getResponseData();
        for (Status status : statuses) {
            String statusJson = new Gson().toJson(status);
            writer.append(statusJson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpFavourites []";
    }
}