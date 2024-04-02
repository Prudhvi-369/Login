import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/Withdraw")
public class Withdraw extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Withdraw() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        long AccountNumber = Long.parseLong(request.getParameter("Accno"));
        String Name = request.getParameter("nme");
        String Password = request.getParameter("psw");
        double Amount = Double.parseDouble(request.getParameter("amt"));

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ghdb", "ghdb");

            PreparedStatement ps = con.prepareStatement("select Amount from bank where AccountNumber=? and Name=? and Password=?");
            ps.setLong(1, AccountNumber);
            ps.setString(2, Name);
            ps.setString(3, Password);

            ResultSet rs = ps.executeQuery();
            double currentBalance = 0.0;
            if (rs.next()) {
                currentBalance = rs.getDouble("Amount");
            }

            double newBalance = currentBalance - Amount;
            PreparedStatement ps1 = con.prepareStatement("update bank set Amount=? where AccountNumber=? and Name=? and Password=?");
            ps1.setDouble(1, newBalance);
            ps1.setLong(2, AccountNumber);
            ps1.setString(3, Name);
            ps1.setString(4, Password);

            int i = ps1.executeUpdate();
            con.close();

            if (i > 0) {
                out.println("Original amount: " + currentBalance + "<br>");
                out.println("withdraw amount: " + Amount + "<br>");
                out.println("Total balance after withdraw: " + newBalance);
            } else {
                out.println("Failed to withdraw.");
            }
        } catch (Exception e) {
            out.print(e);
        }
	}

}
