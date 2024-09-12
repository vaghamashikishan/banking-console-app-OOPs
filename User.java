interface Calculate_Interest {
    void calculateInterest(double amount, int time);
}

public abstract class User implements Calculate_Interest {
    protected String uName;
    private String uPwd;
    private String fullName;

    public User(String uName, String uPwd, String fullName) {
        this.uName = uName;
        this.uPwd = uPwd;
        this.fullName = fullName;
    }


    public String getuName() {
        return uName;
    }

    public String getuPwd() {
        return uPwd;
    }

    public String getFullName() {
        return fullName;
    }

    public void setPassword(String pwd) {
        uPwd = pwd;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    @Override
    public void calculateInterest(double amount, int time) {
        double rate = 5.0;
        double interest = (amount * time * rate) / 100;
        double TotalBalance = amount + interest;
        System.out.println("Interest: " + interest);
        System.out.println("Total balance: " + TotalBalance);
    }
}