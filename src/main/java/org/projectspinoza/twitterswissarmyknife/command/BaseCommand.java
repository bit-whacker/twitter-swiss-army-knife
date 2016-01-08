package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedWriter;
import java.io.IOException;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.Parameter;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public abstract class BaseCommand {

    @Parameter(names = "--help", help = true, description = "Display help for command")
    private boolean help = false;

    @Parameter(names = "-o", description = "output file name")
    private String outputFile = "output.txt";

    public boolean needHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public abstract TsakResponse execute(Twitter twitter) throws TwitterException;

    public abstract void write(TsakResponse tsakResponse, BufferedWriter bufferedWriter) throws IOException;
}
