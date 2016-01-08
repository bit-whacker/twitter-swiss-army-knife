package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpListStatuses", commandDescription = "list's status")
public class CommandDumpListStatuses extends BaseCommand {
    @Parameter(names = "-lid", description = "list id", required = true)
    private long listId;

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<ResponseList<Status>> listStatusesCollection = new ArrayList<ResponseList<Status>>();
        Paging page = new Paging(1, 50);
        int remApiLimits = 1;
        do {
            ResponseList<Status> listStatuses = twitter.getUserListStatuses(
                    this.listId, page);
            listStatusesCollection.add(listStatuses);
            page.setPage(page.getPage() + 1);
            remApiLimits = listStatuses.getRateLimitStatus().getRemaining();
        } while (page.getPage() < 180 && remApiLimits > 0);
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, listStatusesCollection);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<ResponseList<Status>> listStatuses = (List<ResponseList<Status>>) tsakResponse.getResponseData();
        for (ResponseList<Status> statuses : listStatuses) {
            for (Status status : statuses) {
                String listJson = new Gson().toJson(status);
                writer.append(listJson);
                writer.newLine();
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpListStatuses [listId=" + listId + "]";
    }
}