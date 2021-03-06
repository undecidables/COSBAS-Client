package modules;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@author Szymon}
 */
public class OPENCVFaceDetection implements FaceDetection {

    public ArrayList<byte[]> detectFaces(ArrayList<byte[]> _frames)
    {
        ArrayList<OPENCVFaceDetectors> threads = new ArrayList<OPENCVFaceDetectors>();

        List<Mat> tempFrames = Collections.synchronizedList(new ArrayList<Mat>());

        for(byte[] frame : _frames)
        {
            Mat matFrame = Highgui.imdecode(new MatOfByte(frame), Highgui.IMREAD_GRAYSCALE);
            OPENCVFaceDetectors fd = new OPENCVFaceDetectors(matFrame, tempFrames);
            threads.add(fd);
            fd.run();
        }


        for(OPENCVFaceDetectors thread : threads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                System.out.println("An error occured waiting for thread to join.");
                //e.printStackTrace();
            }
        }

        ArrayList<byte[]> frames = new ArrayList<byte[]>();
        ConvertorInterface convertor = new ConvertMatToImageByteArray();

        for (Mat frame : tempFrames)
        {

            frames.add((byte[]) convertor.convert(frame));
        }

        tempFrames = null;


        return frames;

    }
}
