package net.thumbtack.school.library.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.library.dao.EmployeeDao;
import net.thumbtack.school.library.dto.request.LoginDtoRequest;
import net.thumbtack.school.library.dto.request.RegisterUserDtoRequest;
import net.thumbtack.school.library.dto.request.TokenDtoRequest;
import net.thumbtack.school.library.dto.response.EmptySuccessResponse;
import net.thumbtack.school.library.dto.response.ErrorDtoResponse;
import net.thumbtack.school.library.dto.response.LoginDtoResponse;
import net.thumbtack.school.library.dto.response.RegisterUserDtoResponse;
import net.thumbtack.school.library.mappers.UserMapper;
import net.thumbtack.school.library.model.Employee;
import net.thumbtack.school.library.serverresponse.ResponseCodeError;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;
import net.thumbtack.school.library.serverresponse.ServerResponse;


import java.lang.reflect.Type;
import java.util.UUID;

public class UserService extends Exception {
    private static final int MIN_PASSWORD_LEN = 8;
    private static final int SUCCESS_CODE = 200, ERROR_CODE = 400;
    private final EmployeeDao employeeDao = new EmployeeDao();
    private final Gson gson = new Gson();

    public ServerResponse registerUser(String jsonRegisterUserString) throws ResponseCodeException
    {
        try {
            RegisterUserDtoRequest registerUserDtoRequest = getClassFromJson(jsonRegisterUserString, RegisterUserDtoRequest.class);
            validateRegisterUser(registerUserDtoRequest);
            Employee employee = UserMapper.INSTANCE.dtoToEmployee(registerUserDtoRequest);
            employeeDao.insert(employee);
            String token = UUID.randomUUID().toString();
            employeeDao.insertToken(token, employee);
            RegisterUserDtoResponse registerUserDtoResponse = new RegisterUserDtoResponse(token);
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(registerUserDtoResponse) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
            return serverResponse;
        }
    }

    public ServerResponse loginUser(String jsonLoginString) throws ResponseCodeException {
        try {
            LoginDtoRequest loginDtoRequest = gson.fromJson(jsonLoginString, LoginDtoRequest.class);
            validateLogin(loginDtoRequest);
            String token = UUID.randomUUID().toString();
            employeeDao.insertToken(token, employeeDao.getEmployeeDao(loginDtoRequest.getLogin()));
            LoginDtoResponse loginDtoResponse = new LoginDtoResponse(token);
            ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(loginDtoResponse) );
            return serverResponse;
        } catch (ResponseCodeException ex){
            return new ServerResponse( ERROR_CODE, gson.toJson(new ErrorDtoResponse(ex)) );
        }
    }
    public ServerResponse logoutUser(String jsonToken) throws ResponseCodeException {
        String token = gson.fromJson(jsonToken, TokenDtoRequest.class).getToken();
        employeeDao.remove(token);
        ServerResponse serverResponse = new ServerResponse( SUCCESS_CODE, gson.toJson(new EmptySuccessResponse()) );
        return serverResponse;
    }

    private static void validateRegisterUser(RegisterUserDtoRequest registerUser) throws ResponseCodeException {
        if ( (registerUser.getEmployeeSecondName()==null)||(registerUser.getEmployeeFirstName()==null)||(registerUser.getLogin()==null)||(registerUser.getPassword()==null) )
        {
            throw new ResponseCodeException(ResponseCodeError.EMPTY_FIELD);
        }
        if (registerUser.getPassword().length() < MIN_PASSWORD_LEN)
        {
            throw new ResponseCodeException(ResponseCodeError.MIN_PASSWORD);
        }
        if (!hadSpecialSymbolOrHighRegister(registerUser.getPassword()))
        {
            throw new ResponseCodeException(ResponseCodeError.HIGH_REGISTER_ERROR);
        }
    }

    public static boolean hadSpecialSymbolOrHighRegister(String word)
    {
        for (char i : word.toCharArray())
        {
            if (Character.isUpperCase(i)) return true ;
        }
        return false;
    }
    private void validateLogin(LoginDtoRequest loginUser) throws ResponseCodeException {
        if ( (loginUser.getLogin()==null) || (loginUser.getPassword() == null) || (loginUser.getLogin().equals("")) || (loginUser.getPassword().equals(""))  )
        {
            throw new ResponseCodeException(ResponseCodeError.EMPTY_FIELD);
        }
        if (!employeeDao.getEmployeeDao(loginUser.getLogin()).getPassword().equals(loginUser.getPassword()))
        {
            throw new ResponseCodeException(ResponseCodeError.PASSWORD_ERROR);
        }
    }
    public <T> T getClassFromJson(String jsonString, Type type) throws JsonSyntaxException, ResponseCodeException {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, type);
        } catch (JsonSyntaxException ex){
            ServerResponse serverResponse = new ServerResponse( ERROR_CODE, gson.toJson("error") );
            throw new ResponseCodeException(ResponseCodeError.WRONG_JSON);
        }
    }
}
