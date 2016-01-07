package org.projectspinoza.twitterswissarmyknife.writer;

/**
 * DataWriter
 * writes results of the executed command to the mentioned output text file.
 * 
 * @author org.projectspinoza
 * @version v1.0
 *
 */
public interface DataWriter {
	/**
	 * writes results of the executed command to the mentioned output file.
	 * 
	 * @param twitterResponseData
	 * @param parsedCommand
	 * @param outputFile
	 */
    public void write(Object twitterResponseData, String parsedCommand,
            String outputFile);
}
