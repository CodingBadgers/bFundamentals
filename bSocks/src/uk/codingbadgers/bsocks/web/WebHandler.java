/**
 * bFundamentalsBuild 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bsocks.web;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

/**
 * The Class PostHandler.
 */
public class WebHandler extends Thread{

	/** The password for web to server communication. */
	private final String m_password;
	
	/** The web address, held as a url object. */
	private final URL m_webAddress;
	
	/** The post data to be sent along with the request. */
	private Map<String, String> m_data = new HashMap<String, String>();
	
	/** The logger to use. */
	private final Logger m_log;

	/** The request type to use */
	private RequestType m_type;
	
	/**
	 * Instantiates a new post handler.
	 *
	 * @param webAddress the web address of the script
	 * @param password the password
	 * @throws MalformedURLException if the url is in the wrong format
	 */
	public WebHandler(String webAddress, String password, RequestType type) throws MalformedURLException {
		m_webAddress = new URL(webAddress);
		m_password = password;
		m_log = Logger.getLogger("bSocks");
		m_type = type;
	}
	
	/**
	 * Create a MD5 hash based of a string.
	 *
	 * @param word the word
	 * @return the string
	 */
	private static String md5(String word) {
		try {
			// create the md5 digest, which will take the plain text word
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(word.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			
			// return the md5 hash
			return hash.toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		// an error occurred creating the md5 hash
		return "";
	}
	
	/**
	 * Put data into the post data.
	 *
	 * @param data the data
	 */
	public void put(Map<String, String> data) {
		m_data.putAll(data);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		String params = "password=" + md5(m_password) + "&server=" + Bukkit.getServerName();
		for (Map.Entry<String, String> entry : m_data.entrySet()) {
			params += "&" + entry.getKey() + "=" + entry.getValue();
		}
		
		String responce = sendPostRequest(params);
		m_log.info("Recieved responce from query: " + responce);
		m_data.clear();
	}

	/**
	 * Send post request.
	 *
	 * @param data the post data as a formatted string
	 * @return the string response
	 */
	private String sendPostRequest(String data) {
		try {
			HttpURLConnection con = (HttpURLConnection) m_webAddress.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod(m_type.toString());
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

            //write parameters
            writer.write(data);
            writer.flush();
            
            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();

            return answer.toString();
		} catch (FileNotFoundException ex) {
			m_log.warning("Could not find the file " + m_webAddress.getPath() + " on the server " + m_webAddress.getHost());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
