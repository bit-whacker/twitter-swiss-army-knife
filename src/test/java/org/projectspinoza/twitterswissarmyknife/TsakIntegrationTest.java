package org.projectspinoza.twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;

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

	@Before
	public void setup() {
		twitter = Mockito.mock(Twitter.class);
		ids = Mockito.mock(IDs.class);
		rateLimitStatus = Mockito.mock(RateLimitStatus.class);
	}
	
	@Test
	public void testCase_2() throws ParameterException, InstantiationException, IllegalAccessException, TwitterException, IOException{
		String testCommand = "tsak dumpFollowerIDs -uname "+testUserName+" -limit 1 -o " + testOutput;
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

}
