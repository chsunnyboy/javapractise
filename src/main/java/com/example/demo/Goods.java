package com.example.demo;

public class Goods implements Cloneable {
	private String goodsName;
	private String id;
	private String name;
	private String price;
	private Book books;
	
	public Goods(String goodsName,String id,String name,String price,Book books) {
		this.goodsName=goodsName;
		this.id=id;
		this.name=name;
		this.price=price;
		this.books=books;
	}
	public Book getBooks() {
		return books;
	}
	public void setBooks(Book books) {
		this.books = books;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Goods [goodsName=" + goodsName + ", id=" + id + ", name=" + name + ", price=" + price + "]";
	}
	public Goods clone() throws CloneNotSupportedException {
		return (Goods) super.clone();
	}
	
}
