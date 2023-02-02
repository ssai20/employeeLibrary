package net.thumbtack.school.library.dto.response;

import net.thumbtack.school.library.serverresponse.ResponseCodeException;

public class ErrorDtoResponse {
    public String error;
    public ErrorDtoResponse(ResponseCodeException ex)
    {
        this.error = ex.getMessage();
    }
}
