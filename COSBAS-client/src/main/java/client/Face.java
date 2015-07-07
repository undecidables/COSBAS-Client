package client;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import java.util.ArrayList;

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
        ArrayList<Mat> frames = new ArrayList<Mat>();
        ArrayList<BiometricData> datas = new ArrayList<BiometricData>();
        capture(frames);
        if(frames.size() > 0)
        {
            for (Mat frame : frames)
            {
                byte[] image = new byte[(int) (frame.total() * frame.channels())];
                frame.get(0, 0, image);

                BiometricData data = new BiometricData("face", image);
                datas.add(data);
            }
            return datas;
        }
        return null;
    }


    /**
     * This method will capture data from IO device and store somewhere on the client.
     * This will be done by running a script of some sorts or use library.
     */
    private void capture(ArrayList<Mat> frames)
    {
        VideoCapture camera = new VideoCapture(0); //param passed is the device number, dont know if it will always be zero. might have to find the right device num in the client main.
        FaceDetection fd = new FaceDetection();
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
            while(count < 7)
            {

                try
                {
                    Thread.sleep(50);
                }
                catch (Exception e)
                {

                }
                Mat frame = new Mat();
                camera.read(frame);
                //System.out.println("Taking a frame now....");
                //Highgui.imwrite("capture" + count + ".jpg", frame);
                count++;
                if(fd.detect(frame))
                {
                    frames.add(frame);
                }

            }


        }
    }

}
