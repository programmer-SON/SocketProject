package Server;


import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class ConnectThread extends Thread{
    Socket socket;
    ClientThread clientThread[] = new ClientThread[10];
    HashMap<String, DataOutputStream> clientMap = new HashMap<>();
    HashSet<String> nameSet = new HashSet<>();
    TextArea textArea;
    String name = "";
    public ConnectThread(TextArea textArea){
        this.textArea = textArea;
    }

    public void run() {
        System.out.println("ConnectThread");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));
            textArea.appendText("바인드 완료\n");

            while(true) {
                // 블로킹 1
                socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                name = in.readUTF();
                if(nameSet.contains(name)){
                    textArea.appendText("이미 등록된 사용자 입니다.\n");
                    continue;
                }

                nameSet.add(name);
                clientMap.put(name,out);

                textArea.appendText(name + "님이 접속했습니다.\n");
                new ClientThread(socket,clientMap,in,name,textArea).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
            textArea.appendText(name + "님이 나가셨습니다.\n");
            RemoveClient(name);
        }
    }

    public void RemoveClient(String name){
        clientMap.remove(name);
    }
}
