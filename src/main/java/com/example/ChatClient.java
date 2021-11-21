package com.example;
import java.net.*;
import java.util.Set;
import java.io.*;
import java.util.zip.DataFormatException;


public class ChatClient {

    int portaServer= 6789; //la porta
    Socket mioSocket;
    ServerSocket Server;
    BufferedReader tastiera;
    String stringaUtente;
    String stringaNomeUtente;
    String stringaRicevutadalserver;
    BufferedReader in; //stream input
    DataOutputStream out; //stream output
    String NomeServer = "localHost";

    public static void main( String[] args )
    {
        ChatClient cliente = new ChatClient();
        cliente.connetti();
        cliente.comunica();
    }
    
    public void comunica(){
       ClientThreadInput I = new ClientThreadInput(mioSocket, in, out, this);
       I.start();
        try {
            ClientThreadOutput O = new ClientThreadOutput(mioSocket, in);
            O.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public Socket connetti (){
        System.out.println("2 CLIENT partito in esecuzione ...");
        try{
            //per l'input da tastiera 
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            //creo un socket
            mioSocket = new Socket(NomeServer,portaServer);
            //associo due oggetti al mio socket per effettuare la scittura e la lettura
            out = new DataOutputStream(mioSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));
        } catch (UnknownHostException e){
            System.err.println("Host sconosciuto");
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione!");
            System.exit(1);
        }
     
        return mioSocket;        
    }
}