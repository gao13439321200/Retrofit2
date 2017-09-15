package com.mvp.empty.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liwei on 2017/4/18.
 */

public class ToolImage {

    public static final int REQUEST_CAMERA_CODE = 11;
    public static final int REQUEST_PREVIEW_CODE = 22;

    /**
     * Get bitmap from specified image path
     *
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    /**
     * Store bitmap into specified image path
     *
     * @param bitmap
     * @param outPath
     * @throws FileNotFoundException
     */
    public void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(outPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
    }

    /**
     * Compress image by pixel, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param imgPath image path
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @return
     */
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    /**
     * Compress image by size, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param image
     * @param pixelW target pixel of width
     * @param pixelH target pixel of height
     * @return
     */
    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param maxSize     target will be compressed to be smaller than this size.(kb)
     * @param needsDelete Whether delete original file after compress
     * @throws IOException
     */
    public void compressAndGenImage(String imgPath, String outPath, int maxSize, boolean needsDelete) throws IOException {
        compressAndGenImage(getBitmap(imgPath), outPath, maxSize);

        // Delete original file
        if (needsDelete) {
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Ratio and generate thumb to the path specified
     *
     * @param image
     * @param outPath
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @throws FileNotFoundException
     */
    public void ratioAndGenThumb(Bitmap image, String outPath, float pixelW, float pixelH) throws FileNotFoundException {
        Bitmap bitmap = ratio(image, pixelW, pixelH);
        storeImage(bitmap, outPath);
    }

    /**
     * Ratio and generate thumb to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param pixelW      target pixel of width
     * @param pixelH      target pixel of height
     * @param needsDelete Whether delete original file after compress
     * @throws FileNotFoundException
     */
    public void ratioAndGenThumb(String imgPath, String outPath, float pixelW, float pixelH, boolean needsDelete) throws FileNotFoundException {
        Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
        storeImage(bitmap, outPath);

        // Delete original file
        if (needsDelete) {
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }


    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public static Bitmap getimage(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }


    /**
     * 图片按比例大小压缩方法
     *
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        ToolLog.e(w + "---------------" + h);
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        float hh = 512f;
        float ww = 512f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

        //return bitmap;
    }


    public static String compressImage(String filePath, String targetPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    // 压缩图片并上传
    public static File sacveFile(final File tempPic, String imgname) {
        final String pic_path = tempPic.getPath();
        String targetPath = ToolSDCard.getSDCardPath() + imgname;
        //调用压缩图片的方法，返回压缩后的图片path
        final String compressImage = compressImage(pic_path, targetPath, 80);
        final File compressedPic = new File(compressImage);
        if (compressedPic.exists()) {
            ToolLog.e("图片压缩上传");
        }
        return compressedPic;
    }

//    public static PhotoPopupWindow photoPopupWindow = null;
//
//    //选择照片的弹窗和示例图片的弹窗
//    public static PhotoPopupWindow selectImage(final Activity context, int drawableId, View view) {
//        photoPopupWindow = new PhotoPopupWindow(context, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.btn_take_photo://拍照
//                        if (ToolApp.checkPermission(context, "android.permission.CAMERA")) {
//                            Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                        intent2.putExtra(MediaStore.EXTRA_OUTPUT,
////                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), path)));
//                            context.startActivityForResult(intent2, 2);// 采用ForResult打开
//                        } else {
//                            MyDialog myDialog = new MyDialog(context, new MyDialogOnClick() {
//                                @Override
//                                public void onOKClick() {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelClick() {
//                                }
//                            }, 2);
//                            myDialog.setNameText(ToolString.str_j);
//                            myDialog.setButtonOK("去设置");
//                            myDialog.setButtonCancel("偏偏不要");
//                            myDialog.setCancelable(true);
//                            myDialog.show();
//                        }
//                        photoPopupWindow.dismiss();
//                        break;
//                    case R.id.btn_pick_photo://相册选取
//                        if (ToolApp.checkPermission(context, "android.permission.READ_EXTERNAL_STORAGE")
//                                && ToolApp.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//
//                            Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//                            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                            context.startActivityForResult(intent1, 1);
//                        } else {
//                            MyDialog myDialog = new MyDialog(context, new MyDialogOnClick() {
//                                @Override
//                                public void onOKClick() {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelClick() {
//                                }
//                            }, 2);
//                            myDialog.setNameText(ToolString.str_k);
//                            myDialog.setButtonOK("去设置");
//                            myDialog.setButtonCancel("偏偏不要");
//                            myDialog.setCancelable(true);
//                            myDialog.show();
//                        }
//                        photoPopupWindow.dismiss();
//                        break;
//                    case R.id.btn_cancel://取消
//                        photoPopupWindow.dismiss();
//                        break;
//                }
//            }
//        });
//        final ExamplePopupWindow window = new ExamplePopupWindow(context, drawableId, photoPopupWindow);
//        photoPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                window.dismiss();
//            }
//        });
//        window.showAtLocation(view, Gravity.TOP, 0, 0);
//        photoPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//        return photoPopupWindow;
//    }

//    //选择照片的弹窗和示例图片的弹窗
//    public static PhotoPopupWindow selectImage(final Activity context, int drawableId, View view, final String path) {
//        photoPopupWindow = new PhotoPopupWindow(context, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.btn_take_photo://拍照
//                        if (ToolApp.checkPermission(context, "android.permission.CAMERA")) {
//                            Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            // 加载路径
//                            Uri uri = Uri.fromFile(new File(path));
//                            intent2.putExtra(MediaStore.EXTRA_OUTPUT,
//                                    uri);
//                            context.startActivityForResult(intent2, 2);// 采用ForResult打开
//                        } else {
//                            MyDialog myDialog = new MyDialog(context, new MyDialogOnClick() {
//                                @Override
//                                public void onOKClick() {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelClick() {
//                                }
//                            }, 2);
//                            myDialog.setNameText(ToolString.str_j);
//                            myDialog.setButtonOK("去设置");
//                            myDialog.setButtonCancel("偏偏不要");
//                            myDialog.setCancelable(true);
//                            myDialog.show();
//                        }
//                        photoPopupWindow.dismiss();
//                        break;
//                    case R.id.btn_pick_photo://相册选取
//                        if (ToolApp.checkPermission(context, "android.permission.READ_EXTERNAL_STORAGE")
//                                && ToolApp.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//
//                            Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//                            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                            context.startActivityForResult(intent1, 1);
//                        } else {
//                            MyDialog myDialog = new MyDialog(context, new MyDialogOnClick() {
//                                @Override
//                                public void onOKClick() {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
//                                        // 进入设置系统应用权限界面
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                        context.startActivity(intent);
//                                        context.finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelClick() {
//                                }
//                            }, 2);
//                            myDialog.setNameText(ToolString.str_k);
//                            myDialog.setButtonOK("去设置");
//                            myDialog.setButtonCancel("偏偏不要");
//                            myDialog.setCancelable(true);
//                            myDialog.show();
//                        }
//                        photoPopupWindow.dismiss();
//                        break;
//                    case R.id.btn_cancel://取消
//                        photoPopupWindow.dismiss();
//                        break;
//                }
//            }
//        });
//        final ExamplePopupWindow window = new ExamplePopupWindow(context, drawableId, photoPopupWindow);
//        photoPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                window.dismiss();
//            }
//        });
//        window.showAtLocation(view, Gravity.TOP, 0, 0);
//        photoPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//        return photoPopupWindow;
//    }


//    public static final Bitmap create2DCoderBitmap(String url, int QR_WIDTH,
//                                                   int QR_HEIGHT) {
//        try {
//            // 判断URL合法性
//            if (url == null || "".equals(url) || url.length() < 1) {
//                return null;
//            }
//            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
//                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
//            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < QR_HEIGHT; y++) {
//                for (int x = 0; x < QR_WIDTH; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * QR_WIDTH + x] = 0xff000000;
//                    } else {
//                        pixels[y * QR_WIDTH + x] = 0xffffffff;
//                    }
//                }
//            }
//            // 生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
//                    Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
//            // 显示到一个ImageView上面
//            // sweepIV.setImageBitmap(bitmap);
//            return bitmap;
//        } catch (WriterException e) {
//            Log.i("log", "生成二维码错误" + e.getMessage());
//            return null;
//        }
//    }

    //获取图片对应的drawable对象
    public static Drawable getDrawable(Context context, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        return drawable;
    }


    /**
     * 通过uri获取图片路径
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //保存bitmap的图片到本地
    public static void saveFileByBitmap(String path, Bitmap bitmap) {
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 银行的小图标（用的微信端的图片，切记）
     *
     * @param code 银行编码
     * @return 地址
     */
    public static String getBankImageSmall(String code) {
        return "http://wx.91msl.com/assets/images/img/" + code + ".png";
    }
    /**
     * 银行的大图标（用的微信端的图片，切记）
     *
     * @param code 银行编码
     * @return 地址
     */
    public static String getBankImageBig(String code) {
        return "http://wx.91msl.com/assets/images/" + code + ".png";
    }
}
