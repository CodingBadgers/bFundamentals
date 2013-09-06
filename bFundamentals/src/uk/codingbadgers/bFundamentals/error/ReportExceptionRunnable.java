/**
 * bFundamentals 1.2-SNAPSHOT
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
package uk.codingbadgers.bFundamentals.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class ReportExceptionRunnable {

	private Throwable throwable;

	public ReportExceptionRunnable(Throwable ex) {
		this.throwable = ex;
	}
	
	public boolean run() {
		try {
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("password", DigestUtils.md5Hex(bFundamentals.getConfigurationManager().getCrashPassword())));
			data.add(new BasicNameValuePair("project", "bFundamentals"));
			data.add(new BasicNameValuePair("cause", getException(throwable)));
			data.add(new BasicNameValuePair("message", getMessage(throwable)));
			data.add(new BasicNameValuePair("st", buildStackTrace(throwable)));
			HttpPost post = new HttpPost("http://server.mcbadgercraft.com/crash/report.php");
			post.setEntity(new UrlEncodedFormEntity(data));
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse responce = client.execute(post);
            String result = EntityUtils.toString(responce.getEntity());
            
            if (bFundamentals.getConfigurationManager().isDebugEnabled()) System.out.println(result);
            
            JSONObject object = (JSONObject) new JSONParser().parse(result);
            boolean success = (Boolean) object.get("success");
            if (!success) System.err.println(object.get("error"));
            return success;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private String getException(Throwable cause) {
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		
		return cause.getClass().getName();
	}

	private String getMessage(Throwable cause) {
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		
		return cause.getMessage();
	}
	
	private String buildStackTrace(Throwable cause) {
		StringWriter writer = new StringWriter();
		cause.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}
}
