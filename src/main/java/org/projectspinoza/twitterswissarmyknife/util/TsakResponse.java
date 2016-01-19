package org.projectspinoza.twitterswissarmyknife.util;

public class TsakResponse {
    private Integer remApiLimits;
    private String commandDetails;
    private Object responseData;

    /**
     * Constructs TsakResponse Instance with default property values e.g. null
     */
    public TsakResponse() {
    }

    /**
     * Constructs TsakResponse instance with Object ResponseData
     * 
     * @param responseData
     */
    public TsakResponse(Object responseData) {
        this(null, responseData);
    }

    /**
     * Constructs TsakResponse Instance with Integer remApiLimits and Object
     * ResponseData
     * 
     * @param remApiLimits
     * @param responseData
     */
    public TsakResponse(Integer remApiLimits, Object responseData) {
        this.remApiLimits = remApiLimits;
        this.responseData = responseData;
    }

    /**
     * Returns no. of remaining Api calls for this command
     * 
     * @return Integer remainingApiCalls
     */
    public Integer getRemApiLimits() {
        return remApiLimits;
    }

    /**
     * Sets Remaining Api Calls for this command
     * 
     * @param remApiLimits
     */
    public void setRemApiLimits(Integer remApiLimits) {
        this.remApiLimits = remApiLimits;
    }

    /**
     * Returns Object ResponseData
     * 
     * @return Object ResponseData
     */
    public Object getResponseData() {
        return responseData;
    }

    /**
     * Sets ResponseData
     * 
     * @param responseData
     */
    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    /**
     * Returns Command Details
     * 
     * @return String CommandDetails
     */
    public String getCommandDetails() {
        return commandDetails;
    }

    /**
     * Sets CommandDetails
     * 
     * @param commandDetails
     */
    public void setCommandDetails(String commandDetails) {
        this.commandDetails = commandDetails;
    }

    @Override
    public String toString() {
        return "TsakResponse [remApiLimits=" + remApiLimits + ", commandDetails=" + commandDetails + ", responseData="
                + responseData + "]";
    }

}
