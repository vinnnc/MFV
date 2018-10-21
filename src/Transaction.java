import java.util.ArrayList;

public class Transaction
{
    private ArrayList<String> items;
    private double totalPrice;
    private String  transactionID;
    private String userID;

    public Transaction(ArrayList<String> items, double totalPrice, String transactionID, String userID)
    {
        this.items = items;
        this.totalPrice = totalPrice;
        this.transactionID = transactionID;
        this.userID = userID;
    }

    public ArrayList<String> getItems()
    {
        return items;
    }

    public void setItems(ArrayList<String> items)
    {
        this.items = items;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public String getTransactionID()
    {
        return transactionID;
    }

    public void setTransactionID(String transactionID)
    {
        this.transactionID = transactionID;
    }

    public String getUserID()
    {
        return userID;
    }

    public String displayTransaction()
    {
        String transaction = "";
        transaction += "Transaction ID: " + transactionID + ";User ID: " + userID + ";items: \n";
        for (String item : items)
            transaction += item + ";\n";
        return  transaction;
    }

    public String displayTransaction1()
    {
        String transaction = "";
        for (String item : items)
            transaction += item + ";";
        transaction += transactionID + ";" + userID + ";";
        return  transaction;
    }
}
