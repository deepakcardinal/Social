package com.deepak.social.google;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;

public class GoogleAuthServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8081192467055329555L;

	private static final HttpTransport TRANSPORT = new NetHttpTransport();

	private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (!request.getParameter("state").equals(
				request.getSession().getAttribute("state"))) {
			response.setStatus(401);
			// return GSON.toJson("Invalid state parameter.");
		} else {
			try {
				
				String jsonString =sendPost("https://accounts.google.com/o/oauth2/token",createTokenURL(request.getParameter("code"), Constants.CLIENT_ID,
						Constants.CLIENT_SECRET, Constants.REDIRECT_URIS,
						"authorization_code"));
				TokenStatus ts =parseTorkenJson(jsonString);
				
				///////////////////////////////
				
			String meparameters	=createAboutMEURL(request.getParameter("code"), Constants.API_KEY);
			getUsetInfo("https://www.googleapis.com/plus/v1/people/"+ts.gplus_id,meparameters,ts);
			request.setAttribute("ts",ts);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/WEB-INF/jsp/googleResponse.jsp");

		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);

	}

	private String createTokenURL(String code, String client_id, String client_secret,
			String redirect_uri, String grant_type){
		
		String urlParameters = "code=" + code + "&";
		urlParameters = urlParameters + "client_id=" + client_id + "&";
		urlParameters = urlParameters + "client_secret=" + client_secret + "&";
		urlParameters = urlParameters + "redirect_uri=" + redirect_uri + "&";
		urlParameters = urlParameters + "grant_type=" + grant_type;
		return urlParameters;
	}
	
	private String createAboutMEURL(String access_token, String key){
		
		String urlParameters = "";//"access_token=" + access_token + "&";
		urlParameters = urlParameters + "key=" + key ;
		
		return urlParameters;
	}
	
	
	private void getUsetInfo(String url,String parameters,TokenStatus ts) throws Exception {
		 
		
 
		URL obj = new URL(url+"?"+parameters);
		System.out.println("----------------");
		System.out.println(url+"?"+parameters);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
		String resp=response.toString();
		
		
		final JSONObject json = new JSONObject(resp);
		
		ts.setGender((String)json.get("gender"));
		
		JSONObject jsonName = (JSONObject)json.get("name");
		ts.setFirstName((String)jsonName.get("givenName"));
		ts.setLastName((String)jsonName.get("familyName"));
		
	}
	
	private String sendPost(String url, String urlParameters) throws Exception {

		//String url = "";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		String jsonString = response.toString();
		System.out.println(response.toString());
		return jsonString;
		
	}
	
	private TokenStatus parseTorkenJson(String jsonString) throws JSONException{
		
		final JSONObject json = new JSONObject(jsonString);
		// final JSONArray geodata = json.getJSONArray("geodata");
		// final int n = geodata.length();
		// for (int i = 0; i < n; ++i) {
		// final JSONObject person = geodata.getJSONObject(i);

		System.out.println(json.get("access_token"));
		System.out.println(json.get("token_type"));
		System.out.println(json.get("id_token"));
		System.out.println(json.get("expires_in"));
		// }

		String idToken = (String) json.get("id_token");
		String accessToken = (String) json.get("access_token");
        
		TokenStatus idStatus = new TokenStatus();
		if (idToken != null) {
			// Check that the ID Token is valid.
			Checker checker = new Checker(new String[] { Constants.CLIENT_ID },
					Constants.CLIENT_ID);
			GoogleIdToken.Payload jwt = checker.check(idToken);
			if (jwt == null) {
				// This is not a valid token.
				idStatus.setValid(false);
				idStatus.setId("");
				idStatus.setMessage("Invalid ID Token.");
			} else {
				idStatus.setValid(true);
				String gplusId = (String) jwt.get("sub");
				idStatus.setId(gplusId);
				idStatus.setEmail((String) jwt.get("email"));
				idStatus.setMessage("ID Token is valid.");
			}
		} else {
			idStatus.setMessage("ID Token not provided");
		}
		
		TokenStatus accessStatus = new TokenStatus();
		if (accessToken != null) {
			// Check that the Access Token is valid.
			try {
				GoogleCredential credential = new GoogleCredential()
						.setAccessToken(accessToken);
				Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY,
						credential).build();
				Tokeninfo tokenInfo = oauth2.tokeninfo()
						.setAccessToken(accessToken).execute();
				if (tokenInfo.containsKey("error")) {
					// This is not a valid token.
					accessStatus.setValid(false);
					accessStatus.setId("");
					accessStatus.setMessage("Invalid Access Token.");
				} else if (!tokenInfo.getIssuedTo().equals(Constants.CLIENT_ID)) {
					// This is not meant for this app. It is VERY important to
					// check
					// the client ID in order to prevent man-in-the-middle
					// attacks.
					accessStatus.setValid(false);
					accessStatus.setId("");
					accessStatus
							.setMessage("Access Token not meant for this app.");
				} else {
					accessStatus.setValid(true);
					accessStatus.setId(tokenInfo.getUserId());
					accessStatus.setMessage("Access Token is valid.");
				}
			} catch (IOException e) {
				accessStatus.setValid(false);
				accessStatus.setId("");
				accessStatus.setMessage("Invalid Access Token.");
			}
		} else {
			accessStatus.setMessage("Access Token not provided");
		}
		return idStatus;
	}
}
