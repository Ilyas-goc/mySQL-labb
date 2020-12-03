/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Genre;

/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 *
 * Your implementation should access a real database.
 *
 * @author anderslm@kth.se
 * @param <T>
 */
public class MockBooksDb<T> implements BooksDbInterface<T> {

    private static Connection connect;
    private static String userName;

    /**
     * Constructor
     */
    public MockBooksDb() {

    }

    /**
     * returns the current connection
     *
     * @return
     */
    @Override
    public Connection getConnection() {
        return this.connect;
    }

    /**
     * Connects to the database
     *
     * @param database the information for what database to connect to
     * @param username username at login
     * @param password password at login returns true if connected
     * @return
     */
    @Override
    public boolean connect(String database, String username, String password) throws IOException, SQLException {
        try {
            database = "jdbc:mysql://localhost:3306/bookauthordb";

            connect = DriverManager.getConnection(database, username, password);

            System.out.println("Connected");
        } catch (SQLException e) {
        } finally {
            if (connect == null) {
                System.out.println("Could not connect...");
            }
        }

        return true;
    }

    /**
     * Disconnects from the database
     *
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void disconnect() throws IOException, SQLException {
        if (connect != null) {
            connect.close();
            System.out.println("Disconnected");
        }

    }

    /**
     *
     * @returns the name of the current logged in user
     */
    @Override
    public String getCurrentUser() {
        return this.userName;
    }

    /**
     *Returns the reviews of a specific book
     * @param con contains the current connection
     * @param tmp contains the book selected for reviews
     * @returns the reviews of that book (tmp)
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public ArrayList<BookReviewed> getReviews(Connection con, Book tmp) throws IOException, SQLException {
        String STRINGtmp1, STRINGtmp2;
        Date DATEtmp;
        ArrayList<BookReviewed> result = new ArrayList<>();
        // List<ReviewTexts> resultTwo = new ArrayList<ReviewTexts>();
        // mock implementation
        // NB! Your implementation should select the books matching
        // the search string via a query with to a database.
        Statement getReviews = null;
        try {
            // Execute the SQL statement

            getReviews = con.createStatement();
            ResultSet rs = getReviews.executeQuery("CALL getReviews('" + tmp.getTitle() + "');");

            while (rs.next()) {
                // NB! This is an example, -not- the preferred way to retrieve data.
                // You should use methods that return a specific data type, like
                // rs.getInt(), rs.getString() or such.
                // It's also advisable to store each tuple (row) in an object of
                // custom type (e.g. Employee).

                STRINGtmp1 = rs.getString(1);
                STRINGtmp2 = rs.getString(2);
                System.out.println("" + STRINGtmp2);
                DATEtmp = rs.getDate(3);

                result.add(new BookReviewed(STRINGtmp1, STRINGtmp2, DATEtmp));

            }

        } finally {
            if (getReviews != null) {
                getReviews.close();
            }
        }
        return result;

    }

    /**
     * Stores the name of the current user in the database
     * @param con contains the current connection
     * @param username username of the user
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void storeUser(Connection con, String username) throws IOException, SQLException {
        Statement storeUser = null;
        userName = username;
        try {
            // Execute the SQL statement
            storeUser = con.createStatement();
            
            storeUser.executeUpdate("INSERT INTO bookauthordb.reviewer (rev_Name) VALUES ('" + username + "');");

        } catch (SQLException e) {
            System.out.println("");
        } finally {
            if (storeUser != null) {
                storeUser.close();
            }
        }
    }

    /**
     * Returns the books obtained from the Select query
     * @param con contains the current connection 
     * @param searchTitle the name of the book to be sought for
     * @returns the result of the search
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public List<Book> searchBooksByTitle(Connection con, String searchTitle) throws IOException, SQLException {
        List<Book> result = new ArrayList<>();
        String queryText = "SELECT * FROM book WHERE title LIKE '%" + searchTitle + "%';";
        result = executeSearchQuery(con, queryText);
        return result;

    }

    /**
     *
     * @param con contains the current connection
     * @param searchAuthor the name of the author that has written the books
     * @returns the result of the search
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public List<Book> searchBooksByAuthor(Connection con, String searchAuthor) throws IOException, SQLException {
        List<Book> result = new ArrayList<>();
        String queryText = "SELECT * FROM book WHERE ISBN IN "
                + "(SELECT ISBN FROM wrote WHERE authorID IN "
                + "(SELECT authorID FROM author WHERE name ='" + searchAuthor + "'));";
        result = executeSearchQuery(con, queryText);
        return result;
    }

    /**
     *
     * @param con contains the current connection
     * @param searchIsbn the ISBN of the book that is to be sought for
     * @returns the result of the search
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public List<Book> searchBooksByISBN(Connection con, String searchIsbn) throws IOException, SQLException {
        List<Book> result = new ArrayList<>();
        String queryText = "SELECT * FROM book WHERE isbn='" + searchIsbn + "'";
        result = executeSearchQuery(con, queryText);
        return result;

    }

    /**
     *
     * @param con contains the current current connection
     * @param searchGenre the genre of the books that is to be sought for
     * @returns the result of the search
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public List<Book> searchBooksByGenre(Connection con, String searchGenre) throws IOException, SQLException {
        List<Book> result = new ArrayList<>();
        String queryText = "SELECT * FROM book WHERE genre='" + searchGenre + "'";
        result = executeSearchQuery(con, queryText);
        return result;
    }

    /**
     *
     * @param con contains the current connection
     * @param searchRating the rating of the books that is to be sought for
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public List<Book> searchBooksByRating(Connection con, String searchRating) throws IOException, SQLException {
        List<Book> result = new ArrayList<>();
        String queryText = "SELECT * FROM book WHERE rating='" + searchRating + "'";
        result = executeSearchQuery(con, queryText);
        return result;
    }


    /**
     *
     * @param con contains the current connection 
     * @param authorName name of the author that is to added to the book
     * @param bookName name of the book that will get the additional author
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void addAuthorToBook(Connection con, String authorName, String bookName) throws IOException, SQLException {
        Statement addAuthorToBook = null;
        try {
            
            // Execute the SQL statement
            addAuthorToBook = con.createStatement();
            addAuthorToBook.executeQuery("CALL addAuthorToBook('" + authorName + "', '" + bookName + "');");

        } finally {
            if (addAuthorToBook != null) {
                addAuthorToBook.close();
            }
        }
    }

    /**
     *
     * @param con contains the current connection
     * @param book 
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public List<Book> insertAndShow(Connection con, Book book) throws IOException, SQLException {
        String STRINGtmp1, STRINGtmp2, STRINGtmp3;
        Date DATEtmp;
        int INTtmp;
        List<Book> result = new ArrayList<>();
        Statement stmt = null;
        try {
            // Execute the SQL statement
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("CALL insertAndShow(" + book.getIsbn() + ",'" + book.getTitle() + "', '" + book.getGenre().name() + "', " + book.getRating() + ", '" + book.getPublished() + "');");

            // Get the attribute values
            while (rs.next()) {
                // NB! This is an example, -not- the preferred way to retrieve data.
                // You should use methods that return a specific data type, like
                // rs.getInt(), rs.getString() or such.
                // It's also advisable to store each tuple (row) in an object of
                // custom type (e.g. Employee).

                STRINGtmp1 = rs.getString(1);
                STRINGtmp2 = rs.getString(2);
                STRINGtmp3 = rs.getString(3);
                DATEtmp = rs.getDate(5);
                INTtmp = rs.getInt(4);

                //result.add(new Book(STRINGtmp1, STRINGtmp2, DATEtmp, INTtmp, Genre.valueOf(STRINGtmp3), new ArrayList<>(10), new ArrayList<ReviewTexts>()));
                System.out.println("" + STRINGtmp2);
            }

        } finally {
            stmt.close();
        }
        return result;
    }

    /**
     *
     * @param con contains the current connection
     * @param bookName name of the book that will be removed
     * @param isbn isbn of the book that will be removed
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void removeBook(Connection con, String bookName, String isbn) throws IOException, SQLException {
        Statement deleteBookFromBookRows = null;
        Statement deleteBookFromWroteRows = null;
        Statement deleteBookFromReviewsRows = null;
        try {
            con.setAutoCommit(false);
            deleteBookFromWroteRows = con.createStatement();
            deleteBookFromReviewsRows = con.createStatement();
            deleteBookFromBookRows = con.createStatement();
            deleteBookFromReviewsRows.executeUpdate("DELETE FROM reviews WHERE ISBN= '" + isbn + "';");
            deleteBookFromWroteRows.executeUpdate("DELETE FROM wrote WHERE ISBN= '" + isbn + "';");
            deleteBookFromBookRows.executeUpdate("DELETE FROM book WHERE title= '" + bookName + "' AND ISBN= '" + isbn + "';");

            con.commit();
        } catch (Exception e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            if (deleteBookFromBookRows != null) {
                deleteBookFromBookRows.close();
            }
            if (deleteBookFromWroteRows != null) {
                deleteBookFromWroteRows.close();
            }
            if (deleteBookFromReviewsRows != null) {
                deleteBookFromReviewsRows.close();
            }
            con.setAutoCommit(true);
        }

    } // Förkortning av dessa m designmönster

    /**
     *
     * @param con contains the current connection
     * @param bookName name of the book that will be updated
     * @param isbn isbn of the book that will updated
     * @param column 
     * @param newColumnValue
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void updateBook(Connection con, String bookName, String isbn, String column, T newColumnValue) throws IOException, SQLException {
        Statement updateBook = null;
        
        try {
            updateBook = con.createStatement();
            updateBook.executeUpdate("UPDATE book SET " + column + "= '" + newColumnValue + "'"
                    + "WHERE title='" + bookName + "' AND ISBN= '" + isbn + "';");

        } finally {
            if (updateBook != null) {
                updateBook.close();
            }

        }
    }

    /**
     *
     * @param con
     * @param author
     * @param book
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void addBookWithNewAuthor(Connection con, Author author, Book book) throws IOException, SQLException {
        Statement addBookWithNewAuthor = null;
        try {
            addBookWithNewAuthor = con.createStatement();
            addBookWithNewAuthor.executeUpdate("CALL addBookWithNewAuthor('" + book.getIsbn() + "', '" + book.getTitle() + "', '" + book.getGenre() + "', " + book.getRating() + ", '" + book.getPublished() + "', '" + author.getName() + "');");

        } finally {
            if (addBookWithNewAuthor != null) {
                addBookWithNewAuthor.close();
            }

        }
    }

    /**
     *
     * @param con
     * @param tmp
     * @param userName
     * @param reviewText
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void storeReview(Connection con, Book tmp, String userName, String reviewText) throws IOException, SQLException {
        Statement storeReview = null;
        try {
            // Execute the SQL statement
            storeReview = con.createStatement();
            storeReview.executeUpdate("INSERT INTO bookauthordb.reviews(rev_Name,ISBN,reviewText,review_Date )"
                    + "VALUE ('" + userName + "', (SELECT ISBN FROM book WHERE title = '" + tmp.getTitle() + "'), '" + reviewText + "', '" + tmp.getPublished() + "');");

        } finally {
            if (storeReview != null) {
                storeReview.close();
            }

        }

    }

    /**
     *
     * @param con
     * @param queryText
     * @return
     * @throws SQLException
     * @throws SQLException
     */
    @Override
    public List<Book> executeSearchQuery(Connection con, String queryText) throws SQLException, SQLException {

        String STRINGtmp1, STRINGtmp2, STRINGtmp3;
        Date DATEtmp;
        int INTtmp;
        List<Book> result = new ArrayList<>();
        // mock implementation
        // NB! Your implementation should select the books matching
        // the search string via a query with to a database.
        Statement executeSearchQuery = null;
        try {
            // Execute the SQL statement
            executeSearchQuery = con.createStatement();
            ResultSet rs = executeSearchQuery.executeQuery(queryText);

            // Get the attribute values
            while (rs.next()) {
                // NB! This is an example, -not- the preferred way to retrieve data.
                // You should use methods that return a specific data type, like
                // rs.getInt(), rs.getString() or such.
                // It's also advisable to store each tuple (row) in an object of
                // custom type (e.g. Employee).

                STRINGtmp1 = rs.getString(1);
                STRINGtmp2 = rs.getString(2);
                STRINGtmp3 = rs.getString(3);
                DATEtmp = rs.getDate(5);
                INTtmp = rs.getInt(4);

                result.add(new Book(STRINGtmp1, STRINGtmp2, DATEtmp, INTtmp, Genre.valueOf(STRINGtmp3), new ArrayList<ReviewTexts>()));
            }

        } finally {
            if (executeSearchQuery != null) {
                executeSearchQuery.close();
            }

        }
        return result;

    }

}
