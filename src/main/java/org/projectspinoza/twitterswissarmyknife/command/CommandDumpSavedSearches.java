package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpSavedSearches", commandDescription = "Authenticated user's saved searches")
public class CommandDumpSavedSearches extends BaseCommand {

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<SavedSearch> savedSearch = twitter.getSavedSearches();
        int remApiLimits = savedSearch.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, savedSearch);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer)throws IOException {
        ResponseList<SavedSearch> savedSearches = (ResponseList<SavedSearch>) tsakResponse.getResponseData();
        for (SavedSearch savedSearch : savedSearches) {
            String savedsearchesJson = new Gson().toJson(savedSearch);
            writer.append(savedsearchesJson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpSavedSearches []";
    }
}