import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Inventory {
    private static final Random RANDOM = new Random();

    private static final List<Supplier<Weapon>> WEAPONS = List.of(
            Weapon.Builder::buildKatana,
            Weapon.Builder::buildShiruken,
            Weapon.Builder::buildYumi,
            Weapon.Builder::buildNaginata,
            Weapon.Builder::buildYari,
            Weapon.Builder::buildNagamaki,
            Weapon.Builder::buildKusarigama,
            Weapon.Builder::buildFukiya,
            Weapon.Builder::buildTanegashima,
            Weapon.Builder::buildBohiya);

    private static final List<Supplier<Defense>> DEFENSES = List.of(
            Defense.Builder::buildKabutohelmet,
            Defense.Builder::buildMenpo,
            Defense.Builder::buildDo,
            Defense.Builder::buildKote,
            Defense.Builder::buildWoodensuneate,
            Defense.Builder::buildMetalsuneate,
            Defense.Builder::buildHaidate,
            Defense.Builder::buildJinbaori,
            Defense.Builder::buildTabi,
            Defense.Builder::buildSashimono);

    private static final List<Supplier<Tool>> TOOLS = List.of(
            Tool.Builder::buildBandage,
            Tool.Builder::buildMedkit,
            Tool.Builder::buildPainkillers,
            Tool.Builder::buildStim,
            Tool.Builder::buildRabbit,
            Tool.Builder::buildHerbs);

    private static final List<Supplier<Tracking>> TRACKINGS = List.of(
            Tracking.Builder::buildMap,
            Tracking.Builder::buildGPS,
            Tracking.Builder::buildSatcom);

    public static Weapon getRandomWeapon() {
        return WEAPONS.get(RANDOM.nextInt(WEAPONS.size())).get();
    }

    public static Defense getRandomDefense() {
        return DEFENSES.get(RANDOM.nextInt(DEFENSES.size())).get();
    }

    public static Tool getRandomTool() {
        return TOOLS.get(RANDOM.nextInt(TOOLS.size())).get();
    }

    public static Tracking getRandomTracking() {
        return TRACKINGS.get(RANDOM.nextInt(TRACKINGS.size())).get();
    }
}