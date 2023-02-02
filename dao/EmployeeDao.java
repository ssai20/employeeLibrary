package net.thumbtack.school.library.dao;

import net.thumbtack.school.library.daoimpl.EmployeeDaoImpl;
import net.thumbtack.school.library.database.DataBase;
import net.thumbtack.school.library.model.Employee;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;

public class EmployeeDao implements EmployeeDaoImpl {
    public Employee employee;
    public EmployeeDao(Employee employee)
    {
        this.employee = employee;
    }
    public EmployeeDao()
    {
        this(null);
    }
    public void insert(Employee employee) throws ResponseCodeException {
        DataBase.getDataBase().insertEmployee(employee);
    }
    public void insertToken(String token, Employee employee) throws ResponseCodeException {
        DataBase.getDataBase().tokenEmployeeInsert(token, employee);
    }

    public Employee getEmployeeDao(String login) throws ResponseCodeException {
        return DataBase.getDataBase().getEmployee(login);
    }

    public String getLogin(String login) throws ResponseCodeException {
       return DataBase.getDataBase().getEmployee(login).getLogin();
    }

    public Employee getEmployeeByToken(String token) throws ResponseCodeException {
        return DataBase.getDataBase().getEmployeeByToken(token);
    }

    public void remove(String token) {
        DataBase.getDataBase().removeToken(token);
    }
}

