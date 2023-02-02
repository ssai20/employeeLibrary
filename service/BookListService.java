package net.thumbtack.school.library.service;

import com.google.gson.Gson;
import net.thumbtack.school.library.dao.BooksDao;
import net.thumbtack.school.library.dao.EmployeeDao;
import net.thumbtack.school.library.dto.request.AddBookDtoRequest;
import net.thumbtack.school.library.dto.request.AddBookListDtoRequest;
import net.thumbtack.school.library.dto.response.ReserveDtoResponse;
import net.thumbtack.school.library.dto.response.*;
import net.thumbtack.school.library.mappers.UserMapper;
import net.thumbtack.school.library.model.Book;
import net.thumbtack.school.library.model.Employee;
import net.thumbtack.school.library.serverresponse.ResponseCodeError;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;
import net.thumbtack.school.library.serverresponse.ServerResponse;


import java.io.*;
import java.util.*;


public class BookListService {
    private static BooksDao booksDao = new BooksDao();
    private static EmployeeDao employeeDao = new EmployeeDao();
    private static final int SUCCESS_CODE = 200, ERROR_CODE = 400;
    private Gson gson = new Gson();

    public ServerResponse addBook(String jsonToken, String jsonRequest) throws ResponseCodeException {
        try {
            AddBookDtoRequest addBookDtoRequest = gson.fromJson(jsonRequest, AddBookDtoRequest.class);

            //  лишнее
            // чуть ниже у Вас Employee owner = booksDao.getEmployeeByToken( convertTokenToString(jsonToken) );
            // вот это переставить сюда и проверить результат на null
            // это и будет проверкой токена
            // здесь и везде
            Employee owner = employeeDao.getEmployeeByToken( convertTokenToString(jsonToken) );
            //validateToken(convertTokenToString(jsonToken));
            addBookValidate(addBookDtoRequest);
            Book book = UserMapper.INSTANCE.dtoToBook(addBookDtoRequest);
            // не booksDao это дело
            // перенесите getEmployeeByToken в EmployeeDao
            // сервис вполне может использовать несколько DAO
//            Employee owner = employeeDao.getEmployeeByToken( convertTokenToString(jsonToken) );
            book.setOwner(owner);
            booksDao.insertBook(book);
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(new EmptySuccessResponse()) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse addBookList2(String jsonToken, String jsonRequest) throws ResponseCodeException
    {
        try {
            AddBookListDtoRequest addBookListDtoRequest = gson.fromJson(jsonRequest,AddBookListDtoRequest.class);
            if (addBookListDtoRequest == null) throw new ResponseCodeException(ResponseCodeError.EMPTY_BOOK_LIST);
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(jsonToken, LoginDtoResponse.class).getToken());
            for (AddBookDtoRequest addBookDtoRequest:addBookListDtoRequest.getAddBookListDtoRequests())
            {
                Book book = UserMapper.INSTANCE.dtoToBook(addBookDtoRequest);
                booksDao.insertBook(book);
            }
            ServerResponse serverResponse = new ServerResponse(SUCCESS_CODE, gson.toJson(new EmptySuccessResponse()));
            return serverResponse;
        }
        catch (ResponseCodeException ex)
        {
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }


    public String convertTokenToString(String jsonToken) throws ResponseCodeException {
        Gson gson = new Gson();
        if ( (jsonToken == null) || (jsonToken.equals("") || (gson.fromJson(jsonToken, LoginDtoResponse.class).getToken() == null) || gson.fromJson(jsonToken, LoginDtoResponse.class).getToken().equals("")) )
            throw new ResponseCodeException();
        return gson.fromJson(jsonToken, LoginDtoResponse.class).getToken();
    }

    public ServerResponse reserveBook(String jsonToken, String jsonBookAndDays) throws ResponseCodeException {
        try {
            Employee employee = employeeDao.getEmployeeByToken(gson.fromJson(jsonToken, LoginDtoResponse.class).getToken());
            ReserveDtoResponse reserveDtoResponse = gson.fromJson(jsonBookAndDays, ReserveDtoResponse.class);
            booksDao.reserveThisBookForThisEmployeeAtThisDays(employee, reserveDtoResponse.getId(), reserveDtoResponse.getDays());
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(new EmptySuccessResponse()) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse allBooks(String jSonToken) throws ResponseCodeException {
        try{
            BooksDtoResponse booksSet = new BooksDtoResponse(new HashSet<>());
            //validateToken(convertTokenToString(jSonToken));
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            for (Book b: booksDao.allBooks())
            {
                AddBookDtoRequest addBookDtoRequest = UserMapper.INSTANCE.bookToListOfBooksDtoRequest(b);
                booksSet.setBooks(addBookDtoRequest);
            }
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(booksSet) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse allBooksBranch(String jSonToken, String jSonBranch) throws ResponseCodeException {
        try {
            BooksDtoResponse booksSet = new BooksDtoResponse(new HashSet<>());
            //validateToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            BranchesDtoResponse branchesDtoResponse = gson.fromJson(jSonBranch, BranchesDtoResponse.class);
            String branch = branchesDtoResponse.getBranch();
            for (Book b : booksDao.allBooksBranch(branch)) {
                AddBookDtoRequest addBookDtoRequest = UserMapper.INSTANCE.bookToListOfBooksDtoRequest(b);
                booksSet.setBooks(addBookDtoRequest);
            }
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(booksSet) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse allBooksAuthor(String jSonToken, String jSonAuthor) throws ResponseCodeException {
        try {
            BooksDtoResponse booksSet = new BooksDtoResponse(new HashSet<>());
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            AuthorDtoResponse authorDtoResponse = gson.fromJson(jSonAuthor, AuthorDtoResponse.class);
            String author = authorDtoResponse.getAuthor();
            for (Book b : booksDao.allBooksAuthor(author)) {
                AddBookDtoRequest addBookDtoRequest = UserMapper.INSTANCE.bookToListOfBooksDtoRequest(b);
                booksSet.setBooks(addBookDtoRequest);
            }
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(booksSet) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse allBooksNameOfBook(String jSonToken, String jSonNameOfBook) throws ResponseCodeException {
        try {
            BooksDtoResponse booksSet = new BooksDtoResponse(new HashSet<>());
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            BookNameDtoResponse nameOfBookDtoResponse = gson.fromJson(jSonNameOfBook, BookNameDtoResponse.class);
            String nameOfBook = nameOfBookDtoResponse.getBookName();
            for (Book b : booksDao.allBooksWithName(nameOfBook)) {
                AddBookDtoRequest addBookDtoRequest = UserMapper.INSTANCE.bookToListOfBooksDtoRequest(b);
                booksSet.setBooks(addBookDtoRequest);
            }
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(booksSet) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse allBooksListOfBranches (String jSonToken, String jSonBranch) throws ResponseCodeException {
        try {
            BooksDtoResponse booksSet = new BooksDtoResponse(new HashSet<>());
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            BranchesListDtoResponse branchesListDtoResponse = gson.fromJson(jSonBranch, BranchesListDtoResponse.class);
            List<String> branchesList = new ArrayList<>(branchesListDtoResponse.getBranchesListDtoResponse());
            for (Book b : booksDao.allBooksBranchesList(branchesList)) {
                AddBookDtoRequest addBookDtoRequest = UserMapper.INSTANCE.bookToListOfBooksDtoRequest(b);
                booksSet.setBooks(addBookDtoRequest);
            }
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(booksSet) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse allBooksAuthorsList (String jSonToken, String jSonAuthorsList) throws ResponseCodeException {
        try {
            BooksDtoResponse booksSet = new BooksDtoResponse(new HashSet<>());
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            //validateToken(gson.fromJson(jSonToken, LoginDtoResponse.class).getToken());
            AuthorsListDtoResponse authorsListDtoResponse = gson.fromJson(jSonAuthorsList, AuthorsListDtoResponse.class);
            List<String> authorsList = new ArrayList<>(authorsListDtoResponse.getAuthorsListDtoResponse());
            for (Book b : booksDao.allBooksAuthorsList(authorsList)) {
                AddBookDtoRequest addBookDtoRequest = UserMapper.INSTANCE.bookToListOfBooksDtoRequest(b);
                booksSet.setBooks(addBookDtoRequest);
            }
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(booksSet) );
            return serverResponse;
        } catch(ResponseCodeException ex) {
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    private ServerResponse addBookValidate(AddBookDtoRequest addBookDtoRequest) throws ResponseCodeException
    {
        try {
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(new EmptySuccessResponse()) );
            if ((addBookDtoRequest.getBook() == null) || (addBookDtoRequest.getAuthorList() == null) || (addBookDtoRequest == null)) {
                serverResponse = new ServerResponse( ERROR_CODE, gson.toJson("error") );
                throw new ResponseCodeException(ResponseCodeError.EMPTY_FIELD);
            } else serverResponse.setResponseCode(SUCCESS_CODE, gson.toJson(new EmptySuccessResponse()));
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

//    public void validateToken(String token) throws ResponseCodeException {
//        if (!hasToken(token))
//        {
//            throw new ResponseCodeException(ResponseCodeError.ACCESS_ERROR);
//        }
//    }
    //  не нужен
    // достаточно метода getEmployeeByToken
    // он Вам и Employee вернет, и на вопрос hasToken ответит (если вернет null, значит такого токена нет)
//    public static boolean hasToken(String token)
//    {
//        return booksDao.hasToken(token);
//    }
    public ServerResponse writeDataBaseToFile(String tokenJson, String jsonFile) throws ResponseCodeException
    {
        try {
            File file = gson.fromJson(jsonFile, File.class);
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(new EmptySuccessResponse()) );
            Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(tokenJson, LoginDtoResponse.class).getToken());
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(booksDao.getDataBase());
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverResponse.setResponseCode(SUCCESS_CODE, gson.toJson(file));
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }
//ObjectOutputStream.writeObject(database)
//    public PoolDtoResponse writeDataBaseToFile(String tokenJson, File file) throws ResponseCodeException, IOException {
//        //PoolDtoResponse poolDtoResponse = new PoolDtoResponse(booksDaoImpl.getEmployeeHashMap(), booksDaoImpl.getBooksHashMap(), booksDaoImpl.getTokenHashMap());
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
//        {
//            oos.writeObject(this.poolDtoResponse);
//        }
//        return  poolDtoResponse;
//
//    }
//        Gson gson = new Gson();
//        PoolDtoResponse poolDtoResponse = new PoolDtoResponse(booksDaoImpl.getEmployeeHashMap(), booksDaoImpl.getBooksHashMap(), booksDaoImpl.getTokenHashMap());
//        ServerResponse serverResponse = validateToken(convertTokenToString(tokenJson));
//        //File file = gson.fromJson(fileJson, File.class);
//
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file)))
//        {
//            Gson gsonFile = new Gson();
//            gsonFile.toJson(poolDtoResponse, bw);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //serverResponse.setResponseCode(200, gson.toJson(file));
        //return serverResponse;
   // }
//public ServerResponse readFromFile(String tokenJson, File file) throws ResponseCodeException, IOException
//Десериализует Trainee из двоичного файла, имя файла задается экземпляром класса File.
// Предполагается, что данные в файл записаны в формате предыдущего упражнения.
//{
//    Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(tokenJson, LoginDtoResponse.class).getToken());
//    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
//    {
//        //booksDao.getDataBase()
//        poolDtoResponse = (PoolDtoResponse) ois.readObject();
//    }
//    catch (ClassNotFoundException e){
//        e.printStackTrace();
//    }
//    return poolDtoResponse;
//}
//    public ServerResponse readFromFile(String tokenJson, File file) throws ResponseCodeException, IOException {
//        Employee owner = employeeDao.getEmployeeByToken(gson.fromJson(tokenJson, LoginDtoResponse.class).getToken());
//        //File file = gson.fromJson(fileJson, File.class);
//        try (BufferedReader br = new BufferedReader(new FileReader(file)))
//        {
//            Gson gson1 = new Gson();
//            return gson1.fromJson(br, PoolDtoResponse.class);
//        }
//    }


}
