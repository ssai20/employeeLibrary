package net.thumbtack.school.library.serverresponse;

import lombok.NoArgsConstructor;

@NoArgsConstructor

public class ResponseCodeException extends Exception
{
    private ResponseCodeError responseCodeError;
    public ResponseCodeException (ResponseCodeError responseCodeError)
    {
        this.responseCodeError = responseCodeError;
    }
    public ResponseCodeException (ResponseCodeError responseCodeError, String param)
    {
        super (String.format(responseCodeError.getErrorString(),param));
    }

    public ResponseCodeError getResponseCodeError() {
        return responseCodeError;
    }
}
