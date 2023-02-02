package net.thumbtack.school.library.dto.request;

import lombok.Data;

@Data
public class LoginDtoRequest {
    String login;
    String password;
    public LoginDtoRequest(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
}
