package org.projectspinoza.twitterswissarmyknife.streaming;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.gson.Gson;

import jline.internal.Log;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * TwitterStatusStreams
 * 
 * @author org.projectspinoza
 * @version v1.0
 *
 */
public class TwitterStatusStreams implements StatusListener {

    private boolean storeStreamingData;
    private BufferedWriter writer;

    public TwitterStatusStreams(String keywordsArray[], boolean storeStreamData, BufferedWriter writer) {
        this.setStoreStreamingData(storeStreamData);
        this.writer = writer;
    }

    @Override
    public void onException(Exception arg0) {
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice arg0) {
    }

    @Override
    public void onScrubGeo(long arg0, long arg1) {
    }

    @Override
    public void onStallWarning(StallWarning arg0) {
    }

    @Override
    public void onStatus(Status status) {
        try {
            if (storeStreamingData) {
                String jsonTend = new Gson().toJson(status);
                writer.write(jsonTend);
                writer.newLine();
                writer.flush();
                System.out.println("Writble@" + status.getUser().getScreenName() + " - " + status.getText());
            } else {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (IOException ioex) {
            Log.error("Cannot write streaming statuses: {}", ioex.getMessage());
        }
    }

    @Override
    public void onTrackLimitationNotice(int arg0) {
    }

    public boolean isStore_streaming_data() {
        return storeStreamingData;
    }

    public void setStoreStreamingData(boolean storeStreamingData) {
        this.storeStreamingData = storeStreamingData;
    }
}