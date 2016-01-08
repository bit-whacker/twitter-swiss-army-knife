package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "searchUsers", commandDescription = "Search users")
public class CommandSearchUsers extends BaseCommand {
    @Parameter(names = "-keywords", description = "keywords to be search for users", required = true)
    private String keywords;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<ResponseList<User>> usersCollection = new ArrayList<ResponseList<User>>();
        int page = 1;
        int remApiLimits = 0;
        do {
            ResponseList<User> user = twitter.searchUsers(this.keywords, page++);
            usersCollection.add(user);
            remApiLimits = user.getRateLimitStatus().getRemaining();
        } while ((remApiLimits != 0) && (page < 50));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, usersCollection);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<ResponseList<User>> usersCollection = (List<ResponseList<User>>) tsakResponse.getResponseData();
        for (ResponseList<User> users : usersCollection) {
            for (User user : users) {
                String userjson = new Gson().toJson(user);
                writer.append(userjson);
                writer.newLine();
            }
        }
    }

}