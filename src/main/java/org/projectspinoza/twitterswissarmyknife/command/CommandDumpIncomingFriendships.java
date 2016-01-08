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

@Parameters(commandNames = "dumpIncomingFriendships", commandDescription = "Authenticated user's Incoming Friendships")
public class CommandDumpIncomingFriendships extends BaseCommand {
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    private int limit = 1;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<IDs> incomingFriendships = new ArrayList<IDs>();
        int userLimit = this.limit;
        int remApiLimits = 0;
        long cursor = -1;
        do {
            IDs ids = twitter.getIncomingFriendships(cursor);
            incomingFriendships.add(ids);
            remApiLimits = ids.getRateLimitStatus().getRemaining();
            cursor = ids.getNextCursor();
        } while ((cursor != 0) && (remApiLimits < 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, incomingFriendships);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<IDs> idList = (List<IDs>) tsakResponse.getResponseData();
        for (IDs ids : idList) {
            JSONObject idsJson = new JSONObject();
            idsJson.accumulate("ids", ids.getIDs());
            writer.append(idsJson.toString());
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpIncomingFriendships [limit=" + limit + "]";
    }
}