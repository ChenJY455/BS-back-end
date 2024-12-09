package Server.Entities;

import Server.Utils.Utils;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "likes")
@Entity
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long lid;
	@ManyToOne
	@JoinColumn(name = "uid")
	User user;
	Utils.WebsiteType website;
	String name;
	@ManyToOne
	@JoinColumn(name = "tb_gid")
	JDGoods jd_goods;
	@ManyToOne
	@JoinColumn(name = "jd_gid")
	TBGoods tb_goods;
	
	public Likes() {}
	public Likes(User user, Utils.WebsiteType website, String name, JDGoods jd_goods, TBGoods tb_goods) {
		this.user = user;
		this.website = website;
		this.name = name;
		this.jd_goods = jd_goods;
		this.tb_goods = tb_goods;
	}
}
