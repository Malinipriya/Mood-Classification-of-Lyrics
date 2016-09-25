public class Point 
{
    private double x = 0;
    private int cluster_number = 0;
 
    public Point(double x)
    {
        this.setX(x);
    }
    
    public void setX(double x) 
    {
        this.x = x;
    }
    
    public double getX()  
    {
        return this.x;
    }
    
    public void setCluster(int n) 
    {
        this.cluster_number = n;
    }
    
    public int getCluster() 
    {
        return this.cluster_number;
    }
    
    //Calculates the distance between two points.
    public static double distance(Point p, Point centroid) 
    {
        return Math.sqrt(Math.pow((centroid.getX() - p.getX()), 2));
    }
    
    //Creates random point
    public static Point createClusterPoint(int min, int max, double number) 
    {
    	double x = number;
    	return new Point(x);
    }
    
    public String toString() 
    {
    	return "("+x+")";
    }
}