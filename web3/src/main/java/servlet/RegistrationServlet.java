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

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  //      PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>());
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = new BankClientService();


        Map<String, Object> messageForm = new HashMap<>();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.parseLong(req.getParameter("money"));


        if (new BankClientService().getClientByName(name) == null) {
            BankClient newClient = new BankClient(name, password, money);
            bankClientService.addClient(newClient);
            messageForm.put("message", "Add client successful");
        } else {
            messageForm.put("message", "Client not add");
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", messageForm));
    //    PageGenerator.getInstance().getPage("resultPage.html", messageForm);
        resp.setStatus(HttpServletResponse.SC_OK);

    }

}