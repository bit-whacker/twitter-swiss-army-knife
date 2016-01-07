package org.projectspinoza.twitterswissarmyknife.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import twitter4j.AccountSettings;
import twitter4j.Category;
import twitter4j.Friendship;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.PagableResponseList;
import twitter4j.Place;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.User;
import twitter4j.UserList;

import com.google.gson.Gson;

/**
 * TsakResponseWriter
 * writes results of the executed command to the mentioned output text file.
 * 
 * @author org.projectspinoza
 * @version v1.0
 *
 */
public class TsakResponseWriter implements DataWriter {
    private static Logger log = LogManager.getRootLogger();
    private String outputFileName;
    private PrintStream writer;
    
    /**
     * default constructor for TsakResponseWriter
     */
    public TsakResponseWriter() {
    }
    
    /**
     * constructs TsakResponseWriter instance.
     * 
     * @param outputfile
     */
    public TsakResponseWriter(String outputfile) {
        this.outputFileName = outputfile;
    }
    
    @Override
    public void write(Object twitterResponseData, String parsedCommand,
            String outputFile) {
        if (outputFile == null || outputFile.trim().isEmpty()) {
            this.outputFileName = parsedCommand + "_out.txt";
        } else {
            this.outputFileName = outputFile;
        }
        write(twitterResponseData, parsedCommand);
    }

    @SuppressWarnings("unchecked")
    /**
     * writes the response to the output text file.
     * 
     * @param twitterResponseData
     * @param parsedCommand
     */
    public void write(Object twitterResponseData, String parsedCommand) {
        getWriterHandle();
        if (parsedCommand.equals("dumpFollowerIDs")
                || parsedCommand.equals("dumpFriendIDs")
                || parsedCommand.equals("dumpIncomingFriendships")
                || parsedCommand.equals("dumpOutgoingFriendships")
                || parsedCommand.equals("dumpMutesIDs")) {
            IDsListJsonWriter((List<IDs>) twitterResponseData);
        } else if (parsedCommand.equals("dumpRetweeters")) {
            IDsOnlyJsonWriter((IDs) twitterResponseData);
        } else if (parsedCommand.equals("dumpUserListMemberships")
                || parsedCommand.equals("dumpUserListSubscriptions")) {
            pagableResponseListJsonWriter((List<PagableResponseList<UserList>>) twitterResponseData);
        } else if (parsedCommand.equals("dumpHomeTimeLine")
                || parsedCommand.equals("dumpOwnRetweets")
                || parsedCommand.equals("dumpMentionsTimeLine")) {
            statusListJsonWriter((List<Status>) twitterResponseData);
        } else if (parsedCommand.equals("dumpUserTimeLine")
                || parsedCommand.equals("dumpListStatuses")) {
            responseListStatusJsonWriter((List<ResponseList<Status>>) twitterResponseData);
        } else if (parsedCommand.equals("dumpAccountSettings")) {
            accountSettingsJsonWriter((AccountSettings) twitterResponseData);
        } else if (parsedCommand.equals("dumpTweets")) {
            statusListsJsonWriter(((List<List<Status>>) twitterResponseData));
        } else if (parsedCommand.equals("dumpFavourites")) {
            statusListJsonWriter((List<Status>) twitterResponseData);
        } else if (parsedCommand.equals("dumpStatus")) {
            statusJsonWriter((Status) twitterResponseData);
        } else if (parsedCommand.equals("dumpBlockList")) {
            userListJsonWriter((List<User>) twitterResponseData);
        } else if (parsedCommand.equals("showFriendShip")) {
            relationshipJsonWriter((Relationship) twitterResponseData);
        } else if (parsedCommand.equals("dumpSugeestedUserCats")) {
            catListJsonWriter((ResponseList<Category>) twitterResponseData);
        } else if (parsedCommand.equals("dumpUserSuggestions")
                || parsedCommand.equals("dumpMemberSuggestions")) {
            usersResponseListJsonWriter((ResponseList<User>) twitterResponseData);
        } else if (parsedCommand.equals("dumpUserLists")) {
            responseUserListsJsonWriter((ResponseList<UserList>) twitterResponseData);
        } else if (parsedCommand.equals("dumpSavedSearches")) {
            responseSavedSearchesJsonWriter((ResponseList<SavedSearch>) twitterResponseData);
        } else if (parsedCommand.equals("dumpGeoDetails")) {
            geoPlaceJsonWriter((Place) twitterResponseData);
        } else if (parsedCommand.equals("dumpSimilarPlaces")
                || parsedCommand.equals("searchPlace")) {
            geoResponsePlacesListJsonWriter((ResponseList<Place>) twitterResponseData);
        } else if (parsedCommand.equals("dumpAvailableTrends")
                || parsedCommand.equals("dumpClosestTrends")) {
            responseLocationsListJsonWriter((ResponseList<Location>) twitterResponseData);
        } else if (parsedCommand.equals("dumpPlaceTrends")) {
            trendsJsonWriter((Trends) twitterResponseData);
        } else if (parsedCommand.equals("dumpMutesList")
                || parsedCommand.equals("dumpUserListSubscribers")
                || parsedCommand.equals("dumpUserListMembers")
                || parsedCommand.equals("dumpFriendsList")
                || parsedCommand.equals("dumpFollowersList")) {
            pagableResponseUsersJsonWriter((List<PagableResponseList<User>>) twitterResponseData);
        } else if (parsedCommand.equals("dumpUsersLookup")
                || (parsedCommand.equals("searchUsers"))) {
            responselistUserJsonWriter((List<ResponseList<User>>) twitterResponseData);
        } else if (parsedCommand.equals("lookupFriendShip")) {
            responselistFriendshipJsonWriter((List<ResponseList<Friendship>>) twitterResponseData);
        } else if (parsedCommand.equals("streamStatuses")) {

            String trend_json = new Gson().toJson((Status) twitterResponseData);
            writeLine(trend_json, true);

        } else {
            log.info("No Parser found for -> " + parsedCommand);
        }
        closeWriterHandler();
    }
    /**
     * closes writer if its not closed yet.
     * 
     */
    public void closeWriterHandler() {
        if (this.writer != null) {
            this.writer.close();
        }
    }
    
    /**
     * appends the specified string line to the end of the output text file.
     * 
     * @param line
     * @param newline
     */
    public void writeLine(String line, boolean newline) {
        if (newline) {
            this.writer.println(line);
        } else {
            this.writer.print(line);
        }
        this.writer.flush();
    }
    /**
     * appends the specified trends to the end of the output text file.
     * 
     * @param trends
     */
    public void trendsJsonWriter(Trends trends) {
        for (Trend trend : trends.getTrends()) {
            String jsonTrends = new Gson().toJson(trend);
            writeLine(jsonTrends, true);
        }
    }
    /**
     * appends the specified locations to the end of the output text file.
     * 
     * @param locations
     */
    public void responseLocationsListJsonWriter(ResponseList<Location> locations) {
        for (Location location : locations) {
            String jsonLocation = new Gson().toJson(location);
            writeLine(jsonLocation, true);
        }
    }
    /**
     * appends the specified geoPlaces to the end of the output text file.
     * 
     * @param places
     */
    public void geoResponsePlacesListJsonWriter(ResponseList<Place> places) {
        for (Place place : places) {
            geoPlaceJsonWriter(place);
        }
    }
    /**
     * appends the specified geoPlaces to the end of the output text file.
     * 
     * @param place
     */
    public void geoPlaceJsonWriter(Place place) {
        String placeInfo_json = new Gson().toJson(place);
        writeLine(placeInfo_json, true);
    }
    /**
     * appends the specified savedSearches to the end of the output text file.
     * 
     * @param savedsearches
     */
    public void responseSavedSearchesJsonWriter(
            ResponseList<SavedSearch> savedsearches) {
        for (SavedSearch savedSearch : savedsearches) {
            String savedsearchesJson = new Gson().toJson(savedSearch);
            writeLine(savedsearchesJson, true);
        }
    }
    /**
     * appends the specified userLists to the end of the output text file.
     * 
     * @param userlists
     */
    public void responseUserListsJsonWriter(ResponseList<UserList> userlists) {
        for (UserList list : userlists) {
            String ulistJson = new Gson().toJson(list);
            writeLine(ulistJson, true);
        }
    }
    /**
     * appends the specified relationship to the end of the output text file.
     * 
     * @param relation
     */
    public void relationshipJsonWriter(Relationship relation) {
        String relJson = new Gson().toJson(relation);
        writeLine(relJson, true);
    }

    public void userListJsonWriter(List<User> uList) {
        for (User user : uList) {
            String userJson = new Gson().toJson(user);
            writeLine(userJson, true);
        }
    }
    /**
     * appends the specified status list to the end of the output text file.
     * 
     * @param statusList
     */
    public void statusListJsonWriter(List<Status> statusList) {
        for (Status status : statusList) {
            String statusJson = new Gson().toJson(status);
            writeLine(statusJson, true);
        }
    }
    /**
     * appends the specified list of status lists to the end of the output text file.
     * 
     * @param listOfStatusList
     */
    public void statusListsJsonWriter(List<List<Status>> listOfStatusList) {
        for (List<Status> sList : listOfStatusList) {
            for (Status status : sList) {
                String statusJson = new Gson().toJson(status);
                writeLine(statusJson, true);
            }
        }
    }
    /**
     * appends the specified responseList of statuses to the end of the output text file.
     * 
     * @param statusResponseList
     */
    public void responseListStatusJsonWriter(List<ResponseList<Status>> statusResponseList) {
        for (ResponseList<Status> sList : statusResponseList) {
            for (Status status : sList) {
                String statusJson = new Gson().toJson(status);
                writeLine(statusJson, true);
            }
        }
    }
    /**
     * appends the specified responseList of users to the end of the output text file.
     * 
     * @param userResponseList
     */
    public void responselistUserJsonWriter(List<ResponseList<User>> userResponseList) {
        for (ResponseList<User> users : userResponseList) {
            for (User user : users) {
                String statusJson = new Gson().toJson(user);
                writeLine(statusJson, true);
            }
        }
    }
    /**
     * appends the specified responseList of friendship to the end of the output text file.
     * 
     * @param Friendships
     */
    public void responselistFriendshipJsonWriter(
            List<ResponseList<Friendship>> Friendships) {
        for (ResponseList<Friendship> Friendship : Friendships) {
            for (Friendship friendship : Friendship) {
                String statusJson = new Gson().toJson(friendship);
                writeLine(statusJson, true);
            }
        }
    }
    /**
     * appends the specified status to the end of the output text file.
     * 
     * @param status
     */
    public void statusJsonWriter(Status status) {
        String jsonStatus = new Gson().toJson(status);
        writeLine(jsonStatus, true);
    }
    /**
     * appends the specified accountSettings to the end of the output text file.
     * 
     * @param accountSettings
     */
    public void accountSettingsJsonWriter(AccountSettings accountSettings) {
        String jsonSettings = new Gson().toJson(accountSettings);
        writeLine(jsonSettings, true);
    }
    /**
     * appends the specified responseList of categories to the end of the output text file.
     * 
     * @param categories
     */
    public void catListJsonWriter(ResponseList<Category> categories) {
        for (Category category : categories) {
            String jsonCats = new Gson().toJson(category);
            writeLine(jsonCats, true);
        }
    }
    /**
     * appends the specified responseList of users to the end of the output text file.
     * 
     * @param users
     */
    public void usersResponseListJsonWriter(ResponseList<User> users) {
        for (User user : users) {
            String userJson = new Gson().toJson(user);
            writeLine(userJson, true);
        }
    }
    /**
     * appends the specified pagableResponseList of userList to the end of the output text file.
     * 
     * @param responseList
     */
    public void pagableResponseListJsonWriter(
            List<PagableResponseList<UserList>> responseList) {
        for (PagableResponseList<UserList> pResponse : responseList) {
            for (UserList userList : pResponse) {
                String jsonUserList = new Gson().toJson(userList);
                writeLine(jsonUserList, true);
            }
        }
    }
    /**
     * appends the specified pagableResponseList of user to the end of the output text file.
     * 
     * @param usersList
     */
    public void pagableResponseUsersJsonWriter(
            List<PagableResponseList<User>> usersList) {
        for (PagableResponseList<User> users : usersList) {
            for (User user : users) {
                String userJson = new Gson().toJson(user);
                writeLine(userJson, true);
            }
        }
    }
    /**
     * appends the specified list of IDs to the end of the output text file.
     * 
     * @param Ids_list
     */
    public void IDsListJsonWriter(List<IDs> iDsList) {
        for (IDs ids : iDsList) {
            IDsOnlyJsonWriter(ids);
        }
    }
    /**
     * appends the specified IDs to the end of the output text file.
     * 
     * @param ids
     */
    public void IDsOnlyJsonWriter(IDs ids) {
        JSONObject idsJson = new JSONObject();
        idsJson.accumulate("ids", ids.getIDs());
        writeLine(idsJson.toString(), true);
    }
    /**
     * creates writer handle.
     * 
     */
    public void getWriterHandle() {
        try {
            this.writer = new PrintStream(new FileOutputStream(new File(
                    this.outputFileName), true));
        } catch (FileNotFoundException e) {
            log.error("Error writing to file.. ->" + e.getMessage());
        }
    }
}