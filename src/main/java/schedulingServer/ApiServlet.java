package schedulingServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("http://kairos-api-docs.eu1.frbit.net/");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		boolean isCreate;
		
		if (uri == null) throw new IllegalArgumentException("Invalid path for post method");
		if (uri.endsWith("/")) uri = uri.substring(0, uri.length() - 1);
		if (uri.equals("/new")) {
			isCreate = true;
		} else if (uri.equals("/check")) {
			isCreate = false;
		} else {
			throw new IllegalArgumentException("Invalid path for post method");
		}
		
		// Processing post data (in json format)
		JSONObject jsonBody, jsonResponse = null;
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { /* report an error */
		}

		try {
			jsonBody = new JSONObject(jb.toString());
		} catch (JSONException e) {
			throw new IOException("Error parsing JSON request string");
		}
		
		try {
			if (isCreate) {
				jsonResponse = generateSchedule(jsonBody);
			} else {
				jsonResponse = checkSchedule(jsonBody);
			}
		} catch (JSONException e) {
			throw new IOException("Encountered JSONexception");
		}
		response.getWriter().print(jsonResponse.toString());
	}
	
	private JSONObject generateSchedule(JSONObject data) {
		// Work with the data using methods like...
		// int someInt = jsonObject.getInt("intParamName");
		// String someString = jsonObject.getString("stringParamName");
		// JSONObject nestedObj = jsonObject.getJSONObject("nestedObjName");
		// JSONArray arr = jsonObject.getJSONArray("arrayParamName");
		// etc...
		
		System.out.println("Get request for creating new schedule: " + data.toString())
		
		return data;
	}

	private JSONObject checkSchedule(JSONObject data) throws JSONException {
		JSONObject res = new JSONObject();
		
		System.out.println("Get request for checking schedule conflict: " + data.toString())
		
		res.put("unsatisfiedSoft", new ArrayList<String>());
		res.put("unsatisfiedHard", new ArrayList<String>());
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Add code for testing the module

	}

}
