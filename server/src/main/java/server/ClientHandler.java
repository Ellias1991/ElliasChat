package server;





import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private boolean authenticated;
    private String nickname;
    private String login;

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

                    socket.setSoTimeout(120000);

                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                sendMsg("/end");
                                break;
                            }
                            if (str.startsWith("/auth")) {
                                String[] token = str.split(" ", 3);
                                if (token.length < 3) {
                                    continue;
                                }

                                String newNick = server.getAuthService().getNicknameByLoginAndPassword(token[1], token[2]);
                                login = token[1];
                                if (newNick != null) {
                                    if (!server.isLoginAuthenticated(login)) {
                                        nickname = newNick;
                                        sendMsg("/auth_ok" + nickname);
                                        authenticated = true;
                                        server.subscribe(this);
                                        break;
                                    } else {
                                        sendMsg("С этой учетки уже выполнен вход");
                                    }
                                } else {
                                    sendMsg("Логин/пароль неверны");
                                }
                            }

                            if (str.startsWith("/reg ")) {
                                String[] token = str.split(" ");
                                if (token.length < 4) {
                                    continue;
                                }
                                if (server.getAuthService().
                                        registration(token[1], token[2], token[3])) {
                                    sendMsg("/reg success");
                                } else {
                                    sendMsg("/reg failed");
                                }
                            }
                        }
                    }
                    //цикл работы
                    while (authenticated) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                sendMsg("/end");
                                break;
                            }
                            if (str.startsWith("/w")) {
                                String[] token = str.split(" ", 3);
                                if (token.length < 3) {
                                    continue;
                                }
                                server.privateMsg(this, token[1], token[2]);
                            }
                        } else {
                            server.broadcastMsg(this, str);
                        }

                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("Время подключения истекло");
                    try {
                        socket.setSoTimeout(0);
                    } catch (SocketException ex) {
                        ex.printStackTrace();
                    }
                    sendMsg("/end");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    System.out.println("Client disconnected");

                    try{
                        UserDataBase.disconnect();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}






