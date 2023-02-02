package net.thumbtack.school.library.serverresponse;

public class ServerResponse {
    private int responseCode;
    private String responseData;

    public ServerResponse(int responseCode, String responseData) throws ResponseCodeException
    {
        this.responseCode = responseCode;
        this.responseData = responseData;
    }

    public int getResponseCode()
    {
        return responseCode;
    }
    public String getResponseData()
    {
        return responseData;
    }

    public void setResponseCode(int responseCode, String responseData) throws ResponseCodeException
    {
        this.responseCode = responseCode;
        this.responseData = responseData;
    }

}
