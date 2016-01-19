package org.projectspinoza.twitterswissarmyknife.streaming;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectspinoza.twitterswissarmyknife.command.CommandStreamStatuses;

import jline.console.ConsoleReader;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * TwitterStreamingExcecutor contains logic for handling and executing twitter
 * streaming API commands.
 * 
 * @author org.projectspinoza
 * @version v1.0
 *
 */
public class TwitterStreamingExcecutor {
    private static Logger log = LogManager.getRootLogger();
    private TwitterStream twitterStream;
    
    /**
     * executes twitter streaming command.
     * 
     * @param configurationBuilder
     * @param streamStatuses
     * @throws IOException
     */
    public void execute(ConfigurationBuilder configurationBuilder, CommandStreamStatuses streamStatuses) throws IOException {
        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(new File(streamStatuses.getOutputFile())));
            streamedStatuses(configurationBuilder, streamStatuses, writer);
            final ConsoleReader reader = new ConsoleReader();
            boolean run = true;
            while (run) {
                String commandLine = reader.readLine();
                if (commandLine.trim().equals("exit")) {
                    run = false;
                    twitterStream.clearListeners();
                    twitterStream.shutdown();
                    if (writer != null) {
                        writer.flush();
                        writer.close();
                    }
                }
            }
        } catch (IOException ioex) {
            log.error(ioex.getMessage());
            log.error(ioex.getCause());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    // .
                }
            }
        }
    }

    /**
     * dump twitter streaming statuses.
     * 
     * @param configurationBuilder
     * @param streamStatuses
     * @param responseWriter
     */
    private void streamedStatuses(ConfigurationBuilder configurationBuilder, CommandStreamStatuses streamStatuses, BufferedWriter writer) {
        String keywords = streamStatuses.getKeywords();
        String keywordsArray[] = keywords.split(",");

        twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        TwitterStatusStreams statusStreams = new TwitterStatusStreams(keywordsArray, streamStatuses.storeStreaming(),
                writer);

        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(keywordsArray);
        twitterStream.addListener(statusStreams);
        twitterStream.filter(filterQuery);
    }
}
