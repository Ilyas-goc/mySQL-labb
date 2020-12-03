package view;

import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.ArrayList;
import model.SearchMode;
import model.Book;
import model.BooksDbInterface;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import java.sql.Date;
import static javafx.scene.control.Alert.AlertType.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.MockBooksDb;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Pair;
import model.Author;
import model.BookReviewed;
import model.ReviewTexts;

/**
 * The controller is responsible for handling user requests and update the view
 * (and in some cases the model).
 *
 * @author anderslm@kth.se
 */
public class Controller<T> {

    private final BooksPane booksView; // view
    private final BooksDbInterface booksDb; // model
    Book bookResult;
    Author authorResult;
    String isbn, title, columnName, name, searchFor;
    T newvalue;
    SearchMode mode;
    List<Book> result = null;

    public Controller(BooksDbInterface booksDb, BooksPane booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;

    }

    protected void onSearchSelected(String searchFor, SearchMode mode) {
        this.searchFor = searchFor;
        this.mode = mode;
        if (searchFor != null && searchFor.length() >= 1) {
            onSearchSelectedThread task = new onSearchSelectedThread();
            new Thread(task).start();
        }

    }

    EventHandler<ActionEvent> evento = new EventHandler<ActionEvent>() {

        public void handle(ActionEvent e) {
            try {
                ConnectBackgroundWork task = new ConnectBackgroundWork();
                new Thread(task).start();

            } catch (Exception ex) {
                System.out.println("fail");
            }
        }

        class ConnectBackgroundWork extends Task {

            @Override
            protected Integer call() throws Exception {

                booksDb.connect("h", "root", "diyarbekir99");
                return null;

            }

        }
    };

    EventHandler<ActionEvent> exit = new EventHandler<ActionEvent>() {

        public void handle(ActionEvent e) {
            try {
                booksDb.disconnect();
                System.exit(0);
            } catch (Exception ex) {

            }
        }

    };

    EventHandler<ActionEvent> disconnect = new EventHandler<ActionEvent>() {

        public void handle(ActionEvent e) {
            try {
                booksDb.disconnect();
            } catch (Exception ex) {
                System.out.println("Fail");
            }
        }

    };

    protected void addBook(Book bookResult, Author authorResult) {
        this.bookResult = bookResult;
        this.authorResult = authorResult;
        addBookThread task = new addBookThread();
        new Thread(task).start();

    }

    protected void updateBook2(String tmpisbn, String tmptitle, String tmpcolumnName, T tmpnewValue) {

        this.isbn = tmpisbn;
        this.title = tmptitle;
        this.columnName = tmpcolumnName;
        this.newvalue = tmpnewValue;
        System.out.println("s" + this.title);
        updateBooks task = new updateBooks();
        new Thread(task).start();

    }

    protected void removeBooks(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
        removeBook task = new removeBook();
        new Thread(task).start();

    }

    protected void addAuthorToBooks(String authorName, String bookName) {
        this.title = bookName;
        this.name = authorName;
        addAuthorToBookThread task = new addAuthorToBookThread();
        new Thread(task).start();
    }

    protected void loginScreenn(Optional<Pair<String, String>> result, String userName, String password) {
        result.ifPresent(usernamePassword -> {
            try {

                booksDb.connect("h", userName, password);
                booksDb.storeUser(booksDb.getConnection(), userName);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    protected ArrayList<BookReviewed> reviews(Book tmp) {
        ArrayList<BookReviewed> reviewTmp = new ArrayList<BookReviewed>();

        try {
            reviewTmp = booksDb.getReviews(booksDb.getConnection(), tmp);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
//        Alert alert = new Alert(AlertType.NONE);
//        alert.initStyle(StageStyle.UTILITY);
//        alert.setTitle("Recension");
//        alert.setHeaderText("Recensioner");
//        int i = reviewTmp.size();
//
//// Create expandable Exception.
//        GridPane expContent = new GridPane();
//
//        if (i >= 1) {
//            Label label = new Label("Review by: " + reviewTmp.get(0).getReviewerName() + "  Date: " + reviewTmp.get(0).getDate());
//            TextArea textArea = new TextArea(reviewTmp.get(0).getReviewText());
//            textArea.setEditable(false);
//            textArea.setWrapText(true);
//            textArea.setMaxWidth(Double.MAX_VALUE * 2);
//            textArea.setMaxHeight(Double.MAX_VALUE);
//            GridPane.setVgrow(textArea, Priority.ALWAYS);
//            GridPane.setHgrow(textArea, Priority.ALWAYS);
//            expContent.add(label, 0, 0);
//            expContent.add(textArea, 0, 1);
//        }
//
//        if (i >= 2) {
//            Label label1 = new Label("Review by: " + reviewTmp.get(1).getReviewerName() + "  Date: " + reviewTmp.get(1).getDate());
//            TextArea textArea1 = new TextArea(reviewTmp.get(1).getReviewText());
//            textArea1.setEditable(false);
//            textArea1.setWrapText(true);
//            expContent.add(label1, 0, 2);
//            expContent.add(textArea1, 0, 3);
//        }
//
//        if (i >= 3) {
//            Label label2 = new Label("Review by: " + reviewTmp.get(2).getReviewerName() + "  Date: " + reviewTmp.get(2).getDate());
//            TextArea textArea2 = new TextArea(reviewTmp.get(2).getReviewText());
//            expContent.add(textArea2, 0, 5);
//            textArea2.setEditable(false);
//            textArea2.setWrapText(true);
//            expContent.add(label2, 0, 4);
//        }
//
//        if (i >= 4) {
//            Label label3 = new Label("Review by: " + reviewTmp.get(3).getReviewerName() + "  Date: " + reviewTmp.get(3).getDate());
//
//            TextArea textArea3 = new TextArea(reviewTmp.get(3).getReviewText());
//            expContent.add(label3, 0, 6);
//            expContent.add(textArea3, 0, 7);
//            textArea3.setEditable(false);
//            textArea3.setWrapText(true);
//        }
//
//        if (i >= 5) {
//            Label label4 = new Label("Review by: " + reviewTmp.get(4).getReviewerName() + "  Date: " + reviewTmp.get(4).getDate());
//            TextArea textArea4 = new TextArea(reviewTmp.get(4).getReviewText());
//            expContent.add(label4, 0, 8);
//            expContent.add(textArea4, 0, 9);
//            textArea4.setEditable(false);
//            textArea4.setWrapText(true);
//        }
//
//        expContent.setMaxWidth(Double.MAX_VALUE);
//
//        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
//        Node closeButton = alert.getDialogPane().lookupButton(ButtonType.CLOSE);
//
//        // Set expandable Exception into the dialog pane.
//        alert.getDialogPane().setExpandableContent(expContent);
//
//        alert.showAndWait();
//        
        return reviewTmp;
    }

    protected void reviewBooks(Book tmp) {
        Alert alert = new Alert(AlertType.NONE);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Recension");
        alert.setHeaderText("Recensioner");

        Label label = new Label("Review:");

        TextArea textArea = new TextArea();
        textArea.setEditable(true);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE * 2);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        Button b = new Button("Lägg");

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        expContent.add(b, 0, 10);
        alert.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    booksDb.storeReview(booksDb.getConnection(), tmp, booksDb.getCurrentUser(), textArea.getText());
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    class addBookThread extends Task {

        @Override
        protected Object call() throws Exception {
            try {

                booksDb.addBookWithNewAuthor(booksDb.getConnection(), authorResult, bookResult);

            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }

    }

    class updateBooks extends Task {

        @Override
        protected Object call() throws Exception {
            try {

                booksDb.updateBook(booksDb.getConnection(), title, isbn, columnName, newvalue);

            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }

    }

    class removeBook extends Task {

        @Override
        protected Object call() throws Exception {
            try {
                booksDb.removeBook(booksDb.getConnection(), title, isbn);

            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }

    }

    class addAuthorToBookThread extends Task {

        @Override
        protected Object call() throws Exception {
            try {

                booksDb.addAuthorToBook(booksDb.getConnection(), name, title);

            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }

    }


    protected void thread(String searchFor,SearchMode mode) {
        
        new Thread() {

            @Override
            public void run() {
                try {

                switch (mode) {
                    case Title:
                        result = booksDb.searchBooksByTitle(booksDb.getConnection(), searchFor);

                        break;
                    case ISBN:
                        result = booksDb.searchBooksByISBN(booksDb.getConnection(), searchFor);
                        break;
                    case Author:
                        result = booksDb.searchBooksByAuthor(booksDb.getConnection(), searchFor);
                        break;
                    case Genre:
                        result = booksDb.searchBooksByGenre(booksDb.getConnection(), searchFor);
                        break;
                    case Rating:
                        result = booksDb.searchBooksByRating(booksDb.getConnection(), searchFor);
                        break;

                    default:

                }
            } catch (Exception e) {
                booksView.showAlertAndWait("Database error.", ERROR);
            }
            javafx.application.Platform.runLater(() -> {
                if (result.isEmpty()) {
                    booksView.showAlertAndWait("Empty input", ERROR);
                } else {
                    booksView.displayBooks(result);
                }
            });

            }
        }.start();

    }

}
