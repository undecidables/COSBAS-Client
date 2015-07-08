package client;

/*import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;*/

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by POLSKA on 01/07/2015.
 */
public class FaceDetection extends Thread
{
    private Mat frame;
    private List<Mat> frames;

    FaceDetection(Mat _frame, List<Mat> _frames)
    {
        this.frame = _frame;
        this.frames = _frames;
    }


    public void run()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");

        CascadeClassifier faceDetector = new CascadeClassifier("src/main/resources/haarcascade_frontalface_alt.xml");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(frame, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        ArrayList<Rect> rectToCrop = new ArrayList<Rect>();
        //Rect rect_Crop = null;
        if(faceDetections.toArray().length == 1)
        {
            for (Rect rect : faceDetections.toArray()) {
                Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(0, 255, 0));
                rectToCrop.add(new Rect(rect.x, rect.y, rect.width, rect.height));
            }

            for(Rect rectCrop : rectToCrop)
            {
                Mat newFace = new Mat(frame, rectCrop);
                frames.add(newFace);
            }
        }
        else
        {
            frame = null;
        }
    }



}
