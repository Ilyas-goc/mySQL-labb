package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MockBooksDb;
import view.BooksPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application start up.
 *
 * @author anderslm@kth.se
 */
public class Databaselab extends Application {

    @Override
    public void start(Stage primaryStage) {

        MockBooksDb booksDb = new MockBooksDb(); // model

        BooksPane root = new BooksPane(booksDb);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Books Database Client");
        // add an exit handler to the stage (X) ?

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
