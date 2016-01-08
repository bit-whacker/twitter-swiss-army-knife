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

import twitter4j.Friendship;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;

@Parameters(commandNames = "lookupFriendShip", commandDescription = "looking up friendship between gives users and authenticated user")
public class CommandLookupFriendShip extends BaseCommand {
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
        List<ResponseList<Friendship>> friendShip = new ArrayList<ResponseList<Friendship>>();
        ArrayList<String> screenNames = new ArrayList<String>();
        ArrayList<Long> ids = new ArrayList<Long>();
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(new File(this.inputFile)));
            String line;

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
            ResponseList<Friendship> users = twitter.lookupFriendships(names);
            friendShip.add(users);
            remApiLimits = users.getRateLimitStatus().getRemaining();
        }
        if (!ids.isEmpty()) {
            for (int i = 0; i < ids.size(); i++) {
                Ids[i] = ids.get(i);
            }
            ResponseList<Friendship> users = twitter.lookupFriendships(Ids);
            friendShip.add(users);
            remApiLimits = users.getRateLimitStatus().getRemaining();
        }
        TsakResponse tsakResponse = new TsakResponse(remApiLimits, friendShip);
        tsakResponse.setCommandDetails(this.toString());
        return tsakResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(TsakResponse tsakResponse, BufferedWriter writer)
            throws IOException {
        List<ResponseList<Friendship>> friendShipCol = (List<ResponseList<Friendship>>) tsakResponse.getResponseData();
        for (ResponseList<Friendship> friendships : friendShipCol) {
            for (Friendship friendship : friendships) {
                String friendshipJson = new Gson().toJson(friendship);
                writer.append(friendshipJson);
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
        return "CommandLookupFriendShip [inputFile=" + inputFile + "]";
    }

}
