import java.io.*;
import java.util.*; 
import java.nio.channels.FileChannel;

public class ProtFetcher {
    private float scale = 4;                // the scale for you to zoom in or out
    private List<Particle> protAtomList = new ArrayList<Particle>();  
    private List<Prot> protList = new ArrayList<Prot>();

    private void makeProts(int frame, List<Float> protX, List<Float> protY) {
        for(int i = frame-1; i < protX.size(); i+=51) {
            double x = scale * protX.get(i);
            double y = scale * protY.get(i);
            Particle atom = new Particle();
            atom.setXPos(x);
            atom.setYPos(y);
            protAtomList.add(atom);
        }
        Prot m = new Prot(protX,protY);
        int count=0;
        for (Particle p : protAtomList) {
            if(count%344 == 0){
                m = new Prot(protX,protY);
                count=0;
                protList.add(m);
            } 
            m.insertAtoms(p);    
            count++;      
        }
    }

    public List<ProtCluster> findProtClusters(int frame, List<Float> protX, List<Float> protY) {
        makeProts(frame,protX,protY);
        List<ProtCluster> clusterList = new ArrayList<ProtCluster>();
        boolean isMultipleMolecules = false;
        for (Prot p1 : protList) { 
            isMultipleMolecules = false;
            double x1 = p1.getX();
            double y1 = p1.getY(); 
            double w1 = p1.getW();
            double h1 = p1.getH();
            p1.setShape(x1,y1,w1,h1);
            for (Prot p2 : protList) {
                double x2 = p2.getX();
                double y2 = p2.getY();
                double w2 = p2.getW();
                double h2 = p2.getH();
                p2.setShape(x2,y2,w2,h2);
                if (!p1.equals(p2) && p1.getShape().intersects(p2.getShape().getBounds2D())) {    
                if (p1.getCluster()== null && p2.getCluster() == null) {                 
                    ProtCluster cluster = new ProtCluster();
                    p1.setCluster(cluster);
                    p2.setCluster(cluster);
                    cluster.add(p1);
                    cluster.add(p2);
                    clusterList.add(cluster);
                } else if(!(p1.getCluster() == null) && p2.getCluster() == null) {
                    p2.setCluster(p1.getCluster());
                    p1.getCluster().add(p2);
                } else if(p1.getCluster()== null && !(p2.getCluster() == null)) {
                    p1.setCluster(p2.getCluster());
                    p2.getCluster().add(p1);
                }
                    isMultipleMolecules = true;
                }
            }   
            if(!isMultipleMolecules) {
                ProtCluster cluster = new ProtCluster();
                cluster.add(p1);
                p1.setCluster(cluster);
                clusterList.add(cluster);
            }
        }
        return clusterList; 
    }  
}