package org.projectspinoza.twitterswissarmyknife.util;

/**
 * TsakCommand
 * contains definition for all of the available tsakCommands
 * 
 * @author org.projectspinoza
 * @version v1.0
 *
 */
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "tsak", separators = "=", commandDescription = "tsak command")
public class TsakCommandCopy {

    @Parameter(names = "-consumerKey", description = "consumer key")
    private String consumerKey;
    @Parameter(names = "-consumerSecret", description = "consumer secret key")
    private String consumerSecret;
    @Parameter(names = "-accessToken", description = "access token")
    private String accessToken;
    @Parameter(names = "-accessSecret", description = "access secret")
    private String accessSecret;

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String key) {
        consumerKey = key;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String key) {
        consumerSecret = key;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        accessToken = token;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String key) {
        accessSecret = key;
    }
}
/*
class BaseCommand {
    @Parameter(names = "-o", description = "output file name")
    public String outputFile;
}

@Parameters(commandNames = "streamStatuses", commandDescription = "Stream Statuses on specified keywords.")
class CommandStreamStatuses extends BaseCommand {
    @Parameter(names = "-keywords", description = "Status containing Keywords.", required = true)
    public String keywords;
    @Parameter(names = "-store", description = "Should store upcoming statuses.")
    public String store_streaming_data;
}

@Parameters(commandNames = "dumpFollowerIDs", commandDescription = "Followers IDs")
class CommandDumpFollowerIDs extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    public String screenName;
    @Parameter(names = "-uid", description = "user id")
    public long userid;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpFriendIDs", commandDescription = "Friends IDs")
class CommandDumpFriendIDs extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    public String screenName;
    @Parameter(names = "-uid", description = "user id")
    public long userid;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpAccountSettings", commandDescription = "Account Setting")
class CommandDumpAccountSettings extends BaseCommand {
}

@Parameters(commandNames = "dumpUserTimeLine", commandDescription = "UserTimeLine")
class CommandDumpUserTimeLine extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    public String screenName;
    @Parameter(names = "-uid", description = "user id")
    public long userid;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpHomeTimeLine", commandDescription = "Authenticated user HomeTimeLine")
class CommandDumpHomeTimeLine extends BaseCommand {
}

@Parameters(commandNames = "dumpTweets", commandDescription = "Search tweets")
class CommandDumpTweets extends BaseCommand {
    @Parameter(names = "-keywords", description = "keywords to be search for tweets", required = true)
    public String keywords;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpOwnRetweets", commandDescription = "Authenticated user retweets")
class CommandDumpOwnRetweets extends BaseCommand {
}

@Parameters(commandNames = "dumpStatus", commandDescription = "getting status by id")
class CommandDumpStatus extends BaseCommand {
    @Parameter(names = "-sid", description = "Status id", required = true)
    public long status_id;
}

@Parameters(commandNames = "dumpRetweeters", commandDescription = "getting retweeters of status")
class CommandDumpRetweeters extends BaseCommand {
    @Parameter(names = "-sid", description = "Status id", required = true)
    public long status_id;
}

@Parameters(commandNames = "dumpMentionsTimeLine", commandDescription = "Authenticated user MentionsTimeLine")
class CommandDumpMentionsTimeLine extends BaseCommand {
}

@Parameters(commandNames = "dumpUsersLookup", commandDescription = "looking up all the gives user")
class CommandDumpUsersLookup extends BaseCommand {
    @Parameter(names = "-i", description = "intput file", required = true)
    public String input_file;
}

@Parameters(commandNames = "dumpBlockList", commandDescription = "Authenticated user's blocked list")
class CommandDumpBlockList extends BaseCommand {
}

@Parameters(commandNames = "searchUsers", commandDescription = "Search users")
class CommandSearchUsers extends BaseCommand {
    @Parameter(names = "-keywords", description = "keywords to be search for users", required = true)
    public String keywords;
}

@Parameters(commandNames = "showFriendShip", commandDescription = "finding friendship between two users")
class CommandShowFriendShip extends BaseCommand {
    @Parameter(names = "-suser", description = "source username", required = true)
    public String source;

    @Parameter(names = "-tuser", description = "target username", required = true)
    public String target;
}

@Parameters(commandNames = "dumpFollowersList", commandDescription = "Followers list")
class CommandDumpFollowersList extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    public String screenName;
    @Parameter(names = "-uid", description = "user id")
    public long userid;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpFriendsList", commandDescription = "Friends list")
class CommandDumpFriendsList extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    public String screenName;
    @Parameter(names = "-uid", description = "user id")
    public long userid;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpFavourites", commandDescription = "Authenticated user's favourite tweets")
class CommandDumpFavourites extends BaseCommand {
}

@Parameters(commandNames = "dumpSugeestedUserCats", commandDescription = "sugeested user categories")
class CommandDumpSugeestedUserCats extends BaseCommand {
}

@Parameters(commandNames = "dumpUserSuggestions", commandDescription = "user's suggestions")
class CommandDumpUserSuggestions extends BaseCommand {
    @Parameter(names = "-slug", description = "category slug", required = true)
    public String slug;
}

@Parameters(commandNames = "dumpMemberSuggestions", commandDescription = "member's suggestions")
class CommandDumpMemberSuggestions extends BaseCommand {
    @Parameter(names = "-slug", description = "category slug", required = true)
    public String slug;
}

@Parameters(commandNames = "dumpUserLists", commandDescription = "user's lists")
class CommandDumpUserLists extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    public String screenName;

    @Parameter(names = "-uid", description = "user id")
    public long userid;
}

@Parameters(commandNames = "dumpListStatuses", commandDescription = "list's status")
class CommandDumpListStatuses extends BaseCommand {
    @Parameter(names = "-lid", description = "list id", required = true)
    public long list_id;
}

@Parameters(commandNames = "dumpSavedSearches", commandDescription = "Authenticated user's saved searches")
class CommandDumpSavedSearches extends BaseCommand {
}

@Parameters(commandNames = "lookupFriendShip", commandDescription = "looking up friendship between gives users and authenticated user")
class CommandLookupFriendShip extends BaseCommand {
    @Parameter(names = "-i", description = "intput file", required = true)
    public String input_file;
}

@Parameters(commandNames = "dumpIncomingFriendships", commandDescription = "Authenticated user's Incoming Friendships")
class CommandDumpIncomingFriendships extends BaseCommand {
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpOutgoingFriendships", commandDescription = "Authenticated user's Outgoing Friendships")
class CommandDumpOutgoingFriendships extends BaseCommand {
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpGeoDetails", commandDescription = "geo detail of a place")
class CommandDumpGeoDetails extends BaseCommand {
    @Parameter(names = "-pid", description = "place id", required = true)
    public String place_id;
}

@Parameters(commandNames = "dumpSimilarPlaces", commandDescription = "similar places")
class CommandDumpSimilarPlaces extends BaseCommand {
    @Parameter(names = "-lat", description = "latitude", required = true)
    public Double latitude;
    @Parameter(names = "-long", description = "longitude", required = true)
    public Double longitude;
    @Parameter(names = "-pname", description = "place name", required = true)
    public String place_name;
}

@Parameters(commandNames = "searchPlace", commandDescription = "search places")
class CommandSearchPlace extends BaseCommand {
    @Parameter(names = "-lat", description = "latitude", required = true)
    public Double latitude;
    @Parameter(names = "-long", description = "longitude", required = true)
    public Double longitude;
}

@Parameters(commandNames = "dumpAvailableTrends", commandDescription = "twitter available trends")
class CommandDumpAvailableTrends extends BaseCommand {
}

@Parameters(commandNames = "dumpPlaceTrends", commandDescription = "twitter place trends")
class CommandDumpPlaceTrends extends BaseCommand {
    @Parameter(names = "-woeid", description = "where on earth ID")
    public int woeid;
}

@Parameters(commandNames = "dumpClosestTrends", commandDescription = "closest trends places")
class CommandDumpClosestTrends extends BaseCommand {
    @Parameter(names = "-lat", description = "latitude", required = true)
    public Double latitude;
    @Parameter(names = "-long", description = "longitude", required = true)
    public Double longitude;
}

@Parameters(commandNames = "dumpMutesIDs", commandDescription = "Authenticated user's muted IDs")
class CommandDumpMutesIDs extends BaseCommand {
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpMutesList", commandDescription = "Authenticated user's muted lists")
class CommandDumpMutesList extends BaseCommand {
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpUserListMemberships", commandDescription = "user's lists memberships")
class CommandDumpUserListMemberships extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name")
    public String screenName;
    @Parameter(names = "-uid", description = "user id")
    public long userid;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpUserListSubscriptions", commandDescription = "user's lists subscriptions")
class CommandDumpUserListSubscriptions extends BaseCommand {
    @Parameter(names = "-uname", description = "user screen name", required = true)
    public String screenName;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpUserListMembers", commandDescription = "user's lists members")
class CommandDumpUserListMembers extends BaseCommand {
    @Parameter(names = "-lid", description = "list ID", required = true)
    public long list_id;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}

@Parameters(commandNames = "dumpUserListSubscribers", commandDescription = "user's lists subscribers")
class CommandDumpUserListSubscribers extends BaseCommand {
    @Parameter(names = "-lid", description = "list ID", required = true)
    public long list_id;
    @Parameter(names = "-limit", description = "Authenticated user api calls limit")
    public int limit = 1;
}
*/