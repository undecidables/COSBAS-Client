package application.Model;

import modules.ConvertMatToImage;
import modules.OPENCVCamera;
import modules.OPENCVDetectAndBorderFaces;
import modules.OPENCVFaceDetection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@author Szymon}
 */
public class Utilities {

    OPENCVCamera cameraObject;
    OPENCVFaceDetection detectFaces;
    ConvertMatToImage convertMatToImage;
    OPENCVDetectAndBorderFaces detectAndBorderFaces;

    @Autowired
    public Utilities() {
        //cameraObject = (OPENCVCamera) app.getBean("cameraObject");
        //detectFaces = (OPENCVFaceDetection) app.getBean("detectFaces");
        cameraObject = new OPENCVCamera();
        detectFaces = new OPENCVFaceDetection();
        convertMatToImage = new ConvertMatToImage();
        detectAndBorderFaces = new OPENCVDetectAndBorderFaces();
    }

    public OPENCVDetectAndBorderFaces getDetectAndBorderFaces()
    {
        return detectAndBorderFaces;
    }

    public OPENCVCamera getCameraObject() {
        return cameraObject;
    }

    public OPENCVFaceDetection getDetection()
    {
        return detectFaces;
    }

    public ConvertMatToImage getConvertMatToImage()
    {
        return convertMatToImage;
    }

}
