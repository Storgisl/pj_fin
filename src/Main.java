package app;

import model.User;
import service.AuthService;
import service.PersistenceService;
import service.WalletService;
import util.ConsoleUtils;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleUtils.welcome();
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();
        PersistenceService persistence = new PersistenceService();
        WalletService ws = new WalletService(persistence);

        User current = null;
        boolean running = true;

        while (running) {
            try {
                System.out.print(current == null ? "[guest]> " : "[" + current.getLogin() + "]> ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+", 2);
                String cmd = parts[0].toLowerCase();
                String arg = parts.length > 1 ? parts[1].trim() : "";

                switch (cmd) {
                    case "help":
                        ConsoleUtils.printHelp();
                        break;
                    case "register":
                        if (arg.isEmpty()) { System.out.println("Usage: register <login>"); break; }
                        System.out.print("Password: ");
                        String pw = sc.nextLine();
                        if (auth.register(arg, pw)) System.out.println("Registered: " + arg);
                        else System.out.println("User exists or invalid login.");
                        break;
                    case "login":
                        if (arg.isEmpty()) { System.out.println("Usage: login <login>"); break; }
                        System.out.print("Password: ");
                        String pass = sc.nextLine();
                        current = auth.login(arg, pass);
                        if (current != null) {
                            System.out.println("Logged in as " + current.getLogin());
                            ws.loadWalletFor(current);
                        } else System.out.println("Login failed.");
                        break;
                    case "logout":
                        if (current != null) {
                            ws.saveWalletFor(current);
                            System.out.println("Saved wallet and logged out: " + current.getLogin());
                            current = null;
                        } else System.out.println("Not logged in.");
                        break;
                    case "add-income":
                        if (current == null) { System.out.println("Login first."); break; }
                        ws.handleAddIncome(current, arg);
                        break;
                    case "add-expense":
                        if (current == null) { System.out.println("Login first."); break; }
                        ws.handleAddExpense(current, arg);
                        break;
                    case "set-budget":
                        if (current == null) { System.out.println("Login first."); break; }
                        ws.handleSetBudget(current, arg);
                        break;
                    case "summary":
                        if (current == null) { System.out.println("Login first."); break; }
                        ws.printSummary(current);
                        break;
                    case "transfer":
                        if (current == null) { System.out.println("Login first."); break; }
                        ws.handleTransfer(current, arg, auth);
                        break;
                    case "export":
                        if (current == null) { System.out.println("Login first."); break; }
                        ws.exportWallet(current, arg);
                        break;
                    case "import":
                        if (current == null) { System.out.println("Login first."); break; }
                        ws.importWallet(current, arg);
                        break;
                    case "exit":
                        if (current != null) ws.saveWalletFor(current);
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }
}
