package modules;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public interface FaceDetection {
    public ArrayList<byte[]> detectFaces(ArrayList<byte[]> _frames);
}
