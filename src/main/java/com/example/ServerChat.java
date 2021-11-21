package com.example;
import java.net.*;
import java.io.*;
import java.util.*;

public class ServerChat{

    HashMap<String, ServerThread> listaUtenti = new HashMap();

    public static void main( String[] args )
    {
        ServerChat s = new ServerChat();
        s.start();
    }

    public void start(){
        try{
            ServerSocket serverSocket = new ServerSocket(6789);
            for(;;){
                System.out.println("1 server in attesa ...");
                Socket socket = serverSocket.accept();
                System.out.println("3 server socket " + socket);
                ServerThread serverThread = new ServerThread(socket, listaUtenti);
                serverThread.start();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server !");
            System.exit(1);
        }
    }

}