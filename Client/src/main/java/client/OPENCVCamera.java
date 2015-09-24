package client;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class OPENCVCamera implements CameraInterface {

    public ArrayList<byte[]> getImages()
    {
        ArrayList<Mat> matImages = captureFrames();
        ArrayList<byte[]> images = new ArrayList<byte[]>();

        ImageByteArrayConverterInterface convertor = new ConvertMatToImageByteArray();
        for(Mat image : matImages)
        {
            images.add(convertor.convertToImageByteArray(image));
        }

        return images;
    }

    public ArrayList<Mat> captureFrames()
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
            //need to take a few images incase one of them are blurry
            int count = 0;
            ArrayList<OPENCVFaceDetectors> threads = new ArrayList<OPENCVFaceDetectors>();
            while (count < 4) {
                try {
                    Thread.sleep(250);
                } catch (Exception e) {

                }
                Mat tempFrame = new Mat();
                Mat frame = new Mat();
                camera.read(tempFrame);
                Imgproc.cvtColor(tempFrame, frame, Imgproc.COLOR_RGB2GRAY);
                tempFrame = null;
                tempFrames.add(frame);
                System.out.println("Captured a photo.");
                count++;
            }


            camera.release();

            return tempFrames;
        }
        return null;
    }
}
