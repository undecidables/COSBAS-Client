package application;

import org.bytedeco.javacpp.opencv_highgui;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public class WebCamStream {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private JFrame frame;
    private JLabel imageLabel;
    public static void main(String [] args)
    {
        WebCamStream webapp = new WebCamStream();
        webapp.initGUI();
        webapp.runMainLoop(args);
    }
    private void initGUI()
    {
        frame = new JFrame("Camera Input Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        imageLabel = new JLabel();
        frame.add(imageLabel);
        frame.setVisible(true);
    }
    private void runMainLoop(String [] args)
    {
        Mat webcamMatImage = new Mat();
        Image tempImage;
        VideoCapture capture = new VideoCapture(0);
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 320);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 240);

        if(capture.isOpened()){
            while(true)
            {
                capture.read(webcamMatImage);
                if(!webcamMatImage.empty())
                {
                    tempImage = mat2Img(webcamMatImage);
                    ImageIcon imageIcon = new ImageIcon(tempImage, "Captured Video");
                    imageLabel.setIcon(imageIcon);
                    frame.pack();
                }
                else
                {
                    System.out.println("Frame not captured");
                    break;
                }
            }
        }
        else
        {
            System.out.println("Couldn't open capture!");
        }
    }
    public static BufferedImage mat2Img(Mat in)
    {
        BufferedImage out;
        byte[] data = new byte[320 * 240 * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(320, 240, type);

        out.getRaster().setDataElements(0, 0, 320, 240, data);
        return out;
    }
}

