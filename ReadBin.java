
import java.io.*;
import java.util.*; 
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

public class ReadBin { 
    
	private float[] read (File filename) throws Exception{
        // This function create an float array, and fill it with contents from a binary file.
        FileInputStream fin = new FileInputStream(filename);
        
        DataInputStream din = new DataInputStream(fin);    // To read the first two integers
        System.out.println("Remaining Bytes: " + din.available());
        System.out.println(din.readInt());
        System.out.println(din.readInt());   

        FileChannel channel = fin.getChannel();  // To read the rest of data which are all floats
        ByteBuffer bb = ByteBuffer.allocateDirect(64*1024);
        bb.clear();
        float data[] = new float [(int)channel.size()/4];
        long len = 0;
        int offset = 0;
        while ((len = channel.read(bb))!= -1){
            bb.flip();
            bb.asFloatBuffer().get(data,offset,(int)len/4);
            offset += (int)len/4;
            bb.clear();
        }
        System.out.println("Remaining Bytes: " + din.available());
        channel.close();
        din.close();
        fin.close();
        return data;
    }

    List<Float> list = new ArrayList<Float>();
    public List<Float> dataAsList(File file) {
        try {
            float[] data = read(file);
            for (float i : data)  list.add(i);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

}