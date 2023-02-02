package net.thumbtack.school.library.dao;

import net.thumbtack.school.library.daoimpl.BooksDaoImpl;
import net.thumbtack.school.library.database.DataBase;
import net.thumbtack.school.library.model.Book;
import net.thumbtack.school.library.model.Employee;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;

import java.util.List;
import java.util.Set;

public class BooksDao implements BooksDaoImpl {
    public void insertBook(Book book)
    {
        DataBase.getDataBase().insertBook(book);
    }
    public void insertBookList (List bookList)
    {
        DataBase.getDataBase().insertBookList(bookList);
    }

    public Set<Book> allBooks()
    {
        return DataBase.getDataBase().allBooks();
    }


    public void reserveThisBookForThisEmployeeAtThisDays(Employee employee, int id, int days) throws ResponseCodeException {
        DataBase.getDataBase().reserveBookForThisDays(employee, id, days);
    }

    public Set<Book> allBooksBranch(String branch)
    {
        return DataBase.getDataBase().allBooksBranch(branch);
    }
    public Set<Book> allBooksAuthor(String author)
    {
        return DataBase.getDataBase().allBooksAuthor(author);
    }
    public Set<Book> allBooksBranchesList(List<String> branchesList)
    {
        return DataBase.getDataBase().allBooksBranchesFromList(branchesList);
    }
    public Set<Book> allBooksAuthorsList(List<String> authorsList)
    {
        return DataBase.getDataBase().allBooksAuthorsFromList(authorsList);
    }
    public Set<Book> allBooksWithName(String nameOfBook)
    {
        return DataBase.getDataBase().allBooksWithName(nameOfBook);
    }
    public Book SearchBook(int id)
    {
        return DataBase.getDataBase().searchBook(id);
    }
    public DataBase getDataBase()
    {
        return DataBase.getDataBase();
    }
}
