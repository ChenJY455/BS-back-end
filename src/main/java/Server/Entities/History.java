package Server.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "history")
@Entity
public class History {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long hid;
	@Column(name = "tb_gid")
	long tbGid;
	@Column(name = "jd_gid")
	long jdGid;
	@Column(nullable = false)
	double price;
	@Column(nullable = false)
	long t;
	
	public History() {}
	public History(long tbGid, long jdGid, double price) {
		this.tbGid = tbGid;
		this.jdGid = jdGid;
		this.price = price;
		this.t = System.currentTimeMillis();
	}
}
