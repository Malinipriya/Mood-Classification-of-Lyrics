import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class KMeans 
{
	private int NUM_CLUSTERS=4;
	private static final int MIN_COORDINATE = -1;
    private static final int MAX_COORDINATE = 1;
    private ArrayList<Point> points;
    private ArrayList<Cluster> clusters;
    private ArrayList<String> words;
    private ArrayList<ArrayList<Point>> point_count;
    private ArrayList<Double> sentiment_scores;
    public KMeans()
    {
    	this.points=new ArrayList<Point>();
    	this.clusters=new ArrayList<Cluster>();
    	this.words=new ArrayList<String>();
    	this.sentiment_scores=new ArrayList<Double>();
    	this.point_count=new ArrayList<ArrayList<Point>>();
    }
    public void init()
    {
    	String fileName = "sentiwordnet_results/angry6.txt";
    	String line = null;
    	double score=0.0;
    	try 
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) 
            {
                String[] content = line.split("\\s+");
                if(content.length>1)
                {
                	score=Double.parseDouble(content[1]);
                	words.add(content[0]);
                	sentiment_scores.add(Double.parseDouble(content[1]));
                	Point point=Point.createClusterPoint(MIN_COORDINATE,MAX_COORDINATE,score);
                	points.add(point);
                }
            }
            bufferedReader.close();
        }
    	catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");  
            System.out.println(ex);
        }
        catch(IOException ex) 
        {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
    	int i;
    	double angry_score=-0.1931818181818182;
    	double love_score=0.6100000000000001;
    	double happy_score=0.6950000000000002;
    	double sad_score=-0.5909090909090909;
    	for(i=0;i<NUM_CLUSTERS;i++)
    	{
    		Cluster cluster=new Cluster(i);
    		Point centroid;
    		if(i==0)
    			centroid=Point.createClusterPoint(MIN_COORDINATE,MAX_COORDINATE,angry_score);
    		else if(i==1)
    			centroid=Point.createClusterPoint(MIN_COORDINATE,MAX_COORDINATE,happy_score);
    		else if(i==2)
    			centroid=Point.createClusterPoint(MIN_COORDINATE,MAX_COORDINATE,love_score);
    		else
    			centroid=Point.createClusterPoint(MIN_COORDINATE,MAX_COORDINATE,sad_score);
    		cluster.setCentroid(centroid);
    		clusters.add(cluster);
    	}
    	plotClusters();
    }
    public void plotClusters() 
    {
    	int i;
    	point_count.clear();
    	for(i=0;i<NUM_CLUSTERS;i++)
    	{
    		Cluster c=clusters.get(i);
    		point_count=c.plotCluster(point_count);
    	}
    }
    public void calculate() throws IOException
    {
    	boolean finish=false;
    	int iteration=0,max1=0,index1=0,i,j,max2=0,index2=0;
    	FileWriter fw = new FileWriter("clustering_results/angry6.txt");
    	while(!finish)
    	{
    		clearClusters();
    		ArrayList<Point> lastCentroids=getCentroids();
    		assignCluster();
    		calculateCentroids();
    		iteration++;
    		ArrayList<Point> currentCentroids = getCentroids();
    		double distance=0;
    		for(i=0; i<lastCentroids.size();i++) 
    		{
        		distance += Point.distance(lastCentroids.get(i),currentCentroids.get(i));
        	}
        	System.out.println("#################");
        	System.out.println("Iteration: " + iteration);
        	System.out.println("Centroid distances: " + distance);
        	plotClusters();
        	if(distance == 0) 
        	{
        		finish = true;
        		//Get the cluster having the highest number of points.
        		max1=point_count.get(0).size();
        		for(i=1;i<point_count.size();i++)
            	{
            		if(point_count.get(i).size()>max1)
            		{
            			max1=point_count.get(i).size();
            			index1=i;
            		}
            	}
        		System.out.println("Cluster "+index1+" has the highest number of points: "+max1);
            	ArrayList<Point> max_cluster1=point_count.get(index1);
            	ArrayList<Point> empty=new ArrayList<Point>();
        		point_count.set(index1,empty);
        		//Get the cluster having the second highest number of points.
        		max2=point_count.get(0).size();
        		for(i=1;i<point_count.size();i++)
            	{
            		if(point_count.get(i).size()>max2)
            		{
            			max2=point_count.get(i).size();
            			index2=i;
            		}
            	}
        		System.out.println("Cluster "+index2+" has the second highest number of points: "+max2);
            	ArrayList<Point> max_cluster2=point_count.get(index2);
            	//Get the words that belong to cluster1
            	for(i=0;i<max_cluster1.size();i++)
            	{
            		String point=max_cluster1.get(i).toString();
            		point=point.substring(1,point.length()-1);
            		if(sentiment_scores.contains(Double.parseDouble(point)))
            		{
            			j=sentiment_scores.indexOf(Double.parseDouble(point));
            			String word=words.get(j);
            			String[] parts = word.split("#");
            			fw.write(parts[0]+" "+parts[1]);
            			fw.write("\n");
            		}
            	}
            	fw.write("\n");
            	//Get the words that belong to cluster2
            	for(i=0;i<max_cluster2.size();i++)
            	{
            		String point=max_cluster2.get(i).toString();
            		point=point.substring(1,point.length()-1);
            		if(sentiment_scores.contains(Double.parseDouble(point)))
            		{
            			j=sentiment_scores.indexOf(Double.parseDouble(point));
            			String word=words.get(j);
            			String[] parts = word.split("#");
            			fw.write(parts[0]+" "+parts[1]);
            			fw.write("\n");
            		}
            	}
        	}
        }
    	fw.write("\n");
    	fw.close();
    }
    public void clearClusters()
    {
    	for(Cluster cluster:clusters)
    	{
    		cluster.clear();
    	}
    }
    public void assignCluster()
    {
    	double max = Double.MAX_VALUE;
        double min = max; 
        int cluster = 0;                 
        double distance = 0.0; 
        for(Point point : points) 
        {
        	min=max;
        	for(int i=0;i<NUM_CLUSTERS;i++) 
        	{
        		Cluster c = clusters.get(i);
        		distance = Point.distance(point, c.getCentroid());
        		if(distance<min)
        		{
                    min = distance;
                    cluster = i;
                }
        	}
        	point.setCluster(cluster);
        	clusters.get(cluster).addPoint(point);
        }
    }
    public ArrayList<Point> getCentroids() 
    {
    	ArrayList<Point> centroids = new ArrayList<Point>(NUM_CLUSTERS);
    	for(Cluster cluster : clusters) 
    	{
    		Point aux = cluster.getCentroid();
    		Point point = new Point(aux.getX());
    		centroids.add(point);
    	}
    	return centroids;
    }
    public void calculateCentroids() 
    {
        for(Cluster cluster : clusters) 
        {
            double sumX = 0;
            ArrayList<Point> list = cluster.getPoints();
            int n_points = list.size();
            for(Point point : list) 
            {
            	sumX += point.getX();
            }
            Point centroid = cluster.getCentroid();
            if(n_points>0) 
            {
            	double newX = sumX / n_points;
            	centroid.setX(newX);
            }
        }
    }
    public static void main(String[] args) throws IOException
    {
    	KMeans kmeans=new KMeans();
    	kmeans.init();
    	kmeans.calculate();
    }
}
