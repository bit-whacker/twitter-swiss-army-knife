package org.projectspinoza.twitterswissarmyknife.command;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Parameters(commandNames = "dumpOutgoingFriendships", commandDescription = "Authenticated user's Outgoing Friendships")
public class CommandDumpOutgoingFriendships extends BaseCommand {
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
	    List<IDs> outGoingFriendshipsCollection = new ArrayList<IDs>();
        int userLimit = this.limit;
        int remApiLimits = 0;
        long cursor = -1;
        do {
            IDs ids = twitter.getOutgoingFriendships(cursor);
            outGoingFriendshipsCollection.add(ids);
            remApiLimits = ids.getRateLimitStatus().getRemaining();
            cursor = ids.getNextCursor();
        } while ((cursor != 0) && (remApiLimits < 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, outGoingFriendshipsCollection);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public void write(TsakResponse tsakResponse, FileWriter writer) throws IOException {
	    List<IDs> idList = (List<IDs>) tsakResponse.getResponseData();
        for(IDs ids : idList){
            JSONObject idsJson = new JSONObject();
            idsJson.accumulate("ids", ids.getIDs());
            writer.append(idsJson.toString());
        }
	}
	
    @Override
    public String toString() {
        return "CommandDumpOutgoingFriendships [limit=" + limit + "]";
    }
	
}