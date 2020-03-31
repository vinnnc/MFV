import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller
{
    private ArrayList<Cart> cartList;
    private ArrayList<LocalDate> holidayList;
    private String loginID;
    private ArrayList<Product> productList;
    private ArrayList<Shipment> shipmentList;
    private ArrayList<Transaction> transactionList;
    private UserInterface ui;
    private ArrayList<User> userList;

    public Controller()
    {
        cartList = new ArrayList<>();
        holidayList = new ArrayList<>();
        holidayList.add(LocalDate.of(2018, 1, 1));
        holidayList.add(LocalDate.of(2018, 1, 26));
        holidayList.add(LocalDate.of(2018, 3, 30));
        holidayList.add(LocalDate.of(2018, 4, 2));
        holidayList.add(LocalDate.of(2018, 4, 25));
        holidayList.add(LocalDate.of(2018, 12, 24));
        holidayList.add(LocalDate.of(2018, 12, 25));
        holidayList.add(LocalDate.of(2018, 12, 26));
        loginID = "N";
        productList = new ArrayList<>();
        shipmentList = new ArrayList<>();
        transactionList = new ArrayList<>();
        ui = new UserInterface();
        userList = new ArrayList<>();
        initialisation();
        ui.welcome();
        mainMenu();
    }

    private void viewTransaction(char user)
    {
        if (user == 'O')
            ui.displayTransaction(transactionList);
        else
        {
            ArrayList<Transaction> userTransactions = new ArrayList<>();
            for (Transaction t : transactionList)
            {
                if (t.getUserID().equals(loginID))
                    userTransactions.add(t);
            }
            ui.displayTransaction(userTransactions);
        }
    }

    private void checkout()
    {
        while (true)
        {
            if (loginID.charAt(0) == 'C')
            {
                String userID = loginID;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String transactionID = userID + LocalDateTime.now().format(dtf);
                ArrayList<String> items = new ArrayList<>();
                for (Cart item : cartList)
                {
                    String temp = item.getProduct().getProductName() + " " + item.getQuantity();
                    items.add(temp);
                    for (Shipment s: shipmentList)
                    {
                        if (item.getProduct().getProductID() == s.getProductID())
                            s.setTotalQuantity(s.getUnlockedQuantity());
                    }
                }
                double totalPrice = calculateTotal();
                Transaction t = new Transaction(items, totalPrice, transactionID, userID);
                transactionList.add(t);
                cartList.clear();
                ui.displayMessage("success");
                break;
            } else
            {
                ui.displayMessage("askForLogin");
                ui.checkoutMenu();
                label1:
                while (true)
                {
                    switch (ui.intInput())
                    {
                        case 1: register(); break label1;
                        case 2: login(); break label1;
                        case 0: ui.displayMessage("exit"); break label1;
                        default: ui.displayMessage("invalid");
                    }
                }
            }
        }
    }

    private void addToCart(Product product)
    {
        ui.displayMessage("quantity");
        while (true)
        {
            int quantity = ui.intInput();
            if (quantity == 0)
            {
                ui.displayMessage("exit");
                break;
            }
            if (quantity > 0)
            {
                if (totalQuantity(product) >= quantity)
                {
                    lockProduct(product, quantity);
                    Cart c = new Cart(LocalDateTime.now(), product, quantity);
                    cartList.add(c);
                    break;
                }
                else
                {
                    ui.displayMessage("notEnough");
                    ui.displayQuantity(totalQuantity(product));
                }
            }
            else
                ui.displayMessage("invalid");
        }
    }

    private void selectCartItem()
    {
        for (Cart c : cartList)
        {
            if (Duration.between(c.getAddTime(), LocalDateTime.now()).toMinutes() > 10)
                cartList.remove(c);
        }
        ui.displayCart(cartList);
        while (true)
        {
            int input = ui.intInput();
            if (0 < input && input <= cartList.size())
            {
                ui.cartMenu();
                label:
                while(true)
                {
                    switch (ui.intInput())
                    {
                        case 0: ui.displayMessage("exit"); break label;
                        case 1: editCart(input - 1); break label;
                        case 2: removeFromCart(input - 1); break label;
                        case 3: checkout(); return;
                        default: ui.displayMessage("invalid");
                    }
                }
            }
            else if (input == 0)
            {
                ui.displayMessage("exit");
                break;
            } else
                ui.displayMessage("invalid");
        }
    }

    private void removeFromCart(int index)
    {
        int unlockQuantity = cartList.get(index).getQuantity();
        Product product = cartList.get(index).getProduct();
        unlockProduct(product, unlockQuantity);
        cartList.remove(index);
        ui.displayMessage("success");
    }

    private void editCart(int index)
    {
        ui.displayMessage("quantity");
        int quantity = ui.intInput();
        while (true)
        {
            if (quantity > 0)
            {
                int total = totalQuantity(cartList.get(index).getProduct());
                if (quantity > total)
                {
                    ui.displayQuantity(total);
                    ui.displayMessage("notEnough");
                } else
                {
                    Cart c = cartList.get(index);
                    unlockProduct(c.getProduct(), c.getQuantity());
                    lockProduct(c.getProduct(), quantity);
                    c.setQuantity(quantity);
                    ui.displayMessage("success");
                    break;
                }
            }
            else if (quantity == 0)
            {
                ui.displayMessage("exit");
                break;
            } else
                ui.displayMessage("invalid");
        }
    }

    private void mainMenu()
    {
        while (true)
        {
            char user = loginID.charAt(0);
            if (user == 'N') // have not login
            {
                ui.mainMenu(0);
                switch (ui.intInput())
                {
                    case 1:
                        selectProduct(productList);
                        break;
                    case 2:
                        searchProduct();
                        break;
                    case 3:
                        selectCartItem();
                        break;
                    case 4: register(); break;
                    case 5: login(); break;
                    case 0: exit(); System.exit(0); break;
                    default:
                        ui.displayMessage("invalid");
                }
            } else if (user == 'O') // owner login
            {
                ui.mainMenu(1);
                switch (ui.intInput())
                {
                    case 1:
                        selectProduct(productList);
                        break;
                    case 2:
                        searchProduct();
                        break;
                    case 3:
                        selectCartItem();
                        break;
                    case 4: changePassword(); break;
                    case 5: logout(); break;
                    case 6:
                        addProduct();
                        break;
                    case 7:
                        selectProduct(productList);
                        break;
                    case 8:
                        selectProduct(productList);
                        break;
                    case 9:
                        viewTransaction(user);
                        break;
                    case 10: removeCustomer(); break;
                    case 0: exit(); break;
                    default:
                        ui.displayMessage("invalid");
                }
            } else // customer login
            {
                ui.mainMenu(2);
                switch (ui.intInput())
                {
                    case 1:
                        selectProduct(productList);
                        break;
                    case 2:
                        searchProduct();
                        break;
                    case 3:
                        selectCartItem();
                        break;
                    case 4: changePassword(); break;
                    case 5: logout(); break;
                    case 6:
                        viewTransaction(user);
                        break;
                    case 7: removeCustomer(); break;
                    case 0: exit(); break;
                    default:
                        ui.displayMessage("invalid");
                }
            }
        }
    }

    private int totalQuantity(Product product)
    {
        int total = 0;
        for (Shipment s : shipmentList)
        {
            if (s.getProductID() == product.getProductID())
                total += s.getUnlockedQuantity();
        }
        return total;
    }

    private void lockProduct(Product product, int quantity)
    {
        int addToCart = quantity;
        for (Shipment s : shipmentList)
        {
            if (s.getProductID() == product.getProductID())
            {
                if (s.getUnlockedQuantity() >= addToCart)
                {
                    s.setUnlockedQuantity(s.getUnlockedQuantity() - addToCart);
                    break;
                }
                if (s.getUnlockedQuantity() < addToCart)
                {
                    addToCart -= s.getUnlockedQuantity();
                    s.setUnlockedQuantity(0);
                }
            }
        }
    }

    private void unlockProduct(Product product, int quantity)
    {
        int unlockQuantity = quantity;
        for (int i = shipmentList.size() - 1; i >= 0; i--)
        {
            Shipment s = shipmentList.get(i);
            if (s.getProductID() == product.getProductID() && s.getUnlockedQuantity() <= s.getTotalQuantity())
            {
                int difference = s.getTotalQuantity() - s.getUnlockedQuantity();
                if (difference > unlockQuantity)
                {
                    s.setUnlockedQuantity(s.getUnlockedQuantity() + unlockQuantity);
                    break;
                } else
                {
                    s.setUnlockedQuantity(s.getTotalQuantity());
                    unlockQuantity -= difference;
                }
            }
        }
    }

    private void searchProduct()
    {
        ui.displayMessage("search");
        label1:
        while (true)
        {
            String input = ui.stringInput();
            switch (input)
            {
                case "":
                    ui.displayMessage("invalid");
                    break;
                case "0":
                    ui.displayMessage("exit");
                    break label1;
                default:
                    ArrayList<Product> searchList = new ArrayList<>();
                    for (Product p : productList)
                    {
                        if (p.getProductName().contains(input) || p.getCategory().contains(input))
                            searchList.add(p);
                    }
                    ui.sortMenu();
                    label:
                    while (true)
                    {
                        int select = ui.intInput();
                        switch (select)
                        {
                            case 0:
                                ui.displayMessage("exit");
                                break label;
                            case 1:
                            case 2:
                                selectProduct(sortProduct(searchList, select));
                                break label;
                            default:
                                ui.displayMessage("invalid");
                        }
                    }
                    break label1;
            }
        }
    }

    private ArrayList<Product> sortProduct(ArrayList<Product> list, int type)
    {
        for (int i = 0; i < list.size(); i++)
        {
            for (int j = 1; j < list.size() - 1; j++)
            {
                // type 1: sort by price; type2: sort by name;
                if ((type == 1 && list.get(j - 1).getPrice() > list.get(j).getPrice()) ||
                        (type == 2 && list.get(j - 1).getProductName().compareTo(list.get(j).getProductName()) > 0))
                {
                    Product temp = list.get(j - 1);
                    list.set(j - 1, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        return list;
    }

    private void selectProduct(ArrayList<Product> list)
    {
        ui.displayProduct(list);
        while (true)
        {
            int index = ui.intInput() - 1;
            if (index < list.size() && index >= 0)
            {
                ui.productMenu(loginID.charAt(0));
                label:
                while (true)
                {
                    int option = ui.intInput();
                    if (loginID.charAt(0) == 'O')
                    {
                        switch (option)
                        {
                            case 0: ui.displayMessage("exit"); break label;
                            case 1: addShipment(list.get(index)); break label;
                            case 2: editProduct(list.get(index)); break label;
                            case 3: removeProduct(list.get(index)); break label;
                            case 4: displayShipment(list.get(index).getProductID());
                            default: ui.displayMessage("invalid");
                        }
                    }
                    else
                    {
                        switch (option)
                        {
                            case 1: addToCart(list.get(index)); break label;
                            case 2:
                            case 0: ui.displayMessage("exit"); break label;
                            default: ui.displayMessage("invalid");
                        }
                    }
                }
                break;
            } else if (index == -1) // because index minus 1
            {
                ui.displayMessage("exit");
                break;
            } else
                ui.displayMessage("invalid");
        }
    }

    private String createUserID()
    {
        int index = 1001;
        String userID;
        while (true)
        {
            userID = "C" + index;
            if (duplicateValidation(userID, "userID"))
                index++;
            else
                return userID;
        }
    }

    private boolean passwordAvailable(String p)
    {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;
        boolean pass4 = false;
        Pattern pa1 = Pattern.compile("[0-9]");
        Matcher m1 = pa1.matcher(p);
        Pattern pa2 = Pattern.compile("[`~!@#$%^&*()+=|{}':;,.<>/?]");
        Matcher m2 = pa2.matcher(p);
        if (p.length() >= 8)
            pass4 = true;
        for (int i = 0; i < p.length(); i++)
        {
            char c = p.charAt(i);
            if (Character.isUpperCase(c))
                pass1 = true;
            if (m1.find())
                pass2 = true;
            if (m2.find())
                pass3 = true;
        }
        return (!pass1 || !pass2 || !pass3 || !pass4);
    }

    private void addProduct()
    {
        ui.productInformation("name");
        String productName = ui.stringInput();
        if (productName.equals("0"))
            return;
        while (duplicateValidation(productName, "productName"))
        {
            ui.displayMessage("warnDuplication");
            productName = ui.stringInput();
        }
        ui.productInformation("category");
        String category = ui.stringInput();
        if (category.equals("0"))
            return;
        ui.productInformation("description");
        String description = ui.stringInput();
        if (description.equals("0"))
            return;
        ui.productInformation("packaging");
        String packaging;
        label:
        while (true)
        {
            switch (ui.intInput())
            {
                case 0: return;
                case 1: packaging = "KG"; break label;
                case 2: packaging = "package"; break label;
                case 3: packaging = "quantity"; break label;
                default: ui.displayMessage("invalid");
            }
        }
        ui.productInformation("price");
        String input = String.valueOf(ui.intInput());
        if (input.equals("0"))
            return;
        int size = input.length();
        double price = Double.valueOf(input.substring(0, size - 2) + "." + input.substring(size - 2));
        if (ui.askForConfirmation())
        {
            int productID = createProductID();
            Product p = new Product(category, description, packaging, price, productID, productName);
            productList.add(p);
        }
    }

    private void addShipment(Product p)
    {
        String dateOfShipment = LocalDate.now().toString();
        ui.shipmentInformation("duration");
        int expireDuration = ui.intInput();
        if (expireDuration == 0)
            return;
        ui.shipmentInformation("quantity");
        int totalQuantity = ui.intInput();
        if (totalQuantity == 0)
            return;
        int productID = p.getProductID();
        if (ui.askForConfirmation())
        {
            int shipmentID = createShipmentID();
            Shipment s = new Shipment(dateOfShipment, expireDuration, productID, shipmentID, totalQuantity);
            shipmentList.add(s);
        }
    }

    private void editProduct(Product product)
    {
        int index = productList.indexOf(product);
        while (true)
        {
            ui.selectProductAttribute();
            switch (ui.intInput())
            {
                case 1:
                    ui.productInformation("category");
                    String category = ui.stringInput();
                    productList.get(index).setCategory(category);
                    ui.displayMessage("success");
                    break;
                case 2:
                    ui.productInformation("description");
                    String description = ui.stringInput();
                    productList.get(index).setDescription(description);
                    ui.displayMessage("success");
                    break;
                case 3:
                    ui.productInformation("packaging");
                    String packaging = "";
                    label:
                    while (true)
                    {
                        switch (ui.intInput())
                        {
                            case 0: break label;
                            case 1: packaging = "KG"; break label;
                            case 2: packaging = "package"; break label;
                            case 3: packaging = "quantity"; break label;
                            default: ui.displayMessage("invalid");
                        }
                    }
                    productList.get(index).setPackaging(packaging);
                    ui.displayMessage("success");
                    break;
                case 4:
                    ui.productInformation("price");
                    String input = String.valueOf(ui.intInput());
                    int size = input.length();
                    double price = Double.valueOf(input.substring(0, size - 2) + "." + input.substring(size - 2));
                    productList.get(index).setPrice(price);
                    ui.displayMessage("success");
                    break;
                case 5:
                    ui.productInformation("name");
                    String productName = ui.stringInput();
                    productList.get(index).setProductName(productName);
                    break;
                case 0:
                    ui.displayMessage("exit");
                    return;
                default:
                    ui.displayMessage("invalid");
            }
        }
    }

    private void removeProduct(Product product)
    {
        for (Shipment s : shipmentList)
        {
            if (s.getProductID() == product.getProductID())
                shipmentList.remove(s);
        }
        productList.remove(product);
        ui.displayMessage("success");
    }

    private int createProductID()
    {
        int index = 1001;
        while (!duplicateValidation(""+index, "productID"))
            index++;
        return index;
    }

    private int createShipmentID()
    {
        int index = 1001;
        while (!duplicateValidation(""+index, "shipmentID"))
            index++;
        return index;
    }

    private double calculateTotal()
    {
        double totalPrice = 0;
        double[] eachPrice = new double[cartList.size()];
        int index = 0;
        for (Cart c : cartList)
        {
            double expireDiscount = 1;
            LocalDate expire = LocalDate.now();
            for (Shipment s : shipmentList)
            {
                if (c.getProduct().getProductID() == s.getProductID() && s.getUnlockedQuantity() > 0)
                {
                    String[] date = s.getDateOfShipment().split("-");
                    int add = s.getExpireDuration();
                    expire = LocalDate.of(Integer.valueOf(date[0]), Integer.valueOf(date[1]),
                            Integer.valueOf(date[2])).plusDays(add);
                    break;
                }
            }
            if (ChronoUnit.DAYS.between(expire, LocalDate.now()) <= 2)
                expireDiscount *= 0.8;
            eachPrice[index] = c.getProduct().getPrice() * c.getQuantity() * expireDiscount;
            index++;
        }
        for (double p : eachPrice)
            totalPrice += p;
        if (holidayList.contains(LocalDate.now()))
            totalPrice *= 0.8;
        if (totalPrice > 100)
        {
            for (User u : userList)
            {
                if (u.getUserID().equals(loginID) && u.isFirstDiscount())
                {
                    u.setFirstDiscount(false);
                    totalPrice *= 0.9;
                    break;
                }
            }
        }
        return totalPrice;
    }

    private void register()
    {
        ui.userInformation("askEmail");
        String tempEmail = ui.stringInput();
        if (tempEmail.equals("0"))
            return;
        while (duplicateValidation(tempEmail, "email"))
        {
            ui.userInformation("warnDuplication");
            tempEmail = ui.stringInput();
            if (tempEmail.equals("0"))
                return;
        }
        ui.userInformation("askPassword");
        String password = ui.stringInput();
        if (password.equals("0"))
            return;
        while (passwordAvailable(password))
        {
            ui.userInformation("passwordWarn");
            ui.userInformation("askPassword");
            password = ui.stringInput();
            if (password.equals("0"))
                return;
        }
        ui.userInformation("confirmPassword");
        String password2 = ui.stringInput();
        if (password2.equals("0"))
            return;
        while (!password.equals(password2))
        {
            ui.userInformation("passwordDiffer");
            ui.userInformation("confirmPassword");
            password2 = ui.stringInput();
            if (password2.equals("0"))
                return;
        }
        ui.userInformation("askAddress");
        String address = ui.stringInput();
        if (address.equals("0"))
            return;
        ui.userInformation("askQA");
        ui.userInformation("setQ1");
        String Q1 = ui.stringInput();
        if (Q1.equals("0"))
            return;
        ui.userInformation("setA1");
        String A1 = ui.stringInput();
        if (A1.equals("0"))
            return;
        ui.userInformation("setQ2");
        String Q2 = ui.stringInput();
        if (Q2.equals("0"))
            return;
        ui.userInformation("setA2");
        String A2 = ui.stringInput();
        if (A2.equals("0"))
            return;
        String[] securityQA = {Q1, A1, Q2, A2};
        String userID = createUserID();
        userList.add(new Customer(tempEmail, password, userID, address, true, securityQA));
        loginID = userID;
    }

    private boolean duplicateValidation(String item, String type)
    {
        switch (type)
        {
            case "email":
                ArrayList<String> emails = new ArrayList<>();
                for (User u: userList)
                    emails.add(u.getEmail());
                return emails.contains(item);
            case "userID":
                ArrayList<String> userIDs = new ArrayList<>();
                for (User u: userList)
                    userIDs.add(u.getUserID());
                return userIDs.contains(item);
            case "productID":
                int productID = Integer.valueOf(item);
                ArrayList<Integer> productIDs = new ArrayList<>();
                for (Product p: productList)
                    productIDs.add(p.getProductID());
                return productIDs.contains(productID);
            case "shipmentID":
                int shipmentID = Integer.valueOf(item);
                ArrayList<Integer> shipmentIDs = new ArrayList<>();
                for (Shipment p: shipmentList)
                    shipmentIDs.add(p.getShipmentID());
                return shipmentIDs.contains(shipmentID);
            case "productName":
                ArrayList<String> productNames = new ArrayList<>();
                for (Product p: productList)
                    productNames.add(p.getProductName());
                return productNames.contains(item);
        }
        return true;
    }

    private void exit()
    {
        ArrayList<String> arr1 = new ArrayList<>();
        ArrayList<String> arr2 = new ArrayList<>();
        ArrayList<String> arr3 = new ArrayList<>();
        ArrayList<String> arr4 = new ArrayList<>();

        for (User anUserList : userList)
            arr1.add(anUserList.displayUser());
        for (Transaction aTransactionList : transactionList)
            arr2.add(aTransactionList.displayTransaction1());
        for (Product aProductList : productList)
            arr3.add(aProductList.displayProduct1());
        for (Shipment aShipmentList : shipmentList)
            arr4.add(aShipmentList.displayShipment1());
        String[] arr11 = new String[arr1.size()];
        arr11 = arr1.toArray(arr11);
        String[] arr22 = new String[arr2.size()];
        arr22 = arr2.toArray(arr22);
        String[] arr33 = new String[arr3.size()];
        arr33 = arr3.toArray(arr33);
        String[] arr44 = new String[arr4.size()];
        arr44 = arr4.toArray(arr44);
        write("User.txt", arr11);
        write("Transaction.txt", arr22);
        write("Product.txt", arr33);
        write("Shipment.txt", arr44);
        ui.displayMessage("exit");
    }


    private void write(String filename, String[] array)
    {
        try
        {
            FileWriter fw = new FileWriter(filename, false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String arr : array)
            {
                bw.write(arr + "\t\n");
            }
            bw.close();
            fw.close();
        } catch (IOException e)
        {
            ui.display(e.toString());
            System.exit(1);
        }
    }

    private void initialisation()
    {
        String[] filename = {"User.txt", "Transaction.txt", "Product.txt", "Shipment.txt"};
        for (int i = 0; i <= 3; i++)
        {
            try
            {
                FileReader file = new FileReader(filename[i]);
                Scanner parser = new Scanner(file);
                String oneInfo;
                boolean firstUser = true;
                while (parser.hasNextLine())
                {
                    oneInfo = parser.nextLine();
                    if (i == 0)
                    {
                        if (firstUser)
                        {
                            String temp[] = oneInfo.split(";");
                            userList.add(new Owner(temp[0], temp[1], temp[2]));
                            firstUser = false;
                        } else
                        {
                            boolean firstDiscount = true;
                            String temp[] = oneInfo.split(";");
                            if (temp[4].equals("1"))
                                firstDiscount = false;
                            String[] qA = {temp[5], temp[6], temp[7], temp[8]};
                            userList.add(new Customer(temp[0], temp[1], temp[2], temp[3], firstDiscount, qA));
                        }
                    } else if (i == 1)
                    {
                        String temp[] = oneInfo.split(";");
                        ArrayList<String> items = new ArrayList<>();
                        int length = temp.length;
                        for (int a = 0; a < length - 2; a++)
                            items.add(temp[i]);
                        transactionList.add(new Transaction(items, Double.valueOf(temp[length - 3]),
                                temp[length - 2], temp[length - 1]));
                    } else if (i == 2)
                    {
                        String temp[] = oneInfo.split(";");
                        productList.add(new Product(temp[0], temp[1], temp[2], Double.valueOf(temp[3]),
                                Integer.valueOf(temp[4]), temp[5]));
                    } else
                    {
                        String temp[] = oneInfo.split(";");
                        shipmentList.add(new Shipment(temp[0], Integer.valueOf(temp[1]), Integer.valueOf(temp[2]),
                                Integer.valueOf(temp[3]), Integer.valueOf(temp[4])));
                    }
                    parser.close();
                }
            }
            catch (IOException e)
            {
                ui.display(e.toString());
                System.exit(1);
            }
        }
    }

    private void login()
    {
        ui.userInformation("askEmail");
        String cusEmail = ui.stringInput();
        if (!cusEmail.equals("0"))
        {
            ui.userInformation("askPassword");
            String pWords = ui.stringInput();
            if (!pWords.equals("0"))
            {
                if (cusEmail.length() == 0 || cusEmail.trim().isEmpty())
                    ui.userInformation("invalidEmail");
                else
                    checkUser(cusEmail,pWords,0);
            }
        }

    }

    private void changePassword()
    {
        for(User u : userList)
        {
            if(loginID.equals(u.getUserID()))
            {
                while(true)
                {
                    ui.display(u.getQA()[0]);
                    String a1 = ui.stringInput();
                    if(a1.equals("0"))
                        return;
                    ui.display(u.getQA()[2]);
                    String a2 = ui.stringInput();
                    if(a2.equals("0"))
                        return;
                    if(a1.equals(u.getQA()[1]) && a2.equals(u.getQA()[3]))
                    {
                        ui.userInformation("changePass");
                        String newPass = ui.stringInput();
                        if(newPass.equals("0"))
                            return;
                        while(passwordAvailable(newPass))
                        {
                            ui.userInformation("passwordWarn");
                            newPass = ui.stringInput();
                            if(newPass.equals("0"))
                                return;
                        }
                        u.setPassword(newPass);
                        ui.userInformation("updatePass");
                        break;
                    }
                    else
                        ui.userInformation("QADifference");
                }
            }
        }
    }

    private void checkUser(String email, String passWord,int time)
    {
        boolean indicator = false;
        for (User u: userList)
        {
            if (u.getEmail().equals(email))
            {
                indicator = true;
                if(u.getPassword().equals(passWord))
                {
                    ui.userInformation("loginSuccess");
                    loginID = u.getUserID();
                }
                else
                {
                    if(time >= 3)
                        changePassword();
                    else
                    {
                        ui.userInformation("loginFail");
                        passWord = ui.stringInput();
                        if(passWord.equals("0"))
                            return;
                        time +=  1;
                        checkUser(email,passWord,time);
                    }
                }
            }
        }
        if (!indicator)
            ui.userInformation("unregister");
    }

    private void logout()
    {
        loginID= "N";
    }

    private void removeCustomer()
    {
        if (loginID.charAt(0) == 'O')
        {
            for (int i = 1; i < userList.size(); i++)
                ui.display(userList.get(i).displayUserSimpleMessage());
            while (true)
            {
                ui.userInformation("removeCustomer");
                String choice = ui.stringInput();
                if (choice.equals("0"))
                    return;
                for (int i = 1; i < userList.size(); i++)
                {
                    if (userList.get(i).getUserID().equals(choice))
                    {
                        userList.remove(i);
                        for (Transaction t : transactionList)
                        {
                            if (t.getUserID().equals(choice))
                                transactionList.remove(t);
                        }
                        ui.userInformation("removeSuccess");
                        return;
                    }
                }
                ui.displayMessage("invalid");
            }
        } else
        {
            if (ui.askForConfirmation())
            {
                for (int i = 1; i < userList.size(); i++)
                {
                    if (userList.get(i).getUserID().equals(loginID))
                    {
                        userList.remove(i);
                        for (Transaction t : transactionList)
                        {
                            if (t.getUserID().equals(loginID))
                                transactionList.remove(t);
                        }
                        ui.userInformation("removeSuccess");
                        return;
                    }
                }
            }
        }
    }

    private void displayShipment(int productID)
    {
        for(Shipment s: shipmentList)
        {
            if (s.getProductID() == productID)
                ui.display(s.displayShipment());
        }
    }
}