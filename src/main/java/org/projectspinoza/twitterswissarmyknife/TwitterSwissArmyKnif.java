package org.projectspinoza.twitterswissarmyknife;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectspinoza.twitterswissarmyknife.command.BaseCommand;
import org.projectspinoza.twitterswissarmyknife.command.CommandStreamStatuses;
import org.projectspinoza.twitterswissarmyknife.command.TsakCommand;
import org.projectspinoza.twitterswissarmyknife.util.TsakResponse;
import org.reflections.Reflections;

import com.beust.jcommander.JCommander;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSwissArmyKnif{
    private static Logger log = LogManager.getRootLogger();
    private static final String TSAK_CORE_COMMANDS_PACKAGE = "org.projectspinoza.twitterswissarmyknife.command";
    
    private JCommander rootCommander;
    private TsakResponse tsakResponse;
    private Twitter twitter;
    
    private boolean authorized;
    private List<Class<?>> registeredCommands = new ArrayList<Class<?>>();
    
    public TwitterSwissArmyKnif(){
        
    }
    
    public TwitterSwissArmyKnif(JCommander rootCommander){
        this.rootCommander = rootCommander;
        registerCommands(TSAK_CORE_COMMANDS_PACKAGE);
    }
    /**
     * returns true if the user has been authorized false else where
     * 
     * @return boolean
     */
    public boolean isAuthorized() {
        return authorized;
    }
    
    /**
     * authorizes user with the provided credentials.
     * 
     * @throws TwitterException
     */
    public void authorizeUser() throws TwitterException, IOException {
        if (!setCredentials()) {
            log.error("Credentials not provided!");
            authorized = false;
            return;
        }
        if(twitter == null){
            TsakCommand commandTsak = getTsakCommand();
            twitter = new TwitterFactory(
                (new ConfigurationBuilder()
                    .setDebugEnabled(true)
                    .setOAuthConsumerKey(commandTsak.getConsumerKey())
                    .setOAuthConsumerSecret(commandTsak.getConsumerSecret())
                    .setOAuthAccessToken(commandTsak.getAccessToken())
                    .setOAuthAccessTokenSecret(commandTsak.getAccessSecret())).build()).getInstance();
        }
        twitter.verifyCredentials();
        authorized = true;
    }
    
    /**
     * executes the provided command
     * 
     * @param args
     * @return TwitterSwissArmyKnife
     * @throws Exception
     */
    public TwitterSwissArmyKnif executeCommand(String[] args) throws Exception {
        tsakResponse = null;
        if (args == null) {
            log.info("Need help?? run > tsak <commandName> --help");
            return this;
        }
        activateCommands();
        rootCommander.parse(args);
        BaseCommand baseCommand = getActiveCommand();
        
        if (baseCommand.needHelp()) {
            JCommander tsakCommander = rootCommander.getCommands().get("tsak");  
            tsakCommander.usage(tsakCommander.getParsedCommand());
            return this;
        }
        if (!isAuthorized()) {
            authorizeUser();
        }
        tsakResponse = baseCommand.execute(getTwitterInstance());
        
        if(!(tsakResponse == null || baseCommand instanceof CommandStreamStatuses)){
            showRateLimitStatus(tsakResponse);
        }
        
        return this;
    }
    /**
     * writes the result (generated data of the executed command) to the output
     * file.
     * 
     * @return TwitterSwissArmyKnife
     */
    public TwitterSwissArmyKnif write() {
        if (tsakResponse == null) {
            return this;
        }
        BufferedWriter bufferedWriter = null;
        try {
            BaseCommand baseCommand = getActiveCommand();
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
        return this;
    }
    
    /**
     * sets/verifies provided twitter credentials and returns True on Success
     * and False on Failure.
     * 
     * @return boolean
     * @throws IOException
     * @throws NullPointerException
     */
    private boolean setCredentials() throws IOException, NullPointerException{
        
        if (!rootCommander.getParsedCommand().equals("tsak")) {
            log.info("Invalid Command: " + rootCommander.getParsedCommand());
            return false;
        }
        TsakCommand commandTsak = getTsakCommand();
        if (getTsakCommand().getConsumerKey() == null || commandTsak.getConsumerSecret() == null || commandTsak.getAccessToken() == null || commandTsak.getAccessSecret() == null) {
            String env = System.getenv("TSAK_CONF");
            if (env == null || env.isEmpty()) {
                log.error("Environment variable not set. TSAK_CONF {}", env);
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
            String consumerKey = prop.getProperty("consumerKey");
            String consumerSecret = prop.getProperty("consumerSecret");
            String accessToken = prop.getProperty("accessToken");
            String accessSecret = prop.getProperty("accessSecret");
            if(consumerKey == null || consumerSecret == null || accessToken == null || accessSecret == null){
                log.error("some or all keys are missing!");
                return false;
            } 
            commandTsak.setConsumerKey(consumerKey.trim());
            commandTsak.setConsumerSecret(consumerSecret.trim());
            commandTsak.setAccessToken(accessToken.trim());
            commandTsak.setAccessSecret(accessSecret.trim());
        }
        return true;
    }
    
    /**
     * register all command classes in the given package, e.g. the classes which extends BaseCommand
     * 
     * @param packageName
     */
    public void registerCommands(String packageName){
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends BaseCommand>> tsakCommandSet = reflections.getSubTypesOf(BaseCommand.class);
        for (Class<?> clazz : tsakCommandSet) {
            registerCommand(clazz);
        }
    }
    
    /**
     * register the given class as a command class
     * 
     * @param clazz
     */
    public void registerCommand(Class<?> clazz){
        if(BaseCommand.class.isAssignableFrom(clazz) && !registeredCommands.contains(clazz)){
            registeredCommands.add(clazz);
        }else{
            log.error("Cannot register {}, either already registered or un-assignable to BaseCommand", clazz);
        }
    }
    
    /**
     * activates all registered commands
     * 
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void activateCommands() throws InstantiationException, IllegalAccessException{
        for(Class<?> clazz : registeredCommands){
            rootCommander.getCommands().get("tsak").addCommand(clazz.newInstance());
        }
    }
    /**
     * returns all registered commands
     * 
     * @return registeredCommands
     */
    public List<Class<?>> getRegisteredCommands(){
        return Collections.unmodifiableList(registeredCommands);
    }
    
    /**
     * returns Twitter instance
     * 
     * @return Twitter
     */
    public Twitter getTwitterInstance(){
        return twitter;
    }
    /**
     * set twitter instance
     * 
     * @param twitter
     */
    public void setTwitter(Twitter twitter){
        this.twitter = twitter;
    }
    
    /**
     * returns result e.g. the generated response of the executed command.
     * 
     * @return TsakResponse
     */
    public TsakResponse getResult(){
        return tsakResponse;
    }
    
    /**
     * returns rootCommander.
     * 
     * @return JCommander
     */
    public JCommander getRootCommander(){
        return rootCommander;
    }
    
    /**
     * sets rootCommander
     * 
     * @param rootCommander
     */
    public void setRootCommander(JCommander rootCommander){
        this.rootCommander = rootCommander;
        registerCommands(TSAK_CORE_COMMANDS_PACKAGE);
    }
    
    /**
     * returns the instance of the provided command
     * 
     * @param String parsedCommand
     * @return BaseCommand
     */
    public BaseCommand getActiveCommand() {
        String parsedCommand = rootCommander.getCommands().get("tsak").getParsedCommand();
        return (BaseCommand) rootCommander.getCommands().get("tsak").getCommands().get(parsedCommand).getObjects().get(0);
    }
    
    /**
     * returns the instance of TsakCommand
     * 
     * @param parsedCommand
     * @return TsakCommand
     */
    public TsakCommand getTsakCommand() {
        return (TsakCommand) rootCommander.getCommands().get("tsak").getObjects().get(0);
    }
    
    /**
     * prints RateLimitStatus for specific command.
     * 
     * @param remApiLimits
     */
    public void showRateLimitStatus(TsakResponse tsakResponse) {
        log.info("---------------------------------------------------");
        log.info("DONE!!! REMAINING TWITTER API CALLS: [" + tsakResponse.getRemApiLimits() + "]");
        log.info("---------------------------------------------------");
    }
}
