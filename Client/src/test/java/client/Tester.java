package client;

import junit.framework.TestCase;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opencv.core.Core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;


/**
 * Created by POLSKA on 26/06/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class Tester extends TestCase{


    @Test
    public void testFingerPrintScanning()
    {
        FutronicFingerprintScanner scanner = new FutronicFingerprintScanner();
        ArrayList<byte[]> results = new ArrayList<>();
        results.add(scanner.getImage());
        if(!System.getProperty("os.arch").equals("arm"))
        {
            //not runnong on arm/pi, just grab an image from folder
            assertTrue(results.isEmpty());
        }
        else
        {
            assertTrue(!results.isEmpty());
        }
    }

    /*@Test
    public void testFaceDetection() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Path imagePath = Paths.get("testingimages/oneface.png");
        byte[] image = Files.readAllBytes(imagePath);
        ArrayList<byte[]> images = new ArrayList<byte[]>();
        images.add(image);
        modules.FaceDetection fd = new modules.OPENCVFaceDetection();
        ArrayList<byte[]> faces = fd.detectFaces(images);
        System.out.println(faces.size());
        assertTrue(!(faces.isEmpty()));
    }*/
}
