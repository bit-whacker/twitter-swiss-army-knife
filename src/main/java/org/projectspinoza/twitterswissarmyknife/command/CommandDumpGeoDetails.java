package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import twitter4j.Twitter;
import twitter4j.TwitterException;

@Parameters(commandNames = "dumpGeoDetails", commandDescription = "geo detail of a place")
public class CommandDumpGeoDetails extends BaseCommand {
    @Parameter(names = "-pid", description = "place id", required = true)
    private String placeId;

	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
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
