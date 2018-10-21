public class Shipment
{
    private String dateOfShipment;
    private int expireDuration;
    private int productID;
    private int shipmentID;
    private int totalQuantity;
    private int unlockedQuantity;

    public Shipment(String dateOfShipment, int expireDuration, int productID, int shipmentID, int totalQuantity)
    {
        this.dateOfShipment = dateOfShipment;
        this.expireDuration = expireDuration;
        this.productID = productID;
        this.shipmentID = shipmentID;
        this.totalQuantity = totalQuantity;
        this.unlockedQuantity = totalQuantity;
    }

    public String getDateOfShipment()
    {
        return dateOfShipment;
    }

    public void setDateOfShipment(String dateOfShipment)
    {
        this.dateOfShipment = dateOfShipment;
    }

    public int getExpireDuration()
    {
        return expireDuration;
    }

    public int getUnlockedQuantity()
    {
        return unlockedQuantity;
    }

    public void setUnlockedQuantity(int unlockedQuantity)
    {
        this.unlockedQuantity = unlockedQuantity;
    }

    public int getProductID()
    {
        return productID;
    }

    public void setProductID(int productID)
    {
        this.productID = productID;
    }

    public int getShipmentID()
    {
        return shipmentID;
    }

    public int getTotalQuantity()
    {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity)
    {
        this.totalQuantity = totalQuantity;
    }

    public String displayShipment()
    {
        return "Shipment ID: " + shipmentID + "; TotalQuantity: " + totalQuantity + "; DateOfShipment: "
                + dateOfShipment + "Expire duration: " + expireDuration;
    }
    public String displayShipment1()
    {
        return dateOfShipment + ";" + expireDuration + ";" + productID + ";" + shipmentID + ";"
                + totalQuantity + ";" + unlockedQuantity + ";" ;
    }
}