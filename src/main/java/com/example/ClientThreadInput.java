package com.example;
import java.net.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;


public class ClientThreadInput extends Thread{
    int portaServer= 6789; //la porta
    Socket mioSocket;
    BufferedReader tastiera;
    String stringaUtente;
    String stringaNomeUtente;
    String stringaRicevutadalserver;
    BufferedReader in; //stream input
    DataOutputStream out; //stream output
    Thread manda_mess = new Thread();
    ChatClient client;

    public ClientThreadInput(Socket mioSocket,BufferedReader in,DataOutputStream out, ChatClient client){
        this.mioSocket = mioSocket;
        this.in = in;
        this.out= out;
        this.client= client;
    }

    public void run(){

        try
        {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            do{
            System.out.println("utente, inserisci il tuo nome\n nessun carattere speciale ammesso\n");
            stringaNomeUtente = tastiera.readLine();
            if(stringaNomeUtente.matches("[!@#$%&*()_+=|<>?{}\\[\\]~-]")){
                System.out.println("Errore non si inserisce i caratteri speciali nel messaggio\n");
                }
            }while(stringaNomeUtente.matches("[!@#$%&*()_+=|<>?{}\\[\\]~-]"));
            out.writeBytes(stringaNomeUtente);

            String[] stringaDivisa;
            for(;;){
                System.out.println(" utente, inserisci la stringa da trasmettere al server:\n");
                stringaUtente = tastiera.readLine();
                //la spedisco al server
                System.out.println("invio il massaggio\n");
                out.writeBytes(stringaUtente+'\n');
                //leggo la risposta del server
                stringaDivisa=stringaUtente.split("@");
                if (stringaDivisa[0].equals("/FIN")){
                    out.close();
                    break;
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione col server!");
            System.exit(1);
        }
    }
}