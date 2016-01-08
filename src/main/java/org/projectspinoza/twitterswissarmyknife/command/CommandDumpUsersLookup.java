package org.projectspinoza.twitterswissarmyknife.command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "dumpUsersLookup", commandDescription = "looking up all the gives user")
public class CommandDumpUsersLookup extends BaseCommand {
    @Parameter(names = "-i", description = "intput file", required = true)
    private String inputFile;

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public TsakResponse execute(Twitter twitter) throws TwitterException {
        List<ResponseList<User>> usersCollection = new ArrayList<ResponseList<User>>();
        List<String> screenNames = new ArrayList<String>();
        List<Long> ids = new ArrayList<Long>();
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(new File(this.inputFile)));
            String line;
            ids = new ArrayList<Long>();
            while ((line = bReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (isLong(line)) {
                        ids.add(Long.parseLong(line));
                    } else {
                        screenNames.add(line);
                    }
                }
            }
            bReader.close();
        } catch (FileNotFoundException fnfex) {
            fnfex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        int remApiLimits = 0;
        long[] Ids = new long[ids.size()];
        String[] names = new String[screenNames.size()];
        if (!screenNames.isEmpty()) {
            for (int i = 0; i < screenNames.size(); i++) {
                names[i] = screenNames.get(i);
            }
            ResponseList<User> users = twitter.lookupUsers(names);
            usersCollection.add(users);
            remApiLimits = users.getRateLimitStatus().getRemaining();
        }
        if (!ids.isEmpty()) {
            for (int i = 0; i < ids.size(); i++) {
                Ids[i] = ids.get(i);
            }
            ResponseList<User> users = twitter.lookupUsers(Ids);
            usersCollection.add(users);
            remApiLimits = users.getRateLimitStatus().getRemaining();
        }
        TsakResponse tsakResponse = new TsakResponse(remApiLimits,
                usersCollection);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer) throws IOException {
        List<ResponseList<User>> usersCollection = (List<ResponseList<User>>) tsakResponse.getResponseData();
        for (ResponseList<User> users : usersCollection) {
            for (User user : users) {
                String userJson = new Gson().toJson(user);
                writer.append(userJson);
                writer.newLine();
            }
        }
    }

    private boolean isLong(String input) {
        try {
            Long.parseLong(input);
        } catch (ClassCastException | NumberFormatException ex) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommandDumpUsersLookup [inputFile=" + inputFile + "]";
    }

}