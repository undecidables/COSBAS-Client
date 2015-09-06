package client;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by POLSKA on 30/06/2015.
 */
public class Face implements Biometric
{

    /**
     *
     * @param _data takes in an arraylist of BiometricData and will add face data to it
     */

    public void fillData(ArrayList<BiometricData> datas)
    {

        Camera c = new Camera();
        ArrayList<Mat> frames = c.captureFrames();
        frames = detectFaces(frames);
        ArrayList<byte[]> images = prepare(frames);


        if(images != null)
        {
            for(byte[] image : images)
            {
                BiometricData data = new BiometricData("face", image);
                datas.add(data);

            }
        }
    }

    /**
     *
     */
    private ArrayList<byte[]> prepare(ArrayList<Mat> frames)
    {
        ArrayList<byte[]> datas = new ArrayList<byte[]>();
        if(frames.size() > 0)
        {

            ImageByteArrayConverterInterface convertor = new ConvertMatToImageByteArray();


            for (Mat frame : frames)
            {
                byte[] convertedImage = convertor.convertToImageByteArray(frame);
                datas.add(convertedImage);
            }

            return datas;
        }
        return null;
    }


    /**
     * This method will capture data from IO device and store somewhere on the client.
     * This will be done by running a script of some sorts or use library.
     */
    private ArrayList<Mat> detectFaces(ArrayList<Mat> _frames)
    {
        ArrayList<FaceDetection> threads = new ArrayList<FaceDetection>();

        List<Mat> tempFrames = Collections.synchronizedList(new ArrayList<Mat>());

        for(Mat frame : _frames)
        {
            FaceDetection fd = new FaceDetection(frame, tempFrames);
            threads.add(fd);
            System.out.println("Running a thread");
            fd.run();
        }


        for(FaceDetection thread : threads)
        {
            try
            {
                System.out.println("Joining thread");
                thread.join();
            }
            catch (InterruptedException e)
            {
                System.out.println("An error occured waiting for thread to join.");
                //e.printStackTrace();
            }
        }

        ArrayList<Mat> frames = new ArrayList<Mat>();

        for (Mat frame : tempFrames)
        {
            frames.add(frame);
        }

        tempFrames = null;

        return frames;

    }

}
