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
	@ManyToOne
	@JoinColumn(name = "tb_gid")
	TBGoods tbGids;
	@ManyToOne
	@JoinColumn(name = "jd_gid")
	JDGoods jdGids;
	@Column(nullable = false)
	double price;
	@Column(nullable = false)
	long t;
	
	public History() {}
	public History(TBGoods tbGids, JDGoods jdGids, double price) {
		this.tbGids = tbGids;
		this.jdGids = jdGids;
		this.price = price;
		this.t = System.currentTimeMillis();
	}
}
