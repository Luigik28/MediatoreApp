package it.imerc.mediatore.wsClient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import it.imerc.mediatore.Game.Giocatore;
import it.imerc.mediatore.Game.Mazzo;

public abstract class MediatoreOperation<T> {

    public interface SoapCallback<T> {
        void onResponse(T response);

        T parseRequest(AttributeContainer attributeContainer);

        void onError();
    }

    public abstract static class IntegerCallback implements SoapCallback<Integer> {
        @Override
        abstract public void onResponse(Integer response);

        @Override
        public void onError() {

        }

        @Override
        public Integer parseRequest(AttributeContainer attributeContainer) {
            SoapPrimitive soapPrimitive = (SoapPrimitive) attributeContainer;
            return Integer.valueOf(soapPrimitive.getValue().toString());
        }
    }

    public abstract static class BooleanCallback implements SoapCallback<Boolean> {
        @Override
        abstract public void onResponse(Boolean response);

        @Override
        public void onError() {

        }

        @Override
        public Boolean parseRequest(AttributeContainer attributeContainer) {
            SoapPrimitive soapPrimitive = (SoapPrimitive) attributeContainer;
            return Boolean.valueOf(soapPrimitive.getValue().toString());
        }
    }

    public abstract static class MazzoCallback implements SoapCallback<Mazzo> {
        @Override
        abstract public void onResponse(Mazzo response);

        @Override
        public void onError() {

        }

        @Override
        public Mazzo parseRequest(AttributeContainer attributeContainer) {
            SoapPrimitive soapPrimitive = (SoapPrimitive) attributeContainer;
            return new Mazzo(soapPrimitive.getValue().toString());
        }
    }

    public abstract static class GiocatoreCallback implements SoapCallback<Giocatore> {
        @Override
        abstract public void onResponse(Giocatore response);

        @Override
        public void onError() {

        }

        @Override
        public Giocatore parseRequest(AttributeContainer attributeContainer) {
            SoapObject soapObject = (SoapObject) attributeContainer;
            return new Giocatore(soapObject);
        }
    }

    private static final String WSDL_TARGET_NAMESPACE = "http://service.iMerc.it";

    private static String serverUrl = "http://192.168.1.59:8080";

    private static final String SOAP_ADDRESS_NO_URL = "/Mediatore/services/GameService?wsdl";

    private SoapObject request;

    private SoapSerializationEnvelope envelope;

    public abstract String getOperationName();

    public static String getSoapAddress() {
        return serverUrl + SOAP_ADDRESS_NO_URL;
    }

    public String getSoapAction() {
        return serverUrl + "/Mediatore/services/GameService/" + getOperationName();
    }

    public MediatoreOperation() {
        request = new SoapObject(WSDL_TARGET_NAMESPACE,
                getOperationName());
        envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
    }

    protected SoapObject addProperty(String propertyName, Object propertyValue) {
        return request.addProperty(propertyName, propertyValue);
    }

    protected <R extends SoapCallback<T>> void call(R callback) {
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(getSoapAddress());
        new SoapCallTask(callback).execute(httpTransport);
    }

    @SuppressLint("StaticFieldLeak")
    private class SoapCallTask extends AsyncTask<HttpTransportSE, Void, AttributeContainer> {

        final SoapCallback<T> callback;

        private SoapCallTask(SoapCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        protected AttributeContainer doInBackground(HttpTransportSE... httpTransport) {
            AttributeContainer result;
            try {
                httpTransport[0].call(getSoapAction(), envelope);
                result = (AttributeContainer) envelope.getResponse();
            } catch (SoapFault fault) {
                fault.printStackTrace();
                result = new SoapObject();
                result.addAttribute("exception",fault.faultcode);
                result.addAttribute("message",fault.faultstring);
            } catch (IOException e) {
                e.printStackTrace();
                result = new SoapObject();
                result.addAttribute("exception",e.getClass().toString());
                result.addAttribute("message",e.getMessage());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                result = new SoapObject();
                result.addAttribute("exception",e.getClass().toString());
                result.addAttribute("message",e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(AttributeContainer result) {
            if (result == null)
                callback.onError();
            else
                callback.onResponse(callback.parseRequest(result));
        }
    }


}
