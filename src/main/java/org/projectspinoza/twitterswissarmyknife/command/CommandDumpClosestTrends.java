package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpClosestTrends", commandDescription = "closest trends places")
public class CommandDumpClosestTrends extends BaseCommand {
    @Parameter(names = "-lat", description = "latitude", required = true)
    private Double latitude;
    @Parameter(names = "-long", description = "longitude", required = true)
    private Double longitude;
	
    public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@Override
	public TsakResponse execute(Twitter twitter) throws TwitterException {
		ResponseList<Location> closestTrends = twitter.getClosestTrends(
		        new GeoLocation(this.latitude, this.longitude));
		int remApiLimits = closestTrends.getRateLimitStatus().getRemaining();
		TsakResponse tsakResponse = new TsakResponse(remApiLimits, closestTrends);
		tsakResponse.setCommandDetails(this.toString());
		return tsakResponse;
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public void write(TsakResponse tsakResponse, FileWriter writer) throws IOException {
	    ResponseList<Location> locations = (ResponseList<Location>) tsakResponse.getResponseData();
        for (Location location : locations) {
            String jsonLocation = new Gson().toJson(location);
            writer.append(jsonLocation);
        }
	}
    @Override
    public String toString() {
        return "CommandDumpClosestTrends [latitude=" + latitude
                + ", longitude=" + longitude + "]";
    }
}
