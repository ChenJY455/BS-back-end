package Server.Exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;

@RestControllerAdvice
public class ExpHandler {
	@ExceptionHandler(UnAuthedException.class)
	public ResponseEntity<String> authExp(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> NFDExp(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> InternalExp(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		return ResponseEntity.internalServerError().body(e.getMessage());
	}
}

