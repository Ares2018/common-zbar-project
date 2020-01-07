package cn.com.daily.zbar.qr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import cn.bertsir.zbar.Qr.Config;
import cn.bertsir.zbar.Qr.Image;
import cn.bertsir.zbar.Qr.ImageScanner;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.Qr.SymbolSet;

/**
 * Created by Bert on 2017/9/20.
 */

public class QRUtils {

    private static QRUtils instance;


    public static QRUtils getInstance() {
        if (instance == null) {
            instance = new QRUtils();
        }
        return instance;
    }



    /**
     * 识别本地二维码
     *
     * @param path 本地图片地址
     * @return
     */
    public String decodeQRCode(String path) throws Exception {
        //对图片进行灰度处理，为了兼容彩色二维码
        Bitmap qrbmp = compressImage(path);
        qrbmp = toGrayScale(qrbmp);
        if (qrbmp != null) {
            return decodeQRCode(qrbmp);
        } else {
            return "";
        }

    }

    public String decodeQRCode(Bitmap barcodeBmp) throws Exception {
        int width = barcodeBmp.getWidth();
        int height = barcodeBmp.getHeight();
        int[] pixels = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        int result = reader.scanImage(barcode.convert("Y800"));
        String qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        barcodeBmp.recycle();
        return qrCodeString;
    }

    /**
     * 识别本地条形码
     *
     * @param path
     * @return
     */
    public String decodeBarcode(String path)  {
        Bitmap qrbmp = BitmapFactory.decodeFile(path);
        if (qrbmp != null) {
            return decodeBarcode(qrbmp);
        } else {
            return "";
        }

    }

    public String decodeBarcode(Bitmap barcodeBmp) {
        int width = barcodeBmp.getWidth();
        int height = barcodeBmp.getHeight();
        int[] pixels = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.CODE128, Config.ENABLE, 1);
        reader.setConfig(Symbol.CODE39, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN13, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN8, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCA, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        int result = reader.scanImage(barcode.convert("Y800"));
        String qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        return qrCodeString;
    }

    /**
     * 压缩图片
     * @param path
     * @return
     */
    private Bitmap compressImage(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        options.inJustDecodeBounds = false;
        int sampleSizeH = (int) (options.outHeight / (float) 800);
        int sampleSizeW = (int) (options.outWidth / (float) 800);
        int sampleSize = Math.max(sampleSizeH,sampleSizeW);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap qrbmp = BitmapFactory.decodeFile(path,options);
        return qrbmp;
    }

    /**
     * 对bitmap进行灰度处理
     * @param bmpOriginal
     * @return
     */
    private Bitmap toGrayScale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

}
