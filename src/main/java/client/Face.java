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
    @Override
    public void fillData(ArrayList<BiometricData> _data)
    {
        ArrayList<BiometricData> datas = prepare();
        if(datas != null)
        {
            for (BiometricData data : datas)
            {
                _data.add(data);
            }
        }

    }

    /**
     *
     */
    public ArrayList<BiometricData> prepare()
    {
        List<Mat> frames = Collections.synchronizedList(new ArrayList<Mat>());
        ArrayList<BiometricData> datas = new ArrayList<BiometricData>();
        capture(frames);
        if(frames.size() > 0)
        {
            int name = (int) (Math.random() * 100);
            File dir = new File(name + "");
            dir.mkdir();
            String filePath = name + "";
            Path dirPath = Paths.get(filePath);
            String fileName = "/face.png";
            for (Mat frame : frames)
            {
                //byte[] image = new byte[(int) (frame.total() * frame.channels())];
                //frame.get(0, 0, image);

                //this could be done better? only way i could get it to work for now.

                Highgui.imwrite(filePath + fileName, frame);
                Path imagePath = Paths.get(filePath + fileName);

                try {
                    byte[] image = new byte[0];
                    image = Files.readAllBytes(imagePath);
                    BiometricData data = new BiometricData("face", image);
                    datas.add(data);
                    Files.delete(imagePath);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("borked");
                }



            }

            try {
                Files.delete(dirPath);
            } catch (IOException e) {
                System.out.println("Problem deleting dir");
            }

            return datas;
        }
        return null;
    }


    /**
     * This method will capture data from IO device and store somewhere on the client.
     * This will be done by running a script of some sorts or use library.
     */
    private void capture(List<Mat> _frames)
    {
        VideoCapture camera = new VideoCapture(0); //param passed is the device number, dont know if it will always be zero. might have to find the right device num in the client main.
        //camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1280); //camera resolution set here we could maybe have this in the config file???
        //camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 720);
        List<Mat> tempFrames = new ArrayList<Mat>();

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
            ArrayList<FaceDetection> threads = new ArrayList<FaceDetection>();
            while(count < 4)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (Exception e)
                {

                }
                Mat tempFrame = new Mat();
                Mat frame = new Mat();
                camera.read(tempFrame);
                Imgproc.cvtColor(tempFrame, frame,Imgproc.COLOR_RGB2GRAY);
                tempFrame = null;
                tempFrames.add(frame);
                System.out.println("Captured a photo.");
                count++;
            }

            for(Mat frame : tempFrames)
            {
                FaceDetection fd = new FaceDetection(frame, _frames);
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
            camera.release();


        }
    }

}
