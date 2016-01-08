package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpUserSuggestions", commandDescription = "user's suggestions")
public class CommandDumpUserSuggestions extends BaseCommand {
    @Parameter(names = "-slug", description = "category slug", required = true)
    private String slug;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<User> suggestions = twitter.getUserSuggestions(this.slug);
        int remApiLimits = suggestions.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, suggestions);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<User> users = (ResponseList<User>) tsakResponse.getResponseData();
        for (User user : users) {
            String userJson = new Gson().toJson(user);
            writer.append(userJson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpUserSuggestions [slug=" + slug + "]";
    }
}