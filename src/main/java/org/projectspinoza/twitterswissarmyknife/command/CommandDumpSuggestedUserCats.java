package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Category;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpSugeestedUserCats", commandDescription = "sugeested user categories")
public class CommandDumpSuggestedUserCats extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<Category> userCategories = twitter.getSuggestedUserCategories();
        int remApiLimits = userCategories.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, userCategories);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<Category> categories = (ResponseList<Category>) tsakResponse.getResponseData();
        for (Category category : categories) {
            String categoryjson = new Gson().toJson(category);
            writer.append(categoryjson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpSuggestedUserCats []";
    }
}