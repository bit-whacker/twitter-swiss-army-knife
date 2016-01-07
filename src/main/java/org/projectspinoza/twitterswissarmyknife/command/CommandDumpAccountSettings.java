package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.AccountSettings;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpAccountSettings", commandDescription = "Account Setting")
public class CommandDumpAccountSettings extends BaseCommand {

	@Override
	public TsakResponse execute(Twitter twitter) throws TwitterException {
	    AccountSettings settings = twitter.getAccountSettings();
	    int remApiLimits = settings.getRateLimitStatus().getRemaining();
	    TsakResponse tsakResponse = new TsakResponse(remApiLimits, settings);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
	}

	@Override
	public void write(TsakResponse tsakResponse, FileWriter writer) throws IOException {
	    String jsonSettings = new Gson().toJson(tsakResponse);
        writer.append(jsonSettings);
	}

    @Override
    public String toString() {
        return "CommandDumpAccountSettings []";
    }
}
