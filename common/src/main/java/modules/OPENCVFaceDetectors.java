package modules;

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

        ArrayList<Rect> rectToCrop = new ArrayList<Rect>();


        int lowest = 999999;

        int frameCenterX = (frame.width()/2);
        int frameCenterY = (frame.height()/2);

        Mat centerFace = null;

        for (Rect rect : faceDetections.toArray()) {
            rectToCrop.add(new Rect(rect.x, rect.y, rect.width, rect.height));
        }

        if(rectToCrop.size() > 1)
        {
            for(Rect rectCrop : rectToCrop)
            {
                int rectCenterX = (rectCrop.x + rectCrop.width/2);
                int rectCenterY = (rectCrop.y + rectCrop.height/2);

                int distance = getDistance(frameCenterX, frameCenterY, rectCenterX, rectCenterY);

                if(distance < lowest)
                {
                    lowest = distance;
                    centerFace = cropToRectangle(frame, rectCrop);

                }

            }
        }
        else
        {
            centerFace = cropToRectangle(frame, rectToCrop.get(0));
        }



        frames.add(centerFace);

    }

    private Mat cropToRectangle(Mat frame, Rect rectToCrop)
    {
        Mat newFrame = new Mat(frame, rectToCrop);
        if(!newFrame.isContinuous())
        {
            Mat temp = newFrame.clone();
            newFrame = temp;
        }
        return newFrame;
    }

    private int getDistance(int x1, int y1, int x2, int y2)
    {
        int distance = -1;

        int x = (x1 - x2);
        int y = (y1 - y2);

        x = x * x;
        y = y * y;

        distance = x + y;
        distance = (int) Math.sqrt(distance);

        return distance;
    }


}