package sam2BorderPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("ElliasChat");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);

        File file = new File("user1/history_Ellias.txt");
        InputStreamReader in1 = new InputStreamReader(new FileInputStream("user1/history_Ellias.txt"), StandardCharsets.UTF_8);
        int x;
        while ((x = in1.read()) >= 0) {
            System.out.print((char) x);
        }
        in1.close();

        File file1 = new File("user2/history_Misto.txt");

        InputStreamReader in2 = new InputStreamReader(new FileInputStream("user2/history_Misto.txt"), StandardCharsets.UTF_8);
        int y;
        while ((y = in2.read()) >= 0) {
            System.out.print((char) y);
        }
        in2.close();
        File file2 = new File("user3/history_Tuman.txt");
        InputStreamReader in3 = new InputStreamReader(new FileInputStream("user3/history_Tuman.txt"), StandardCharsets.UTF_8);
        int z;
        while ((z = in3.read()) >= 0) {
            System.out.print((char) z);
        }
        in3.close();
        System.out.println(Files.readAllLines(Paths.get("user1/history_Ellias.txt")));
        System.out.println(Files.readAllLines(Paths.get("user2/history_Misto.txt")));
        System.out.println(Files.readAllLines(Paths.get("user3/history_Tuman.txt")));


    }
}