package modules;

import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

/**
 * {@author Szymon}
 */
public class OPENCVDetectAndBorderFaces {
    public Mat detectAndBorderFaces(Mat frame)
    {
        MatOfRect faceDetections = new MatOfRect();
        CascadeClassifier faceDetector = new CascadeClassifier("src/main/resources/haarcascade_frontalface_alt.xml");
        faceDetector.detectMultiScale(frame, faceDetections);

        for(Rect rect : faceDetections.toArray())
        {
            Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        return frame;
    }
}
