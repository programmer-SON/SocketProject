package Server;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class ClientThread  extends Thread{
    Socket socket;
    HashMap<String, DataOutputStream> clientMap;
    DataInputStream in;
    String name;
    TextArea textArea;
    ClientThread(Socket socket, HashMap<String,DataOutputStream> clientMap, DataInputStream in, String name, TextArea textArea) throws IOException {
        this.socket = socket;
        this.clientMap = clientMap;
        this.in = in;
        this.name = name;
        this.textArea = textArea;
    }

    public void run() {
        try {

            while(in != null){
                String msg = in.readUTF();
                sendMessage(name+": "+msg);
            }
        } catch (Exception e) {
            textArea.appendText(name + "님이 나가셨습니다.\n");
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
