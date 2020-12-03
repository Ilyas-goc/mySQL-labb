package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 * 
 * @author anderslm@kth.se
 */
public interface BooksDbInterface <T>{
    
    /**
     * Connect to the database.
     * @param database
     * @return true on successful connection.
     */
   public boolean connect(String database,String username,String password) throws IOException, SQLException;;
    
    public void disconnect() throws IOException, SQLException;
    
    public void storeUser(Connection con,String username) throws IOException, SQLException;;
   
    public void storeReview(Connection con,Book tmp,String userName,String reviewText) throws IOException, SQLException;;
    
    
    
    public ArrayList<BookReviewed> getReviews(Connection con,Book tmp) throws IOException,SQLException;;
    
    public List<Book> executeSearchQuery(Connection con,String queryText) throws IOException,SQLException;;
    
    public List<Book> searchBooksByTitle(Connection con,String searchTitle) throws IOException, SQLException;;
    
    public List<Book> searchBooksByISBN(Connection con,String searchIsbn) throws IOException, SQLException;;
       
    public List<Book> searchBooksByAuthor(Connection con,String searchAuthor) throws IOException, SQLException;;
    
    public List<Book> searchBooksByGenre(Connection con,String searchGenre) throws IOException, SQLException;;
    
    public List<Book> searchBooksByRating(Connection con,String searchRating) throws IOException, SQLException;;
    
    public void addAuthorToBook(Connection con,String authorName,String bookName) throws IOException, SQLException;;
    
    public List<Book> insertAndShow(Connection con,Book book) throws IOException, SQLException;;
    
    public void removeBook(Connection con,String bookName,String isbn) throws IOException, SQLException;;
    
    public void updateBook(Connection con, String bookName,String isbn,String column,T newColumnValue) throws IOException, SQLException;;
    
    public void addBookWithNewAuthor(Connection con, Author author,Book book) throws IOException, SQLException;;
    
    public Connection getConnection();;
    
    public String getCurrentUser();;
    
}
