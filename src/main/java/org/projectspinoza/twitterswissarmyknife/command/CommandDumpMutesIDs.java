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

@Parameters(commandNames = "dumpMutesIDs", commandDescription = "Authenticated user's muted IDs")
public class CommandDumpMutesIDs extends BaseCommand {
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
        List<IDs> mutesIDsCollection = new ArrayList<IDs>();
        long cursor = -1;
        int remApiLimits = 1;
        int userLimit = this.limit;
        do {
            IDs ids = twitter.getMutesIDs(cursor);
            mutesIDsCollection.add(ids);
            remApiLimits = ids.getRateLimitStatus().getRemaining();
            cursor = ids.getNextCursor();
        } while ((cursor != 0) && (remApiLimits < 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, mutesIDsCollection);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<IDs> mutesIDs = (List<IDs>) tsakResponse.getResponseData();
        for (IDs ids : mutesIDs) {
            JSONObject idsJson = new JSONObject();
            idsJson.accumulate("ids", ids.getIDs());
            writer.append(idsJson.toString());
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpMutesIDs [limit=" + limit + "]";
    }
}