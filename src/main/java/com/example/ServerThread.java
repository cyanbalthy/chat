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
    Socket socket = null;
    String stringRicevuta = null;
    String stringModificata = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    HashMap<String, ServerThread> listaUtenti;
    String nome="";
    ListaComandi comandi;
    ServerThread serverThread = null;
    boolean alive=false;
    boolean nomeEsiste=false;
    

    public ServerThread(Socket socket, HashMap<String, ServerThread> listaUtenti){
        this.socket=socket;
        this.listaUtenti = listaUtenti;
        try{
        inDalClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outVersoClient = new DataOutputStream(socket.getOutputStream());
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
            stringDivisa=stringRicevuta.split(comandi.suffix);
                switch(stringDivisa[0]){
                    case "/mesPriv": 
                        outVersoClient.writeBytes("metti il nome della persona a cui vuoi inviare il messaggio \n");
                        stringRicevuta=inDalClient.readLine();
                        serverThread = listaUtenti.get(stringRicevuta);
                        if(serverThread.isAlive()){
                            stringModificata = nome+") "+ stringDivisa[1]+"\n";
                            serverThread.outVersoClient.writeBytes(stringModificata);
                        }else{
                            outVersoClient.writeBytes(stringRicevuta + " non esiste \n");
                        }
                        break;
                    case "/mesTut":
                        for(String key: listaUtenti.keySet()){
                            if(key!=nome){
                                serverThread = listaUtenti.get(key);
                                stringModificata = nome+") "+ stringDivisa[1] + "\n";
                                serverThread.outVersoClient.writeBytes(stringModificata);
                            }
                        }
                        break;
                    case "/listUt":
                        outVersoClient.writeBytes("gli utenti collegati sono:\n");
                        for(String key: listaUtenti.keySet()){
                            outVersoClient.writeBytes(key + "\n");
                        }
                        break;
                    case "/listCom":
                            outVersoClient.writeBytes("Lista comandi:\n");
                            outVersoClient.writeBytes(
                            comandi.COMMAND_LISTCOMM + " lista dei comandi \n" + 
                            comandi.COMMAND_LISTUT + " lista utenti collegati \n" + 
                            comandi.COMMAND_MESPRIV + " prefisso messaggio privato \n" + 
                            comandi.COMMAND_MESPUBL + " prefisso messaggio pubblico \n" +
                            comandi.COMMAND_FIN + ", per disconnetterti \n");
                        break;
                    case "/FIN":
                        disconnessione();
                        break;
                    default:
                        outVersoClient.writeBytes("inserire un comando con la formula "+
                        comandi.COMMAND_FORMULA+
                        ", scrivere il comando "+ 
                        comandi.COMMAND_LISTCOMM+
                        " per la lista di tutti i comandi. \n");
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

            for(;;){
                stringRicevuta=inDalClient.readLine();
                for(String key: listaUtenti.keySet()){
                    if(key==stringRicevuta){
                        outVersoClient.writeBytes("nome esistente\n");
                        nomeEsiste=true;
                        break;
                    }else{
                        nomeEsiste=false;
                    }
                }
                if(nomeEsiste==false){
                    listaUtenti.put(stringRicevuta, this);
                    nome=stringRicevuta;
                    alive=true;
                    break;
                }else{
                    outVersoClient.writeBytes("inserire un nuovo nome utente: ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }    
        for(String key: listaUtenti.keySet()){
            serverThread = listaUtenti.get(key);
            try {
                serverThread.outVersoClient.writeBytes(nome + " si e' unito alla chat\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnessione(){
        try{
            alive=false;
            for(String key: listaUtenti.keySet()){
                if(key!=nome){
                    serverThread = listaUtenti.get(key);
                    try {
                        serverThread.outVersoClient.writeBytes(nome + " si e' disconnesso");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            serverThread = listaUtenti.get(nome);
            serverThread.outVersoClient.writeBytes("/FIN");
            listaUtenti.remove(nome);
            inDalClient.close();
            outVersoClient.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
