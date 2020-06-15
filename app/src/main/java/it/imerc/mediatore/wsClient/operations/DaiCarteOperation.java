package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.Game.Mazzo;
import it.imerc.mediatore.wsClient.MediatoreOperation;

public class DaiCarteOperation extends MediatoreOperation<Mazzo> {

    @Override
    public String getOperationName() {
        return "daiCarte";
    }

    public void doCall(Integer id, MazzoCallback callback) {
        this.addProperty("id", id);
        super.call(callback);
    }
}
