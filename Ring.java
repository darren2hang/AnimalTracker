public class Ring implements Region {
    int[][] grid;
    private static final int INTEREST = 1;
    private static final int NON_INTEREST = 0;

    public Ring(int row, int col) {
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
    public void markRing(int centerRow, int centerCol, int innerRadius, int outerRadius) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                double distFromCenter = Math.sqrt((r - centerRow) * (r - centerRow) + (c - centerCol) * (c - centerCol));
                if (distFromCenter < outerRadius && distFromCenter > innerRadius) {
                    markPixelInRegion(r,c);
                }
            }
        }
    }
}
