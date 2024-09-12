import java.io.*;
import java.util.Scanner;

interface TransferMoney {
    public void transfer(Account sourceAccount, int targetAccount, double amount) throws IOException;
}
interface ApproveLoan {
    void approveSavingLoan();
    void approveCurrentLoan();
}

public class Account extends User implements TransferMoney,ApproveLoan {
    private int accNum;
    private double balance;
    private boolean accType;

    public Account(String uName, String uPwd, String fullName, double balance, boolean accType) {
        super(uName, uPwd, fullName);
        this.balance = balance;
        this.accType = accType;
    }

    public void ShowProfile() {
        System.out.println("Username  : " + this.getuName());
        System.out.println("Full Name : " + this.getFullName());
        System.out.println("Account No: " + this.getAccNum());
        System.out.println("Balance  : " + this.getBalance());
        if (!this.getAccType()) System.out.println("Account  : Savings");
        else System.out.println("Account  : Current");
    }

    public int getAccNum() {
        return accNum;
    }

    public double getBalance() {
        return balance;
    }

    public boolean getAccType() {
        return accType;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAccNum(int accNum) {
        this.accNum = accNum;
    }

    public void setAccType(boolean accType) {
        this.accType = accType;
    }

    private static void updateFile(String original,String temp){
        File originalFile = new File(original);
        File tempFile = new File(temp);

        if (tempFile.exists()) {
            if (originalFile.exists()) {
                originalFile.delete(); // Delete the original file if it already exists
            }

            tempFile.renameTo(originalFile); // Rename the temporary file to the original file
        } else {
            System.err.println("Error: Temporary file does not exist in which you should write the original data.");
        }
    }

    @Override
    public void transfer(Account sourceAccount, int targetAccount, double amount) throws IOException {
//        If current user has insufficient balance
        if (sourceAccount.getBalance() < amount) {
            System.out.println("Insufficient balance");
            return;
        }


        FileReader fin = new FileReader("user-details.dat");
        BufferedReader reader = new BufferedReader(fin);

        boolean flag = false;
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            if (targetAccount == Integer.parseInt(parts[3]) && targetAccount!=this.getAccNum()) {
                flag = true;  //  target account present
            }
        }

        reader.close();
        fin.close();

//        target account not present
        if (!flag) {
            System.out.println("Invalid Receiver's account");
            return;
        }

//        Target account present, therefore update the amounts of both account

        fin = new FileReader("user-details.dat");
        reader = new BufferedReader(fin);

//        Temporary file
        FileOutputStream fout = new FileOutputStream("temp-user-details.dat");
        PrintWriter writer = new PrintWriter(fout);

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

//            update the receiver's account
// parts[3]--->accountnumber in file
// parts[4]--->amount in file
            if (targetAccount == Integer.parseInt(parts[3]) && targetAccount!=this.getAccNum()) {
                parts[4] = Double.toString(Double.parseDouble(parts[4]) + amount);
            } else {
//                update the sender's account
                if (this.getAccNum() == Integer.parseInt(parts[3])) {
                    parts[4] = Double.toString(Double.parseDouble(parts[4]) - amount);
                }
            }
            writer.write(String.join(",", parts) + "\n");
        }

        writer.close();
        fout.close();

        fin.close();
        reader.close();

//        Delete the original and rename the new file
        updateFile("user-details.dat","temp-user-details.dat");

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        System.out.println("Transfer successful");
    }

    public void deposit(double amount) throws IOException {
        this.setBalance(this.getBalance() + amount);

        System.out.println("Deposit of " + amount + " successful. New balance is " + balance);

//        Update in file
        FileReader fin = new FileReader("user-details.dat");
        BufferedReader reader = new BufferedReader(fin);

//        Temporary file
        FileOutputStream fout = new FileOutputStream("temp-user-details.dat");
        PrintWriter writer = new PrintWriter(fout);

        boolean flag = false;
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

//            Found the current's user record in the file and update the balance
            if (this.getAccNum() == Integer.parseInt(parts[3])) {
                parts[4] = Double.toString(this.getBalance());
            }
            writer.write(String.join(",", parts) + "\n");
        }

        writer.close();
        fout.close();

        reader.close();
        fin.close();

//        Delete the ori and rename the new file
        updateFile("user-details.dat","temp-user-details.dat");

    }

    public void withdraw(double amount) throws IOException {

        try {

            if (balance < amount) {
                System.out.println("Withdrawal of " + amount + " failed. Insufficient balance.");
            } else {
                balance -= amount;
                System.out.println("Withdrawal of " + amount + " successful. New balance is " + balance);

    //            Also update in the file
                FileReader fin = new FileReader("user-details.dat");
                BufferedReader reader = new BufferedReader(fin);

    //        Temporary file
                FileOutputStream fout = new FileOutputStream("temp-user-details.dat");
                PrintWriter writer = new PrintWriter(fout);

                boolean flag = false;
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    String unameFromFile = parts[0];
                    String pwdFromFile = parts[1];

                    if (this.getAccNum() == Integer.parseInt(parts[3])) {
                        parts[4] = Double.toString(this.getBalance());
                    }

                    writer.write(String.join(",", parts) + "\n");
                }

                writer.close();
                fout.close();

                reader.close();
                fin.close();

    //        Delete the ori and rename the new file
                updateFile("user-details.dat","temp-user-details.dat" );
            } 
        }
        catch (Exception e) {
            System.out.println("Excepiton caught--->"+e);
        }
    }

    public void approveSavingLoan() {
        if (this.balance > 100000) {
            double approveAmount = 1.5 * this.balance;
            System.out.println("Maximum amount of loan you can take: " + approveAmount);

            System.out.println(
                    "Enter amount which you want to take loan:"
            );
            Scanner scan=new Scanner(System.in);
            double loanAmt = scan.nextDouble();

            if (loanAmt > approveAmount) {
                System.out.println(
                        "You are not eligible to take loan more than " + approveAmount + " amount"
                );
            } else {
                System.out.println("Loan approved");
                this.setBalance(this.balance+loanAmt);
            }
        } else {
            System.out.println("You are not eligible for taking loan");
        }
    }

    public void approveCurrentLoan() {
        if (this.balance > 500000) {
            double approveAmount = 3 * this.balance;
            System.out.println("Maximum amount of loan you can take: " + approveAmount);

            Scanner scan=new Scanner(System.in);
            double loanAmt = scan.nextDouble();

            if (loanAmt > approveAmount) {
                System.out.println(
                        "You are not eligible to take loan more than " + approveAmount + " amount"
                );
            } else {
                this.setBalance(this.balance+loanAmt);
            }
        } else {
            System.out.println("You are not eligible for taking loan");
        }
    }
}