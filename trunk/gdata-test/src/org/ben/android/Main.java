package org.ben.android;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.data.Extension;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

public class Main {

	public static void makeRequestsVerbose() {
	    Logger httpRequestLogger =
	        Logger.getLogger(HttpGDataRequest.class.getName());

	    httpRequestLogger.setLevel(Level.FINEST);
	    ConsoleHandler handler = new ConsoleHandler();
	    handler.setLevel(Level.FINEST);
	    httpRequestLogger.addHandler(handler);
	    httpRequestLogger.setUseParentHandlers(false);
	}
	
	public static void createDoc(String token, String title, String plaintextBody) throws MalformedURLException, IOException, ServiceException {
		HttpGDataRequest.Factory f = new HttpGDataRequest.Factory();
		f.setHeader("Authorization", "GoogleLogin auth=" + token);
		f.setHeader("Slug", title);
		HttpGDataRequest r = (HttpGDataRequest) f.getRequest(RequestType.INSERT, new URL("http://docs.google.com/feeds/documents/private/full/"), ContentType.TEXT_PLAIN);
		r.getRequestStream().write(new String(plaintextBody).getBytes("UTF8"));
		r.execute();
	}
	
	public static String readDocAtom(String token, String url) throws MalformedURLException, IOException, ServiceException {
		HttpGDataRequest.Factory f = new HttpGDataRequest.Factory();
		f.setHeader("Authorization", "GoogleLogin auth=" + token);
		HttpGDataRequest r = (HttpGDataRequest) f.getRequest(RequestType.QUERY, new URL("http://docs.google.com/feeds/documents/private/full/document%3Adc7tmcmg_11dtkhj3fh/fncgg5xk"), ContentType.TEXT_PLAIN);
		r.execute();
		InputStreamReader reader = new InputStreamReader(r.getResponseStream());
		char[] buf = new char[1000];
		reader.read(buf);
		return buf.toString();
	}
	
	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, IOException, ServiceException {
		DocsService service = new DocsService("ben-gdata-test");
				
		// OAuth flow
		//service.setAuthSubToken(token);
		
		// U/p token flow
		// To get a u/p token post like this:
		// curl -d "accountType=GOOGLE&Email=EMAIL&Passwd=PASSWORD&service=writely&source=ben-gdata-test" -H "Content-type:application/x-www-form-urlencoded" https://www.google.com/accounts/ClientLogin
		//service.setUserToken(token);
		
		// U/p flow
		//service.setUserCredentials(username, password);
		
		DocumentListFeed feed = service.getFeed(new URL("http://docs.google.com/feeds/documents/private/full/-/document"),
			      DocumentListFeed.class);

/*		
        Old way of creating a document.  Couldn't get content to work this way and addFolder
        doesn't really work at all.
 		DocumentEntry newDocument = new DocumentEntry();
		newDocument.setTitle(new PlainTextConstruct("ur doing it right"));
		newDocument.setContent(new PlainTextConstruct("happy happy joy joy"));
		newDocument.addFolder(new Person("Android Notes"), "notes");
		service.insert(new URL("http://docs.google.com/feeds/documents/private/full/"), newDocument);
	*/	

		/**
		 * print out all the titles of your documents
		 */
		for (DocumentListEntry entry : feed.getEntries()) {
			entry.setTitle(new PlainTextConstruct("programming stff"));
			System.out.println(entry.getTitle().getPlainText());
		}


	}

}
