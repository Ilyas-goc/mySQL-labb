package model;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Representation of a book.
 *
 * @author anderslm@kth.se
 */
public class Book {

    private String isbn; // should check format
    private String title;
    private int[] rating = {1, 2, 3, 4, 5}; // Val av rating
    private int bookRating;  // Bokens rating 
    private Date published;
    private String storyLine = "";
    private Genre genre;
    private ArrayList<Author> authors;
    private Author author;
    private ArrayList<ReviewTexts> listOfBookTexts;
    // TODO: 
    // Add authors, and corresponding methods, to your implementation 
    // as well, i.e. "private ArrayList<Author> authors;"

    public Book(String isbn, String title, Date published, int ratingChoice, Genre genre, ArrayList<ReviewTexts> listOfBookTexts) {
        this.authors = new ArrayList<Author> ();
        this.authors.add(author);
        this.isbn = isbn;
        this.title = title;
        this.published = published;
        
        this.genre = genre;
        this.bookRating = rating[ratingChoice - 1];
        this.listOfBookTexts = listOfBookTexts;
    }

    public Book(String strunt, String isbn, String title, Date published, int ratingChoice, Genre genre,ArrayList<ReviewTexts> listOfBookTexts) {
        this(isbn, title, published, ratingChoice, genre,listOfBookTexts);
    }

    public void addAuthor(Author a) {
        this.authors.add(a);
    }
    
    public Author getFirstAuthor(){
        Author tmp = this.authors.get(0);
        return tmp;
    }

    public ArrayList<ReviewTexts> getBookTexts() {
        ArrayList<ReviewTexts> tmp = new ArrayList<ReviewTexts>();
        for (int i = 0; i < listOfBookTexts.size(); i++) {
            tmp.add(listOfBookTexts.get(i));

        }
        return tmp;
    }

    public int getRating() {
        int tmp = this.bookRating;
        return tmp;
    }

    public Genre getGenre() {
        Genre tmp = this.genre;
        return tmp;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublished() {
        Date tmp = this.published;
        return tmp;
    }

    public String getStoryLine() {
        return storyLine;
    }

    public Boolean searchForAuthor(String authorName) {
        Boolean flag = false;
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).getName().contains(authorName)) {
                flag = true;
            }
        }
        return flag;
    }

    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    @Override
    public String toString() {
        return this.getTitle() + ", " + this.getIsbn() + ", " + this.getGenre()+", "+ this.getRating()+", "+ this.getPublished();
    }
}
