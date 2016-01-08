package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Place;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

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
        Place place = twitter.getGeoDetails(this.placeId);
        int remApiLimits = place.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, place);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        Place place = (Place) tsakResponse.getResponseData();
        String placeInfo_json = new Gson().toJson(place);
        writer.write(placeInfo_json);
        writer.newLine();
    }

    @Override
    public String toString() {
        return "CommandDumpGeoDetails [placeId=" + placeId + "]";
    }
}
