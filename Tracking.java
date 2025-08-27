public class Tracking {
    private int navigation;
    private String name;

    public int getNavigation() {
        return navigation;
    }


    public static class Builder {
        public static Tracking buildMap() {
            Tracking tracking = new Tracking();
            tracking.navigation = 1;
            tracking.name = "Map";
            return tracking;
        }

        public static Tracking buildGPS() {
            Tracking tracking = new Tracking();
            tracking.navigation = 2;
            tracking.name = "GPS";
            return tracking;
        }

        public static Tracking buildSatcom() {
            Tracking tracking = new Tracking();
            tracking.navigation = 3;
            tracking.name = "Satcom";
            return tracking;
        }
    }

    @Override
    public String toString() {
        return name + " (Réduction combats : " + navigation + ")";
    }


}

