package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "searchPlace", commandDescription = "search places")
public class CommandSearchPlace extends BaseCommand {
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
        ResponseList<Place> places = twitter.searchPlaces(new GeoQuery(
                       new GeoLocation(this.latitude, this.longitude)));
        int remApiLimits = places.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, places);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<Place> places = (ResponseList<Place>) tsakResponse.getResponseData();
        for (Place place : places) {
            String placejson = new Gson().toJson(place);
            writer.append(placejson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandSearchPlace [latitude=" + latitude + ", longitude="
                + longitude + "]";
    }
}
