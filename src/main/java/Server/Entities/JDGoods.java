package Server.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "jd")
@Entity
public class JDGoods implements Goods {
	@Id
	long gid;
	@Column(columnDefinition = "TEXT")
	String img_url;
	@Column(nullable = false, columnDefinition = "TEXT")
	String name;
	@Column(nullable = false)
	double price;
	@Column(columnDefinition = "TEXT")
	String click_url;
	String factory;
	@Column(nullable = false)
	String keyword;
	long t;
	String website;
	
	public JDGoods() {}
	public JDGoods(long gid, String imgUrl, String name, double price, String clickUrl, String factory, String keyword) {
		this.gid = gid;
		this.img_url = imgUrl;
		this.name = name;
		this.price = price;
		this.click_url = clickUrl;
		this.factory = factory;
		this.keyword = keyword;
		this.t = System.currentTimeMillis();
		this.website = "JD";
	}
	@Override
	public boolean OverDue() {
		return (System.currentTimeMillis() - this.t > 3600000);
	}
}
