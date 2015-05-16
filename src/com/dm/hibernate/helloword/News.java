package com.dm.hibernate.helloword;

import java.sql.Date;

public class News {
	private Integer id;
	private String title;
	private String author;
	private Date mydate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getMydate() {
		return mydate;
	}
	public void setMydate(Date mydate) {
		this.mydate = mydate;
	}
	public News(String title, String author, Date mydate) {
		super();
		this.title = title;
		this.author = author;
		this.mydate = mydate;
	}
	public News() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", author=" + author
				+ ", mydate=" + mydate + "]";
	}

	
	
	
	
	
}
