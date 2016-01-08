package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpStatus", commandDescription = "getting status by id")
public class CommandDumpStatus extends BaseCommand {
    @Parameter(names = "-sid", description = "Status id", required = true)
    private long statusId;

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        Status status = twitter.showStatus(this.statusId);
        int remApiLimits = status.getRateLimitStatus().getRemaining();
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, status);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        Status status = (Status) tsakResponse.getResponseData();
        String statusjson = new Gson().toJson(status);
        writer.append(statusjson);
        writer.newLine();
    }

    @Override
    public String toString() {
        return "CommandDumpStatus [statusId=" + statusId + "]";
    }
}