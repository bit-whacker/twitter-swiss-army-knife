package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import twitter4j.Twitter;
import twitter4j.TwitterException;

@Parameters(commandNames = "dumpUserListSubscriptions", commandDescription = "user's lists subscriptions")
public class CommandDumpUserListSubscriptions extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name", required = true)
    private String screenName;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    private int limit = 1;
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	@Override
	public TsakResponse execute(Twitter twitter) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void write(TsakResponse tsakResponse, FileWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
