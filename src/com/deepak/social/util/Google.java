package com.deepak.social.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

public class Google {

	
	public void getGoogleToken(HttpServletRequest request,String CLIENT_ID, String APPLICATION_NAME) throws FileNotFoundException{
		// Create a state token to prevent request forgery.
		  // Store it in the session for later validation.
		  String state = new BigInteger(130, new SecureRandom()).toString(32);
		  request.getSession(true).setAttribute("state", state);
		  // Read index.html into memory, and set the client ID,
		  // token state, and application name in the HTML before serving it.
		  /*
		  return new Scanner(new File("index.html"), "UTF-8")
		      .useDelimiter("\\A").next()
		      .replaceAll("[{]{2}\\s*CLIENT_ID\\s*[}]{2}", CLIENT_ID)
		      .replaceAll("[{]{2}\\s*STATE\\s*[}]{2}", state)
		      .replaceAll("[{]{2}\\s*APPLICATION_NAME\\s*[}]{2}",
		                  APPLICATION_NAME);
         */
	}
	
	public void getGoogleToken(HttpServletRequest request) throws FileNotFoundException{
		// Create a state token to prevent request forgery.
		  // Store it in the session for later validation.
		  String state = new BigInteger(130, new SecureRandom()).toString(32);
		  request.getSession(true).setAttribute("state", state);
		  // Read index.html into memory, and set the client ID,
		  // token state, and application name in the HTML before serving it.
		  /*
		  return new Scanner(new File("index.html"), "UTF-8")
		      .useDelimiter("\\A").next()
		      .replaceAll("[{]{2}\\s*CLIENT_ID\\s*[}]{2}", CLIENT_ID)
		      .replaceAll("[{]{2}\\s*STATE\\s*[}]{2}", state)
		      .replaceAll("[{]{2}\\s*APPLICATION_NAME\\s*[}]{2}",
		                  APPLICATION_NAME);
         */
	}
}
