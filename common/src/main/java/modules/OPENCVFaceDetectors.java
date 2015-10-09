package modules;/*import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;*/

import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.List;

/**
 * {@author Szymon}
 */
public class OPENCVFaceDetectors extends Thread
{
    private Mat frame;
    private List<Mat> frames;

    OPENCVFaceDetectors(Mat _frame, List<Mat> _frames)
    {
        this.frame = _frame;
        this.frames = _frames;
    }


    public void run()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        CascadeClassifier faceDetector = new CascadeClassifier("src/main/resources/haarcascade_frontalface_alt.xml");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(frame, faceDetections);

       // System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
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
                if(!newFace.isContinuous())
                {
                    Mat temp = newFace.clone();
                    newFace = temp;
                }
                frames.add(newFace);
            }
        }
        else
        {
            frame = null;
        }
    }



}
