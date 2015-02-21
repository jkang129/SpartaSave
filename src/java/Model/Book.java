package Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for a book posted by a user.
 * 
 * @author jackkang
 */
public class Book {
    private int user_id, price;
    private String isbn, title, author, book_condition, category, detailPageURL, imageURL, listPrice, lowestNewPrice, lowestPrice;
    private List<String> authors;
    private String post_date;
    private int id;

    /**
     * Constructs a book created by a user.
     * Follows the construction of the UserListings table on spartasavedb.
     */
    public Book(int user_id, String isbn, String title, String author, 
            String book_condition, String category, int price, String post_date)
    {
        this.user_id = user_id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.book_condition = book_condition;
        this.category = category;
        this.price = price;
        this.post_date = post_date;
    }
    
    public Book() {
        authors = new ArrayList<String>();
    }
    
    /**
     * Getter and setter methods below.
     */
    public void setDetailPageURL(String url) {
        this.detailPageURL = url;
    }
    
    public String getDetailPageURL() {
        return this.detailPageURL;
    }
    
    public void setImageURL(String url) {
        this.imageURL = url;
    }
    
    public String getImageURL() {
        return this.imageURL;
    }
    
    public void setListPrice(String price) {
        this.listPrice = price;
    }
    
    public String getListPrice() {
        return this.listPrice;
    }
    
    public void setLowestNewPrice(String price) {
        this.lowestNewPrice = price;
    }
    
    public String getLowestNewPrice() {
        return this.lowestNewPrice;
    }
    
    public void setLowestPrice(String price) {
        this.lowestPrice = price;
    }
    
    public String getLowestPrice() {
        return this.lowestPrice;
    }
    
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBook_condition(String book_condition) {
        this.book_condition = book_condition;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getPrice() {
        return price;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBook_condition() {
        return book_condition;
    }

    public String getCategory() {
        return category;
    }

    public String getPost_date() {
        return post_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public List<String> getAuthors() {
        return authors;
    }
}