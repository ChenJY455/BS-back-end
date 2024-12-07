package Server.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Table(name = "user")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer userid;
	@Column(unique = true, nullable = false)
	String username;
	@Column(nullable = false)
	String password;
	@Email(message = "邮箱格式错误")
	@Column(unique = true, nullable = false)
	String email;
	
	public User() {}
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
}
