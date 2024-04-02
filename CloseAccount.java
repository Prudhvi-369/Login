import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/CloseAccount")
public class CloseAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public CloseAccount() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		Long AccountNumber=Long.parseLong(request.getParameter("Accno"));
		String Name=request.getParameter("nme");
		String Password=request.getParameter("psw");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ghdb","ghdb");
			PreparedStatement ps=con.prepareStatement("delete from bank where AccountNumber=? and Name=? and Password=?");
			ps.setLong(1,AccountNumber);
			ps.setString(2,Name);
			ps.setString(3,Password);
			int i=ps.executeUpdate();
			out.print(i+"Account deactivate sucessfully");
			con.close();
	}
		catch (Exception e) {
		out.print(e);	
		}
		}

}
