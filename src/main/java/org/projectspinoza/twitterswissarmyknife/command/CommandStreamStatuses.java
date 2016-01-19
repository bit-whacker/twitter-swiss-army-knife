package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "streamStatuses", commandDescription = "Stream Statuses on specified keywords.")
public class CommandStreamStatuses extends BaseCommand {

    @Parameter(names = "-keywords", description = "Status containing Keywords.", required = true)
    private String keywords;
    @Parameter(names = "-store", description = "use parameter [-store] if you want to store streaming to output file")
    private boolean storeStreaming = false;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean storeStreaming() {
        return storeStreaming;
    }

    public void setStoreStreaming(boolean storeStreamingStatus) {
        this.storeStreaming = storeStreamingStatus;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "CommandStreamStatuses [keywords=" + keywords + ", storeStreamingStatus=" + storeStreaming + "]";
    }
    
    

}