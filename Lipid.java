import java.util.ArrayList;
import java.util.List;

public class Lipid {
    private List<Double> vectorLipid = new ArrayList<Double>(); 
    private double X =0, Y=0;
    private int index = 0;
    private List<Particle> Atoms;
    private LipidCluster cluster = null;
    
    public Lipid(List<Float> lipidX, List<Float> lipidY) {
        Atoms = new ArrayList<Particle>();
        setCenters(lipidX, lipidY);
    }

    private void setCenters(List<Float> lipidX, List<Float> lipidY) {
        double xCenter = 0, yCenter = 0;
        for(int i = 0; i < lipidX.size(); i+=51) {
            float x = lipidX.get(i);
            float y = lipidY.get(i);
            xCenter += x;
            yCenter += y;
            if (i!=0 && i%(51*13)==0) {
                xCenter = xCenter/13;
                yCenter = yCenter/13;
                vectorLipid.add(xCenter);
                vectorLipid.add(yCenter);
                xCenter = 0;
                yCenter = 0;
            }
        }      
    }

    public List<Double> getBoundaries() {
        return vectorLipid;
    }
    
    public LipidCluster getCluster() {
        return cluster;
    }
      
    public void setCluster(LipidCluster cluster) {
        this.cluster=cluster;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int i) {
        this.index = i;
    }
    public double getX() {
        return X;
    }
    public void setX(double x) {
        this.X = x;
    }

    public double getY() {
        return Y;
    }
    public void setY(double y) {
        this.Y = y;
    }

    public List<Particle> getAtoms() {
        return Atoms;
    }

    public void insertAtoms(Particle atom) {
        Atoms.add(atom);
    }
}
