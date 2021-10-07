import java.util.*;
import java.io.*;

public class AccountHolderClient {
    static Scanner keyb = new Scanner(System.in);
    static String FILE_NAME = "friendsData.txt";
    public static void main(String[] args) {
        System.out.println("Welcome to the social network progam.");
        if (args.length >= 1) {
            FILE_NAME = args[0];
        }
        
        Map<String,AccountHolder> users = readAccountsFromFile(FILE_NAME);
           
        System.out.printf("There are %d account holders\n",users.size());
        int choice = 0;
        while (choice != 5) {
            printMenu();
            System.out.print("\nEnter your choice (1-5): ");
            choice = Integer.parseInt(keyb.nextLine());
            switch (choice) {
                case 1:
                    users = addNewAccount(users);
                    break;
                case 2:
                    users = connectTwoPersons(users);
                    break;
                case 3:
                    listAccountFriends(users);
                    break;
                case 4:
                    checkIfFriends(users);
                    break;
                case 5:
                    saveChanges(users);
                    System.out.println("Thanks for using the social network. Bye.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
        
        
        
        
    }//main
    
    //saveChanges(users);
    static void saveChanges(Map<String,AccountHolder> users) {
        try (PrintWriter pw = new PrintWriter(new File(FILE_NAME))) {
            for (Map.Entry<String, AccountHolder> user : users.entrySet()) {
                pw.println(user.getValue().toFileString());
            }
        } catch (FileNotFoundException e) {
            System.err.printf("Could not write file, '%s'\n",FILE_NAME);
        }
    }
    
    //users = connectTwoPersons(users);
    static Map<String,AccountHolder> connectTwoPersons(Map<String,AccountHolder> users) {
        String id1 = "";
        String id2 = "";
        while (id1.equals("") || id2.equals("")) {
            while (id1.equals("")) {
                System.out.print("What's the first person's ID? ");
                id1 = keyb.nextLine();
                if (!users.containsKey(id1)) {
                    System.out.println("Invalid ID");
                    id1 = "";
                }
            }
            System.out.printf("(%s) %s\n",id1, users.get(id1).getFullName());
            
            while (id2.equals("")) {
                System.out.print("What's the second person's ID? ");
                id2 = keyb.nextLine();
                if (!users.containsKey(id2)) {
                    System.out.println("Invalid ID");
                    id2 = "";
                } else if (id2.equals(id1)) {
                    System.out.println("Choose a different ID");
                    id2 = "";
                }
            }
            System.out.printf("(%s) %s\n",id2, users.get(id2).getFullName());
        }
        
        if (users.get(id1).getFriendIDs().contains(id2)) {
            System.out.printf("%s and %s are already friends.\n",
                             users.get(id1).getFullName(),users.get(id2).getFullName());
        } else {
            users.get(id1).getFriendIDs().add(id2);
            users.get(id2).getFriendIDs().add(id1);
            System.out.printf("%s and %s are now connected as friends.\n",
                             users.get(id1).getFullName(),users.get(id2).getFullName());
        }
        
        return users;
    }
    
    //users = addNewAccount(users);
    static Map<String,AccountHolder> addNewAccount(Map<String,AccountHolder> users) {
        String newID = "";
        while (newID.equals("")) {
            System.out.print("What's the ID of the new person? ");
            newID = keyb.nextLine();
            if (users.containsKey(newID)) {
                System.out.println("Sorry, that ID is already taken.");
                newID = "";
            }  
        }
        System.out.print("First name: ");
        String newFirstName = keyb.nextLine();
        System.out.print("Last name: ");
        String newLastName = keyb.nextLine();
        System.out.print("Email: ");
        String newEmail = keyb.nextLine();
        AccountHolder newUser = new AccountHolder(newID,newFirstName,
                                                  newLastName,newEmail);
        users.put(newID, newUser);
        System.out.printf("(%s) %s added.\n",newID,newUser.getFullName());
        return users;
    }
    
    //checkIfFriends(users);
    static void checkIfFriends(Map<String,AccountHolder> users) {
        System.out.print("What's the first person's ID? ");
        String target1 = keyb.nextLine().toLowerCase();
        AccountHolder user1 = users.get(target1);
        System.out.printf("(%s) %s\n",target1,user1.getFullName());
        
        System.out.print("What's the second person's ID? ");
        String target2 = keyb.nextLine().toLowerCase();
        AccountHolder user2 = users.get(target2);
        System.out.printf("(%s) %s\n",target2,user2.getFullName());
        
        if (user1.getFriendIDs().contains(target2)) {
            System.out.printf("%s and %s are friends\n",
                              user1.getFullName(),user2.getFullName());
        } else {
            System.out.printf("%s and %s are not friends\n",
                              user1.getFullName(),user2.getFullName());
        }
    }
    
    //listAccountFriends(keyb.nextLine().toLowerCase, users);
    static void listAccountFriends(Map<String,AccountHolder> users) {
        
        System.out.print("What's the account holder ID? ");
        String target = keyb.nextLine().toLowerCase();
        AccountHolder targetUser = users.get(target);
        if (targetUser != null) {
            String targetName = targetUser.getFullName();
            List<String> targetFriends = new ArrayList<>(targetUser.getFriendIDs());
            if (targetFriends.size() >= 1) {
                System.out.printf("(%s) %s has %d friends:\n",target,targetName,targetFriends.size());
                for (String acctID : targetFriends) {
                    AccountHolder acct = users.get(acctID);
                    System.out.printf("\t%s\n",acct.getFullName());
                }
            } else {
                System.out.printf("(%s) %s has no friends\n",target,targetName);
            }
        } else {
            System.out.println("User does not exist");
        } 
    }
    
    //printMenu();
    static void printMenu() {
        System.out.println("\n  - - < < Main Menu > > - - ");
        System.out.println("1) Add new account");
        System.out.println("2) Connect two persons as friends");
        System.out.println("3) List all friends of a person");
        System.out.println("4) Check whether two people are friends");
        System.out.println("5) Save & Quit");
    }
    
    //Map<String,AccountHolder> users = readAccountsFromFile(FILE_NAME);
    static Map<String,AccountHolder> readAccountsFromFile(String filename) {
        Map<String,AccountHolder> users = new HashMap<>();
        try (Scanner fileScan = new Scanner (new File(filename))) {
            while (fileScan.hasNextLine()) {
               String dataLine = fileScan.nextLine();
                AccountHolder newAccount = new AccountHolder(dataLine);
                users.put(newAccount.getID(), newAccount) ;
            }
        } catch (Exception e) {
            System.err.printf("Couldn't open '%s'\n",filename);
            System.exit(0);
        }
        return users;
    }
    
    //printAccountHolders(users);
    static void printAccountHolders(Map<String,AccountHolder> users) {
        for (Map.Entry<String, AccountHolder> user : users.entrySet()) {
            System.out.println(user.getValue());
        }
    }
    
    
}//class