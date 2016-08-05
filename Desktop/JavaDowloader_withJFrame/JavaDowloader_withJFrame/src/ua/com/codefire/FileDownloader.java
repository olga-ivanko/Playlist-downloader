/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire;

import ua.com.codefire.DownloadFileListener;
import ua.com.codefire.DownloadFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author homefulloflove
 */
public class FileDownloader implements Runnable {

    private File store;
    private DownloadFile downloadFile;
    private DownloadFileListener listener;
    private int progress;

    public FileDownloader(File store, DownloadFile downloadFile) {
        this.store = store;
        this.downloadFile = downloadFile;
    }

    public FileDownloader(File store, DownloadFile downloadFile, DownloadFileListener listener) {
        this.store = store;
        this.downloadFile = downloadFile;
        this.listener = listener;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void run() {
        changeState(DownloadFile.State.READY);

        try {
            URLConnection conn = downloadFile.getAddress().openConnection();
            conn.getContentType();

            URL url = conn.getURL();
            String filePath = new String(url.getFile().getBytes("ISO-8859-1"), "UTF-8");
            String fileName = new File(URLDecoder.decode(filePath, "UTF-8")).getName();
            byte[] buffer = new byte[4096];
//            int progress = 0;
            File target = new File(store, fileName);
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(target);
            while (true) {
                int read = is.read(buffer);

                if (read < 0) {
                    break;
                }

                progress += read;
                fos.write(buffer, 0, read);
                fos.flush();

//                System.out.println("Download " + url);
//                Files.copy(conn.getInputStream(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // (поток который копируем | путь на файловой системе | что делать если файл существует)
//                System.out.println("Download complete: " + target);
                changeState(DownloadFile.State.PROGRESS);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }

        changeState(DownloadFile.State.COMPLETE);
    }

    private void changeState(DownloadFile.State state) {
        downloadFile.setState(state);
        if (listener != null) {
            listener.stateChanged(downloadFile);
        }
    }
}
