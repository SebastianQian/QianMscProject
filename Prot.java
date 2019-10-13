import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Ellipse2D;

public class Prot {
    private List<Double> vectorProt = new ArrayList<Double>(); 
    private double X =0, Y=0, W = 0, H = 0;
    private int index = 0;
    private List<Particle> Atoms;
    private ProtCluster cluster = null;
    private Ellipse2D.Double circle;
    
    public Prot(List<Float> protX, List<Float> protY) {
        Atoms = new ArrayList<Particle>();
        setBoundaries(protX, protY);
    }

    private void setBoundaries(List<Float> protX, List<Float> protY) {
        double xMax = Double.MIN_VALUE;
        double yMax = Double.MIN_VALUE;
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        double xRadius =0, yRadius=0, xCenter = 0, yCenter = 0;
        for(int i = 0; i < protX.size(); i+=51) {
            float x = protX.get(i);
            float y = protY.get(i);
            if (xMax < x)  xMax = x;
            if (yMax < y)  yMax = y;
            if (xMin > x)  xMin = x;
            if (yMin > y)  yMin = y;
            xCenter += x;
            yCenter += y;
            if (i!=0 && i%(51*343)==0) {
                xRadius = xMax - xMin;
                yRadius = yMax - yMin;
                xCenter = xCenter/344;
                yCenter = yCenter/344;
                vectorProt.add(xRadius);
                vectorProt.add(yRadius);
                vectorProt.add(xCenter);
                vectorProt.add(yCenter);

                xMax = Double.MIN_VALUE;
                yMax = Double.MIN_VALUE;
                xMin = Double.MAX_VALUE;
                yMin = Double.MAX_VALUE;
                xCenter = 0;
                yCenter = 0;
            }
        }      
    }

    public List<Double> getBoundaries() {
        return vectorProt;
    }
    
    public ProtCluster getCluster() {
        return cluster;
    }
      
    public void setCluster(ProtCluster cluster) {
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

    public double getW() {
        return W;
    }
    public void setW(double w) {
        this.W = w;
    }

    public double getH() {
        return H;
    }
    public void setH(double h) {
        this.H = h;
    }

    public void setShape(double x, double y, double w, double h) {
        circle = new Ellipse2D.Double(x, y, w, h);
    }

    public Ellipse2D.Double getShape() {
        return circle;
    }

    public List<Particle> getAtoms() {
        return Atoms;
    }

    public void insertAtoms(Particle atom) {
        Atoms.add(atom);
    }
}
