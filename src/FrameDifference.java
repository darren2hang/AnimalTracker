import processing.core.PApplet;

public class FrameDifference implements PixelFilter, Clickable {
    DImage previousImg;
    public int threshold;
    public FrameDifference(){
        threshold=35;
    }
    public DImage processImage(DImage img) {
        if (previousImg==null){
            previousImg = new DImage(img);
            return img;
        }
        short [][] current = img.getBWPixelGrid();
        short [][] previous = previousImg.getBWPixelGrid();
        for (int r = 0; r < current.length; r++) {
            for (int c = 0; c < current[0].length; c++) {
                int difference = (short) Math.abs(current[r][c]-previous[r][c]);
                if (difference>threshold){//moved

                }else {
                    current[r][c] = 255;
                }
            }
        }
        previousImg = new DImage(img);
        img.setPixels(current);
        return img;
    }

    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        System.out.println(threshold);
    }

    @Override
    public void keyPressed(char key) {
        if (key=='-'){
            threshold -= 5;
        }
        if (key=='+'){
            threshold +=5;
        }
    }
}
