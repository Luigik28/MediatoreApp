package it.imerc.mediatore.Game;

import it.imerc.mediatore.wsClient.operations.CreaPartitaOperation;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;

public class GameManager {

    private Giocatore io;
    private int idPartita;

    public void creaPartita(String nome) {
        new CreaPartitaOperation().doCall(nome, new IntegerCallback() {
            @Override
            public void onResponse(Integer response) {
                idPartita = response;
            }
        });
        io = new Giocatore(nome);
    }
}
