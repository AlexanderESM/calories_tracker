package net.orekhov.calories_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при отсутствии запрашиваемого ресурса в базе данных.
 * <p>
 * Например, используется, если пользователь, блюдо или прием пищи не найдены.
 * </p>
 * <p>
 * Аннотировано {@link ResponseStatus}, что автоматически возвращает HTTP 404 Not Found.
 * </p>
 *
 * @see net.orekhov.calories_tracker.exception.GlobalExceptionHandler
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Создает исключение с заданным сообщением.
     *
     * @param message Описание причины исключения.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Создает исключение с заданным сообщением и причиной.
     *
     * @param message Описание причины исключения.
     * @param cause   Первоначальная причина исключения.
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
