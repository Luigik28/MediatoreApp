package it.imerc.mediatore.Game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Mazzo implements Serializable {

    private static final long serialVersionUID = -2966124301631969484L;
    private List<Carta> carteCoperte = new LinkedList<>();
    private List<Carta> carteConsegnate = new LinkedList<>();

    public Mazzo() {
    }

    public List<Carta> getCarteCoperte() {
        return carteCoperte;
    }

    public List<Carta> getCarteConsegnate() {
        return carteConsegnate;
    }

    public void addCartaCoperta(Carta c) {
        carteCoperte.add(c);
    }

    public Carta consegnaCarta(int pos) {
        Carta c = carteCoperte.remove(pos);
        carteConsegnate.add(c);
        return c;
    }

    public int getCarteInMano() {
        return getCarteCoperte().size();
    }

    public Carta getCartaRandom() {
        if (getCarteInMano() > 0)
            return consegnaCarta(new Random().nextInt(getCarteInMano()));
        else
            return null;
    }

    public Carta getTrionfo() {
        if (getCarteInMano() > 0)
            return carteCoperte.get(new Random().nextInt(getCarteInMano()));
        else
            return null;
    }
}
