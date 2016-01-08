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

@Parameters(commandNames = "dumpUserListSubscriptions", commandDescription = "user's lists subscriptions")
public class CommandDumpUserListSubscriptions extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name", required = true)
    private String screenName;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    private int limit = 1;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<PagableResponseList<UserList>> listSubscriptions = new ArrayList<PagableResponseList<UserList>>();
        int userLimit = this.limit;
        int remApiLimits = 0;
        long cursor = -1;
        do {
            PagableResponseList<UserList> subscriptions = twitter.getUserListSubscriptions(this.screenName, cursor);
            listSubscriptions.add(subscriptions);
            cursor = subscriptions.getNextCursor();
            remApiLimits = subscriptions.getRateLimitStatus().getRemaining();
        } while ((cursor != 0) && (remApiLimits != 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, listSubscriptions);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<PagableResponseList<UserList>> subscriptions = (List<PagableResponseList<UserList>>) tsakResponse.getResponseData();
        for (PagableResponseList<UserList> userslist : subscriptions) {
            for (UserList list : userslist) {
                String listJson = new Gson().toJson(list);
                writer.append(listJson);
                writer.newLine();
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpUserListSubscriptions [screenName=" + screenName
                + ", limit=" + limit + "]";
    }
}
