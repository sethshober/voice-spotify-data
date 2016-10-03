package com.mkyong.core;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class SetupDb {
	
	private static MongoClient mongo;
	private static DB db;
	private static DBCollection collection;
	public String uriLocal = "localhost";
	public int port = 27017;
	
	void connectToMongo(String url, int port) throws UnknownHostException {
		try {
			mongo = new MongoClient(url, port);
			System.out.println("Successfully connected to Mongo.");
    	} catch (UnknownHostException e) {
    		e.printStackTrace();
    	} catch (MongoException e) {
    		e.printStackTrace();
    	}
		
	}
	
	void getDatabase(String dbName) {
		db = mongo.getDB(dbName);
		System.out.printf("Using %s database.\n", dbName);
	}
	
	DBCollection getCollection(String collectionName) {
		collection = db.getCollection(collectionName);
		System.out.printf("Using %s collection.\n", collectionName);
		return collection;
	}
	
}
