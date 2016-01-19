package org.projectspinoza.twitterswissarmyknife;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale.Category;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;

import com.beust.jcommander.ParameterException;

import twitter4j.AccountSettings;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Paging.class, Query.class, QueryResult.class, GeoLocation.class, GeoQuery.class })
public class TsakIntegrationTest {

    long testUserId = 101010111;
    long testSlugId = 101010111;
    long testListId = 101010111;
    String testUserName = "bit-whacker";
    String testOutput = "testOutput.txt";

    @Mock
    Twitter twitter;
    IDs ids;
    RateLimitStatus rateLimitStatus;
    ResponseList<Status> statuses;
    Paging page;
    PagableResponseList<User> userPagableResponseList;
    ResponseList<UserList> userlist;
    QueryResult queryResult;
    Query query;
    List<Status> tweets;
    AccountSettings accountSettings;
    Status status;
    ResponseList<Category> categoryResponseList;
    ResponseList<User> user;
    ResponseList<SavedSearch> savedSearch;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        twitter = Mockito.mock(Twitter.class);
        ids = Mockito.mock(IDs.class);
        rateLimitStatus = Mockito.mock(RateLimitStatus.class);

        statuses = Mockito.mock(ResponseList.class);
        page = Mockito.mock(Paging.class);
        userPagableResponseList = Mockito.mock(PagableResponseList.class);
        userlist = Mockito.mock(ResponseList.class);
        queryResult = Mockito.mock(QueryResult.class);
        query = Mockito.mock(Query.class);
        tweets = Mockito.mock(List.class);
        accountSettings = Mockito.mock(AccountSettings.class);
        status = Mockito.mock(Status.class);
        categoryResponseList = Mockito.mock(ResponseList.class);
        user = Mockito.mock(ResponseList.class);
        savedSearch = Mockito.mock(ResponseList.class);
    }

    @Test
    public void testCase_1() throws ParameterException, InstantiationException, IllegalAccessException, TwitterException, IOException {
        String testCommand = "tsak dumpFollowerIDs -uname " + testUserName + " -limit 1 -o " + testOutput;
        TsakResponse expected = new TsakResponse(0, new ArrayList<IDs>(Arrays.asList(ids)));
        TwitterSwissArmyKnife tsak = Mockito.spy(TwitterSwissArmyKnife.getInstance());

        Mockito.when(tsak.isAuthorized()).thenReturn(true);
        Mockito.when(tsak.getTwitterInstance()).thenReturn(twitter);

        Mockito.when(twitter.getFollowersIDs(testUserName, -1)).thenReturn(ids);
        Mockito.when(ids.getNextCursor()).thenReturn(0L);
        Mockito.when(ids.getRateLimitStatus()).thenReturn(rateLimitStatus);
        Mockito.when(rateLimitStatus.getRemaining()).thenReturn(0);

        tsak.executeCommand(testCommand.split(" "));
        TsakResponse result = tsak.getResult();

        assertEquals(expected.getRemApiLimits(), result.getRemApiLimits());
        assertEquals(expected.getResponseData(), result.getResponseData());
    }

    @Test
    public void testCase_2() throws TwitterException, ParameterException, InstantiationException, IllegalAccessException, IOException {
        String testCommand = "tsak dumpFriendIDs -uname " + testUserName + " -limit 1 -o " + testOutput;
        TsakResponse expected = new TsakResponse(0, new ArrayList<IDs>(Arrays.asList(ids)));
        TwitterSwissArmyKnife tsak = Mockito.spy(TwitterSwissArmyKnife.getInstance());

        Mockito.when(tsak.isAuthorized()).thenReturn(true);
        Mockito.when(tsak.getTwitterInstance()).thenReturn(twitter);

        Mockito.when(twitter.getFriendsIDs(testUserName, -1)).thenReturn(ids);
        Mockito.when(ids.getNextCursor()).thenReturn(0L);
        Mockito.when(ids.getRateLimitStatus()).thenReturn(rateLimitStatus);
        Mockito.when(rateLimitStatus.getRemaining()).thenReturn(0);

        tsak.executeCommand(testCommand.split(" "));
        TsakResponse result = tsak.getResult();

        assertEquals(expected.getRemApiLimits(), result.getRemApiLimits());
        assertEquals(expected.getResponseData(), result.getResponseData());
    }

    @Test
    public void testCase_3() throws TwitterException, ParameterException, InstantiationException, IllegalAccessException, IOException {
        String testCommand = "tsak dumpHomeTimeLine -o " + testOutput;
        TsakResponse expected = new TsakResponse(0, statuses);
        TwitterSwissArmyKnife tsak = Mockito.spy(TwitterSwissArmyKnife.getInstance());

        Mockito.when(tsak.isAuthorized()).thenReturn(true);
        Mockito.when(tsak.getTwitterInstance()).thenReturn(twitter);

        Mockito.when(twitter.getHomeTimeline()).thenReturn(statuses);
        Mockito.when(statuses.getRateLimitStatus()).thenReturn(rateLimitStatus);

        tsak.executeCommand(testCommand.split(" "));
        TsakResponse result = tsak.getResult();

        assertEquals(expected.getRemApiLimits(), result.getRemApiLimits());
        assertEquals(expected.getResponseData(), result.getResponseData());
    }

    @Test
    public void testCase_4() throws TwitterException, ParameterException, InstantiationException, IllegalAccessException, IOException {
        String testCommand = "tsak dumpAccountSettings -o " + testOutput;
        TsakResponse expected = new TsakResponse(0, accountSettings);
        TwitterSwissArmyKnife tsak = Mockito.spy(TwitterSwissArmyKnife.getInstance());

        Mockito.when(tsak.isAuthorized()).thenReturn(true);
        Mockito.when(tsak.getTwitterInstance()).thenReturn(twitter);

        Mockito.when(twitter.getAccountSettings()).thenReturn(accountSettings);
        Mockito.when(accountSettings.getRateLimitStatus()).thenReturn(rateLimitStatus);

        tsak.executeCommand(testCommand.split(" "));
        TsakResponse result = tsak.getResult();

        assertEquals(expected.getRemApiLimits(), result.getRemApiLimits());
        assertEquals(expected.getResponseData(), result.getResponseData());
    }

    @Test
    public void testCase_5() throws TwitterException, ParameterException, InstantiationException, IllegalAccessException, IOException {
        String testCommand = "tsak dumpOwnRetweets -o " + testOutput;
        TsakResponse expected = new TsakResponse(0, statuses);
        TwitterSwissArmyKnife tsak = Mockito.spy(TwitterSwissArmyKnife.getInstance());

        Mockito.when(tsak.isAuthorized()).thenReturn(true);
        Mockito.when(tsak.getTwitterInstance()).thenReturn(twitter);

        Mockito.when(twitter.getRetweetsOfMe()).thenReturn(statuses);
        Mockito.when(statuses.getRateLimitStatus()).thenReturn(rateLimitStatus);

        tsak.executeCommand(testCommand.split(" "));
        TsakResponse result = tsak.getResult();

        assertEquals(expected.getRemApiLimits(), result.getRemApiLimits());
        assertEquals(expected.getResponseData(), result.getResponseData());
    }

    @Test
    public void testCase_6() throws ParameterException, InstantiationException, IllegalAccessException, TwitterException, IOException {
        String testCommand = "tsak math -squareOf 4 -o " + testOutput;
        TwitterSwissArmyKnife tsak = Mockito.spy(TwitterSwissArmyKnife.getInstance());

        Mockito.when(tsak.isAuthorized()).thenReturn(true);
        Mockito.when(tsak.getTwitterInstance()).thenReturn(twitter);

        tsak.executeCommand(testCommand.split(" "));
        TsakResponse result = tsak.getResult();

        int square = (int) result.getResponseData();
        assertEquals(square, 16);
    }

    @Test
    public void testCase_7() throws ParameterException, InstantiationException, IllegalAccessException, TwitterException, IOException {
        String testCommand = "tsak math -squareOf 3 -o " + testOutput;
        TwitterSwissArmyKnife tsak = Mockito.spy(TwitterSwissArmyKnife.getInstance());

        Mockito.when(tsak.isAuthorized()).thenReturn(true);
        Mockito.when(tsak.getTwitterInstance()).thenReturn(twitter);

        tsak.executeCommand(testCommand.split(" "));
        TsakResponse result = tsak.getResult();

        int square = (int) result.getResponseData();
        assertFalse(5 == square);
    }

}