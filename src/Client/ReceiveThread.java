package Client;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ReceiveThread extends Thread{
    Socket socket;
    TextArea textArea;
    DataInputStream in;
    public ReceiveThread(Socket socket, TextArea textArea) throws IOException {
        this.socket =socket;
        this.textArea = textArea;
        this.in = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while(true) {

                //DataInputStream in = new DataInputStream(socket.getInputStream());
                if(in == null){
                    break;
                }
                String s = in.readUTF();
                textArea.appendText(s + "\n");

                /*
                InputStream is = socket.getInputStream();
                byte[] recvData = new byte[4096];
                int size = is.read(recvData);
                if(size == -1) break;

                String str = new String(recvData, 0, size, "utf-8");
                textArea.appendText(str + "\n");

                 */
            }
        } catch (IOException e) {
            System.err.println("연결이 끊겼습니다.");
            e.printStackTrace();
        }
    }
}
