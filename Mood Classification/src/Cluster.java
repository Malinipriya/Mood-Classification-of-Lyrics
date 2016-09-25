import java.util.ArrayList;

public class Cluster 
{
	public ArrayList<Point> points;
	public Point centroid;
	public int id;
	
	
	public Cluster(int id) 
	{
		this.id = id;
		this.points = new ArrayList<Point>();
		this.centroid = null;
	}
 
	public ArrayList<Point> getPoints() 
	{
		return points;
	}
	
	public void addPoint(Point point) 
	{
		points.add(point);
		//System.out.println("Inside addPoint:"+point);
	}
 
	public void setPoints(ArrayList<Point> points) 
	{
		this.points = points;
	}
 
	public Point getCentroid() 
	{
		return centroid;
	}
 
	public void setCentroid(Point centroid) 
	{
		this.centroid = centroid;
	}
 
	public int getId() 
	{
		return id;
	}
	
	public void clear() 
	{
		points.clear();
	}
	
	public ArrayList<ArrayList<Point>> plotCluster(ArrayList<ArrayList<Point>> point_count) 
	{
		System.out.println("[Cluster: " + id+"]");
		System.out.println("[Centroid: " + centroid + "]");
		System.out.println("[Points: \n");
		for(Point p : points) 
		{
			System.out.println(p);
		}
		point_count.add(points);
		System.out.println("]");
		return point_count;
	}
}