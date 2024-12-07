package Server.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Goods {
    @JsonProperty
    public String imgUrl;
    @JsonProperty
    public String name;
    @JsonProperty
    public double price;
    @JsonProperty
    public String clickUrl;
    @JsonProperty
    public String factory;
    public Goods(String imgUrl, String name, double price, String clickUrl, String factory) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.price = price;
        this.clickUrl = clickUrl;
        this.factory = factory;
    }
}
