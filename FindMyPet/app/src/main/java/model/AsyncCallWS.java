package model;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Alumne on 03/05/2016.
 */
public class AsyncCallWS extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {

      login();


        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    public String login(){
        //llamara al webservice, que a su vez llamara al ado
        String result="";


        String NAMESPACE = "http://ws.findmypet/";
        String URL="http://localhost:8080/FindMyPetWS/WSfindmypet";//PONER WSDL
        String METHOD_NAME = "WSfindmypet";
        String SOAP_ACTION = "http://localhost:8080/FindMyPetWS/WSfindmypet";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("arg0", "admin");
        request.addProperty("arg1", "admin");

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);


        try {
            transporte.call(SOAP_ACTION, envelope);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        SoapPrimitive resultado_xml = null;
        try {
            resultado_xml = (SoapPrimitive)envelope.getResponse();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        }
        result = resultado_xml.toString();




      /*  if(user != null){
            return 1;
        }else {
            return 0;
        }*/

        return result;
    }

}
