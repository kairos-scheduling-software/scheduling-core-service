package schedulingServer;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import scheduler.Scheduler;

public class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		response.getWriter().write("ERROR: All api calls must be made with post");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String uri = request.getPathInfo();
		boolean isCreate = false;
		
		if (uri == null) 
		{
			response.getWriter().print("Error: Invalid path for post method");
		}
		if (uri.endsWith("/")) 
		{
			uri = uri.substring(0, uri.length() - 1);
		}
		if (uri.equals("/new")) 
		{
			isCreate = true;
			response.getWriter().print("Hello");
		} 
		else if (uri.equals("/check")) 
		{
			BufferedReader reader = request.getReader();
			StringBuffer jb = new StringBuffer();
			String line;
			
			while ((line = reader.readLine()) != null)
				jb.append(line);
			
			String json = jb.toString();
			
			try 
			{
				checkSchedule(json);
			} 
			catch (JSONException e) 
			{
				json = "Error: error parsing JSON request string";
			}
			
			response.getWriter().print(json);
		} 
		else if(uri.equals("/requestKey"))
		{
			//parse the response
			
			//generate key
			
			//register username to key
		}
		else 
		{
			response.getWriter().print("Error: The requested API path does not exist");
		}
	}
	
	private JSONObject generateSchedule(JSONObject data) throws JSONException {
		// Work with the data using methods like...
		// int someInt = jsonObject.getInt("intParamName");
		// String someString = jsonObject.getString("stringParamName");
		// JSONObject nestedObj = jsonObject.getJSONObject("nestedObjName");
		// JSONArray arr = jsonObject.getJSONArray("arrayParamName");
		// etc...
		
		System.out.println("Get request for creating new schedule: " + data.toString());
		
		Scheduler scheduler = new Scheduler(data);
		
		if (scheduler.findSolution())
			return scheduler.getSolution();
		
		return null;
	}

	private JSONObject checkSchedule(String json) throws JSONException {
		JSONObject res = new JSONObject(json);
		
		Scheduler scheduler = new Scheduler(res);
		
		scheduler.findSolution();
		
		return scheduler.getSolution();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Add code for testing the module

	}

}
