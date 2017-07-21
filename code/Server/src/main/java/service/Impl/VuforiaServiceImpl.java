package service.Impl;

import service.VuforiaService;
import tool.ByteFileWithName;
import tool.SignatureBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class VuforiaServiceImpl implements VuforiaService {
	
	private String accessKey = "0585ba24169c7e5422b49ed19c9e098afb7ed3bd";
	private String secretKey = "cfa8abf6ad402cb757f8b54346e8f6a177c8eab9";
	
	private String url = "https://vws.vuforia.com";
	
	public String uploadImage(ByteFileWithName target) throws URISyntaxException, ParseException, IOException {
		HttpPost postRequest = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		postRequest.setURI(new URI(url + "/targets"));
		JSONObject requestBody = new JSONObject();
		
		setRequestBody(requestBody, target);
		postRequest.setEntity(new StringEntity(requestBody.toString()));
		setHeaders(postRequest);
		
		HttpResponse response = client.execute(postRequest);
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println(responseBody);
		
		JSONObject jobj = new JSONObject(responseBody);
		
		String uniqueTargetId = jobj.has("target_id") ? jobj.getString("target_id") : "";
		System.out.println("\nCreated target with id: " + uniqueTargetId);
		
		return uniqueTargetId;
	}
	
	public JSONObject getTarget(String targetID) 
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpGet getRequest = new HttpGet();
		HttpClient client = new DefaultHttpClient();
		getRequest.setURI(new URI(url + "/targets/" + targetID));
		setHeadersForGet(getRequest);
		
		HttpResponse response = client.execute(getRequest);
		
		JSONObject getTargetResultJSON = null;
		
		if( response != null ){
			
			try {
				
				getTargetResultJSON =  new JSONObject( EntityUtils.toString(response.getEntity()) );
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("getTarget() = "+ getTargetResultJSON.toString());
		
		return getTargetResultJSON;
	}
	
	public void setRequestBody(JSONObject requestBody, ByteFileWithName target){
		requestBody.put("name", target.getFileName()); // Mandatory
		requestBody.put("width", 1); // Mandatory
		requestBody.put("image", Base64.encodeBase64String(target.getByteElement())); // Mandatory
		requestBody.put("active_flag", 1); // Optional
	}
	
	public void setHeaders(HttpUriRequest request){
		SignatureBuilder sb = new SignatureBuilder();
		request.setHeader(new BasicHeader("Date", DateUtils.formatDate(new Date()).replaceFirst("[+]00:00$", "")));
		request.setHeader(new BasicHeader("Content-Type", "application/json"));
		request.setHeader("Authorization", "VWS " + accessKey + ":" + sb.tmsSignature(request, secretKey));
	}
	
	public void setHeadersForGet(HttpUriRequest request){
		SignatureBuilder sb = new SignatureBuilder();
		request.setHeader(new BasicHeader("Date", DateUtils.formatDate(new Date()).replaceFirst("[+]00:00$", "")));
		request.setHeader("Authorization", "VWS " + accessKey + ":" + sb.tmsSignature(request, secretKey));
	}
}
