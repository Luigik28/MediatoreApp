package it.imerc.mediatore.Game;

import android.os.Bundle;

import java.io.Serializable;

import it.imerc.mediatore.wsClient.operations.CreaPartitaOperation;
import it.imerc.mediatore.wsClient.operations.SetMonteOperation;
import it.imerc.mediatore.wsClient.operations.callback.BooleanCallback;
import it.imerc.mediatore.wsClient.operations.callback.StringCallback;

public class GameManager implements Serializable {

    public static String GAME_MANAGER = "gameManager";

    public static int PASSA = 1;
    public static int CHIAMA = 2;
    public static int SOLA = 3;
    public static int GIOCA_CARTA = 4;

    public static GameManager gameManager = new GameManager();
    private Giocatore io;
    private String idPartita;
    private boolean monte;
    private int nGiocatori = 0;
    private int idGiocatore;

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

    public Giocatore getHost() {
        return io;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameManager.GAME_MANAGER, this);
        return bundle;
    }

    public static GameManager getGameManager(Bundle bundle) {
        return (GameManager) bundle.getSerializable(GameManager.GAME_MANAGER);
    }

    public void creaPartita(String partita, String giocatore, final StringCallback callback) {
        io = new Giocatore(giocatore);
        setIdGiocatore(0);
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

    public void setIdGiocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
    }

    public int getIdGiocatore() {
        return idGiocatore;
    }
}
