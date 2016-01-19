package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import twitter4j.Twitter;
import twitter4j.TwitterException;

@Parameters(commandNames = "math", commandDescription = "Test Command")
public class CommandTest extends BaseCommand {
	@Parameter(names = "-squareOf", required = true, description = "square of")
    private int squareOf;

	public int getSquareOf() {
		return squareOf;
	}
	public void setSquareOf(int squareOf) {
		this.squareOf = squareOf;
	}

	@Override
	public TsakResponse execute(Twitter twitter) throws TwitterException {
		// TODO Auto-generated method stub
		TsakResponse tsakResponse = new TsakResponse(0, squareOf*squareOf);
		tsakResponse.setCommandDetails(this.toString());
		return tsakResponse;
	}
	@Override
	public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		return "CommandTest [squareOf=" + squareOf + "]";
	}
}
