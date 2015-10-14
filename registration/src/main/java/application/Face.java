package application;

import application.Model.ApplicationModel;
import modules.Biometric;
import modules.BiometricData;
import modules.OPENCVCamera;
import modules.OPENCVFaceDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public class Face implements Biometric {

    Utilities utilities;
    OPENCVCamera cameraObject;
    OPENCVFaceDetection detectFaces;

    @Autowired
    public Face()
    {
        ApplicationContext app = ApplicationModel.app;
        utilities = (Utilities) app.getBean("utilities");
        cameraObject = utilities.getCameraObject();
        detectFaces = utilities.getDetection();
    }

    @Override
    public void fillData(ArrayList<BiometricData> datas) {

        ArrayList<byte[]> images =  new ArrayList<byte[]>();
        for (int i = 0; i < 12; i++)
        {
            images.add(cameraObject.getImage());
        }

        images = detectFaces.detectFaces(images);


        if(images != null)
        {
            for(byte[] image : images)
            {
                BiometricData data = new BiometricData("biometric-FACE", image);
                datas.add(data);
            }
        }


    }
}
