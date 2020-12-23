package pro.nvart.apps.transportCitypoint.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException {
}
