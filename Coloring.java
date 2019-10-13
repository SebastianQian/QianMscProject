import java.io.*;
import java.util.*; 


public class Coloring {

    List<Double> radiusX = new ArrayList<Double>();
    List<Double> radiusY = new ArrayList<Double>();
    List<Double> radiusZ = new ArrayList<Double>();
    List<Double> centerX = new ArrayList<Double>(); 
    List<Double> centerY = new ArrayList<Double>(); 
    List<String> clus = new ArrayList<String>();   
    private double xRadius =0, yRadius=0, zRadius=0, xCenter = 0, yCenter = 0;


    private void getProtDiameter(List<Float> protX, List<Float> protY, List<Float> protZ) {
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        for(int i = 0; i < protX.size(); i+=51) {
            float x = protX.get(i);
            float y = protY.get(i);
            float z = protZ.get(i);
            if (maxX < x) {
                maxX = x;
            }
            if (maxY < y) {
                maxY = y;
            }
            if (maxZ < z) {
                maxZ = z;
            }
            if (minX > x) {
                minX = x;
            }
            if (minY > y) {
                minY = y;
            }
            if (minZ > z) {
                minZ = z;
            }
            xCenter += x;
            yCenter += y;
            if (i!=0 && i%(51*343)==0) {
                xRadius = (maxX - minX)/2;
                yRadius = (maxY - minY)/2;
                zRadius = (maxZ - minZ)/2;
                xCenter = xCenter/344;
                yCenter = yCenter/344;
                radiusX.add(xRadius);
                radiusY.add(yRadius);
                radiusZ.add(zRadius);
                centerX.add(xCenter);
                centerY.add(yCenter);
                maxX = Double.MIN_VALUE;
                maxY = Double.MIN_VALUE;
                maxZ = Double.MIN_VALUE;
                minX = Double.MAX_VALUE;
                minY = Double.MAX_VALUE;
                minZ = Double.MAX_VALUE;
                xCenter = 0;
                yCenter = 0;
            }
        }
    }

    private void protCluster(List<Float> protX, List<Float> protY, List<Float> protZ) {
        getProtDiameter(protX,protY,protZ);
        for(int i=0; i+1<centerX.size(); i++) {
            double xR1 = radiusX.get(i);
            double xR2 = radiusX.get(i+1);
            double yR1 = radiusY.get(i);
            double yR2 = radiusY.get(i+1);
            double xCenter1 = centerX.get(i);
            double xCenter2 = centerX.get(i+1);
            double yCenter1 = centerY.get(i);
            double yCenter2 = centerY.get(i+1);            
            if (Math.abs(xCenter2-xCenter1) < (xR1+xR2) || Math.abs(yCenter2-yCenter1) < (yR1+yR2) ) {
                clus.add(""+i+""+(i+1));
            }
        }
    }

    public void getProtCluster(List<Float> protX, List<Float> protY, List<Float> protZ) {
        protCluster(protX,protY,protZ);
        for(String i: clus)  System.out.println(i);
        
    }
}
