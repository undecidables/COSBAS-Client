package application;

import application.Model.ApplicationModel;
import modules.OPENCVCamera;
import modules.OPENCVFaceDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created by simon on 2015-10-06.
 */
public class Utilities {

    OPENCVCamera cameraObject;
    OPENCVFaceDetection detectFaces;

    @Autowired
    public Utilities() {
        //cameraObject = (OPENCVCamera) app.getBean("cameraObject");
        //detectFaces = (OPENCVFaceDetection) app.getBean("detectFaces");
        cameraObject = new OPENCVCamera();
        detectFaces = new OPENCVFaceDetection();
    }

    public OPENCVCamera getCameraObject() {
        return cameraObject;
    }

    public OPENCVFaceDetection getDetection()
    {
        return detectFaces;
    }

}
