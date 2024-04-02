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

@WebServlet("/Transfer")
public class Transfer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Transfer() {
        super();
    }

    @SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        long AccountNumber = Long.parseLong(request.getParameter("Accno"));
        String Name = request.getParameter("nme");
        String Password = request.getParameter("psw");
        double Amount = Double.parseDouble(request.getParameter("amt"));
        long TargetAccountNumber = Long.parseLong(request.getParameter("TAccno"));

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ghdb", "ghdb");

            PreparedStatement selectBalance = con.prepareStatement("select Amount from bank where AccountNumber=? and Name=? and Password=?");
            selectBalance.setLong(1, AccountNumber);
            selectBalance.setString(2, Name);
            selectBalance.setString(3, Password);
            ResultSet rs = selectBalance.executeQuery();
            if (rs.next()) {
                out.println("<center><h2>My Original balance is " + rs.getDouble(1) + "</h2><br>");
            }

            out.println("Transfer amount is..." + Amount + " rupees<br>");

            PreparedStatement selectTargetBalance = con.prepareStatement("select Amount from bank where AccountNumber=?");
            selectTargetBalance.setLong(1, TargetAccountNumber);
            ResultSet rs2 = selectTargetBalance.executeQuery();
            if (rs2.next()) {
                out.println("Target account balance is " + rs2.getDouble(1) + "<br>");
            }

            PreparedStatement updateSenderBalance = con.prepareStatement("update bank set Amount = Amount - ? where AccountNumber=? and Name=? and Password=?");
            updateSenderBalance.setDouble(1, Amount);
            updateSenderBalance.setLong(2, AccountNumber);
            updateSenderBalance.setString(3, Name);
            updateSenderBalance.setString(4, Password);
            int i = updateSenderBalance.executeUpdate();
            out.println(Amount + " debited from your account<br>");

            PreparedStatement updateTargetBalance = con.prepareStatement("update bank set Amount = Amount + ? where AccountNumber=?");
            updateTargetBalance.setDouble(1, Amount);
            updateTargetBalance.setLong(2, TargetAccountNumber);
            int j = updateTargetBalance.executeUpdate();
            out.println(Amount + " rupees transferred successfully<br>");

            PreparedStatement selectTargetBalanceAfterTransfer = con.prepareStatement("select Amount from bank where AccountNumber=?");
            selectTargetBalanceAfterTransfer.setLong(1, TargetAccountNumber);
            ResultSet rs3 = selectTargetBalanceAfterTransfer.executeQuery();
            if (rs3.next()) {
                out.println("After transfer target account balance is " + rs3.getDouble(1) + "<br>");
            }

            PreparedStatement selectSenderBalanceAfterTransfer = con.prepareStatement("select Amount from bank where AccountNumber=? and Name=? and Password=?");
            selectSenderBalanceAfterTransfer.setLong(1, AccountNumber);
            selectSenderBalanceAfterTransfer.setString(2, Name);
            selectSenderBalanceAfterTransfer.setString(3, Password);
            ResultSet rs4 = selectSenderBalanceAfterTransfer.executeQuery();
            if (rs4.next()) {
                out.println("After transfer my account balance is " + rs4.getDouble(1) + "</center>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
