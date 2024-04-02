import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/CheckBalance")
public class CheckBalance extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public CheckBalance() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		long AccountNumber=Long.parseLong(request.getParameter("Accno"));
		String Name=request.getParameter("nme");
		String Password=request.getParameter("psw");
		try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ghdb","ghdb");
				PreparedStatement ps=con.prepareStatement("select * from bank where AccountNumber=? and Name=? and Password=?");
				ps.setLong(1, AccountNumber);
				ps.setString(2,Name);
				ps.setString(3, Password);
				ResultSet rs=ps.executeQuery();
				ResultSetMetaData rsmd=rs.getMetaData();
				out.print("<table border='1'>");
				int n=rsmd.getColumnCount();
				for(int i=1;i<=n;i++) {
					out.println("<th> <font color=red size=4>"+"<br>"+rsmd.getColumnName(i));
					out.println("<tr>");
					while(rs.next()) {
						for(i=1;i<=n;i++) {
							out.println("<td><br>"+rs.getString(i));
						}
						out.println("<tr>");
					}
					out.print("</table>");
					con.close();
				}
			} catch (Exception e) {
				out.print(e);
			}
	}

}
