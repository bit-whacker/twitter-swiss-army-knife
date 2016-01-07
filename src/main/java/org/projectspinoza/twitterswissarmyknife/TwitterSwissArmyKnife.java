package org.projectspinoza.twitterswissarmyknife;

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
 * TwitterSwissArmyKnife
 * A simple command line utility that allows a user to interact with Twitter's public API.
 * it is very simple, straight forward, easy to use and flexible API. It
 * provides Method chaining, re-usability, flexibility and simplicity.
 * 
 * @author org.projectspinoza
 * @version v1.0
 *
 */
public class TwitterSwissArmyKnife {
    private static Logger log = LogManager.getRootLogger();
    private static final String COMMANDS_PACKAGE = "org.projectspinoza.twitterswissarmyknife.command";
    private static TwitterSwissArmyKnife tsakInstance = null;
    
    private TsakCommand tsakCommand;
    private JCommander subCommander;
    private JCommander rootCommander;
    private String parsedCommand;

    private boolean authorize;
    private TsakResponse tsakResponse;

    private ConfigurationBuilder configurationBuilder;
    private Twitter twitter;
    
    /**
     * default Constructor for TwitterSwissArmyKnife, it prepares CommandlineDriver, tsakCommand and dataWriter, which can be override later if needed.
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
     * returns True if user has authorization to access twitterAPI false other wise.
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
        tsakInstance.tsakCommand = tsakCommands;
        return tsakInstance;
    }
    /**
     * returns result e.g. the generated data of the executed command in Object format.
     * 
     * @return data
     */
    public TsakResponse getResult() {
        return tsakInstance.tsakResponse;
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
     * writes the result (generated data of the executed command) to the output file.
     *  
     * @return tsakInstance
     */
    public TwitterSwissArmyKnife write(){
    	FileWriter fw = null;
    	try{
    		BaseCommand bc = getSubCommand(subCommander.getParsedCommand());
    		fw = new FileWriter(new File(bc.getOutputFile()));
    		bc.write(tsakResponse, fw);
    		tsakResponse = null;
    		if(fw != null){
    			fw.close();
    		}
    	}catch(IOException ioex){
    		log.debug(ioex.getMessage());
    	}catch(NullPointerException npex){
    		log.debug(npex.getMessage());
    	}
        return tsakInstance;
    }
    /**
     * executes twitter streaming command.
     * 
     * @throws IOException
     */
    public void executeStreamingCommand() throws IOException {
        CommandStreamStatuses streamStatuses = (CommandStreamStatuses) getSubCommand(parsedCommand);
        (new TwitterStreamingExcecutor()).execute(configurationBuilder,
                streamStatuses);
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
            tsakResponse = baseCommand.execute(twitter);
            if(tsakResponse != null){
            	showRateLimitStatus(tsakResponse.getRemApiLimits());
            }
        } else {
            log.error("User not authorized!");
        }
    }
    /**
     * executes (provided in the argument) command.
     * 
     * @param args
     * @return tsakInstance
     * @throws TwitterException
     * @throws ParameterException
     * @throws IOException
     */
    public TwitterSwissArmyKnife executeCommand(String[] args)
            throws TwitterException, ParameterException, IOException, InstantiationException, IllegalAccessException {
        if (args == null) {
        	log.debug("Need help?? run > tsak <commandName> --help");
            return tsakInstance;
        }
        rootCommander = new JCommander();
        rootCommander.addCommand("tsak", tsakCommand);
        subCommander = rootCommander.getCommands().get("tsak");
        activateSubCommands();
        rootCommander.parse(args);
        parsedCommand = subCommander.getParsedCommand();
        BaseCommand baseCommand = getSubCommand(parsedCommand);
       if(baseCommand.needHelp()){
        	subCommander.usage(parsedCommand);
        	return tsakInstance;
        }
        setConfigurationBuilder();
        if (parsedCommand.equals("streamStatuses")) {
            executeStreamingCommand();
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
     * sets twitter configuration builder.
     * 
     * @throws IOException
     */
    private void setConfigurationBuilder() throws IOException {
        if (isAuthorized()) {
            return;
        }
        if (!setCredentials()) {
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
     * sets/verifies provided twitter credentials and returns True on Success and False on Failure.
     * 
     * @return boolean
     * @throws IOException
     */
    private boolean setCredentials() throws IOException {
        if (!rootCommander.getParsedCommand().equals("tsak")) {
            log.info("Invalid Command: " + rootCommander.getParsedCommand());
            return false;
        }
        if (tsakCommand.getConsumerKey() == null
                || tsakCommand.getConsumerSecret() == null
                || tsakCommand.getAccessToken() == null
                || tsakCommand.getAccessSecret() == null) {
            String env_var = System.getenv("TSAK_CONF");
            if (env_var == null || env_var.isEmpty()) {
                log.error("Environment variable not set. TSAK_CONF {}");
                return false;
            }
            File propConfFile = new File(env_var + File.separator
                    + "tsak.properties");
            if (!propConfFile.exists()) {
                log.error("tsak.properties file does not exist in: " + env_var);
                return false;
            }
            Properties prop = new Properties();
            InputStream propInstream = new FileInputStream(propConfFile);
            prop.load(propInstream);
            propInstream.close();
            tsakCommand.setConsumerKey(prop.getProperty("consumerKey").trim());
            tsakCommand.setConsumerSecret(prop.getProperty("consumerSecret")
                    .trim());
            tsakCommand.setAccessToken(prop.getProperty("accessToken").trim());
            tsakCommand
                    .setAccessSecret(prop.getProperty("accessSecret").trim());
        }
        return true;
    }
    /**
     * activates/prepares all of the commands for execution.
     * 
     */
    public void activateSubCommands() throws InstantiationException, IllegalAccessException{
    	Reflections reflections = new Reflections(COMMANDS_PACKAGE);
    	Set<Class<? extends BaseCommand>> tsakCommandSet = reflections.getSubTypesOf(BaseCommand.class);
    	for (Class<?> commandClazz : tsakCommandSet) {
    		this.subCommander.addCommand(commandClazz.newInstance());
    	}
    }
    /**
     * returns parsedCommand e.g. the provided command.
     * 
     * @param parsedCommand
     * @return subCommand
     */
    public BaseCommand getSubCommand(String parsedCommand) {
        return (BaseCommand) subCommander.getCommands().get(parsedCommand).getObjects().get(0);
    }
    public void showRateLimitStatus(int remApiLimits) {
        log.info("---------------------------------------------------");
        log.info("REMAINING TWITTER API CALLS: [" + remApiLimits + "]");
        log.info("---------------------------------------------------");
    }
}
