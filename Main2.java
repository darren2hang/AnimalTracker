public class Main2 {
    public static void main(String[] args) {
        DataSet dataset = new DataSet(1920, 1080, 25, 0.1872037915,  211);
        DataSet dober = new DataSet(1920, 1080, 25, 0.1872037915,  211);
        Circle r = new Circle(1080, 1920);
        r.markCircle(233, 308, 100);
        dataset.addRegionOfInterest(r);
        dober.addRegionOfInterest(r);
        dataset.readFileColRow("data/ZHANG_DARREN.csv");
        System.out.println("Darren's data");
        System.out.println("distance traveled from 0 to 180: " + dataset.getDistanceTraveledDuring(0, 180));
     System.out.println("distance traveled from 50 to 65: " + dataset.getDistanceTraveledDuring(50, 65));
     System.out.println("average speed from 30 to 40: " + dataset.getAverageSpeedDuring(30, 40));
     System.out.println("maxspeed: "+dataset.getMaxSpeedDuring(0,180));
        System.out.println("time spent in roi: " + dataset.getTimeSpentInROIDuring(0, 180));
     System.out.println(dataset.getIntervalsInROI(0,180));
        dober.readFileRowCol("data/DOBERVICH_DAVID.csv");
        System.out.println("Mr. Dobervich's data");
        System.out.println("distance traveled from 0 to 180: " + dober.getDistanceTraveledDuring(0, 180));
     System.out.println("distance traveled from 50 to 65: " + dober.getDistanceTraveledDuring(50, 65));
     System.out.println("average speed from 30 to 40: " + dober.getAverageSpeedDuring(30, 40));
     System.out.println("maxspeed: "+dober.getMaxSpeedDuring(0,180));
     System.out.println("time spent in roi: " + dober.getTimeSpentInROIDuring(0, 180));
     System.out.println(dober.getIntervalsInROI(0,180));
    }
}
