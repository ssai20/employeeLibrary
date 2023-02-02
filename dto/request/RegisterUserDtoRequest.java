package net.thumbtack.school.library.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class RegisterUserDtoRequest{
    private String employeeSecondName;
    private String employeeFirstName;
    private String login;
    private String password;


}
