package nl.amis.rest.model;

import com.google.gson.Gson;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.core.MultivaluedMap;

import oracle.adf.share.logging.ADFLogger;

import nl.amis.rest.model.entities.Record;
import nl.amis.rest.model.entities.ITunesResult;

public class ITunesClient {
	
	private static ADFLogger logger = ADFLogger.createADFLogger(ITunesClient.class);
	
    public ITunesClient() {
    }

    private ClientConfig config = new DefaultClientConfig();
    private Client client = Client.create(config);

    public static final String MEDIA = "music";
    public static final int LIMIT = 100;
    public static final String URL =
        "http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZStoreServices.woa/wa/itmsSearch";

    public ITunesResult searchITunes(String search) {
    	logger.info("searh ITunes with "+search);

    	if ( search == null || "".equalsIgnoreCase(search)){
    		return null;
    	}
    	WebResource webResource = client.resource(URL);
        MultivaluedMap<String,String> queryParams = new MultivaluedMapImpl();
        queryParams.add("term", search);
        queryParams.add("media", MEDIA);
        queryParams.add("limit", Integer.toString(LIMIT));
        queryParams.add("output", "json");
        webResource = webResource.queryParams(queryParams);

        ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);
        logger.info("HTTP response status: "+response.getStatus());

        InputStream is = response.getEntityInputStream();
        Reader reader = new InputStreamReader(is);
        Gson gson = new Gson();
        ITunesResult itunesResult = null;
        itunesResult = gson.fromJson(reader, ITunesResult.class);
        logger.info("max results: "+itunesResult.getResultCount());
        logger.info("max records: "+itunesResult.getRecords().size());
        return itunesResult;
    }

    public static void main(String[] args) {
        ITunesClient iTunesClient = new ITunesClient();
        ITunesResult itunesResult = iTunesClient.searchITunes("public enemy");
        for ( Record record : itunesResult.getRecords() ) {
    	  logger.info(record.getArtistName() + " "+ record.getItemName());
        }
    }
}
