import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;

public class Main extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    if (req.getRequestURI().endsWith("/db")) {
      showDatabase(req,resp);
    } else {
      showHome(req,resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    StringBuffer jb = new StringBuffer();
    String line = null;
    try {
      BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null)
        jb.append(line);
    } catch (Exception e) { /*report an error*/ }

    try {
      JSONObject jsonObject = new JSONObject(jb.toString());
      response.getWriter().print(jsonObject.toString());
    } catch (JSONException e) {
		// TODO Auto-generated catch block
    	throw new IOException("Error parsing JSON request string");
	}

    // Work with the data using methods like...
    // int someInt = jsonObject.getInt("intParamName");
    // String someString = jsonObject.getString("stringParamName");
    // JSONObject nestedObj = jsonObject.getJSONObject("nestedObjName");
    // JSONArray arr = jsonObject.getJSONArray("arrayParamName");
    // etc...
  }

  private void showHome(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.getWriter().printf("%s\tHello from Java!", req.getRequestURI());
  }

  private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      Connection connection = getConnection();

      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      String out = "Hello!\n";
      while (rs.next()) {
          out += "Read from DB: " + rs.getTimestamp("tick") + "\n";
      }

      resp.getWriter().print(out);
    } catch (Exception e) {
      resp.getWriter().print("There was an error: " + e.getMessage());
    }
  }

  private Connection getConnection() throws URISyntaxException, SQLException {
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

    return DriverManager.getConnection(dbUrl, username, password);
  }

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    server.start();
    server.join();
  }
}
