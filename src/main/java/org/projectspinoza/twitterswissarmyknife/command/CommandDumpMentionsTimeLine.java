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

@Parameters(commandNames = "dumpMentionsTimeLine", commandDescription = "Authenticated user MentionsTimeLine")
public class CommandDumpMentionsTimeLine extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<Status> mentions = twitter.getMentionsTimeline();
        int remApiLimits = mentions.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, mentions);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<Status> mentions = (ResponseList<Status>) tsakResponse.getResponseData();
        for (Status mention : mentions) {
            String mentionJson = new Gson().toJson(mention);
            writer.append(mentionJson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpMentionsTimeLine []";
    }
}