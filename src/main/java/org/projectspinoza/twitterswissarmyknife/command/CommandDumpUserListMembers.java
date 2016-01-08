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

@Parameters(commandNames = "dumpUserListMembers", commandDescription = "user's lists members")
public class CommandDumpUserListMembers extends BaseCommand {
    @Parameter(names = "-lid", description = "list ID", required = true)
    private long listId;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    private int limit = 1;

    public long getList_id() {
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
        List<PagableResponseList<User>> ListMembersCollection = new ArrayList<PagableResponseList<User>>();
        int userLimit = this.limit;
        long cursor = -1;
        int remApiLimits = 0;
        do {
            PagableResponseList<User> user = twitter.getUserListMembers(
                    this.listId, cursor);
            ListMembersCollection.add(user);
            cursor = user.getNextCursor();
            remApiLimits = user.getRateLimitStatus().getRemaining();
        } while ((cursor != 0) && (remApiLimits != 0) && (--userLimit > 0));
        TsakResponse tsakResponse = new TsakResponse(remApiLimits,
                ListMembersCollection);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<PagableResponseList<User>> listMembers = (List<PagableResponseList<User>>) tsakResponse.getResponseData();
        for (PagableResponseList<User> users : listMembers) {
            for (User user : users) {
                String userJson = new Gson().toJson(user);
                writer.append(userJson);
                writer.newLine();
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpUserListMembers [listId=" + listId + ", limit="
                + limit + "]";
    }
}
