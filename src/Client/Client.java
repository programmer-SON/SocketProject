package Client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Client extends Application{
    Socket socket;
    DataOutputStream out;

    @Override
    public void start(Stage ps) throws Exception {
        VBox root = new VBox();
        root.setPrefSize(400, 300);
        root.setSpacing(5);

        Button btn1 = new Button("접속");
        Button btn2 = new Button("데이타 전송");
        Button btn3 = new Button("클라이언트 종료");
        TextField tf = new TextField("아이디를 입력하세요.");
        TextField tf2 = new TextField();
        TextArea ta = new TextArea();

        tf.setVisible(false);
        tf2.setVisible(false);
        ta.setVisible(false);

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress("localhost", 5001));
                    out = new DataOutputStream(socket.getOutputStream());
                    tf.setVisible(true);
                    new ReceiveThread(socket,ta).start();
                } catch (Exception e) {
                    // TODO: handle exception
                    try {
                        socket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        tf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    out.writeUTF(tf.getText());
                    tf2.setVisible(true);
                    ta.setVisible(true);
                    tf.setVisible(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    socket.close();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });


        tf2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String s = tf2.getText();
                    out.writeUTF(s);
                    tf2.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        root.getChildren().addAll(btn1, btn3,tf,tf2,ta);
        //-------------------------------------------------

        Scene scene = new Scene(root);
        ps.setScene(scene);
        ps.setTitle("클라이언트");
        ps.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
