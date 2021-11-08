package com.example;
import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread{
    ServerSocket server = null;
    Socket client = null;
    String stringRicevuta = null;
    String stringModificata = null;
    char charRicevuta;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    HashMap<String, ServerThread> listaUtenti;
    

    public ServerThread(Socket client, HashMap<String, ServerThread> listaUtenti){
        this.client=client;
        this.listaUtenti = listaUtenti;
    }

    public void run(){
        try{
            comunica();
        }catch(Exception e){
            System.out.println("" + e.getMessage());
        }
    }

    public void comunica() throws Exception{

        // nome client
        



        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outVersoClient = new DataOutputStream(client.getOutputStream());
        outVersoClient.writeBytes("inserire nome utente: ");
        stringRicevuta=inDalClient.readLine();
        listaUtenti.put(stringRicevuta, this);
        for(;;){
            stringRicevuta=inDalClient.readLine();
            do{
                int i=0;
                charRicevuta=stringRicevuta.charAt(i);
            }while(charRicevuta== 9);
            if(stringRicevuta.equals("")){

            }
        }
    }
}
