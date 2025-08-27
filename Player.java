import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private static final int MAX_HP = 100;

    private final String firstName;
    private final String lastName;
    private final List<Tool> tools = new ArrayList<>();
    private final boolean bot;
    private final int difficulty;

    private int armor;
    private int attack;
    private int precision;
    private int hp;
    private int luck;
    private Weapon weapon;
    private Defense defense;
    private Tracking tracking;

    public Player(String firstName, String lastName, boolean bot, int difficulty) {
        this.difficulty = difficulty;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bot = bot;
        Random rand = new Random();
        this.armor = rand.nextInt(13);
        this.attack = rand.nextInt(1, 6);
        this.precision = rand.nextInt(20, 81);
        this.hp = MAX_HP;
        this.luck = rand.nextInt(1, 41);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public int getArmor() {
        return armor;
    }

    public int getAttack() {
        return attack;
    }

    public int getHp() {
        return hp;
    }

    public int getLuck() {
        return luck;
    }

    public int calculateTotalLuck() {
        return getLuck() + (defense != null ? defense.getLuck() : 0);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Defense getDefense() {
        return defense;
    }

    public void setDefense(Defense defense) {
        this.defense = defense;
    }

    public Tracking getTracking() {
        return tracking;
    }

    public void setTracking(Tracking tracking) {
        this.tracking = tracking;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public boolean isBot() {
        return bot;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getFullname());
        return sb.append(": ")
                .append("HP:").append(hp).append("/100 | ")
                .append("ATK:").append(attack).append(" | ")
                .append("ARM:").append(armor).append(" | ")
                .append("PRÉC:").append(precision).append(" | ")
                .append("LUCK:").append(luck)
                .append("\r\n")
                .append("Arme:").append((weapon != null && weapon.isUsable()) ? weapon.toString() : "Aucune").append("\r\n")
                .append("Défense:").append((defense != null && defense.isUsable()) ? defense.toString() : "Aucune").append("\r\n")
                .append("Tracking:").append(tracking != null ? tracking.toString() : "Aucun").append("\r\n")
                .append("Outil(s):").append(tools.isEmpty() ? "Aucun" : tools).toString();
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int calculateDamage() {
        Random rand = new Random();
        boolean hit = rand.nextInt(1, 101) < calculateTotalLuck() / 2 + precision;
        int finalDamage = 0;
        if (hit) {
            boolean isCritical = rand.nextInt(1, 101) < calculateTotalLuck();

            int baseDamage = attack;
            if (weapon != null && weapon.isUsable()) {
                baseDamage += weapon.getDamage();
                weapon.decreaseDurability();
            }

            finalDamage = isCritical ? (int) (baseDamage * 1.5) : baseDamage;
            if (isCritical) {
                Console.println(getFullname() + " fait un COUP CRITIQUE ! (" + finalDamage + " dégâts)", Color.PURPLE);
            }
        } else {
            Console.println(getFullname() + "  a raté la cible! :(", Color.PURPLE);
        }

        return finalDamage;
    }

    public void takeDamage(int incomingDamage) {
        if (incomingDamage == 0) {
            return;
        }
        int totalDefense = armor;

        if (defense != null && defense.isUsable()) {
            totalDefense += defense.getDefense();
            defense.decreaseDurability();
        } else {
            Console.println(getFirstName() + " n'a plus de protection !", Color.YELLOW);
        }

        int finalDamage = Math.max(0, incomingDamage - totalDefense);
        hp -= finalDamage;

        if (hp < 0) {
            hp = 0;
        }

        Console.println(getFullname() + " subit " + finalDamage + " dégâts !", Color.RED);
    }

    public void useTool() {
        if (hp == MAX_HP) {
            Console.println("Vous avez deja le maximum de hp.", Color.YELLOW);
            return;
        }
        if (tools.isEmpty()) {
            Console.println("Vous n'avez pas d'outils.", Color.RED);
            return;
        }

        Console.println("Quel outil voulez-vous utiliser ?", Color.WHITE);
        List<Tool> toolsToRemove = new ArrayList<>();
        for (Tool tool : tools) {
            Scanner scanner = new Scanner(System.in);
            String choice;

            do {
                Console.println("HP= " + hp + ", voulez-vous utiliser: " + tool + " ? (yes/no)", Color.WHITE);
                choice = scanner.nextLine().trim().toLowerCase();
            } while (!choice.equals("yes") && !choice.equals("no"));

            if (choice.equals("yes")) {
                hp += tool.getHealing();
                toolsToRemove.add(tool);
                if (hp > MAX_HP) {
                    hp = MAX_HP;
                }
            }
        }
        tools.removeAll(toolsToRemove);
    }

    public void combat(Player target) {
        Console.println(getFullname() + " VS " + target.getFullname(), Color.YELLOW);
        boolean userIsFirst = userAttacksFirst(target);
        Player attacker = userIsFirst ? this : target;
        Player defender = userIsFirst ? target : this;
        boolean fuir = false;

        while (isAlive() && target.isAlive()) {
            displayCombatInventory(target);
            Console.println("\nAu tour de " + attacker.getFullname(), Color.YELLOW);
            if (attacker.isBot()) {
                fuir = attacker.aiCombat(attacker, defender);
            } else {
                fuir = userCombat(attacker, defender);
            }
            if (fuir) {
                break;
            }

            if (defender.isAlive()) {
                Player tmp = attacker;
                attacker = defender;
                defender = tmp;
            }
        }

        if (!fuir && isAlive() && !target.isAlive()) {
            Console.println(attacker.getFullname() + " a gagné le combat !", Color.GREEN);
            displayHpAndUseTools();
        } else if (!fuir && target.isAlive() && !isAlive()) {
            Console.println(defender.getFullname() + " a gagné le combat !", Color.RED);
        }
    }

    private boolean aiCombat(Player attacker, Player defender) {
        if (difficulty == 1) {
            Console.println(attacker.getFirstName() + "ne fait rien", Color.PURPLE);
            return false;
        } else {
            return attacker.aiCombatHard(defender); // IA avancée
        }
    }

    private boolean aiCombatHard(Player defender) {
        Random rand = new Random();

        int selfDamage = attack + (weapon != null && weapon.isUsable() ? weapon.getDamage() : 0);
        int selfDefense = armor + (defense != null && defense.isUsable() ? defense.getDefense() : 0);
        boolean hasTool = !tools.isEmpty();

        int enemyDamage = defender.getAttack() +
                (defender.getWeapon() != null && defender.getWeapon().isUsable() ? defender.getWeapon().getDamage() : 0);
        int enemyDefense = defender.getArmor() +
                (defender.getDefense() != null && defender.getDefense().isUsable() ? defender.getDefense().getDefense() : 0);
        int enemyHP = defender.getHp();

        boolean canFinishEnemy = enemyHP + enemyDefense <= selfDamage;
        boolean canBeFinished = hp + selfDefense <= enemyDamage;
        boolean hopeless = !hasTool && !canFinishEnemy && canBeFinished;

        if (hopeless || hp < 30 && !hasTool) {
            int chance = rand.nextInt(1, 101);
            if (chance <= calculateTotalLuck()) {
                Console.println(getFirstName() + " a fui avec succès !", Color.BLUE);
                return true;
            } else {
                Console.println(getFirstName() + " a tenté de fuir mais a échoué...", Color.RED);
            }
            return false;
        }

        if (hp <= 50 && hasTool) {
            Tool tool = tools.getFirst();
            int heal = tool.getHealing();
            hp = Math.min(MAX_HP, hp + heal);
            tools.remove(tool);
            Console.println(getFirstName() + " utilise " + tool + " et récupère " + heal + " HP.", Color.GREEN);
            return false;
        }

        int calculatedDamage = this.calculateDamage();
        defender.takeDamage(calculatedDamage);
        Console.println(getFullname() + " attaque et inflige " + calculatedDamage + " dégâts !", Color.RED);
        return false;
    }

    private boolean userCombat(Player attacker, Player defender) {
        int choice;
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        while (true) {
            Console.println("1. Attaquer", Color.RED);
            Console.println("2. Utiliser un outil", Color.GREEN);
            Console.println("3. Fuir", Color.BLUE);
            Console.println("4. Ne rien faire", Color.YELLOW);
            System.out.println();
            Console.println("Votre choix: (1-4)", Color.WHITE);
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 4) break;
            } else {
                scanner.nextLine();
            }
            Console.println("Entrée invalide. Veuillez entrer un nombre entre 1 et 4.", Color.RED);
        }
        switch (choice) {
            case 1:
                int damage = calculateDamage();
                defender.takeDamage(damage);
                Console.println(this.getFirstName() + " attaque et inflige " + damage + " dégâts !", Color.RED);
                break;

            case 2:
                attacker.useTool();
                break;

            case 3:
                int chanceLeave = rand.nextInt(100) + 1;
                if (chanceLeave <= attacker.getLuck()) {
                    Console.println(attacker.getFirstName() + " a réussi à fuir !", Color.BLUE);
                    return true;
                } else {
                    Console.println(attacker.getFirstName() + " n'a pas réussi à fuir...", Color.RED);
                }
                break;

            case 4:
                Console.println("Vous n'avez rien fait.", Color.BLUE);
                break;
        }
        return false;
    }

    public void loot(Player deadPlayer) {
        Scanner scanner = new Scanner(System.in);

        Console.println("Vous obtenez un lapin suite à la transformation de: " + deadPlayer.getFullname(), Color.WHITE_BOLD_BRIGHT);
        getTools().add(Tool.Builder.buildRabbit());
        Console.println(this.toString(), Color.GREEN);

        if (deadPlayer.getWeapon() != null
                || deadPlayer.getTracking() != null
                || deadPlayer.getDefense() != null) {
            Console.println("Votre ennemi avait ces item(s):", Color.WHITE_BOLD_BRIGHT);

            if (deadPlayer.getDefense() != null) {
                Console.println("- " + deadPlayer.getDefense(), Color.WHITE_BOLD_BRIGHT);
                if (askYesNo(scanner, "Voulez-vous le looter? (yes/no)")) {
                    setDefense(deadPlayer.getDefense());
                }
            }

            if (deadPlayer.getWeapon() != null) {
                Console.println("- " + deadPlayer.getWeapon(), Color.WHITE_BOLD_BRIGHT);
                if (askYesNo(scanner, "Voulez-vous le looter? (yes/no)")) {
                    setWeapon(deadPlayer.getWeapon());
                }
            }

            if (deadPlayer.getTracking() != null) {
                Console.println("- " + deadPlayer.getTracking(), Color.WHITE_BOLD_BRIGHT);
                if (askYesNo(scanner, "Voulez-vous le looter? (yes/no)")) {
                    setTracking(deadPlayer.getTracking());
                }
            }
        }
    }

    private boolean userAttacksFirst(Player target) {
        Random rand = new Random();
        int userLuck = getLuck();
        int targetLuck = target.getLuck();
        int remainingLuck = 100 - (userLuck + targetLuck);
        if (remainingLuck % 2 == 1) {
            if (userLuck >= targetLuck) {
                userLuck++;
            }
            remainingLuck--;
        }
        userLuck += remainingLuck / 2;
        return rand.nextInt(1, 100) <= userLuck;
    }

    public void displayHpAndUseTools() {
        Scanner scanner = new Scanner(System.in);

        Console.println("HP actuel : " + hp + "/100", Color.RED);

        if (tools.isEmpty()) {
            Console.println("Vous n'avez pas d'outils pour vous guérir dans votre inventaire.", Color.RED);
            return;
        }

        Console.println("tools disponibles :", Color.WHITE);
        for (int i = 0; i < tools.size(); i++) {
            Console.println((i + 1) + ". " + tools.get(i), Color.GREEN);
        }

        Console.println("Voulez-vous utiliser un tool pour récupérer des HP ? (yes/no)", Color.WHITE);
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            int toolIndex;
            while (true) {
                Console.println("Entrez le numéro de l'outil à utiliser :", Color.YELLOW);
                if (scanner.hasNextInt()) {
                    toolIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (toolIndex >= 0 && toolIndex < tools.size()) break;
                } else {
                    scanner.nextLine();
                }
                Console.println("Entrée invalide. Choisissez un numéro valide.", Color.RED);
            }

            Tool selectedTool = tools.get(toolIndex);
            int healAmount = selectedTool.getHealing();
            hp = Math.min(100, hp + healAmount);
            tools.remove(toolIndex);

            Console.println("Vous avez utilisé " + selectedTool + " et récupéré " + healAmount + " HP !", Color.GREEN);
            Console.println("HP après soin : " + hp + "/100", Color.RED);
        } else {
            Console.println("Vous avez décidé de ne pas utiliser d'outil.", Color.BLUE);
        }
    }

    public void setCheatStats() {
        this.hp = 100;
        this.attack = 100;
        this.armor = 100;
        this.precision = 95;
        this.luck = 80;

        Console.println("Stats boostées pour Bruce Wayne !", Color.PURPLE);
    }

    public void displayCombatInventory(Player target) {
        Console.println("INVENTAIRE & ÉQUIPEMENT", Color.CYAN_BOLD_BRIGHT);
        Console.println(this.toString(), Color.YELLOW);
        Console.println("\n", Color.YELLOW);
        Console.println(target.toString(), Color.RED);
    }

    private boolean askYesNo(Scanner scanner, String prompt) {
        String response;
        while (true) {
            Console.println(prompt, Color.WHITE_BOLD_BRIGHT);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                return true;
            } else if (response.equals("no")) {
                return false;
            } else {
                Console.println("Entrée invalide. Veuillez répondre par 'yes' ou 'no'.", Color.RED);
            }
        }
    }
}
