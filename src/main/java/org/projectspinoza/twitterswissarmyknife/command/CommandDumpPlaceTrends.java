package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import twitter4j.Twitter;
import twitter4j.TwitterException;

@Parameters(commandNames = "dumpPlaceTrends", commandDescription = "twitter place trends")
public class CommandDumpPlaceTrends extends BaseCommand {
    @Parameter(names = "-woeid", description = "where on earth ID")
    private int woeId;

	public int getWoeId() {
		return woeId;
	}
	public void setWoeId(int woeId) {
		this.woeId = woeId;
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
