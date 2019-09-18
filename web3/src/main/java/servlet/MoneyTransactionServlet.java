package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", null));
      //  PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = new BankClientService();

        Map<String, Object> messageForm = new HashMap<>();
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        Long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo"); // name - receiver проверка в сервисе

        BankClient bankClient = new BankClient(senderName, senderPass, count);
// исключение перенести в сервис или дао
        try {
            if (bankClientService.sendMoneyToClient(bankClient, nameTo, count)) {
                messageForm.put("message", "The transaction was successful");
            } else {
                messageForm.put("message", "transaction rejected");
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", messageForm));
        resp.setStatus(HttpServletResponse.SC_OK);





    }
}
