package client;

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

    }

    /**
     *
     * @param _data takes in a BiometricData object and prepares it to be sent to the server. in this case we perform facial detection on the image so see if there is a face.
     *              i think that in opencv you can crop the image to just the face? if so then we will do that as well
     * @return returns the BiometricData
     */
    public BiometricData prepare(BiometricData _data)
    {
        return null;
    }


    /**
     * This method will capture data from IO device and store somewhere on the client.
     * This will be done by running a script of some sorts or use library.
     */
    private void capture()
    {

    }

}
