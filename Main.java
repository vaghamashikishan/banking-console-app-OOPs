    import java.io.*;
    import java.util.Scanner;

    public class Main {
        public static int acId = 11111;

        public static void setAcId() throws IOException {
            FileReader fin = new FileReader("user-details.dat");
            BufferedReader reader = new BufferedReader(fin);

            boolean flag = false;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                acId = Integer.parseInt(parts[3]);
            }
            reader.close();
            fin.close();
        }

        public static String toString(char[] a) {
            String string = new String(a);

            return string;
        }

        // Read password
        private static String readPassword() {
            Console console;
            if ((console = System.console()) != null) {
                char[] password = console.readPassword();
                for (int i = 0; i < password.length; i++) {
                    System.out.print("*");
                }
                System.out.println();
                return toString(password);
            }
            return null;
        }

        static boolean ExistUser(String uname) throws IOException {
            boolean flag = false;
            try {
                
                FileReader fin = new FileReader("user-details.dat");
                BufferedReader reader = new BufferedReader(fin);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String unameFromFile = parts[0];

                    if (uname.equals(unameFromFile)) {
                        flag = true;
                        break;
                    }
                }
                reader.close();
                fin.close();
            } catch (Exception e) {
                System.out.println("Exception caught--->"+e);
            }
                return flag;
        }

        static Account Login(String curruname, String currupwd) throws IOException {

            Account ac = null;

            try {
                boolean flag = false;
                FileReader fin = new FileReader("user-details.dat");
                BufferedReader reader = new BufferedReader(fin);


                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    String unameFromFile = parts[0];
                    String pwdFromFile = parts[1];

                    if (curruname.equals(unameFromFile) && currupwd.equals(pwdFromFile)) {

                        ac = new Account(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Boolean.parseBoolean(parts[4]));

        //                Access the current logged-in user
                        ac.setuName(parts[0]);
                        ac.setPassword(parts[1]);
                        ac.setFullName(parts[2]);
                        ac.setAccNum(Integer.parseInt(parts[3]));
                        ac.setBalance(Double.parseDouble(parts[4]));
                        ac.setAccType(Boolean.parseBoolean(parts[5]));

                        flag = true;
                        break;
                    }
                }
                reader.close();
                fin.close();

            } catch (Exception e) {
                System.out.println("Exception caught--->"+e);
            }

            return ac;
        }

        private static void updateFile(String original,String temp){

            try {
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
            } catch (Exception e) {
                System.out.println("Exception caught--->"+e);
            }
        }

        static boolean forgotPassword(String uname) throws IOException {
    //                Update the password
    //                1-->Write the contents to temporary file along with the new password
    //                2-->Delete the original file and rename the temporary file

    //        Original file

            boolean flag = false;

            try {
                FileReader fin = new FileReader("user-details.dat");
                BufferedReader reader = new BufferedReader(fin);

        //        Temporary file
                FileOutputStream fout = new FileOutputStream("temp-user-details.dat");
                PrintWriter writer = new PrintWriter(fout);

                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    String unameFromFile = parts[0];
                    String pwdFromFile = parts[1];

                    if (uname.equals(unameFromFile)) {
                        System.out.println("Enter your new password: ");
                        String newpwd = readPassword();

                        parts[1] = newpwd;
                        flag = true;
                    }
                    writer.write(String.join(",", parts) + "\n");
                }

                writer.close();
                fout.close();

                reader.close();
                fin.close();

        //        Delete the ori and rename the new file
                updateFile("user-details.dat","temp-user-details.dat" );
            } catch (Exception e) {
                System.out.println("Exception caught--->"+e);
            }
            return flag;
        }

        public static void adminLogin() throws IOException {
            FileReader fin = new FileReader("user-details.dat");
            BufferedReader reader = new BufferedReader(fin);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String accNo = parts[3];
                String balance = parts[4];
                System.out.println(accNo + "             " + balance);

            }
            reader.close();
            fin.close();

            System.out.println();
            System.out.println("1--->Log out");
            System.out.println("Enter your choice : ");

            Scanner scanner=new Scanner(System.in);
            scanner.nextInt();
        }

        public static void main(String[] args) throws IOException {
            setAcId();
            Scanner scan = new Scanner(System.in);
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(r);
            while (true) {
                try {
                    System.out.println(
                            "------------------------------------- Tech Squad Bank --------------------------------------"
                    );
                    System.out.println("1--->User Login");
                    System.out.println("2--->Admin Login");
                    System.out.println("3--->Account Creation");
                    System.out.println("4--->Exit");
                    System.out.println("Enter your choice : ");
                    int ch = scan.nextInt();
                    Account ac = null;
                    switch (ch) {
                        case 1:
                            System.out.println(
                                    "-------------------------------------User Log In --------------------------------------"
                            );
                            System.out.println("Enter Username : ");
                            String curruname = br.readLine();
                            System.out.println("Enter Password : ");
                            String currupwd = readPassword();

                            ac = Login(curruname, currupwd);
                            if (ac == null) {
                                System.out.println(
                                        "-----------------------------------------------------------------------------------"
                                );
                                System.out.println("1-->Forgot Password");
                                System.out.println("2-->Back to Main Menu");
                                System.out.println(
                                        "-----------------------------------------------------------------------------------"
                                );

                                ch = scan.nextInt();

                                // Retry , i.e start from the beginning
                                if (ch == 2) continue;

                                // Forgot password
                                System.out.println("Enter the username : ");
                                String findUser = br.readLine();
                                boolean status = forgotPassword(findUser);

                                if (status == true) System.out.println("Password updated successfully");
                                else System.out.println("Password not updated or Username not found");
                            }else{
                                System.out.println(
                                        "You are successfully logged in..."
                                );
                            }
                            break;
                        case 2:
                            System.out.println("Username : ");
                            curruname = br.readLine();
                            System.out.println("Password : ");
                            currupwd = readPassword();

                            // Admin LogIn
                            if (curruname.equals("admin") && currupwd.equals("admin")) {
                                System.out.println("Admin Login successful!!");
                                System.out.println(
                                        "-----------------------------------------------------------------------------------"
                                );
                                System.out.println(
                                        "Account No.       Balance "
                                );
                                adminLogin();
                                System.out.println(
                                        "-----------------------------------------------------------------------------------"
                                );

                            } else {
                                System.out.println("Admin Login Failed");
                            }

                            break;
                        case 3:
                            System.out.println(
                                    "------------------------------------ Registration ---------------------------------"
                            );


                            System.out.println("Enter Username : ");
                            String uname = br.readLine();

                            //check if username is taken or not
    //                        If not then create an account and insert the record in the file
                            if (!ExistUser(uname)) {
                                System.out.println("Enter Full name : ");
                                String fullname = br.readLine();

                                System.out.println("Enter password : ");
                                String uPwd = readPassword();

                                System.out.println("Enter initial Amount : ");
                                double balance = scan.nextDouble();

                                System.out.println("0-->Savings account");
                                System.out.println("1-->Current account");
                                int savings = scan.nextInt();
                                boolean ac_type;
                                if (savings == 1) ac_type = true;
                                else ac_type = false;

                                FileOutputStream fout = new FileOutputStream("user-details.dat", true);
                                PrintWriter writer = new PrintWriter(fout);

                                String str = uname + "," + uPwd + "," + fullname + "," + ++acId + "," + balance + "," + ac_type + "\n";
                                writer.write(str);
                                System.out.println("Account created successfully......");
                                writer.close();
                                fout.close();
                            } else {
                                System.out.println("User already exists!");
                            }

                            break;
                        case 4:
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid Choice!!");
                            break;
                    }

                    //User Side
                    while (ac!=null){
                        System.out.println(
                                "-----------------------------------------------------------------------------------"
                        );
                        System.out.println("1-->Display Profile");
                        System.out.println("2-->Calculate Interest");
                        System.out.println("3-->Transfer Money");
                        System.out.println("4-->Deposit Money");
                        System.out.println("5-->Withdraw Money");
                        System.out.println("6-->Take Loan");
                        System.out.println("7-->Logout");
                        System.out.println(
                                "-----------------------------------------------------------------------------------"
                        );
                        System.out.println("Enter your choice : ");
                        try {
                            int logichoice = scan.nextInt();
                            System.out.println();
                            switch (logichoice) {
                                // Show profile
                                case 1:
                                    ac.ShowProfile();
                                    System.out.println(
                                            "-----------------------------------------------------------------------------------"
                                    );
                                    System.out.println("1-->Back");
                                    System.out.println("2-->Logout");
                                    System.out.println(
                                            "-----------------------------------------------------------------------------------"
                                    );

                                    System.out.println("Enter your choice : ");
                                    int profilechoice = scan.nextInt();

                                    // Logout
                                    if (profilechoice == 2) {
                                        ac = null;
                                        System.out.println("Successfully Logged Out!!");
                                    }

                                    break;

                                //calculate interest
                                case 2:
                                    System.out.println(
                                            "Enter amount to calculate interest:"
                                    );
                                    double amt = scan.nextDouble();
                                    System.out.println(
                                            "Enter time period:"
                                    );
                                    int time = scan.nextInt();
                                    ac.calculateInterest(amt,time);
                                    break;

                                //Transfer Money
                                case 3:
                                    System.out.println(
                                            "Enter bank account number:"
                                    );
                                    int recAccNum = scan.nextInt();


                                    System.out.println(
                                            "Enter amount to transfer:"
                                    );
                                    double transferAmt = scan.nextDouble();
                                    ac.transfer(ac,recAccNum,transferAmt);
                                    break;

                                // Deposit Money
                                case 4:
                                    System.out.println(
                                            "Enter amount to Deposit:"
                                    );
                                    double depositAmt = scan.nextDouble();
                                    ac.deposit(depositAmt);
                                    break;
                                // Withdraw Money
                                case 5:
                                    System.out.println(
                                            "Enter amount to Withdraw:"
                                    );
                                    double withdrawAmt = scan.nextDouble();
                                    ac.withdraw(withdrawAmt);
                                    break;

                                //take loan
                                case 6:
                                    if(ac.getAccType()==false){
                                        ac.approveSavingLoan();
                                    }else{
                                        ac.approveCurrentLoan();
                                    }
                                    break;
                                case 7:
                                    ac = null;
                                    System.out.println("Successfully Logged Out!!");
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            System.out.println("Exception Caught--->" + e);
                            scan.nextLine();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Exception caught-->" + e);
                }
            }
        }
    }
