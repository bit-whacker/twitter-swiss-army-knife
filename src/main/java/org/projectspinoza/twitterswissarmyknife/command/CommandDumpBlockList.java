package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpBlockList", commandDescription = "Authenticated user's blocked list")
public class CommandDumpBlockList extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        PagableResponseList<User> blockList = twitter.getBlocksList();
        int remApiLimits = blockList.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, blockList);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        PagableResponseList<User> uList = (PagableResponseList<User>) tsakResponse.getResponseData();
        for (User user : uList) {
            String userJson = new Gson().toJson(user);
            writer.append(userJson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpBlockList []";
    }
}
