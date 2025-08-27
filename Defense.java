public class Defense {
    private String name;
    private int defense;
    private int luck;
    private int durability;

    private Defense() {
    }

    public String getName() {
        return name;
    }

    public int getDefense() {
        return defense;
    }

    public int getLuck() {
        return luck;
    }

    public int getDurability() {
        return durability;
    }

    public class Builder {
        public static Defense buildKabutohelmet() {
            Defense defense = new Defense();
            defense.name = "Kabuto Helmet";
            defense.defense = 30;
            defense.luck = 0;
            defense.durability = 100;
            return defense;
        }

        public static Defense buildMenpo() {
            Defense defense = new Defense();
            defense.name = "Menpô";
            defense.defense = 20;
            defense.luck = 0;
            defense.durability = 10;
            return defense;
        }

        public static Defense buildDo() {
            Defense defense = new Defense();
            defense.name = "Do";
            defense.defense = 20;
            defense.luck = 10;
            defense.durability = 10;
            return defense;
        }

        public static Defense buildKote() {
            Defense defense = new Defense();
            defense.name = "Kote";
            defense.defense = 5;
            defense.luck = 20;
            defense.durability = 30;
            return defense;
        }

        public static Defense buildWoodensuneate() {
            Defense defense = new Defense();
            defense.name = "Wooden Suneate";
            defense.defense = 5;
            defense.luck = 0;
            defense.durability = 10;
            return defense;
        }

        public static Defense buildMetalsuneate() {
            Defense defense = new Defense();
            defense.name = "Metal Suneate";
            defense.defense = 15;
            defense.luck = 10;
            defense.durability = 40;
            return defense;
        }

        public static Defense buildHaidate() {
            Defense defense = new Defense();
            defense.name = "Haidate";
            defense.defense = 10;
            defense.luck = 0;
            defense.durability = 20;
            return defense;
        }

        public static Defense buildJinbaori() {
            Defense defense = new Defense();
            defense.name = "Jinbaori";
            defense.defense = 5;
            defense.luck = 20;
            defense.durability = 50;
            return defense;
        }

        public static Defense buildTabi() {
            Defense defense = new Defense();
            defense.name = "Tabi";
            defense.defense = 5;
            defense.luck = 0;
            defense.durability = 10;
            return defense;
        }

        public static Defense buildSashimono() {
            Defense defense = new Defense();
            defense.name = "Sashimono";
            defense.defense = 0;
            defense.luck = 30;
            defense.durability = 10;
            return defense;
        }
    }

    @Override
    public String toString() {
        return name + " (Defense: " + defense + ", Luck: " + luck + ", Durability: " + durability + ")";
    }

    public void decreaseDurability() {
        if (durability > 0) {
            durability--;
            if (durability == 0) {
                Console.println(name + " est brisé et ne protège plus !", Color.RED);
            }
        }
    }

    public boolean isUsable() {
        return durability > 0;
    }


}
