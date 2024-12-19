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
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "uid")
	User user;
	Utils.WebsiteType website;
	String name;
	@ManyToOne
	@JoinColumn(name = "jd_goods")
	JDGoods jdGoods;
	@ManyToOne
	@JoinColumn(name = "tb_goods")
	TBGoods tbGoods;
	
	public Likes() {}
	public Likes(User user, Utils.WebsiteType website, String name, TBGoods tbGoods, JDGoods jdGoods) {
		this.user = user;
		this.website = website;
		this.name = name;
		this.tbGoods = tbGoods;
		this.jdGoods = jdGoods;
	}
}
