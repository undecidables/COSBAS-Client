package modules;

/**
 * {@author Szymon}
 */
public class BiometricData {
    private final byte[] data;
    private final String type;

    public byte[] getData()
    {
        return data;
    }

    public String getType()
    {
        return type;
    }

    public BiometricData(String _type, byte[] _data)
    {
        data = _data;
        type = _type;
    }

}
