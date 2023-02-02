package net.thumbtack.school.library.daoimpl;

import net.thumbtack.school.library.model.Employee;
import net.thumbtack.school.library.serverresponse.ResponseCodeException;

public interface EmployeeDaoImpl {
    public void insert (Employee employee) throws ResponseCodeException;
    public void insertToken(String token, Employee employee) throws ResponseCodeException;
    public Employee getEmployeeDao(String login) throws ResponseCodeException;
    public void remove(String token);
    public Employee getEmployeeByToken(String token) throws ResponseCodeException;
}
