package it.imerc.mediatore.wsClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Client {

    private static final String SOAP_ACTION = "http://mediatore-sviluppo.ddns.net/Mediatore/services/GameService/ciao";

    private static final String OPERATION_NAME = "ciao";

    private static final String WSDL_TARGET_NAMESPACE = "http://service.iMerc.it";

    private static final String SOAP_ADDRESS = "http://mediatore-sviluppo.ddns.net/Mediatore/services/GameService?wsdl";


    public Client() {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
                OPERATION_NAME);

        request.addProperty("n","valore");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        try {

            httpTransport.call(SOAP_ACTION, envelope);

            Object response = envelope.getResponse();

            System.out.println("WS: " + response.toString());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
