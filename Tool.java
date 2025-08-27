import java.util.Random;

public class Tool {
    private final String name;
    private final int healing;
    private final boolean random;

    private Tool(String name, int healing, boolean random) {
        this.name = name;
        this.healing = healing;
        this.random = random;
    }


    public int getHealing() {
        boolean negative = false;
        if (random) {
            negative = new Random().nextBoolean();
        }

        return negative ? Math.negateExact(healing) : healing;
    }

    @Override
    public String toString() {
        return name + " (Effet: " + (random ? "+/- " : " ") + healing + " HP)";
    }

    public static class Builder {
        public static Tool buildBandage() {
            return new Tool("Bandage", 5, false);
        }

        public static Tool buildMedkit() {
            return new Tool("Medkit", 20, false);
        }

        public static Tool buildPainkillers() {
            return new Tool("Anti-douleurs", 10, false);
        }

        public static Tool buildStim() {
            return new Tool("Stim", 40, false);
        }

        public static Tool buildRabbit() {
            return new Tool("Lapin", 10, false);
        }

        public static Tool buildHerbs() {
            return new Tool("Herbes Louches", 5, true);
        }
    }
}
