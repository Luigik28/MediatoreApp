package it.imerc.mediatore.Game;

import android.os.Bundle;

import java.io.Serializable;

import it.imerc.mediatore.wsClient.operations.CreaPartitaOperation;
import it.imerc.mediatore.wsClient.operations.SetMonteOperation;
import it.imerc.mediatore.wsClient.operations.callback.BooleanCallback;
import it.imerc.mediatore.wsClient.operations.callback.StringCallback;

public class GameManager implements Serializable {

    public static String GAME_MANAGER = "gameManager";

    public static final int TIMEOUT = 0;
    public static final int PASSA = -1;
    public static final int CHIAMA = -2;
    public static final int SOLA = -3;

    public static final int MAX_TEMPO = 30000;

    public static GameManager gameManager = new GameManager();
    private Giocatore io;
    private String idPartita;
    private boolean monte;
    private int nGiocatori = 0;
    private int giocatoreAttivo;
    public int myId;

    private GameManager() {}

    public static GameManager getGameManager() {
        return gameManager == null ? new GameManager() : gameManager;
    }

    public String getIdPartita() {
        return idPartita;
    }

    public void setIdPartita(String id) {
        this.idPartita = id;
    }

    public Giocatore getGiocatore() {
        return io;
    }

    public boolean isMyTurn() {
        return myId == getGiocatoreAttivo();
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameManager.GAME_MANAGER, this);
        return bundle;
    }

    public static GameManager getGameManager(Bundle bundle) {
        return (GameManager) bundle.getSerializable(GameManager.GAME_MANAGER);
    }

    public void creaPartita(String partita, String giocatore, int nGiocatori, final StringCallback callback) {
        io = new Giocatore(giocatore);
        setnGiocatori(nGiocatori);
        setGiocatoreAttivo(0);
        myId = getGiocatoreAttivo();
        new CreaPartitaOperation().doCall(partita, giocatore, new StringCallback() {
            @Override
            public void onResponse(String response) {
                idPartita = response;
                callback.onResponse(response);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                callback.onError(message);
            }
        });
    }

    public void setMonte(Boolean monte, final BooleanCallback callback) {
        new SetMonteOperation().doCall(getIdPartita(), monte, new BooleanCallback() {
            @Override
            public void onResponse(Boolean response) {
                GameManager.this.monte = response;
                callback.onResponse(response);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                callback.onError(message);
            }
        });
    }

    public boolean getMonte() {
        return  monte;
    }

    public int getnGiocatori() {
        return nGiocatori;
    }

    public void setnGiocatori(int nGiocatori) {
        this.nGiocatori = nGiocatori;
    }

    public void setGiocatoreAttivo(int giocatoreAttivo) {
        this.giocatoreAttivo = giocatoreAttivo;
    }

    public int getGiocatoreAttivo() {
        return giocatoreAttivo;
    }

    public int getMaxTempo() {
        return MAX_TEMPO;
    }
}
