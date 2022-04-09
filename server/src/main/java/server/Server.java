package server;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService executorService;

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Server() {
        executorService= Executors.newCachedThreadPool();
        clients = new CopyOnWriteArrayList<>();
        authService = new SimpleAuthService();

        try {
            if (!SQLUserDataBase.connect()) {
                throw new RuntimeException("Не удалось подключиться к БД");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        authService = new SQLAuthService();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started");

            while (true) {
                socket = server.accept();
                System.out.println("Client connected");
                new ClientHandler(this, socket);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            SQLUserDataBase.disconnect();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg) {    ///sender----чтоб знать от кого пришло сообщение
        String message = String.format("[ %s ]:%s", sender.getNickname(), msg);


        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }

    public void privateMsg(ClientHandler sender, String receiver, String msg) {    ///sender----чтоб знать от кого пришло сообщение
        String message = String.format("[ %s ] to [ %s ] :%s", sender.getNickname(), receiver, msg);


        for (ClientHandler c : clients) {
            if (c.getNickname().equals(receiver)) {
                c.sendMsg(message);
                if (!sender.getNickname().equals(receiver)) {
                    sender.sendMsg(message);
                }
                return;
            }

        }
        sender.sendMsg("not found user: "+ receiver);
    }

    public boolean isLoginAuthenticated(String login){
        for (ClientHandler c : clients) {
            if(c.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList() {    ///sender----чтоб знать от кого пришло сообщение
     StringBuilder sb = new StringBuilder("/clientlist");

        for (ClientHandler c : clients) {
            sb.append(" ").append(c.getNickname());
        }

        String msg=sb.toString();

        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }


    }



    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public AuthService getAuthService() {
        return authService;
    }
}
