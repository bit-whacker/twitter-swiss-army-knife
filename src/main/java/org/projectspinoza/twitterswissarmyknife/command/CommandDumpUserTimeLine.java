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

@Parameters(commandNames = "dumpUserTimeLine", commandDescription = "UserTimeLine")
public class CommandDumpUserTimeLine extends BaseCommand {
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
        List<ResponseList<Status>> userTimeline = new ArrayList<ResponseList<Status>>();
        int pagecounter = 1;
        int remApiLimits = 0;
        int userLimit = this.limit;
        Paging page = new Paging(pagecounter, 200);
        do {
            ResponseList<Status> statuses = this.screenName == null ? twitter
                    .getUserTimeline(this.userId, page) : twitter
                    .getUserTimeline(this.screenName, page);
            userTimeline.add(statuses);
            pagecounter++;
            page.setPage(pagecounter);
            remApiLimits = statuses.getRateLimitStatus().getRemaining();
        } while ((remApiLimits != 0) && --userLimit > 0);
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, userTimeline);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<ResponseList<Status>> userTimeline = (List<ResponseList<Status>>) tsakResponse.getResponseData();
        for (ResponseList<Status> statuses : userTimeline) {
            for (Status status : statuses) {
                String statusJson = new Gson().toJson(status);
                writer.append(statusJson);
                writer.newLine();
            }
        }
    }

    @Override
    public String toString() {
        return "CommandDumpUserTimeLine [screenName=" + screenName
                + ", userId=" + userId + ", limit=" + limit + "]";
    }
}