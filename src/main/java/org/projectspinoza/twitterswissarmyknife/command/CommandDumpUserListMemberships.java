package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UserList;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpUserListMemberships", commandDescription = "user's lists memberships")
public class CommandDumpUserListMemberships extends BaseCommand {
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

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<PagableResponseList<UserList>> listMemberships = new ArrayList<PagableResponseList<UserList>>();
        int userLimit = this.limit;
        int remApiLimits = 0;
        long cursor = -1;

        do {
            PagableResponseList<UserList> userLists = this.screenName == null ? twitter
                    .getUserListMemberships(this.userId, cursor) : twitter
                    .getUserListMemberships(this.screenName, cursor);
            listMemberships.add(userLists);
            cursor = userLists.getNextCursor();
            remApiLimits = userLists.getRateLimitStatus().getRemaining();
        } while ((cursor != 0) && (remApiLimits != 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, listMemberships);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<PagableResponseList<UserList>> userLists = (List<PagableResponseList<UserList>>) tsakResponse.getResponseData();
        for (PagableResponseList<UserList> userslist : userLists) {
            for (UserList list : userslist) {
                String listJson = new Gson().toJson(list);
                writer.append(listJson);
                writer.newLine();
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpUserListMemberships [screenName=" + screenName
                + ", userId=" + userId + ", limit=" + limit + "]";
    }
}
