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
	@Column(name = "tb_gid")
	long tbGid;
	@Column(name = "jd_gid")
	long jdGid;
	
	public Likes() {}
	public Likes(User user, Utils.WebsiteType website, String name, long tbGid, long jdGid) {
		this.user = user;
		this.website = website;
		this.name = name;
		this.tbGid = tbGid;
		this.jdGid = jdGid;
	}
}
