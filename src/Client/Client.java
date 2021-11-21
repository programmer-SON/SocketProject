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


//class Receive extends Thread{
//    Socket socket;
//    TextArea textArea;
//
//    public Receive(Socket socket, TextArea textArea){
//        this.socket =socket;
//        this.textArea = textArea;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while(true) {
//                InputStream is = socket.getInputStream();
//                byte[] recvData = new byte[4096];
//                int size = is.read(recvData);
//                if(size == -1) break;
//
//                String str = new String(recvData, 0, size, "utf-8");
//                textArea.appendText(str + "\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

public class Client extends Application{
    Socket socket;

    @Override
    public void start(Stage ps) throws Exception {
        VBox root = new VBox();
        root.setPrefSize(400, 300);
        root.setSpacing(5);
        //-------------------------------------------------

        Button btn1 = new Button("접속");
        Button btn2 = new Button("데이타 전송");
        Button btn3 = new Button("클라이언트 종료");
        TextField tf = new TextField();
        TextArea ta = new TextArea();

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress("localhost", 5001));
                    new ReceiveThread(socket,ta).start();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });



//        btn2.setOnAction(new EventHandler<ActionEvent>() {
//            int count = 0;
//            @Override
//            public void handle(ActionEvent arg0) {
//                try {
//                    // 출력 스트림 객체 생성
//                    OutputStream os = socket.getOutputStream();
//                    byte[] sendData;
//                    String s = "apple:" + count++;
//                    // 분해 작업을 한다.
//                    sendData = s.getBytes("utf-8");
//
//                    // 보내어 보자
//                    os.write(sendData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


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


        tf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    String s = tf.getText();
                    // 분해 작업을 한다.

                    // 보내어 보자
                    out.writeUTF(s);
                    tf.setText("");


                    /*
                    // 출력 스트림 객체 생성
                    OutputStream os = socket.getOutputStream();
                    byte[] sendData;
                    String s = tf.getText();
                    // 분해 작업을 한다.
                    sendData = s.getBytes("utf-8");

                    // 보내어 보자
                    os.write(sendData);
                    tf.setText("");

                     */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


//        ta.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//                try {
//                    InputStream is = socket.getInputStream();
//                    byte[] recvData = new byte[4096];
//                    int size = is.read(recvData);
//
//                   String str = new String(recvData, 0, size, "utf-8");
//                   ta.appendText(str+"\n");
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

        root.getChildren().addAll(btn1, btn3,tf,ta);
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
