package client;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class ConvertMatToImageByteArray<T> implements ImageByteArrayConverterInterface<T>
{


    /**
     * This funcion converts mat objects to image and stores then in the byte array. This allows us to use other facial recognition apis and not only opencv.
     * @param _data image data stored in a different form like a Mat object
     * @return returns the image
     */
    public byte[] convertToImageByteArray(T _data)
    {
        if(_data != null)
        {
            int dirName = (int) (Math.random() * 100);
            File dir = new File(dirName + "");
            dir.mkdir();
            String filePath = dirName + "";
            Path dirPath = Paths.get(filePath);
            String fileName = "/face.png";

            Mat data = (Mat) _data;
            Highgui.imwrite(filePath + fileName, data);
            Path imagePath = Paths.get(filePath + fileName);

            byte[] image = new byte[0];

            try
            {

                image = Files.readAllBytes(imagePath);
                Files.deleteIfExists(imagePath);
                Files.deleteIfExists(dirPath);
            }
            catch (Exception e)
            {
                System.out.println("Whoops!");
            }

            return image;
        }

        return null;
    }
}
