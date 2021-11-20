package com.example;
import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.RowFilter.Entry;
import javax.swing.text.Keymap;
import javax.xml.crypto.dsig.keyinfo.KeyName;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class ServerThread extends Thread{
    ServerSocket server = null;
    Socket client = null;
    String stringRicevuta = null;
    String stringModificata = null;
    char charRicevuta;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    HashMap<String, ServerThread> listaUtenti;
    String nome="";
    ListaComandi comandi;
    ServerThread serverThread = null;
    boolean alive=false;
    

    public ServerThread(Socket client, HashMap<String, ServerThread> listaUtenti){
        this.client=client;
        this.listaUtenti = listaUtenti;
        try{
        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outVersoClient = new DataOutputStream(client.getOutputStream());
        }catch(Exception e){
            System.out.println(e);
        }
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

        
        login();
        String[] stringDivisa;
        while(alive){
            stringRicevuta=inDalClient.readLine();
            stringDivisa=stringRicevuta.split("@");
                switch(stringDivisa[0]){
                    case "/mesPriv": 
                        outVersoClient.writeBytes("metti il nome della persona a cui vuoi inviare il messaggio");
                        stringRicevuta=inDalClient.readLine();
                        serverThread = listaUtenti.get(stringRicevuta);
                        stringModificata = nome+") "+ stringDivisa[1];
                        serverThread.outVersoClient.writeBytes(stringModificata);
                        break;
                    case "/mesTut":
                        for(String key: listaUtenti.keySet()){
                            serverThread = listaUtenti.get(key);
                            stringModificata = nome+") "+ stringDivisa[1];
                            serverThread.outVersoClient.writeBytes(stringModificata);
                        }
                        break;
                    case "/listUt":
                        for(String key: listaUtenti.keySet()){
                            outVersoClient.writeBytes("gli utenti collegati sono:/n");
                            outVersoClient.writeBytes(key + "/n");
                        }
                        break;
                    case "/listCom":
                            outVersoClient.writeBytes("Lista comandi:/n");
                            outVersoClient.writeBytes(
                            comandi.COMMAND_LISTCOMM + " lista dei comandi/n" + 
                            comandi.COMMAND_LISTUT + " lista utenti collegati/n" + 
                            comandi.COMMAND_MESPRIV + " prefisso messaggio privato/n" + 
                            comandi.COMMAND_MESPUBL + " prefisso messaggio pubblico/n" +
                            comandi.COMMAND_FIN + ", per disconnetterti/n");
                        break;
                    case "/FIN":
                        disconnessione();
                        break;
                    default:
                        outVersoClient.writeBytes("inserire un comando con la formula "+
                        comandi.COMMAND_FORMULE+
                        ", scrivere il comando "+ 
                        comandi.COMMAND_LISTCOMM+
                        " per la lista di tutti i comandi.");
                        break;
                }

        }



    }

    //Set<String> allClientNames = listaUtenti.keySet();

        /*
        String nomeDest = "pippo";
        String msg = nomeDest + "xxxxxx";

        ServerThread serverThread = listaUtenti.get(nomeDest); // -> ServerThread che gestisce il client "pippo"
        serverThread.inviaMessaggio(msg);*/

    public void login(){
        try {
            outVersoClient.writeBytes("inserire nome utente: ");
            stringRicevuta=inDalClient.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }    
        listaUtenti.put(stringRicevuta, this);
        nome=stringRicevuta;
        alive=true;
    }

    public void disconnessione(){
        try{
            alive=false;
            listaUtenti.remove(nome);
            inDalClient.close();
            outVersoClient.close();
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
