package net.thumbtack.school.library.dto.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

public class AddBookListDtoRequest {
    List<AddBookDtoRequest> addBookListDtoRequests = new ArrayList<>();

    public AddBookListDtoRequest(AddBookDtoRequest addBookDtoRequest)
    {
        this.addBookListDtoRequests.add(addBookDtoRequest);
    }
    public void setAddBookListDtoRequests(AddBookDtoRequest listOfBooksDtoRequest) {
        this.addBookListDtoRequests.add(listOfBooksDtoRequest);
    }
    public Set getSetAddBookListDtoRequests()
    {
        return new HashSet<>(getAddBookListDtoRequests());
    }
}
