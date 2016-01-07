package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameters;

import twitter4j.Twitter;
import twitter4j.TwitterException;

@Parameters(commandNames = "dumpAccountSettings", commandDescription = "Account Setting")
public class CommandDumpAccountSettings extends BaseCommand {

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
