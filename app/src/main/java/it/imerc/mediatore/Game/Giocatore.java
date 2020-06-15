package it.imerc.mediatore.Game;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import it.imerc.mediatore.util.Deserialization;

public class Giocatore implements Serializable, KvmSerializable {

    private static final long serialVersionUID = 8197106193737329087L;
    public Mazzo mano;
    public String nome;

    public Giocatore() {
        this.mano = new Mazzo();
    }

    public Giocatore(String nome) {
        this.mano = new Mazzo();
        this.nome = nome;
    }

    public Giocatore(SoapObject object) {
        new Deserialization().soapDeserilize(this, object);
    }

    public void mettiInMano(Carta c) {
        mano.addCartaCoperta(c);
    }

    public Mazzo giocaCarta(Carta c) {
        int pos;
        for(pos = 0; pos < mano.getCarteInMano(); pos++ )
            if(c.equals(mano.getCarteCoperte().get(pos)))
                break;
        if(pos < mano.getCarteInMano())
            mano.consegnaCarta(pos);
        return mano;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public Mazzo getMano() {
        return mano;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Giocatore)
            return this.getNome().equals(((Giocatore) obj).getNome());
        return super.equals(obj);
    }

    @Override
    public Object getProperty(int index) {
        switch (index) {
            case 0:
                return mano;
            case 1:
                return nome;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void setProperty(int index, Object value) {
        switch (index) {
            case 1:
                this.mano = (Mazzo) value;
                break;
            case 2:
                this.nome = value.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.OBJECT_CLASS;
                info.name = "mano";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "nome";
                break;
            default:
                break;
        }
    }
}
