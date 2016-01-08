package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpFollowersList", commandDescription = "Followers list")
public class CommandDumpFollowersList extends BaseCommand {
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
        List<PagableResponseList<User>> followersList = new ArrayList<PagableResponseList<User>>();
        int userLimit = this.getLimit();
        int remApiLimits = 0;
        long cursor = -1;
        do {
            PagableResponseList<User> users = this.screenName != null ? twitter
                    .getFollowersList(this.screenName, cursor) : twitter
                    .getFollowersList(this.userId, cursor);
            followersList.add(users);
            cursor = users.getNextCursor();
            remApiLimits = users.getRateLimitStatus().getRemaining();
        } while ((cursor != 0) && (remApiLimits != 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, followersList);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<PagableResponseList<User>> followersList = (List<PagableResponseList<User>>) tsakResponse.getResponseData();
        for (PagableResponseList<User> users : followersList) {
            for (User user : users) {
                String userJson = new Gson().toJson(user);
                writer.append(userJson);
                writer.newLine();
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpFollowersList [screenName=" + screenName
                + ", userId=" + userId + ", limit=" + limit + "]";
    }
}