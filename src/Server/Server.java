package Server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Server extends Application{

    @Override
    public void start(Stage ps) throws Exception {
        VBox root = new VBox();
        root.setPrefSize(400, 300);
        root.setSpacing(5);
        OutputStreamWriter os = new OutputStreamWriter(System.out);
        //-------------------------------------------------
        Button btn1 = new Button("서버오픈");
        TextArea ta = new TextArea();

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                new ConnectThread(ta).start();
            }
        });

        root.getChildren().addAll(btn1,ta);
        //-------------------------------------------------
        Scene scene = new Scene(root);
        ps.setScene(scene);
        ps.setTitle("서버");
        ps.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
