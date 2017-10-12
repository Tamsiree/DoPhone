package com.zbar.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.io.FileNotFoundException;
import java.util.Hashtable;

/**
 * Created by master on 2015-02-28.
 */
public class DecoderLocalFile extends LuminanceSource {
    private final byte[] luminances;

    public DecoderLocalFile(String path) throws FileNotFoundException {
        this(loadBitmap(path));
    }

    public DecoderLocalFile(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        // In order to measure pure decoding speed, we convert the entire image
        // to a greyscale array
        // up front, which is the same as the Y channel of the
        // YUVLuminanceSource in the real app.
        luminances = new byte[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = pixels[offset + x];
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                if (r == g && g == b) {
                    // Image is already greyscale, so pick any channel.
                    luminances[offset + x] = (byte) r;
                } else {
                    // Calculate luminance cheaply, favoring green.
                    luminances[offset + x] = (byte) ((r + g + g + b) >> 2);
                }
            }
        }
    }

    public String handleQRCodeFormPhoto(Context context, Bitmap bitmap) {
        // TODO Auto-generated method stub

        Hashtable<DecodeHintType, String> tab = new Hashtable<DecodeHintType, String>();
        tab.put(DecodeHintType.CHARACTER_SET, "utf-8");

        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result;
        try {
            result = reader.decode(bitmap1, tab);
//          result = reader.decode(bitmap1, hints);
            System.out.println("res" + result.getText());
//            T.s(context, result.getText());
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return "-1";
        } catch (ChecksumException e) {
            e.printStackTrace();
            return "-1";
        } catch (FormatException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException(
                    "Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }
        System.arraycopy(luminances, y * width, row, 0, width);
        return row;
    }

    // Since this class does not support cropping, the underlying byte array
    // already contains
    // exactly what the caller is asking for, so give it to them without a copy.
    @Override
    public byte[] getMatrix() {
        return luminances;
    }

    public static Bitmap  loadBitmap(String path) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            throw new FileNotFoundException("Couldn't open " + path);
        }
        return bitmap;
    }
}
