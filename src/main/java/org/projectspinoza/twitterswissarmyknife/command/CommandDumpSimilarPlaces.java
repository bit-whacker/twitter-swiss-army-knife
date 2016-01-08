package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpSimilarPlaces", commandDescription = "similar places")
public class CommandDumpSimilarPlaces extends BaseCommand {
    @Parameter(names = "-lat", description = "latitude", required = true)
    private Double latitude;
    @Parameter(names = "-long", description = "longitude", required = true)
    private Double longitude;
    @Parameter(names = "-pname", description = "place name", required = true)
    private String placeName;

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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<Place> similarPlaces = twitter.getSimilarPlaces(
                new GeoLocation(this.latitude, this.longitude), this.placeName, null, null);
        int remApiLimits = similarPlaces.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, similarPlaces);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<Place> places = (ResponseList<Place>) tsakResponse.getResponseData();
        for (Place place : places) {
            String placesjson = new Gson().toJson(place);
            writer.append(placesjson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpSimilarPlaces [latitude=" + latitude
                + ", longitude=" + longitude + ", placeName=" + placeName + "]";
    }
}
