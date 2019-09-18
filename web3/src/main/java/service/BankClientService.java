package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }
/*
    //Метод не используется???
    public BankClient getClientById(long id) throws SQLException, DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
*/
    public BankClient getClientByName(String name) { // Почему нет исключения?????
        return getBankClientDAO().getClientByName(name);
    }


    public List<BankClient> getAllClient() /*throws DBException*/ {// Почему нет исключения????? - оно в методе в ДАО!!!
      //  try {
            return getBankClientDAO().getAllBankClient();

    }

    public boolean deleteClient(String name) { // add deleteClient to BankClientDAO
        if (getClientByName(name) != null) {// Проверка, существует ли клиент в сервисе. Удалить если есть
            getBankClientDAO().deleteClient(name);
            return true;
        }
        return false;
    }

    public boolean addClient(BankClient client)  /* throws DBException*/ {// Idea говорит, что такого исключения нет???
        if (getClientByName(client.getName()) == null) {// Проверка, существует ли клиент в сервисе. Добавить если нет
            getBankClientDAO().addClient(client);
            return true;
        }
        return false;
    }
/*
    // add validateClient to BankClientDAO
    public boolean validateClient(String name, String password) throws SQLException {
        if (getBankClientDAO().validateClient(name, password)) {
            return true;
            //} else {
            //    return false;
            // }
        }
        return false;
    }

 */
/*
    // add sendMoneyToClient to BankClientDAO
    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException {
        try {
            getBankClientDAO().sendMoneyToClient(sender, name, value); // добавить if - true?????
         //   return true;
        } catch (SQLException e) {
              throw new DBException(e);
            //return false;
        }

        return false;
    }
*/
public boolean sendMoneyToClient(BankClient sender, String name, long value) /*throws SQLException*/ {
    BankClientDAO bankClientDAO = getBankClientDAO();
    try {
        if (bankClientDAO.validateClient(sender.getName(), sender.getPassword())) {
            if (bankClientDAO.isClientHasSum(sender.getName(), value)) {
                BankClient receiver = bankClientDAO.getClientByName(name);
                if (receiver == null) {
                    return false; //неправильный получатель
                }

                bankClientDAO.updateClientsMoney(sender.getName(), sender.getPassword(), -value);
                bankClientDAO.updateClientsMoney(receiver.getName(), receiver.getPassword(), value);

                return true;

            } else {
                return false;// когда нет денег...
            }
        } else {
            return false; // клиент не прошёл проверку
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
        //return false;
    return false;
}





    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login Alex
                    append("password=19181938");       //password 19181938

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
