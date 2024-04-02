
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
@WebServlet("/NewAccount")
public class NewAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public NewAccount() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		long AccountNumber=Long.parseLong(request.getParameter("Accno"));
		String Name=request.getParameter("nme");
		String Password=request.getParameter("psw");
		Double Amount=Double.parseDouble(request.getParameter("amt"));
		String Addresss=request.getParameter("addr");
		long Mobile_Number=Long.parseLong(request.getParameter("mbno"));
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ghdb","ghdb");
			PreparedStatement ps=con.prepareStatement("insert into bank values(?,?,?,?,?,?)");
			ps.setLong(1,AccountNumber);
			ps.setString(2,Name);
			ps.setString(3,Password);
			ps.setDouble(4,Amount);
			ps.setString(5, Addresss);
			ps.setLong(6, Mobile_Number);
			int i=ps.executeUpdate();
			out.print(i+"successfully created account");
			con.close();
		
		}catch (Exception e) {
			out.print(e);
		}
	}

}
