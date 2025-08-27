public class Weapon {
    private String name;
    private int damage;
    private int durability;

    private Weapon() {
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getDurability() {
        return durability;
    }

    public static class Builder {
        public static Weapon buildKatana() {
            Weapon weapon = new Weapon();
            weapon.name = "Katana";
            weapon.damage = 45;
            weapon.durability = 100;
            return weapon;
        }

        public static Weapon buildShiruken() {
            Weapon weapon = new Weapon();
            weapon.name = "Shiruken";
            weapon.damage = 25;
            weapon.durability = 1;
            return weapon;
        }

        public static Weapon buildYumi() {
            Weapon weapon = new Weapon();
            weapon.name = "Yumi Bow";
            weapon.damage = 20;
            weapon.durability = 100;
            return weapon;
        }

        public static Weapon buildNaginata() {
            Weapon weapon = new Weapon();
            weapon.name = "Naginata";
            weapon.damage = 35;
            weapon.durability = 50;
            return weapon;
        }

        public static Weapon buildYari() {
            Weapon weapon = new Weapon();
            weapon.name = "Yari";
            weapon.damage = 20;
            weapon.durability = 75;
            return weapon;
        }

        public static Weapon buildNagamaki() {
            Weapon weapon = new Weapon();
            weapon.name = "Nagamaki";
            weapon.damage = 40;
            weapon.durability = 75;
            return weapon;
        }

        public static Weapon buildKusarigama() {
            Weapon weapon = new Weapon();
            weapon.name = "Kusarigama";
            weapon.damage = 60;
            weapon.durability = 50;
            return weapon;
        }

        public static Weapon buildFukiya() {
            Weapon weapon = new Weapon();
            weapon.name = "Fukiya Blowgun";
            weapon.damage = 15;
            weapon.durability = 100;
            return weapon;
        }

        public static Weapon buildTanegashima() {
            Weapon weapon = new Weapon();
            weapon.name = "Tanegashima";
            weapon.damage = 120;
            weapon.durability = 1;
            return weapon;
        }

        public static Weapon buildBohiya() {
            Weapon weapon = new Weapon();
            weapon.name = "Bohiya";
            weapon.damage = 70;
            weapon.durability = 50;
            return weapon;
        }
    }

    @Override
    public String toString() {
        return name + " (Damage: " + damage + ", Durability: " + durability + ")";
    }

    public void decreaseDurability() {
        if (durability > 0) {
            durability--;
            if (durability == 0) {
                Console.println(name + " est cassée et ne peut plus être utilisée !", Color.RED);
            }
        }
    }

    public boolean isUsable() {
        return durability > 0;
    }

}
