package it.imerc.mediatore.Game;

import android.os.Bundle;

import java.io.Serializable;

import it.imerc.mediatore.wsClient.operations.CreaPartitaOperation;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;

public class GameManager implements Serializable {

    public static String GAME_MANAGER = "gameManager";

    private Giocatore io;
    private int idPartita;

    public int getIdPartita() {
        return idPartita;
    }

    public Giocatore getHost() {
        return io;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameManager.GAME_MANAGER, this);
        return  bundle;
    }

    public static GameManager getGameManager(Bundle bundle) {
        return (GameManager) bundle.getSerializable(GameManager.GAME_MANAGER);
    }

    public void creaPartita(String nome, final IntegerCallback callback) {
        io = new Giocatore(nome);
        new CreaPartitaOperation().doCall(nome, new IntegerCallback() {
            @Override
            public void onResponse(Integer response) {
                idPartita = response;
                callback.onResponse(response);
            }
        });
    }
}
