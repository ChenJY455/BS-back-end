package Server.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb")
@Entity
public class TBGoods implements Goods {
	@Id
	long id;
	String img_url;
	@Column(nullable = false)
	String name;
	@Column(nullable = false)
	double price;
	@Column(columnDefinition = "TEXT")
	String click_url;
	String factory;
	
	public TBGoods() {}
	public TBGoods(long id, String imgUrl, String name, double price, String clickUrl, String factory) {
		this.id = id;
		this.img_url = imgUrl;
		this.name = name;
		this.price = price;
		this.click_url = clickUrl;
		this.factory = factory;
	}
}