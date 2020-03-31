import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    public UserInterface() {
    }

    public void welcome() {
        System.out.println(" #    #  ######  #        ####    ####   #    #  ######  ");
        System.out.println(" #    #  #       #       #    #  #    #  ##  ##  #      ");
        System.out.println(" #    #  #####   #       #       #    #  # ## #  #####  ");
        System.out.println(" # ## #  #       #       #       #    #  #    #  #      ");
        System.out.println(" ##  ##  #       #       #    #  #    #  #    #  #      ");
        System.out.println(" #    #  ######  ######   ####    ####   #    #  ###### ");
        System.out.println();
        System.out.println("**************************************************************");
        System.out.println("Welcome to Monash Fruit and Vegetable Store.");
    }

    public void displayTransaction(ArrayList<Transaction> list) {
        if (list.size() > 0) {
            for (Transaction transaction : list) {
                String[] temp = transaction.displayTransaction().split(";");
                for (String s : temp)
                    System.out.println(s);
            }
        } else
            System.out.println("There is no transaction.");
    }

    public void displayCart(ArrayList<Cart> list) {
        if (list.size() > 0) {
            int i = 1;
            for (Cart c : list) {
                System.out.println(i + ". " + c.displayCart());
                i++;
            }
        } else
            System.out.println("There is no item in cart.");
    }

    public void displayProduct(ArrayList<Product> list) {
        if (list.size() > 0) {
            int i = 1;
            for (Product p : list) {
                System.out.println(i + ". " + p.displayProduct());
                i++;
            }
        } else
            System.out.println("There is no product.");
    }

    public void displayMessage(String type) {
        switch (type) {
            case "invalid":
                System.out.println("Invalid input, please enter again!");
                break;
            case "exit":
                System.out.println("You have exit!");
                break;
            case "select":
                System.out.println("Please input number to select:");
                break;
            case "quantity":
                System.out.println("Please in put the quantity:");
                break;
            case "success":
                System.out.println("Action Success.");
                break;
            case "notEnough":
                System.out.println("Product quantity is not enough!");
                break;
            case "search":
                System.out.println("Please enter the name of product:");
                break;
            case "askForLogin":
                System.out.println("Please login before checkout.");
                break;
        }
    }

    public void sortMenu() {
        System.out.println("1. Sort by name");
        System.out.println("2. Sort by price");
    }

    public void cartMenu() {
        System.out.println("1. Edit the quantity");
        System.out.println("2. Remove this product");
        System.out.println("3. Checkout");
    }

    public void mainMenu(int type) {
        System.out.println("1. View product");
        System.out.println("2. Search product");
        System.out.println("3. Show cart");
        if (type == 0) // have not login
        {
            System.out.println("4. Register");
            System.out.println("5. Login");
        }
        if (type == 1) // owner login
        {
            System.out.println("4. Change password");
            System.out.println("5. Logout");
            System.out.println("6. Create product");
            System.out.println("7. Edit product");
            System.out.println("8. Remove product");
            System.out.println("9. View transaction");
            System.out.println("10. Remove registered customers");
        }
        if (type == 2) // customer login
        {
            System.out.println("4. Change password");
            System.out.println("5. Logout");
            System.out.println("6. View transaction");
            System.out.println("7. Unregister");
        }
    }

    public String stringInput() {
        System.out.println("(0 to exit)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        sc.close();
        return input;
    }

    public boolean askForConfirmation() {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Please enter \"Confirm\" or \"confirm\" to confirmation (others input will cancel to " + "action): ");
        boolean res = sc.nextLine().trim().toLowerCase().equals("confirm");
        sc.close();
        return res;
    }

    public void displayQuantity(int quantity) {
        System.out.println("Quantity: " + quantity);
    }

    public void productMenu(char user) {
        if (user == 'O') {
            System.out.println("1. Add new shipment");
            System.out.println("2. Edit this product");
            System.out.println("3. Remove this product");
            System.out.println("4. Show shipment information");
        } else {
            System.out.println("1. Add to cart");
            System.out.println("2. Return to main menu");
        }
    }

    public void checkoutMenu() {
        System.out.println("1. Register");
        System.out.println("2. Login");
    }

    public void productInformation(String type) {
        switch (type) {
            case "name":
                System.out.println("Please input product name:");
                break;
            case "warnDuplication":
                System.out.println("This product already exists, please change!");
                break;
            case "category":
                System.out.println("Please input product category:");
                break;
            case "description":
                System.out.println("Please input product description:");
                break;
            case "packaging":
                System.out.println("Please select type of packaging:");
                System.out.println("1. packaging per KG");
                System.out.println("2. packaging per package");
                System.out.println("3. packaging per quantity");
                break;
            case "price":
                System.out.println("Please input product price: (unit: cents)");
                break;
        }
    }

    public void shipmentInformation(String type) {
        switch (type) {
            case "dos":
                System.out.println("Please input date of shipment:");
                break;
            case "duration":
                System.out.println("Please input expire duration days:");
                break;
            case "quantity":
                System.out.println("Please input total quantity:");
                break;
        }
    }

    public void selectProductAttribute() {
        System.out.println("1. Change category");
        System.out.println("2. Change description");
        System.out.println("3. Change packaging");
        System.out.println("4. Change price");
        System.out.println("5. Change product name");
    }

    public void display(String info) {
        System.out.println(info);
    }

    public int intInput() {
        System.out.println("(0 to exit)");
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                int res = sc.nextInt();
                sc.close();
                return res;
            } catch (Exception e) {
                displayMessage("invalid");
            }
        }
    }

    public void userInformation(String type) {
        switch (type) {
            case "askForLogin":
                System.out.println("Please login before checkout.");
                break;
            case "askEmail":
                System.out.println("Please enter your e-mail address");
                break;
            case "warnDuplication":
                System.out.println("Your email is already used, please change!");
                break;
            case "askPassword":
                System.out.println("Please enter your password:");
                break;
            case "confirmPassword":
                System.out.println("Please enter your password again!");
                break;
            case "passwordWarn":
                System.out.println("Your password dissatisfy requirement!");
                System.out.println("Password should contain at least one capital letter, at least one number and "
                        + "at least one symbol!");
                break;
            case "passwordDiffer":
                System.out.println("Your passwords are different!");
                break;
            case "askAddress":
                System.out.println("Please enter your address:");
                break;
            case "askQA":
                System.out.println("Now,please set your Security Questions! (Security Question helps "
                        + "you get your password when you forget your password!)");
                break;
            case "setQ1":
                System.out.println("Please enter your first Security Question:");
                break;
            case "setA1":
                System.out.println("Please enter your answer to first Question:");
                break;
            case "setQ2":
                System.out.println("Please enter your second Security Question:");
                break;
            case "setA2":
                System.out.println("Please enter your answer to second Question:");
                break;
            case "unregister":
                System.out.println("You have not register this email!");
                break;
            case "loginSuccess":
                System.out.println("Login Successful!");
                break;
            case "loginFail":
                System.out.println("Your password is incorrect! Please enter again :");
                break;
            case "invalidEmail":
                System.out.println("Please enter a valid Email address:");
                break;
            case "QADifference":
                System.out.println("Your answer is wrong, please enter again:");
                break;
            case "updatePass":
                System.out.println("Password update successfully!");
                break;
            case "removeCustomer":
                System.out.println("Please enter customerID that you want to remove:");
                break;
            case "removeSuccess":
                System.out.println("You have already removed customer successfully!");
                break;
            case "changePass":
                System.out.println("Please enter your new password: ");
        }
    }
}