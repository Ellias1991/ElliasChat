package server;

import sun.management.snmp.jvmmib.EnumJvmThreadCpuTimeMonitoring;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public ClientHandler(Server server, Socket socket) {

        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());//слушаем входящий канал,те сообщения которые нам напишет клиент,
            // то при помощи чего слушать сообщения,передается инф.в сокет
            out = new DataOutputStream(socket.getOutputStream());//надо записывать исходящий канал.использует буфер,
            // копит сообщения,как накопит-он их отправляет.В данном случае сразу просим Автофшалем не копить,а отправлять сообщения.
            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/end")) {
                            break;
                        }

                        server.broadcastMsg(str);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}






