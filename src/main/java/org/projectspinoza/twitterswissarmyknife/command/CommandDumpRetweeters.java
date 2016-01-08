package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "dumpRetweeters", commandDescription = "getting retweeters of status")
public class CommandDumpRetweeters extends BaseCommand {
    @Parameter(names = "-sid", description = "Status id", required = true)
    private long statusId;

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<IDs> retweetersIDs = new ArrayList<IDs>();
        long cursor = -1;
        int remApiLimits = 1;
        do {
            IDs ids = twitter.getRetweeterIds(this.statusId, -1);
            remApiLimits = ids.getRateLimitStatus().getRemaining();
            cursor = ids.getNextCursor();
            retweetersIDs.add(ids);
        } while ((cursor != 0) && (remApiLimits < 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, retweetersIDs);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<IDs> retweetersIDs = (List<IDs>) tsakResponse.getResponseData();
        for (IDs ids : retweetersIDs) {
            JSONObject idsJson = new JSONObject();
            idsJson.accumulate("ids", ids.getIDs());
            writer.append(idsJson.toString());
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpRetweeters [statusId=" + statusId + "]";
    }
}