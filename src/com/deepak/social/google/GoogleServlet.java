package com.deepak.social.google;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deepak.social.util.Google;

public class GoogleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889905484906388997L;

	

	/* Consent form Creation is important else u will get an error
	 * https://accounts.google.com/o/oauth2/auth?
	 * client_id=424911365001.apps.googleusercontent.com&
	 * //930679742182-b38883gdl6ajudako5f5vgm1agniu4m1
	 * .apps.googleusercontent.com response_type=code& scope=openid%20email&
	 * redirect_uri=https://oa2cb.example.com/&
	 * state=security_token%3D138r5719ru3e1
	 * %26url%3Dhttps://oa2cb.example.com/myHome& login_hint=jsmith@example.com&
	 * openid.realm=example.com& // hd=example.com //OPtional
	 */

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/WEB-INF/jsp/g.jsp");
		StringBuffer googleAuthUrl = new StringBuffer();

		new Google().getGoogleToken(request);
		String state = (String) request.getSession().getAttribute("state");
		googleAuthUrl.append("https://accounts.google.com/o/oauth2/auth?");
		googleAuthUrl.append("client_id=" + Constants. CLIENT_ID + "&");
		googleAuthUrl.append("response_type=" + "code" + "&");
		googleAuthUrl.append("redirect_uri=" + Constants.REDIRECT_URIS + "&");
		googleAuthUrl.append("state=" + state + "&");
		//googleAuthUrl.append("scope=https://www.googleapis.com/auth/plus.login");
		googleAuthUrl.append("scope=openid%20profile%20email");
		 
		System.out.println("googleAuthUrl----" + googleAuthUrl);
		request.setAttribute("googleAuthUrl", googleAuthUrl.toString());
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);

	}
	
	
	

}
