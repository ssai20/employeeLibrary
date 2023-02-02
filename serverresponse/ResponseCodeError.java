package net.thumbtack.school.library.serverresponse;

public enum ResponseCodeError
{
    EMPTY_FIELD("Заполните, пожалуйста, все поля!"),
    LOGIN_ERROR("Извините, логин %s не зарегистрирован."),
    PASSWORD_ERROR("Пароль не верен!"),
    LOGIN_ALREADY_USED("Извините, логин %s уже существует."),
    MIN_PASSWORD("Пароль должен содержать не менее 8 символов."),
    HIGH_REGISTER_ERROR("Пароль должен содержать заглавные буквы."),
    ACCESS_ERROR("Войдите или зарегистирируйтесь."),
    BOOK_NOT_FOUND("Извините, такой книги в нашей базе нет..."),
    BOOK_RESERVED("Книга забронирована и освободится через %s дней."),
    WRONG_JSON("Json с ошибкой!"),
    LOGIN_ALREADY_AUTHORIZED("Логин %s уже авторизирован!"),
    EMPTY_BOOK_LIST("Список книг пуст!");

    private String errorString;
    ResponseCodeError(String error) {
        this.errorString = error;
    }

    public String getErrorString() {
        return errorString;
    }

}
