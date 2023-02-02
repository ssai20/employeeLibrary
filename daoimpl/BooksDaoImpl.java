package net.thumbtack.school.library.daoimpl;

import net.thumbtack.school.library.model.Book;
import net.thumbtack.school.library.model.Employee;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BooksDaoImpl {
    public void insertBook(Book book);
    public Set<Book> allBooks();
    public void reserveThisBookForThisEmployeeAtThisDays(Employee employee, int id, int days) throws ResponseCodeException;
    public Set<Book> allBooksBranch(String branch);
    public Set<Book> allBooksAuthor(String author);
    public Set<Book> allBooksBranchesList(List<String> branchesList);
    public Set<Book> allBooksAuthorsList(List<String> authorsList);
    public Set<Book> allBooksWithName(String nameOfBook);
    public Book SearchBook(int id);
}
