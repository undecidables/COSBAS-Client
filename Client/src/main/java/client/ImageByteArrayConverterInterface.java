package client;

import java.util.ArrayList;

/**
 * Created by simon on 2015-07-23.
 */
public interface ImageByteArrayConverterInterface<T> {
    public byte[] convertToImageByteArray(T _data);
}
