package modules;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class OPENCVCamera implements CameraInterface {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private VideoCapture camera;

    public OPENCVCamera()
    {
        //camera = new VideoCapture(0);
        camera = new VideoCapture(0);


    }

    public boolean isOpened()
    {
        return camera.isOpened();
    }


    public void startCamera()
    {
        if(!camera.isOpened())
        {
            camera.open(0);
        }

    }

    public void releaseCamera()
    {
        if (camera.isOpened())
        {
            camera.release();
        }

    }
    public byte[] getImage()
    {
        Mat image = captureFrame();
        ConvertorInterface convertor = new ConvertMatToImageByteArray();


        if(image!=null)
        {
            return (byte[]) convertor.convert(image);
        }
        return null;
    }

    public Mat captureFrame()
    {
        ArrayList<Mat> tempFrames = new ArrayList<Mat>();

        if(!camera.isOpened())
        {
            //let try open it one more time
            camera.open(0);
        }


        try //Need to put thread to sleep so that camera has some time to initialize
        {
            Thread.sleep(100);
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



            return frame;
        }
        return null;
    }
}
