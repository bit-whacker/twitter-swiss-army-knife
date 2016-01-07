package org.projectspinoza.twitterswissarmyknife.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "tsak", separators = "=", commandDescription = "tsak command")
public class TsakCommand {

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
