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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class ClientThread  extends Thread{
    Socket socket;
    HashMap<String,DataOutputStream> clientMap;
    DataInputStream in;
    String name;
    ClientThread(Socket socket, HashMap<String,DataOutputStream> clientMap, DataInputStream in, String name) throws IOException {
        this.socket = socket;
        this.clientMap = clientMap;
        this.in = in;
        this.name = name;
    }

    public void run() {
        try {

            while(in != null){
                String msg = in.readUTF();
                sendMessage(name+": "+msg);
            }
            /*
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            byte[] recvData = new byte[4096];

            while(true) {
                // 블로킹 2 함수이다.(read)
                int size = is.read(recvData);
                if(size == -1) {
                    System.out.println("클라이언트 아웃됨");
                    break;
                }
                // 분해되어서 받은 데이타를 조립한다.
                String s = new String(recvData, 0, size, "utf-8");
                os.write(s.getBytes());

                //System.out.println(s);

            }*/
        } catch (Exception e) {
            System.err.println(name + "님이 나가셨습니다.");
            RemoveClient(name);
        }
    }

    public void sendMessage(String msg){
        Iterator<String> it = clientMap.keySet().iterator();
        String key = "";
        while(it.hasNext()){
            key = it.next();
            try {
                clientMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void RemoveClient(String name){
        clientMap.remove(name);
    }
}

class ConnectThread extends Thread{
    Socket socket;
    int idx = 0;
    ClientThread clientThread[] = new ClientThread[10];
    HashMap<String, DataOutputStream> clientMap = new HashMap<>();
    HashSet<String> nameSet = new HashSet<>();

    public void run() {
        System.out.println("ConnectThread");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));
            System.out.println("바인드 완료");

            while(true) {
                // 블로킹 1
                socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                String name = in.readUTF();
                if(nameSet.contains(name)){
                    System.out.println("이미 등록된 사용자 입니다.");
                    continue;
                }

                nameSet.add(name);
                clientMap.put(name,out);

                System.out.println(name + " 님이 접속함.");
                new ClientThread(socket,clientMap,in,name).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Server extends Application{
    @Override
    public void start(Stage ps) throws Exception {
        VBox root = new VBox();
        root.setPrefSize(400, 300);
        root.setSpacing(5);
        OutputStreamWriter os = new OutputStreamWriter(System.out);
        //-------------------------------------------------
        Button btn1 = new Button("서버오픈");
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                new ConnectThread().start();
            }
        });

        root.getChildren().addAll(btn1);
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
