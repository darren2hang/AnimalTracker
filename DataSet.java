import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataSet {
    private int resolutionWidth, resolutionHeight,radiusOfEnclosureInPixels;
    private double framesPerSec, distancePerPixel;
    private Region regionOfInterest;
    private ArrayList<Point> position = new ArrayList<>();
    private ArrayList<TimeInterval> timesInROI = new ArrayList<TimeInterval>();

    public DataSet(int resolutionWidth, int resolutionHeight, double framePerSec, double distancePerPixel, int radiusOfEnclosureInPixels) {
        this.resolutionWidth = resolutionWidth;
        this.resolutionHeight = resolutionHeight;
        this.framesPerSec = framePerSec;
        this.distancePerPixel = distancePerPixel;
        this.radiusOfEnclosureInPixels = radiusOfEnclosureInPixels;
    }

    public void addRegionOfInterest(Region r) {
        regionOfInterest = r;
    }

    public void setFrameRate(double n) {
        framesPerSec = n;
    }

    public void setResolution(int width, int height) {
        resolutionWidth = width;
        resolutionHeight = height;
        regionOfInterest = new Ring(resolutionHeight, resolutionWidth);
    }

    public void addPoint(Point p) {
        if (p != null) {
            position.add(p);
        }
    }


    public double getAverageSpeedDuring(double time1, double time2) {
        int i = Math.max(0,convertTimeIntoFrame(time1));
        int f = Math.min(position.size()-1,convertTimeIntoFrame(time2));
        int speedSum = 0;
        for (int j = i; j <= f; j++) {
            speedSum += getSpeedAtTime(convertFramesIntoTime(j));
        }
        return speedSum / (double)(f-i);
    }

    public double getTimeMovingAtSpeedInterval(int minSpeed, int maxSpeed) {
        int numFrames = 0;
        for (int i = 0; i < position.size(); i++) {
            if (getSpeedAtTime(convertFramesIntoTime(i)) < maxSpeed && getSpeedAtTime(convertFramesIntoTime(i)) > minSpeed) {
                numFrames++;
            }
        }
        return convertFramesIntoTime(numFrames);
    }

    public double getDiameter() {
        return distancePerPixel * radiusOfEnclosureInPixels * 2;
    }

    public double getFrameRate() {
        return framesPerSec;
    }

    public double getTimeSpentInROIDuring(double time1, double time2) {
        int i = Math.max(0,convertTimeIntoFrame(time1));
        int f = Math.min(position.size()-1,convertTimeIntoFrame(time2));
        int numOfFrames = 0;
        for (int j = i; j <= Math.min(f, position.size() - 1); j++) {
            if (regionOfInterest.containsPoint(position.get(j).getRow(), position.get(j).getCol())) {
                numOfFrames++;
            }
        }
        return convertFramesIntoTime(numOfFrames);
    }

    public double getTimeSpentNotInROIDuring(double time1, double time2) {
        return time2 - time1 - getTimeSpentInROIDuring(time1, time2);
    }

    public double getLongestContinuousDurationInROI(double time1, double time2) {
        ArrayList<TimeInterval> intervals = getIntervalsInROI(time1, time2);
        double maxLength = 0;
        for (int i = 0; i < intervals.size(); i++) {
            if (intervals.get(i).getEndTime() - intervals.get(i).getStartTime() > maxLength) {
                maxLength = intervals.get(i).getEndTime() - intervals.get(i).getStartTime();
            }
        }
        return maxLength;
    }

    public double getAverageSpeedInROI(double time1, double time2) {
        ArrayList<TimeInterval> intervals = getIntervalsInROI(time1, time2);
        double speedSum = 0;
        double time = 0;
        for (int i = 0; i < intervals.size(); i++) {
            speedSum += getAverageSpeedDuring(intervals.get(i).getStartTime(), intervals.get(i).getEndTime());
            time += intervals.get(i).getEndTime() - intervals.get(i).getStartTime();
        }
        return speedSum / (double)intervals.size();
    }

    public double getAverageSpeedInROI(TimeInterval t) {
        int i = Math.max(0,convertTimeIntoFrame(t.getStartTime()));
        int f = Math.min(position.size()-1,convertTimeIntoFrame(t.getEndTime()));
        double speedSum = 0;
        int frames=0;
        for (int j = i; j <= Math.min(f, position.size() - 1); j++) {
            if (regionOfInterest.containsPoint(position.get(i))) {
                speedSum += getSpeedAtTime(convertFramesIntoTime(j));
                frames++;
            }
        }
        return speedSum / (double)frames;
    }

    public ArrayList<TimeInterval> getIntervalsInROI() {
    return getIntervalsInROI(0,convertFramesIntoTime(position.size()));
    }

    public ArrayList<TimeInterval> getIntervalsInROI(double time1, double time2) {

        int i = Math.max(0,convertTimeIntoFrame(time1));
        int f = Math.min(position.size()-1,convertTimeIntoFrame(time2));
          int startFrame = 0;
          int endFrame = 0;
          for (int j = i + 1; j <= Math.min(f, position.size() - 1); j++) {
              if (regionOfInterest.containsPoint(position.get(j))) {
                  if (regionOfInterest.containsPoint(position.get(j - 1))) {
                  } else {
                      startFrame = j;
                  }
              } else {
                  if (regionOfInterest.containsPoint(position.get(j - 1))) {
                      endFrame = j;
                      timesInROI.add(new TimeInterval(convertFramesIntoTime(startFrame), convertFramesIntoTime(endFrame)));
                      startFrame = j + 1;
                  } else {

                  }
              }
          }
        return timesInROI;
    }

    public Point getPositionAtTime(double time) {
        return position.get(convertTimeIntoFrame(time));
    }

    public double getSpeedAtTime(double time) {
        int i = convertTimeIntoFrame(time);
        if (i >= position.size()) return 0;
        if (i - 1 >= 0) {
            if (i + 1 < position.size()) {
                //go from i-1 to i+1
                return (getDistanceBetween(position.get(i - 1).getRow(), position.get(i - 1).getCol(), position.get(i + 1).getRow(), position.get(i + 1).getCol()) / (convertFramesIntoTime(2)));
            } else {
                //go from i-1 to i
                return (getDistanceBetween(position.get(i - 1).getRow(), position.get(i - 1).getCol(), position.get(i).getRow(), position.get(i).getCol()) / (convertFramesIntoTime(1)));
            }
        } else {
            if (i + 1 < position.size()) {
                //go from i to i+1
                return (getDistanceBetween(position.get(i).getRow(), position.get(i).getCol(), position.get(i + 1).getRow(), position.get(i + 1).getCol()) / (convertFramesIntoTime(1)));
            } else {
                return 0;
            }
        }
    }

    private double getDistanceBetween(int r1, int c1, int r2, int c2) {
        return distancePerPixel * Math.sqrt((r1 - r2) * (r1 - r2) + (c1 - c2) * (c1 - c2));
    }

    public double getMaxSpeedDuring(double time1, double time2) {
        int i = Math.max(0,convertTimeIntoFrame(time1));
        int f = Math.min(position.size()-1,convertTimeIntoFrame(time2));
        double maxSpeed = getSpeedAtTime(time1);
        for (int j = i + 1; j <= Math.min(f, position.size() - 1); j++) {
            if (getSpeedAtTime(convertFramesIntoTime(j)) > maxSpeed) maxSpeed = getSpeedAtTime(convertFramesIntoTime(j));
        }
        return maxSpeed;
    }
//    public void displaySpeeds(){
//        for (int i = 0; i < 4500; i++) {
//            System.out.println(getSpeedAtTime(convertFramesIntoTime(i)));
//        }
//    }
    public double getDistanceTraveledDuring(double time1, double time2) {
        int i = Math.max(0,convertTimeIntoFrame(time1));
        int f = Math.min(position.size()-1,convertTimeIntoFrame(time2));
        double distanceSum = 0;
        for (int j = i; j < Math.min(f, position.size() - 1); j++) {
            Point p1 = position.get(j);
            Point p2 = position.get(j + 1);
            distanceSum += getDistanceBetween(p1.getRow(), p1.getCol(), p2.getRow(), p2.getCol());
        }
        return distanceSum;
    }

    private double convertPixelsIntoDistance(int numOfPixel) {
        return numOfPixel * distancePerPixel;
    }

    private int convertTimeIntoFrame(double time) {
        return (int) (time * getFrameRate());
    }

    private double convertFramesIntoTime(int frame) {
        return frame / framesPerSec;
    }

    public void writeDataFromMouseTrackerToFile(String filePath) {
        try (
                PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));) {
            //p.println("Time(s)\tPosition(x, y)\tVelocity(cm/s)");
            //p.println();
            // DecimalFormat df = new DecimalFormat("###.##");
            for (int i = 0; i < position.size(); i++) {
                Point point = position.get(i);
                //p.print(convertFramesIntoTime(i)+"\t");
                p.println(point.getCol() + "," + point.getRow());
                // p.println(getSpeedAtTime(convertFramesIntoTime(i)));
            }
        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filePath);
            error.printStackTrace();
        }
    }

    public void readFileColRow(String filepath) {
        try (Scanner scanner = new Scanner(new File(filepath))) {
            while (scanner.hasNext()) {
                String line = scanner.next();
                String arr[] = line.split(",");
                Point p = new Point(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                addPoint(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void readFileRowCol(String filepath) {
        try (Scanner scanner = new Scanner(new File(filepath))) {
            while (scanner.hasNext()) {
                String line = scanner.next();
                String arr[] = line.split(",");
                Point p = new Point(Integer.parseInt(arr[1]), Integer.parseInt(arr[0]));
                addPoint(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
