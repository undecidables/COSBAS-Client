package modules;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import java.io.ByteArrayInputStream;

/**
 * {@author Szymon}
 */
public class ConvertMatToImage implements ConvertorInterface{
    @Override
    public Object convert(Object _data) {
        Mat data = (Mat) _data;

        MatOfByte buffer = new MatOfByte();
        Highgui.imencode(".png", data, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
