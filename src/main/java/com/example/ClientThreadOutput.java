package com.example;
import java.net.*;
import java.io.*;
import java.util.zip.DataFormatException;


public class ClientThreadOutput extends Thread{
    ClientThreadInput input = null;
    Socket mioSocket;
    String stringaUtente;
    BufferedReader in; //stream input
    String stringaRicevutadalserver;
    ChatClient client;

    public ClientThreadOutput(Socket mioSocket, BufferedReader in){
        this.mioSocket = mioSocket;
        this.in=in;
    }

    public void run(){
        for(;;){
        try {
            stringaRicevutadalserver=in.readLine();
            System.out.println(stringaRicevutadalserver+"\n");
        if (stringaRicevutadalserver.equals("/FIN")){
            in.close();
            mioSocket.close();
            break;
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    }
}