package net.thumbtack.school.library.dto.request;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddBookDtoRequest implements Serializable {
    public String book;
    public List<String> authorList = new ArrayList<>();
    public List<String> branchList = new ArrayList<>();
    public Integer id;
}
