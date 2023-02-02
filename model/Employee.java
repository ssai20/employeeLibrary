package net.thumbtack.school.library.model;

import lombok.*;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Employee {
    private String secondName;
    private String firstName;
    private String login;
    private String password;
    private List<Book> takenBooks = new ArrayList<>();
}
