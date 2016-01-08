package org.projectspinoza.twitterswissarmyknife;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jline.console.ConsoleReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.TwitterException;

import com.beust.jcommander.ParameterException;

/**
 * Main is the Driver class for the TwitterSwissArmyKnife utility, and contains
 * various usage examples for this utility.
 * 
 * @author org.projectspinoza
 * @version v1.0.0
 * 
 */
public class Main {
    private static Logger log = LogManager.getRootLogger();
    private static ConsoleReader consoleReader;

    /**
     * contains the logic for executing commands.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        log.info("Initializing TSAK");

        /**
         * TSAK USAGE
         * 
         * 1. USAGE (NORMAL|SIMPLE) 
         * =======================================
         * TwitterSwissArmyKnife tsak = TwitterSwissArmyKnife.getInstance();
         * tsak.executeCommand(args).write();
         */

        /**
         * TSAK USAGE
         * 
         * 2. Using TSAK as a shell
         * ===================================
         * 
         */
        consoleReader = new ConsoleReader();
        TwitterSwissArmyKnife tsak = TwitterSwissArmyKnife.getInstance();
        while (true) {
            try {
                tsak.executeCommand(getCommandLineArguments()).write();
            } catch (ParameterException ex) {
                consoleReader.println(ex.getMessage());
            } catch (TwitterException ex) {
                consoleReader.println(ex.getMessage());
            } catch (IllegalStateException ex) {
                consoleReader.println(ex.getMessage());
            } catch (IOException ex) {
                consoleReader.println(ex.getMessage());
            } finally {
                consoleReader.flush();
            }
        }

        /**
         * 3: USAGE
         * 
         * (Don't want to provide credentials for each command OR want to run
         * multiple commands in a loop?)
         * ===================================================================
         * 
         * String[] followersCommand = {"tsak","dumpFollowerIDs","-uname", "dmkavanagh", "-o", "followersIds.txt"}; 
         * String[] userListsCommand = {"tsak", "dumpUserLists", "-uname", "dmkavanagh", "-o", "userLists.txt"}; 
         * String[] userSuggestionsCommand = {"tsak", "dumpUserSuggestions", "-slug", "Sports", "-o", "userSuggestions.txt"};
         * 
         * List<String[]> commands = new ArrayList<String[]>();
         * 
         * commands.add(followersCommand); 
         * commands.add(userListsCommand);
         * commands.add(userSuggestionsCommand);
         * 
         * TwitterSwissArmyKnife tsak = TwitterSwissArmyKnife.getInstance();
         * for(String[] command : commands){
         * tsak.executeCommand(command).write(); }
         * 
         */

        /**
         * 4: USAGE (Do you want to get/analyze the original response from twitter API ?)
         * ==============================================================================
         * 
         * TwitterSwissArmyKnife tsak = TwitterSwissArmyKnife.getInstance();
         * 
         * i. get the result only 
         * -------------------------------- 
         * TsakResponse result = tsak.executeCommand(args).getResult();
         * 
         * ii. write and then get the result
         * --------------------------------
         * TsakResponse result = tsak.executeCommand(args).write().getResult();
         * 
         */

        /**
         * 5: USAGE (much more) 
         * ====================
         * 
         * for more detailed usage study the API. (TwitterSwissArmyKnife)
         * 
         */
    }

    /**
     * reads command from the console, and returns it after parsing.
     * 
     * @return String[] parsed command
     * 
     * @throws IOException
     */
    public static String[] getCommandLineArguments() throws IOException {
        String commandLine = consoleReader.readLine("tsak>");
        if (commandLine.trim().equals("")) {
            return null;
        }
        if (commandLine.trim().equals("exit")) {
            consoleReader.println("Good Bye...");
            consoleReader.flush();
            System.exit(0);
        }
        List<String> cmdArgs = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(commandLine);
        while (regexMatcher.find()) {
            cmdArgs.add(regexMatcher.group());
        }
        return cmdArgs.toArray(new String[cmdArgs.size()]);
    }
}