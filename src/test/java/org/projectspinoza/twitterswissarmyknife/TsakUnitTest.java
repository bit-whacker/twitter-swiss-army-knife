package org.projectspinoza.twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.projectspinoza.twitterswissarmyknife.command.CommandDumpFollowerIDs;
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
public class TsakUnitTest {

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
	public void testCase_1() throws TwitterException {
		CommandDumpFollowerIDs testCommand = new CommandDumpFollowerIDs();
		testCommand.setScreenName(testUserName);
		testCommand.setOutputFile(testOutput);
		testCommand.setLimit(1);

		TsakResponse expected = new TsakResponse(0, new ArrayList<IDs>(Arrays.asList(ids)));
		expected.setCommandDetails(testCommand.toString());

		Mockito.when(twitter.getFollowersIDs(testUserName, -1)).thenReturn(ids);
		Mockito.when(ids.getNextCursor()).thenReturn(0L);
		Mockito.when(ids.getRateLimitStatus()).thenReturn(rateLimitStatus);
		Mockito.when(rateLimitStatus.getRemaining()).thenReturn(0);
		TsakResponse result = testCommand.execute(twitter);

		assertEquals(expected.getRemApiLimits(), result.getRemApiLimits());
		assertEquals(expected.getResponseData(), result.getResponseData());
		assertEquals(expected.getCommandDetails(), result.getCommandDetails());
		
	}
}
