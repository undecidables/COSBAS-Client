package modules;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class OPENCVCamera implements CameraInterface {

    public byte[] getImage()
    {
        Mat image = captureFrame();
        ImageByteArrayConverterInterface convertor = new ConvertMatToImageByteArray();


        if(image!=null)
        {
            return convertor.convertToImageByteArray(image);
        }
        return null;
    }

    public Mat captureFrame()
    {
        VideoCapture camera = new VideoCapture(0); //param passed is the device number, dont know if it will always be zero. might have to find the right device num in the client main.
        //camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1280); //camera resolution set here we could maybe have this in the config file???
        //camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 720);
        ArrayList<Mat> tempFrames = new ArrayList<Mat>();

        try //Need to put thread to sleep so that camera has some time to initialize
        {
            Thread.sleep(1000);
        }
        catch(Exception e)
        {
            //print to file??? do some error handling
        }
        if(!camera.isOpened())
        {
            //something went wrong, log to file. handle this
            System.out.println("Error");
        }
        else
        {


            Mat tempFrame = new Mat();
            Mat frame = new Mat();
            camera.read(frame);
            //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
            tempFrame = null;
            tempFrames.add(frame);
            System.out.println("Captured a photo.");


            camera.release();

            return frame;
        }
        return null;
    }
}
