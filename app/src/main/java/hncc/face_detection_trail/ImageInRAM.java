package hncc.face_detection_trail;

import java.io.File;

public class ImageInRAM {
    static File uFile=null;
    static ImageInRAM instance=new ImageInRAM();
    public ImageInRAM() {
    }

    public static ImageInRAM getInstance() {
        return instance;
    }

    public static File getFile() {
        return uFile;
    }

    public void setuFile(File uFile) {
        this.uFile = uFile;
    }
}
