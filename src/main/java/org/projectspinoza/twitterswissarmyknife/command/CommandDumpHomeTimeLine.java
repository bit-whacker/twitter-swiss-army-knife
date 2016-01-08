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

@Parameters(commandNames = "dumpHomeTimeLine", commandDescription = "Authenticated user HomeTimeLine")
public class CommandDumpHomeTimeLine extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<Status> homeTimeLine = twitter.getHomeTimeline();
        int remApiLimits = homeTimeLine.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, homeTimeLine);
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
        return "CommandDumpHomeTimeLine []";
    }
}