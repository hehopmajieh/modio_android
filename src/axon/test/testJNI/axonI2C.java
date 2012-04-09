package axon.test.testJNI;

/**
 * This is an I2C operation class
 */
public class axonI2C {
    /**
     * @param nodeName
     *            node path name
     * @return return file hander else return <0 on fail
     */
    public native int open(String nodeName);

    /**
     * @param fileHander
     * @param i2c_adr
     *            slave addr
     * @param buf
     * @param Lenth
     *            of buf
     * @return read length
     */
    public native int read(int fileHander, int i2c_adr, int buf[], int Length);

    /**
     * @param fileHander
     * @param i2c_adr
     *            slave addr
     * @param sub_adr
     *            sub addr
     * @param buf
     * @param Lenth
     *            of buf
     * @return write length
     */
    public native int write(int fileHander, int i2c_adr, int sub_adr,
            int buf[], int Length);

    public native void close(int fileHander);

    static {
        System.loadLibrary("axonI2C");
    }
}
