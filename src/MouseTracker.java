import processing.core.PApplet;

import java.util.ArrayList;

public class MouseTracker implements PixelFilter {
    private static final int MAX_FRAMES = 4500;
    private DataSet dataset;
    private int frameCount = 0;
    private int centerR = 234;
    private int centerC = 309;//dataset.getCenterColOfEnclosure;
    private int radius = 211;
    private int mouseCenterRow;
    private int mouseCenterCol;
    private ArrayList<Point> trail = new ArrayList<>();
    private ThresholdFilter threshold = new ThresholdFilter();
    private ConvolutionBoxBlur blur = new ConvolutionBoxBlur();
    private FrameDifference d = new FrameDifference();

    public MouseTracker() {
        dataset = new DataSet(1920,1080,25,0.1872037915,radius);
        Ring r = new Ring(1080,1920);
        r.markRing(centerR,centerC,150,211);
        dataset.addRegionOfInterest(r);
    }

    @Override
    public DImage processImage(DImage img) {
        frameCount++;

        if (frameCount < MAX_FRAMES) {
            short[][] grid = img.getBWPixelGrid();
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    if (Math.sqrt((r - centerR) * (r - centerR) + (c - centerC) * (c - centerC)) > radius) {
                        grid[r][c] = 255;
                    }
                }
            }
            img.setPixels(grid);
            img = blur.processImage(img);
            img = threshold.processImage(img);

            img = blur.processImage(img);
            threshold.increaseThreshold(10);
            img = threshold.processImage(img);

            img = blur.processImage(img);
            threshold.increaseThreshold(10);
            img = threshold.processImage(img);

            img = blur.processImage(img);
            threshold.increaseThreshold(10);
            img = threshold.processImage(img);

            img = blur.processImage(img);
            threshold.increaseThreshold(10);
            img = threshold.processImage(img);
            threshold.increaseThreshold(-40);

            grid = img.getBWPixelGrid();
            int rSum = 0;
            int cSum = 0;
            int numOfBlackPoints = 1;
            for (int r = 10; r < grid.length - 10; r++) {
                for (int c = 10; c < grid[r].length - 10; c++) {
                    if (grid[r][c] == 0) {
                        rSum += r;
                        cSum += c;
                        numOfBlackPoints++;
                    }
                }
            }
            numOfBlackPoints--;
            mouseCenterRow = rSum / numOfBlackPoints;
            mouseCenterCol = cSum / numOfBlackPoints;
            Point p = new Point(mouseCenterRow,mouseCenterCol);
            trail.add(p);
            dataset.addPoint(p);
        } else if (frameCount == MAX_FRAMES){    
            displayInfo(dataset);           
            outputCSVData(dataset);         
        }

        return img;
    }

    private void displayInfo(DataSet dataset) {
        System.out.println(dataset.getDistanceTraveledDuring(0,50));

    }

    private void outputCSVData(DataSet dataset) {
        dataset.writeDataFromMouseTrackerToFile("data/ZHANG_DARREN.csv");
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.fill(86, 237, 78);
        window.stroke(0, 30, 255);
        if (trail.size() > 0)
            window.ellipse(trail.get(trail.size() - 1).getCol(), trail.get(trail.size() - 1).getRow(), 1, 1);
        for (int i = 0; i < trail.size(); i++) {
            if (i > 0) {
                window.line(trail.get(i - 1).getCol(), trail.get(i - 1).getRow(), trail.get(i).getCol(), trail.get(i).getRow());
            }
        }
        window.stroke(0);
    }
}
