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

@Parameters(commandNames = "dumpUserListSubscribers", commandDescription = "user's lists subscribers")
public class CommandDumpUserListSubscribers extends BaseCommand {
    @Parameter(names = "-lid", description = "list ID", required = true)
    private long listId;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    private int limit = 1;

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<PagableResponseList<User>> listSubscribers = new ArrayList<PagableResponseList<User>>();
        int userLimit = this.limit;
        int remApiLimits = 0;
        long cursor = -1;
        do {
            PagableResponseList<User> subscribers = twitter.getUserListSubscribers(this.listId, cursor);
            listSubscribers.add(subscribers);
            cursor = subscribers.getNextCursor();
            remApiLimits = subscribers.getRateLimitStatus().getRemaining();
        } while ((cursor != 0) && (remApiLimits != 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, listSubscribers);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<PagableResponseList<User>> subscribers = (List<PagableResponseList<User>>) tsakResponse.getResponseData();
        for (PagableResponseList<User> users : subscribers) {
            for (User user : users) {
                String userJson = new Gson().toJson(user);
                writer.append(userJson);
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpUserListSubscribers [listId=" + listId + ", limit="
                + limit + "]";
    }
}
