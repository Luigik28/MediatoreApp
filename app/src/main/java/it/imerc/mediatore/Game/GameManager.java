package it.imerc.mediatore.Game;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.CreaPartitaOperation;

public class GameManager {

    private Giocatore io;
    private int idPartita;

    public void creaPartita(String nome) {
        new CreaPartitaOperation().doCall(nome, new MediatoreOperation.IntegerCallback() {
            @Override
            public void onResponse(Integer response) {
                idPartita = response;
            }
        });
        io = new Giocatore(nome);
    }



}
