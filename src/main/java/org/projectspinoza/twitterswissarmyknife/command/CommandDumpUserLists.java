package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UserList;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpUserLists", commandDescription = "user's lists")
public class CommandDumpUserLists extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    private String screenName;
    @Parameter(names = "-uid", description = "user id")
    private long userId;

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

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        ResponseList<UserList> userLists = this.screenName == null ? twitter
                .getUserLists(this.userId) : twitter
                .getUserLists(this.screenName);
        int remApiLimits = userLists.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, userLists);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        ResponseList<UserList> userLists = (ResponseList<UserList>) tsakResponse.getResponseData();
        for (UserList list : userLists) {
            String listJson = new Gson().toJson(list);
            writer.append(listJson);
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "CommandDumpUserLists [screenName=" + screenName + ", userId="
                + userId + "]";
    }
}