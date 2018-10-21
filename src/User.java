public class User
{
    private String email;
    private String password;
    private String userID;

    public User(String email, String password, String userID)
    {
        this.email = email;
        this.password = password;
        this.userID = userID;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUserID()
    {
        return userID;
    }

    public boolean isFirstDiscount()
    {
        return false;
    }

    public void setFirstDiscount(boolean boo)
    {
    }

    public String[] getQA()
    {
        String[] a = {"0","1"};
        return a;
    }

    public String displayUser()
    {
        return email + ";" + password + ";" + userID;
    }

    public String displayUserSimpleMessage()
    {
        return userID + " : " + email;
    }
}
