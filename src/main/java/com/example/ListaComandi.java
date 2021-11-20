package com.example;

public class ListaComandi {
    static final String prefix= "/";
    static final String suffix= "@";

    static final String messaggioPublico = "mesTut";
    static final String messaggioPrivato = "mesPriv";
    static final String listaUtenti = "listUt";
    static final String listaComandi = "listCom";
    static final String fineConnessione="FIN";

    public static final String COMMAND_MESPRIV = prefix + messaggioPrivato + suffix;
    public static final String COMMAND_MESPUBL = prefix + messaggioPublico + suffix;
    public static final String COMMAND_LISTUT = prefix + listaUtenti + suffix;
    public static final String COMMAND_LISTCOMM = prefix + listaComandi + suffix;
    public static final String COMMAND_FIN = prefix + fineConnessione + suffix;
    public static final String COMMAND_FORMULE = prefix + "comando" + suffix;
}