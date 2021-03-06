package org.projectspinoza.twitterswissarmyknife;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectspinoza.twitterswissarmyknife.command.BaseCommand;
import org.projectspinoza.twitterswissarmyknife.command.CommandStreamStatuses;
import org.projectspinoza.twitterswissarmyknife.command.TsakCommand;
import org.projectspinoza.twitterswissarmyknife.streaming.TwitterStreamingExcecutor;
import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;
import org.reflections.Reflections;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * TwitterSwissArmyKnife A simple command line utility that allows a user to
 * interact with Twitter's public API. it is very simple, straight forward, easy
 * to use and flexible API. It provides Method chaining, re-usability,
 * flexibility and simplicity.
 * 
 * @author org.projectspinoza
 * @version v1.0.0
 *
 */
public class TwitterSwissArmyKnife {
    private static Logger log = LogManager.getRootLogger();
    private static final String COMMANDS_SCAN_PACKAGE = "org.projectspinoza.twitterswissarmyknife.command";
    private static TwitterSwissArmyKnife tsakInstance = null;

    private TsakCommand tsakCommand;
    private JCommander subCommander;
    private TsakResponse tsakResponse;

    private boolean authorize;

    private ConfigurationBuilder configurationBuilder;
    private Twitter twitter;

    /**
     * Default private constructor for TwitterSwissArmyKnife. Prepares
     * TsakCommand by calling its default constructor, you may reset it later.
     * 
     */
    private TwitterSwissArmyKnife() {
        tsakCommand = new TsakCommand();
    }

    /**
     * returns TwitterSwissArmyKnife's static instance
     * 
     * @return tsakInstance
     */
    public synchronized static TwitterSwissArmyKnife getInstance() {
        if (tsakInstance == null) {
            tsakInstance = new TwitterSwissArmyKnife();
        }
        return tsakInstance;
    }

    /**
     * returns True if user has authorization to access twitterAPI false other
     * wise.
     * 
     * @return authorize
     */
    public boolean isAuthorized() {
        return authorize;
    }

    /**
     * sets OR overrides default tsakCommand.
     * 
     * @param tsakCommands
     * @return tsakInstance
     */
    public TwitterSwissArmyKnife setTsakCommand(TsakCommand tsakCommands) {
        log.info("setting tsakCommand");
        tsakCommand = tsakCommands;
        return tsakInstance;
    }

    /**
     * returns result e.g. the generated response of the executed command.
     * 
     * @return tsakResponse
     */
    public TsakResponse getResult() {
        return tsakResponse;
    }

    /**
     * returns twitter instance
     * 
     * @return twitter
     */
    public Twitter getTwitterInstance() {
        return twitter;
    }

    /**
     * writes the result (generated data of the executed command) to the output
     * file.
     * 
     * @return tsakInstance
     */
    public TwitterSwissArmyKnife write() {
        if (tsakResponse == null) {
            return tsakInstance;
        }
        BufferedWriter bufferedWriter = null;
        try {
            BaseCommand baseCommand = getSubCommand(subCommander.getParsedCommand());
            bufferedWriter = new BufferedWriter(new FileWriter(new File(baseCommand.getOutputFile())));
            baseCommand.write(tsakResponse, bufferedWriter);
            tsakResponse = null;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException ioex) {
            log.debug(ioex.getMessage());
        } catch (NullPointerException npex) {
            log.debug(npex.getMessage());
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException ioex) {
                    log.error("cannot close writer!!! {}", ioex.getMessage());
                }
            }
        }
        return tsakInstance;
    }

    /**
     * executes twitter streaming command.
     * 
     * @throws IOException
     */
    public void executeStreamingCommand(CommandStreamStatuses baseCommand) throws IOException {
        (new TwitterStreamingExcecutor()).execute(configurationBuilder, baseCommand);
    }

    /**
     * executes dump command.
     * 
     * @throws IOException
     * @throws TwitterException
     */
    public void executeDumpCommand(BaseCommand baseCommand) throws TwitterException {
        if (!isAuthorized()) {
            authorizeUser();
        }
        if (isAuthorized()) {
            tsakResponse = baseCommand.execute(getTwitterInstance());
            if (tsakResponse != null) {
                showRateLimitStatus(tsakResponse.getRemApiLimits());
            }
        } else {
            log.error("User not authorized!");
        }
    }

    /**
     * executes the provided command.
     * 
     * @param String[] Command
     * @return TsakInstance
     * @throws TwitterException
     * @throws ParameterException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public TwitterSwissArmyKnife executeCommand(String[] args) throws TwitterException, ParameterException, IOException, InstantiationException, IllegalAccessException {
        tsakResponse = null;
        if (args == null) {
            log.info("Need help?? run > tsak <commandName> --help");
            return tsakInstance;
        }
        JCommander rootCommander = new JCommander();
        rootCommander.addCommand("tsak", tsakCommand);
        subCommander = rootCommander.getCommands().get("tsak");
        activateSubCommands();
        rootCommander.parse(args);
        String parsedCommand = subCommander.getParsedCommand();
        BaseCommand baseCommand = getSubCommand(parsedCommand);
        if (baseCommand.needHelp()) {
            subCommander.usage(parsedCommand);
            return tsakInstance;
        }
        if (!isAuthorized()) {
            setConfigurationBuilder(rootCommander);
        }
        if (parsedCommand.equals("streamStatuses")) {
            executeStreamingCommand( (CommandStreamStatuses) baseCommand);
        } else {
            executeDumpCommand(baseCommand);
        }
        return tsakInstance;
    }

    /**
     * authorizes user with the provided credentials.
     * 
     * @throws TwitterException
     */
    private void authorizeUser() throws TwitterException {
        twitter = new TwitterFactory(getConfigurationBuilder().build()).getInstance();
        twitter.verifyCredentials();
        authorize = true;
    }

    /**
     * Sets twitter configuration builder
     * 
     * @param rootCommander
     * @throws IOException
     */
    private void setConfigurationBuilder(JCommander rootCommander) throws IOException {
        if (!setCredentials(rootCommander)) {
            log.error("Credentials not provided!");
            authorize = false;
            return;
        }
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
        .setOAuthConsumerKey(tsakCommand.getConsumerKey())
        .setOAuthConsumerSecret(tsakCommand.getConsumerSecret())
        .setOAuthAccessToken(tsakCommand.getAccessToken())
        .setOAuthAccessTokenSecret(tsakCommand.getAccessSecret());
    }

    /**
     * returns twitter configuration builder.
     * 
     * @return
     */
    private ConfigurationBuilder getConfigurationBuilder() {
        return configurationBuilder;
    }

    /**
     * sets/verifies provided twitter credentials and returns True on Success
     * and False on Failure.
     * 
     * @param rootCommander
     * @return boolean TRUE/FALSE
     * @throws IOException
     */
    private boolean setCredentials(JCommander rootCommander) throws IOException {
        if (!rootCommander.getParsedCommand().equals("tsak")) {
            log.info("Invalid Command: " + rootCommander.getParsedCommand());
            return false;
        }
        if (tsakCommand.getConsumerKey() == null || tsakCommand.getConsumerSecret() == null || tsakCommand.getAccessToken() == null || tsakCommand.getAccessSecret() == null) {
            String env = System.getenv("TSAK_CONF");
            if (env == null || env.isEmpty()) {
                log.error("Environment variable not set. TSAK_CONF {}");
                return false;
            }
            File propConfFile = new File(env + File.separator + "tsak.properties");
            if (!propConfFile.exists()) {
                log.error("tsak.properties file does not exist in: " + env);
                return false;
            }
            Properties prop = new Properties();
            InputStream propInstream = new FileInputStream(propConfFile);
            prop.load(propInstream);
            propInstream.close();
            tsakCommand.setConsumerKey(prop.getProperty("consumerKey").trim());
            tsakCommand.setConsumerSecret(prop.getProperty("consumerSecret").trim());
            tsakCommand.setAccessToken(prop.getProperty("accessToken").trim());
            tsakCommand.setAccessSecret(prop.getProperty("accessSecret").trim());
        }
        return true;
    }

    /**
     * activates/prepares all of the commands for execution.
     * 
     */
    public void activateSubCommands() throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(COMMANDS_SCAN_PACKAGE);
        Set<Class<? extends BaseCommand>> tsakCommandSet = reflections.getSubTypesOf(BaseCommand.class);
        for (Class<?> commandClazz : tsakCommandSet) {
            this.subCommander.addCommand(commandClazz.newInstance());
        }
    }

    /**
     * returns parsedCommand e.g. the provided command.
     * 
     * @param parsedCommand
     * @return BaseCommand
     */
    public BaseCommand getSubCommand(String parsedCommand) {
        return (BaseCommand) subCommander.getCommands().get(parsedCommand).getObjects().get(0);
    }

    /**
     * prints RateLimitStatus for specific command.
     * 
     * @param remApiLimits
     */
    public void showRateLimitStatus(int remApiLimits) {
        log.info("---------------------------------------------------");
        log.info("DONE!!! REMAINING TWITTER API CALLS: [" + remApiLimits + "]");
        log.info("---------------------------------------------------");
    }
}
