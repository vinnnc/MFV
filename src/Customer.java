public class Customer extends User {
    private String address;
    private boolean firstDiscount;
    private String[] scurityQA;

    public Customer(String email, String password, String userID, String address, boolean firstDiscount, String[] arr) {
        super(email, password, userID);
        this.address = address;
        this.firstDiscount = firstDiscount;
        this.scurityQA = arr;
    }

    public String getAddress() {
        return address;
    }

    public boolean isFirstDiscount() {
        return firstDiscount;
    }

    public void setFirstDiscount(boolean firstDiscount) {
        this.firstDiscount = firstDiscount;
    }

    public String[] getQA() {
        return scurityQA;
    }

    public String displayUser() {
        int a = 0;
        if (!firstDiscount) {
            a = 1;
        }
        return getEmail() + ";" + getPassword() + ";" + getUserID() + ";" + getAddress() + ";" + a + ";" + scurityQA[0]
                + ";" + scurityQA[1] + ";" + scurityQA[2] + ";" + scurityQA[3] + ";";
    }
}
