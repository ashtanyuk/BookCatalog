package sample;

/**
 * Created by anton.shtanyuk on 22.05.2018.
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private int year;

    public Book(int id,String title,String author,int year) {
        this.id=id;
        this.title=title;
        this.author=author;
        this.year=year;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
