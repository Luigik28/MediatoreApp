package it.imerc.mediatore.Game;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class Carta implements Comparable<Carta>, Serializable {

    private static final long serialVersionUID = 1341106024248870488L;
    public int id;
    public boolean consegnata = false;
    public boolean giocata = false;

    public Carta(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        int n = id % 10;
        return n == 0 ? 10 : n;
    }

    public String getSeme() {
        String tipo = "";
        switch (--id / 10) {
            case 0:
                tipo = "denare";
                break;
            case 1:
                tipo = "coppe";
                break;
            case 2:
                tipo = "mazze";
                break;
            case 3:
                tipo = "spade";
                break;
        }
        return tipo;
    }

    public boolean isConsegnata() {
        return consegnata;
    }

    public void setConsegnata(boolean consegnata) {
        this.consegnata = consegnata;
    }

    public boolean isGiocata() {
        return giocata;
    }

    public void setGiocata(boolean giocata) {
        this.giocata = giocata;
    }

    @NonNull
    @Override
    public String toString() {
        return getNumero() + " di " + getSeme();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(Carta c) {
        return Integer.compare(getId(), c.getId());
    }
}
