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

@Parameters(commandNames = "dumpFriendIDs", commandDescription = "Friends IDs")
public class CommandDumpFriendIDs extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    private String screenName;

    @Parameter(names = "-uid", description = "user id")
    private long userId;

    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    private int limit = 1;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userid) {
        this.userId = userid;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<IDs> friendsIDsCollection = new ArrayList<IDs>();
        int userLimit = this.limit;
        int remApiLimits = 0;
        long cursor = -1;
        do {
            IDs ids = this.screenName != null ? twitter.getFriendsIDs(
                    this.screenName, cursor) : twitter.getFriendsIDs(
                    this.userId, cursor);
            friendsIDsCollection.add(ids);
            cursor = ids.getNextCursor();
            remApiLimits = ids.getRateLimitStatus().getRemaining();
        } while ((cursor != 0) && (remApiLimits != 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, friendsIDsCollection);
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
        return "CommandDumpFriendIDs [screenName=" + screenName + ", userId="
                + userId + ", limit=" + limit + "]";
    }
}
