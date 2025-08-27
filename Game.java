import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private static final int MAX_FIGHTS = 4;

    private final List<Player> players;
    private final List<Player> deadPlayers;
    private final Player userPlayer;

    private int day = 1;

    public Game(int difficulty) {
        this.userPlayer = GameInitializer.generateUserPlayer(difficulty);
        this.players = GameInitializer.generateBots(41, difficulty);
        this.deadPlayers = new ArrayList<>();
    }

    public void startGame() {
        displayWelcomeMessage();

        while (userPlayer.isAlive() && players.size() > 1 && day <= 3) {
            delay();
            executeDay();
            executeNight();
            day++;
        }

        if (userPlayer.isAlive()) {
            Console.println("Vous avez gagné! 🏆", Color.GREEN);
        } else {
            Console.println("Vous êtes mort!", Color.RED);
        }
    }

    private void displayDeadPlayers() {
        if (!deadPlayers.isEmpty()) {
            Console.println("Joueurs éliminés : ", Color.RED);
            for (Player player : deadPlayers) {
                Console.println("- " + player.getFirstName() + " " + player.getLastName(), Color.RED);
            }
            deadPlayers.clear();
        }
    }

    private void executeNight() {
        Console.println("La nuit tombe... le fléau se répend!", Color.CYAN);
        nightSurvivalCheck();
        displayDeadPlayers();
        if (!players.isEmpty()) {
            Console.println("Les survivants se préparent pour un nouveau combat.", Color.CYAN);
        }
    }

    private void executeDay() {
        Console.println("*** JOUR " + day + " ***", Color.YELLOW_BOLD_BRIGHT);
        int fights = Math.max(1, MAX_FIGHTS - (userPlayer.getTracking() != null ? userPlayer.getTracking().getNavigation() : 0));
        int idx = 0;
        while (idx < fights && !players.isEmpty()) {
            Player target = getRandomOpponent();
            userPlayer.combat(target);
            idx++;
            if (!userPlayer.isAlive()) {
                Console.println("GAME OVER!!!!", Color.RED_BOLD_BRIGHT);
                break;
            } else if (!target.isAlive()) {
                deadPlayers.add(target);
                players.remove(target);
                userPlayer.loot(target);
            }
        }
    }


    private Player getRandomOpponent() {
        if (players.isEmpty()) return null;
        Random rand = new Random();
        return players.get(rand.nextInt(players.size()));
    }

    public void displayWelcomeMessage() {
        Console.println("Le monde est au bord du chaos. La surpopulation, les conflits incessants et l'effondrement des ressources ont poussé les gouvernements à adopter des mesures radicales.\n" +
                "\nChaque année, des milliers de participants sont sélectionnés pour s'affronter dans l'Acte Battle Royale. Le but est simple : survivre.\n" +
                "\nBienvenue dans l'arène. Votre vie ne tient qu'à vos choix.", Color.BLUE);
        Console.println(userPlayer.toString(), Color.BLUE_BOLD_BRIGHT);
    }

    private void delay() {
        for (int i = 5; i > 0; i--) {
            System.out.println("Le jour " + day + " de cette édition de Battle Royale commence dans + " + i + " secondes...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void nightSurvivalCheck() {
        Random rand = new Random();
        for (Player player : players) {
            int deathChance = switch (day) {
                case 1 -> 45;
                case 2 -> 30;
                case 3 -> 20;
                default -> 0;
            };

            if (rand.nextInt(101) > deathChance) {
                deadPlayers.add(player);
            }
        }
        players.removeAll(deadPlayers);
    }


}
