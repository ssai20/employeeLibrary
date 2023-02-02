package net.thumbtack.school.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.thumbtack.school.library.dto.request.AddBookDtoRequest;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter

public class BooksDtoResponse implements Serializable {
    AddBookDtoRequest book;

    Set<AddBookDtoRequest> booksSet = new HashSet<>();
    public BooksDtoResponse(Set<AddBookDtoRequest> booksSet)
    {
        this.booksSet = booksSet;
    }

    public void setBooks(AddBookDtoRequest books) {
        this.booksSet.add(books);
    }
    public Set<AddBookDtoRequest> getBooksSet() {
        return booksSet;
    }

}
