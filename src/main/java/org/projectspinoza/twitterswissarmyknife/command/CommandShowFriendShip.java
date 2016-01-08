package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "showFriendShip", commandDescription = "finding friendship between two users")
public class CommandShowFriendShip extends BaseCommand {
    @Parameter(names = "-suser", description = "source username", required = true)
    private String source;
    @Parameter(names = "-tuser", description = "target username", required = true)
    private String target;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        Relationship relationship;
        int remApiLimits = 0;
        if (isLong(this.source) && isLong(this.target)) {
            relationship = twitter.showFriendship(Long.parseLong(this.source),Long.parseLong(this.target));
            remApiLimits = relationship.getRateLimitStatus().getRemaining();
        } else {
            relationship = twitter.showFriendship(this.source, this.target);
            remApiLimits = relationship.getRateLimitStatus().getRemaining();
        }
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, relationship);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        Relationship relationship = (Relationship) tsakResponse.getResponseData();
        String relationshipjson = new Gson().toJson(relationship);
        writer.append(relationshipjson);
        writer.newLine();
    }

    private boolean isLong(String input) {
        try {
            Long.parseLong(input);
        } catch (ClassCastException | NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
