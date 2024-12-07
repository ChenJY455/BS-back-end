package Server.Exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> authExp(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> InternalExp(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		return ResponseEntity.internalServerError().body(e.getMessage());
	}
}
