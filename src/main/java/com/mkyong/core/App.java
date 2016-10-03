package com.mkyong.core;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;



public class App {
	
	private static DBCollection search_queries;
	
    public static void main( String[] args ) throws IOException {
    	
    	HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new SaveSearchQuery());
        server.createContext("/queries", new GetQueries());
        server.setExecutor(null); // creates a default executor
        server.start();
        
        SetupDb dbSetup = new SetupDb();
        
        dbSetup.connectToMongo(dbSetup.uriLocal , dbSetup.port);
        dbSetup.getDatabase("javatestdb");
        search_queries = dbSetup.getCollection("search_queries");
      
    }
    
    static class SaveSearchQuery implements HttpHandler {
       
    	@Override
        public void handle(HttpExchange t) throws IOException {
        	System.out.println(t.getRequestURI());
        	String uri = t.getRequestURI().toString();
        	String uriQuery = formatUriQuery(uri);
            
            BasicDBObject query = new BasicDBObject();
            query.put("query", uriQuery);
            search_queries.insert(query);        	

        	String response = "Saved query: " + query;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        String formatUriQuery(String query) {
        	int start = query.indexOf("q=");
        	if(start != -1) {
        		query = query.substring(start + 2).replace('+', ' ');
        		System.out.println(query);
        	}
        	return query;
        }
    }
    
    static class GetQueries implements HttpHandler {

		@Override
        public void handle(HttpExchange t) throws IOException {

            String response = "";
            DBCursor queries = search_queries.find().limit(1000);
            while (queries.hasNext()) {
            	System.out.println(queries.next());
            	response += queries.next() + "\n";
            }
       
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
}
    
