import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameInitializer {
    public static List<Player> generateBots(int nbBot, int difficulty) {
        List<Player> players = new ArrayList<>();
        try {
            List<String> firstNames = Files.readAllLines(Paths.get("firstName.txt"));
            List<String> lastNames = Files.readAllLines(Paths.get("lastName.txt"));
            Random rand = new Random();

            for (int i = 0; i < nbBot; i++) {
                String firstName = firstNames.get(rand.nextInt(firstNames.size()));
                String lastName = lastNames.get(rand.nextInt(lastNames.size()));
                Player bot = new Player(firstName, lastName, true, difficulty);
                assignRandomStartingItem(bot);
                players.add(bot);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    public static Player generateUserPlayer(int difficulty) {
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";

        while (firstName.isBlank()) {
            Console.println("Entrez votre prénom : ", Color.GREEN);
            firstName = scanner.nextLine().trim();
            if (firstName.isBlank()) {
                Console.println("Le prénom ne peut pas être vide.", Color.RED_BOLD_BRIGHT);
            }
        }

        while (lastName.isBlank()) {
            Console.println("Entrez votre nom : ", Color.GREEN);
            lastName = scanner.nextLine().trim();
            if (lastName.isBlank()) {
                Console.println("Le nom ne peut pas être vide.", Color.RED_BOLD_BRIGHT);
            }
        }

        Player user = new Player(firstName, lastName, false, difficulty);

        if (firstName.equalsIgnoreCase("Bruce") && lastName.equalsIgnoreCase("Wayne")) {
            Console.println("Cheat mode activated!", Color.GREEN_BOLD_BRIGHT);

            user.setWeapon(Inventory.getRandomWeapon());
            user.setDefense(Inventory.getRandomDefense());
            user.setTracking(Inventory.getRandomTracking());
            user.getTools().add(Tool.Builder.buildRabbit());
            user.getTools().add(Tool.Builder.buildRabbit());
            user.getTools().add(Tool.Builder.buildRabbit());
            user.setCheatStats();
        } else {
            assignRandomStartingItem(user);
        }
        return user;
    }


    public static void assignRandomStartingItem(Player player) {
        Random rand = new Random();
        int itemType = rand.nextInt(1, 4);

        switch (itemType) {
            case 1:
                player.setDefense(Inventory.getRandomDefense());
                break;
            case 2:
                player.setWeapon(Inventory.getRandomWeapon());
                break;
            case 3:
                player.setTracking(Inventory.getRandomTracking());
                break;
        }

        player.getTools().add(Tool.Builder.buildRabbit());
        player.getTools().add(Tool.Builder.buildRabbit());
        player.getTools().add(Tool.Builder.buildRabbit());
        player.getTools().add(Inventory.getRandomTool());
    }

    public static int askDifficulty() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        while (true) {
            Console.println("\nChoisissez la difficulté :", Color.WHITE_BOLD_BRIGHT);
            Console.println("1 - Facile (bots inactifs)", Color.WHITE_BOLD_BRIGHT);
            Console.println("2 - Difficile (IA stratégique)", Color.WHITE_BOLD_BRIGHT);
            Console.print("Votre choix : ", Color.WHITE_BOLD_BRIGHT);

            choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                Console.println("Difficulté sélectionnée : Facile", Color.YELLOW_BOLD_BRIGHT);
                return 1;
            } else if (choice.equals("2")) {
                Console.println("Difficulté sélectionnée : Difficile", Color.CYAN_BOLD);
                return 2;
            } else {
                Console.println("Entrée invalide. Veuillez entrer 1 ou 2.", Color.RED_BOLD_BRIGHT);
            }
        }
    }

}
