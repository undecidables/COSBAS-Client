package client;

import java.util.ArrayList;

/**
 * Created by POLSKA on 30/06/2015.
 */
public class Fingerprint implements Biometric {

    /**
     *
     * @param _data takes in an arraylist of BiometricData and will add fingerprint data to it
     */
    @Override
    public void fillData(ArrayList<BiometricData> _data)
    {

    }

    /**
     * This method will capture data from IO device and store somewhere on the client
     */
    private void capture()
    {

    }


}
