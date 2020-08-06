public class Rectangle implements Region {
    int[][] grid;
    private static final int INTEREST = 1;
    private static final int NON_INTEREST = 0;

    public Rectangle(int row, int col) {
        grid = new int[row][col];
    }

    public void markPixelInRegion(int row, int col) {
        grid[row][col] = INTEREST;
    }

    public void unmarkPixelInRegion(int row, int col) {
        grid[row][col] = NON_INTEREST;
    }
    public boolean containsPoint(int row, int col){
        return grid[row][col]==INTEREST;
    }
    public boolean containsPoint(Point p){
        return grid[p.getRow()][p.getCol()]==INTEREST;
    }
    public void markRect(int x, int y, int width, int height) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
               if (c>x && c<x+width && r>y &&r<y+height){
                   markPixelInRegion(r,c);
               }
            }
        }
    }
}
