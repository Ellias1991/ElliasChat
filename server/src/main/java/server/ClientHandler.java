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

    private boolean authenticated;
    private String nickname;

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
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();
                        if(str.startsWith("/")){
                            if (str.equals("/end")) {
                                sendMsg("/end");
                                break;
                        }
                            if(str.startsWith("/auth ")){
                          String[]token=str.split(" ", 3);
                          if(token.length<3){
                              continue;

                          }
                          String newNick=server.getAuthService().getNicknameByLoginAndPassword(token[1],token[2]);
                          if (newNick!=null) {
                              nickname=newNick;
                              sendMsg("/auth_ok");
                              authenticated=true;
                              server.subscribe(this);
                              break;
                          }else{
                              sendMsg("Логин/пароль неверны");
                          }
                            }
                        }


                    }


                    //цикл работы
                    while (authenticated) {
                        String str = in.readUTF();
                        if (str.equals("/end")) {
                            sendMsg("/end");
                            break;
                        }

                        server.broadcastMsg(str);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    System.out.println("Client disconnected");

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






