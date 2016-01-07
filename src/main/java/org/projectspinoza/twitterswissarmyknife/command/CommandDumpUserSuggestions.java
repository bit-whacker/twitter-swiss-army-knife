package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import twitter4j.Twitter;
import twitter4j.TwitterException;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(TsakResponse tsakResponse, FileWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
}