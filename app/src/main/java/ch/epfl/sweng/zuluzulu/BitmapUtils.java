package ch.epfl.sweng.zuluzulu;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;

public interface BitmapUtils {

    /**
     * rotate a bitmap
     * @param bitmap the bitmap to rotate
     * @param angle the angle to rotate the bitmap
     * @return the bitmap rotated
     */
    static Bitmap rotateBitmap(Bitmap bitmap, float angle) {
        Matrix m = new Matrix();
        m.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                m, true);
    }

    /**
     * write the bitmap at the specified place in the SD Card as a JPEG
     * @param bitmap the bitmap to write
     * @param path the path in the SD Card
     */
    static void writeBitmapInSDCard(Bitmap bitmap, String path){
        File toWriteInSDCard = new File(path);

        if (toWriteInSDCard.exists ()) toWriteInSDCard.delete ();
        try {
            FileOutputStream out = new FileOutputStream(toWriteInSDCard);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
