package net.thumbtack.school.library.database;

import net.thumbtack.school.library.model.Book;
import net.thumbtack.school.library.model.Employee;
import net.thumbtack.school.library.serverresponse.ResponseCodeError;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.util.*;

public class DataBase {
    private static DataBase dataBase;
    private Map<String, Employee> employeeByLogin = new HashMap<String, Employee>();
    private Map<Integer, Book> bookById = new HashMap<>();
    private MultiValuedMap<String, Book> bookByName = new HashSetValuedHashMap<>();
    private MultiValuedMap<String, Book> bookByAuthor = new HashSetValuedHashMap<>();
    private MultiValuedMap<String, Book> bookByBranch = new HashSetValuedHashMap<>();
    private BidiMap<String, Employee> employeeByToken = new DualHashBidiMap<>();
    private int id = 1;

    public static synchronized DataBase getDataBase() {
        if (dataBase == null) {
            dataBase = new DataBase();
        }
        return dataBase;
    }

    public void insertEmployee(Employee employee) throws ResponseCodeException {
        if ( employeeByLogin.putIfAbsent(employee.getLogin(), employee) != null )
            throw new ResponseCodeException( ResponseCodeError.LOGIN_ALREADY_USED, employee.getLogin() );
        employeeByLogin.putIfAbsent(employee.getLogin(), employee);
    }
    public Employee getEmployee(String login) throws ResponseCodeException {
        if ( employeeByLogin.get(login) == null )
            throw new ResponseCodeException( ResponseCodeError.LOGIN_ERROR, login );
        return employeeByLogin.get(login);
    }

    public void tokenEmployeeInsert(String token, Employee employee) throws ResponseCodeException {
        if ( employeeByToken.inverseBidiMap().putIfAbsent(employee, token) != null )
            throw new ResponseCodeException(ResponseCodeError.LOGIN_ALREADY_AUTHORIZED, employee.getLogin());
        else employeeByToken.putIfAbsent(token, employee);
    }

    public Employee getEmployeeByToken(String token) throws ResponseCodeException {
        if (employeeByToken.get(token) == null) throw new ResponseCodeException(ResponseCodeError.ACCESS_ERROR);
        return employeeByToken.get(token);
    }
    public void removeToken(String token) {
        employeeByToken.remove(token);
    }
    //
    public boolean hasToken(String token) {
        return employeeByToken.containsKey(token);
    }

    public void insertBook(Book book)
    {
        // замените там Integer на int
        // а проверка эта не нужна вообще
        // мы же тут вставляем книгу, поэтому ее id ГАРАНТИРОВАННО 0
        book.setId(id);
        id++;
        bookById.put(book.getId(), book);
        bookByName.put(book.getName(), book);
        for (String author:book.getAuthorList())
        {
            bookByAuthor.put(author, book);
        }
        for (String branch:book.getBranchList())
        {
            bookByBranch.put(branch, book);
        }
    }

    public void insertBookList(List<Book> bookList)
    {
        for (Book book : bookList)
        {
            insertBook(book);
        }
    }

    public void reserveBookForThisDays(Employee reader, int id, int days) throws ResponseCodeException {
        //Book book = new Book();
        // мешанина из if-else
        // попробуйте привести к следующему виду
        // if(ошибка1) { throw.. }
        // здесь нет ошибки1
        // if(ошибка2) { throw.. }
        // здесь нет ошибки1 и ошибки 2
        // а теперь делаем что нужно
        // т.н. плоская модель
        // кстати, containsKey не нужен, get сама скажет
        if (bookById.get(id)==null)
        {
            throw new ResponseCodeException(ResponseCodeError.BOOK_NOT_FOUND);
        }

        Book book = bookById.get(id);
        if (book.getReservedForThisDays()>0)
        {
            throw new ResponseCodeException(ResponseCodeError.BOOK_RESERVED, String.valueOf(book.getReservedForThisDays()));
        }
        book.setReservedForThisDays(days);
        book.setReader(reader);
        // хм. Книга уже была вставлена и вставлять ее заново не нужно
        // надо reserveBook(book)
        // и reserveBook не вставляет, а лишь модифицирует уже вставленный Book
        //insertBook(book);

        Book bookPrev = bookById.get(id);
        reserveBook(bookPrev, book);
    }

    public void reserveBook(Book book1, Book book2)
    {
        for (Book book : bookById.values())
        {
            if (book.equals(book1))
            {
                book = book2;
            }
        }
        for (Book book : bookByAuthor.values())
        {
            if (book.equals(book1))
            {
                book = book2;
            }
        }
        for (Book book : bookByBranch.values())
        {
            if (book.equals(book1))
            {
                book = book2;
            }
        }
        for (Book book : bookByName.values())
        {
            if (book.equals(book1))
            {
                book = book2;
            }
        }
    }

    public Set<Book> allBooks()
    {
        return new HashSet<>(bookByName.values());
    }


    public Set<Book> allBooksWithName(String nameOfBook)
    {
        return new HashSet<>(bookByName.get(nameOfBook));
    }
    public Set<Book> allBooksBranch(String branch)
    {
        return new HashSet<>(bookByBranch.get(branch));
    }
    public Set<Book> allBooksAuthor(String author)
    {
        return new HashSet<>(bookByAuthor.get(author));
    }

    public Set<Book> allBooksBranchesFromList(List<String> branches)
    {
        Set<Book> bookSet = new HashSet<>();
        for (String branch : branches)
            bookSet.addAll(bookByBranch.get(branch));
        return bookSet;
    }

    public Set<Book> allBooksAuthorsFromList(List<String> authors)
    {
        Set<Book> bookSet = new HashSet<>();
        for (String author : authors)
            bookSet.addAll(bookByAuthor.get(author));
        return bookSet;
    }

    public Book searchBook(int id)
    {
        return (Book) bookById.get(id);
    }
}


