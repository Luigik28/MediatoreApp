package it.imerc.mediatore.Game;

import androidx.annotation.NonNull;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import it.imerc.mediatore.util.Deserialization;


public class Carta implements Comparable<Carta>, Serializable, KvmSerializable {

    private static final long serialVersionUID = 1341106024248870488L;
    public int id;
    public boolean consegnata = false;
    public boolean giocata = false;

    public Carta() {
    }

    public Carta(SoapObject object) {
        new Deserialization().soapDeserilize(this, object);
    }

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

    @Override
    public int compareTo(Carta c) {
        return Integer.valueOf(getId()).compareTo(c.getId());
    }

    @Override
    public Object getProperty(int index) {
        switch (index) {
            case 0:
                return id;
            case 1:
                return consegnata;
            case 2:
                return giocata;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void setProperty(int index, Object value) {
        switch (index) {
            case 1:
                this.id = Integer.parseInt(value.toString());
                break;
            case 2:
                this.consegnata = Boolean.parseBoolean(value.toString());
                break;
            case 3:
                this.giocata = Boolean.parseBoolean(value.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "id";
                break;
            case 1:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "consegnata";
                break;
            case 2:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "giocata";
                break;
            default:
                break;
        }
    }
}
