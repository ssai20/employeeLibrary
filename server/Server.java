package net.thumbtack.school.library.server;

import net.thumbtack.school.library.serverresponse.ResponseCodeException;
import net.thumbtack.school.library.serverresponse.ServerResponse;
import net.thumbtack.school.library.service.BookListService;
import net.thumbtack.school.library.service.BookListService;
import net.thumbtack.school.library.service.UserService;


public class Server{
    private UserService userService = new UserService();
    private BookListService listOfBooksService = new BookListService();


    public ServerResponse registerEmployee(String json) throws ResponseCodeException {
        return this.userService.registerUser(json);
    }

    public ServerResponse loginEmployee(String json) throws ResponseCodeException {
        return this.userService.loginUser(json);
    }
    public ServerResponse listOfBooks(String bookName, String json) throws ResponseCodeException {
        return this.listOfBooksService.addBook(bookName, json);
    }

    public UserService getUserService() {
        return userService;
    }

    public BookListService getBookListService() {
        return listOfBooksService;
    }
    public ServerResponse logout(String json) throws ResponseCodeException {
        return this.userService.logoutUser(json);
    }
}
