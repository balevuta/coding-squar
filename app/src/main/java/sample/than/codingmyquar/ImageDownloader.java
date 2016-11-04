package sample.than.codingmyquar;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader extends AsyncTask<String, Integer, Void> {

    private Context mContext;

    private String fileName;

    private String fileUrl;
    private int fileIndex;

    private NotificationCompat.Builder mCurrBuilder;
    private NotificationManager mNotifyManager;

    private DownloadStatus mDownloadStatus;

    public ImageDownloader(Context pContext, String url, int index) {

        fileUrl = url;
        fileIndex = index;

        mContext = pContext;
        mNotifyManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void downloadImagesToSdCard(String downloadUrl, String imageName) {
        FileOutputStream fos;
        InputStream inputStream = null;

        try {
            URL url = new URL(downloadUrl);
			/* making a directory in sdcard */
            String sdCard = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(sdCard, "DemoDownload");
			/* if specified not exist create new */
            if (!myDir.exists()) {
                myDir.mkdir();
                Log.v("", "inside mkdir");
            }
			/* checks the file and if it already exist delete */
            String fname = imageName;
            int downloaded = 0;
            File file = new File(myDir, fname);
            Log.d("file===========path", "" + file);
            if (file.exists())
                file.delete();
            URLConnection ucon = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) ucon;
            if(mDownloadStatus.equals(DownloadStatus.STATE_DOWNLOADING)) {
                downloaded = (int) file.length();
                Log.d("ImageDownloader", "resume download at: " + downloaded);
                httpConn.setRequestProperty("Range", "bytes="+(downloaded)+"-");

                if (file.exists() && (downloaded == httpConn.getContentLength()))
                    file.delete();
            } else {
                Log.d("ImageDownloader", "resume download from beginning: " + file.length());
                httpConn.setRequestProperty("Range", "bytes="+(file.length())+"-");

                if (file.exists())
                    file.delete();
            }
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            int fileLength = httpConn.getContentLength();
            inputStream = httpConn.getInputStream();
            fos = new FileOutputStream(file);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = inputStream.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    inputStream.close();
                    return;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                fos.write(data, 0, count);
            }

            inputStream.close();
            fos.close();
            Log.d("test", "Image Saved in sdcard..");
        } catch (IOException io) {
            inputStream = null;
            fos = null;
            io.printStackTrace();
            mDownloadStatus = DownloadStatus.STATE_DOWNLOADING;
        } catch (Exception e) {
            e.printStackTrace();
            mDownloadStatus = DownloadStatus.STATE_DOWNLOADING;
        } finally {

        }
    }

    @Override
    protected Void doInBackground(String... param) {
        mDownloadStatus = DownloadStatus.STATE_START_DOWNLOAD;
        mCurrBuilder.setContentText("Downloading file" + fileIndex + " (" + 0 + "/100)");
        mNotifyManager.notify(fileIndex, mCurrBuilder.build());
        fileName = "Image" + fileIndex + ".png";
        downloadImagesToSdCard(fileUrl, fileName);
        return null;
    }

    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        Log.d("Progress", "Progress: " + progress);
        mCurrBuilder.setProgress(100, progress, false);
        if(progress < 100) {
            mCurrBuilder.setContentText("Downloading file" + fileIndex + " (" + progress + "/100)");
            mCurrBuilder.setOngoing(true);
        } else {
            mCurrBuilder.setContentText(fileName + " is downloaded!");
            mCurrBuilder.setOngoing(false);
            mDownloadStatus = DownloadStatus.STATE_DOWNLOADED;
        }
        mNotifyManager.notify(fileIndex, mCurrBuilder.build());
    }

    @Override
    protected void onPreExecute() {
        Log.i("Async-Example", "onPreExecute Called");
        mCurrBuilder = new NotificationCompat.Builder(mContext);
        mCurrBuilder.setProgress(100, 0, false);
        mCurrBuilder.setSmallIcon(R.mipmap.ic_launcher);
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.i("Async-Example", "onPostExecute Called");
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String pFileUrl) {
        fileUrl = pFileUrl;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int pFileIndex) {
        fileIndex = pFileIndex;
    }

    public DownloadStatus getDownloadStatus() {
        return mDownloadStatus;
    }

    public void setDownloadStatus(DownloadStatus pDownloadStatus) {
        mDownloadStatus = pDownloadStatus;
    }
}
