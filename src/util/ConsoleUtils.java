package util;

import model.Wallet;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class ConsoleUtils {
    private static final Map<String, Wallet> runtimeWallets = new HashMap<>();

    public static void welcome() { System.out.println("Personal Finance Manager (CLI)\\nType 'help' for commands."); }
    public static void printHelp() {
        System.out.println("Commands:\n" +
                "  register <login>\n" +
                "  login <login>\n" +
                "  logout\n" +
                "  add-income <cat> <amt> [note]\n" +
                "  add-expense <cat> <amt> [note]\n" +
                "  set-budget <cat> <limit>\n" +
                "  summary\n" +
                "  exit");
    }

    public static void setWallet(User u, Wallet w) { runtimeWallets.put(u.getLogin(), w); }
    public static Wallet getWallet(User u) { return runtimeWallets.computeIfAbsent(u.getLogin(), k->new Wallet()); }
}

