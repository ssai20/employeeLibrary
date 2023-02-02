package net.thumbtack.school.library.mappers;

import net.thumbtack.school.library.dto.request.AddBookDtoRequest;
import net.thumbtack.school.library.dto.request.RegisterUserDtoRequest;
import net.thumbtack.school.library.model.Book;
import net.thumbtack.school.library.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    Employee dtoToEmployee(RegisterUserDtoRequest registerUserDtoRequest);

    @Mapping(target = "name", source = "book")
    Book dtoToBook(AddBookDtoRequest listOfBooksDtoRequest);

    @Mapping(target = "book", source = "name")
    AddBookDtoRequest bookToListOfBooksDtoRequest(Book book);

}
