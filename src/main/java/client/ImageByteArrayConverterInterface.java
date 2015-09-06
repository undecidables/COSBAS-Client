package client;

import java.util.ArrayList;

/**
 * {@author Szymon}
 */
public interface ImageByteArrayConverterInterface<T> {
    public byte[] convertToImageByteArray(T _data);
}
