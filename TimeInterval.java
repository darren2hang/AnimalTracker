public class TimeInterval {
    private double startTime, endTime;
    public TimeInterval(double startTime, double endTime){
        this.startTime=startTime;
        this.endTime =endTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    public String toString(){
        return "("+getStartTime()+", "+getEndTime()+")";
    }
}
