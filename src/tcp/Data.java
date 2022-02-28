package tcp;

public class Data {
    public static final String LENGTH_HEADER = "10";

    public byte[] lengthBytes;
    public byte[] dataBytes;

    public Data(byte[] lengthBytes, byte[] dataBytes) {
        this.lengthBytes = lengthBytes;
        this.dataBytes = dataBytes;
    }
}
