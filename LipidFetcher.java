import java.io.*;
import java.util.*; 
import java.nio.channels.FileChannel;


public class LipidFetcher {
    private float scale = 4;                // the scale for you to zoom in or out
    private List<Lipid> lipidList = new ArrayList<Lipid>();
    private List<Particle> lipidAtomList = new ArrayList<Particle>();
    
    private void makeLipids(int frame, List<Float> lipidX, List<Float> lipidY) {
        for(int i = frame-1; i < lipidX.size(); i+=51) {
            double x = scale * lipidX.get(i);
            double y = scale * lipidY.get(i);
            Particle atom = new Particle();
            atom.setXPos(x);
            atom.setYPos(y);
            lipidAtomList.add(atom);
        }
        Lipid l = new Lipid(lipidX,lipidY);
        int count=0;
        for (Particle p : lipidAtomList) {
            if(count%13 == 0){
                l = new Lipid(lipidX,lipidY);
                count=0;
                lipidList.add(l);
            } 
            l.insertAtoms(p);    
            count++;      
        }
    }
    
    public double findMinDist(int frame,List<Float> lipidX, List<Float> lipidY){
        double min = Double.MAX_VALUE;
        double dis = 0;
        makeLipids(frame,lipidX,lipidY);
        for(Lipid l1 : lipidList) {
            double x1 = l1.getX();
            double y1 = l1.getY();
            for(Lipid l2 : lipidList) {
                double x2 = l2.getX();
                double y2 = l2.getY();
                dis = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
                if (dis < min ) {
                    min = dis;
                }
            }
        }
        return min;
    }

    public List<LipidCluster> findLipidClusters(int frame, List<Float> lipidX, List<Float> lipidY) {
        double min = findMinDist(frame, lipidX, lipidY);
        List<LipidCluster> clusterList = new ArrayList<LipidCluster>();
        boolean isMultipleMolecules = false;
        for (Lipid l1 : lipidList) { 
            isMultipleMolecules = false;
            double x1 = l1.getX();
            double y1 = l1.getY();
            for(Lipid l2: lipidList) {
                double x2 = l2.getX();
                double y2 = l2.getY();
                if (Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2)) < 3*min) {
                    if (l1.getCluster()== null && l2.getCluster() == null) {                 
                        LipidCluster cluster = new LipidCluster();
                        l1.setCluster(cluster);
                        l2.setCluster(cluster);
                        cluster.add(l1);
                        cluster.add(l2);
                        clusterList.add(cluster);
                    } else if(!(l1.getCluster() == null) && l2.getCluster() == null) {
                        l2.setCluster(l1.getCluster());
                        l1.getCluster().add(l2);
                    } else if(l1.getCluster()== null && !(l2.getCluster() == null)) {
                        l1.setCluster(l2.getCluster());
                        l2.getCluster().add(l1);
                    }
                    isMultipleMolecules = true;
                }              
            }
            if(!isMultipleMolecules) {
                LipidCluster cluster = new LipidCluster();
                cluster.add(l1);
                l1.setCluster(cluster);
                clusterList.add(cluster);
            }
        }
        return clusterList; 
    }
}