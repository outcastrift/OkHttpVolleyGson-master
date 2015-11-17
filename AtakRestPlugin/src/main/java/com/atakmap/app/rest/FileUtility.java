package com.atakmap.app.rest;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * Created by sam on 11/17/15.
 */


import java.io.BufferedInputStream;
    import java.io.BufferedOutputStream;
    import java.io.Closeable;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;


    /**
     * FileUtil provides some file location and I/O related utility methods.
     *
     * @author Marko Teittinen
     */
    public class FileUtility {
        // All these are relative to SD root
        private static final String DATA_DIR = "AtakRestSources";
        private static final String GEOCODE_DIR = DATA_DIR + "/geocode";
        private static final String ROUTE_DIR = DATA_DIR + "/routes";
        private static final String WIKI_DIR = DATA_DIR + "/wiki";

        private static final String CACHE_DIR = DATA_DIR + "/cache";
        private static final String SD_DCIM = "DCIM";
        private static final String SD_PHOTOS = SD_DCIM + "/Camera";
        private static final String SD_PHOTOS_2 = SD_DCIM + "/100MEDIA";
        private static final String SD_DOWNLOADS = "download";
        private static final String SD_DOWNLOADS_2 = "downloads";
        private static final String LOG_TAG = "AtakRestPlugin";
        private static final String TMP_IMAGE_NAME = "mapimage.jpg";
        public static final String KMZ_IMAGE_DIR = "images/";

        public static File getSdRoot() {
            return Environment.getExternalStorageDirectory();
        }

        public static File getPhotosDirectory() {
            // G1, Nexus One, and Nexus S use this folder
            File photoDir = new File(getSdRoot(), SD_PHOTOS);
            if (photoDir.exists() && photoDir.isDirectory()) {
                return photoDir;
            }
            // At least Droid Eris uses this folder
            return new File(getSdRoot(), SD_PHOTOS_2);
        }

        public static boolean isPhotosDirectory(File dir) {
            // Some devices use other directory names under sdcard/DCIM
            String photoRootPath = new File(getSdRoot(), SD_DCIM).getAbsolutePath();
            return dir != null && dir.getAbsolutePath().startsWith(photoRootPath);
        }

        public static File getDownloadsDirectory() {
            File downloadDir = new File(getSdRoot(), SD_DOWNLOADS);
            if (downloadDir.exists() && downloadDir.isDirectory()) {
                return downloadDir;
            }
            return new File(getSdRoot(), SD_DOWNLOADS_2);
        }

        public static File getDataDirectory() {
            File dataDir = new File(getSdRoot(), DATA_DIR);
            verifyDir(dataDir);
            return dataDir;
        }

        public static File getGeocodeDirectory() {
            File imageDir = new File(getSdRoot(), GEOCODE_DIR);
            verifyDir(imageDir);
            return imageDir;
        }
        public static File getRouteDirectory() {
            File routeDir = new File(getSdRoot(), ROUTE_DIR);
            verifyDir(routeDir);
            return routeDir;
        }
        public static File getWikiDirectory() {
            File wikiDir = new File(getSdRoot(), WIKI_DIR);
            verifyDir(wikiDir);
            return wikiDir;
        }





        public static File getCacheDirectory() {
            File cacheDir = new File(getSdRoot(), CACHE_DIR);
            verifyDir(cacheDir);
            return cacheDir;
        }

        /**
         * Helper method to initialize directory. If a given directory does not exist,
         * the directory is created and a .nomedia file is created in it to prevent
         * internal resources from appearing in Android Gallery.
         *
         * @param dir File object pointing to directory that needs to exist.
         * @return Boolean value indicating if the directory now exists.
         */
        private static boolean verifyDir(File dir) {
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.w(FileUtility.LOG_TAG, "Failed to create dir: " + dir.getAbsolutePath());
                    return false;
                }
            }
            addNoMediaFile(dir);
            return true;
        }

        /**
         * @param file
         * @return {@code true} if 'file' is in browser's download directory
         */
        public static boolean isDownloadFile(File file) {
            return isFileInDirectory(file, getDownloadsDirectory());
        }

        /**
         * @param file
         * @return {@code true} if 'file' is in this app's data directory
         */
        public static boolean isInDataDirectory(File file) {
            return isFileInDirectory(file, getDataDirectory());
        }

        /**
         * @param file
         * @param dir
         * @return {@code true} if 'file' is in 'dir' or one of its subdirs
         */
        private static boolean isFileInDirectory(File file, File dir) {
            String filePath = getBestPath(file);
            String dirPath = getBestPath(dir);
            return filePath.startsWith(dirPath);
        }

        /**
         * @param file
         * @return Canonical path for file, or if that fails, absolute path for it
         */
        public static String getBestPath(File file) {
            if (file == null) {
                return null;
            }
            try {
                return file.getCanonicalPath();
            } catch (IOException ex) {
                Log.w(FileUtility.LOG_TAG, "Failed to resolve canonical path for: " + file, ex);
            }
            return file.getAbsolutePath();
        }



        /**
         * Generates a new file reference to a non-existing file in the app's data
         * directory.
         *
         * @param nameFormat String.format to be used in generating the filename.
         *  Must contain a single '%d' field to indicate where number is inserted.
         * @return File reference to non-existing file (safe to create)
         */
        public static File newFileInDataDirectory(String nameFormat) {
            int i = 1;
            File dataDir = getDataDirectory();
            File file = new File(dataDir, String.format(nameFormat, i));
            while (file.exists()) {
                file = new File(dataDir, String.format(nameFormat, ++i));
            }
            return file;
        }

        /**
         * Copies file to data directory if it is not there already. Overwrites any
         * existing file with the same name.
         *
         * @param file
         * @return File pointing at the file in data directory, or 'null' in case of
         *         failure
         */
        public static File copyToDataDirectory(File file) {
            if (isInDataDirectory(file)) {
                return file;
            }
            long timestamp = file.lastModified();
            InputStream in = null;
            OutputStream out = null;
            File destination = null;
            File tmpDestination = null;
            try {
                destination = new File(getDataDirectory(), file.getName());
                if (destination.exists()) {
                    tmpDestination = newFileInDataDirectory("%d_" + file.getName());
                }
                in = new FileInputStream(file);
                out = new FileOutputStream(tmpDestination != null ? tmpDestination : destination);

                copyContents(in, out);

                return destination;
            } catch (IOException e) {
                Log.w(FileUtility.LOG_TAG, "Failed to copy file to data directory: " + file, e);
            } finally {
                tryToClose(in);
                tryToClose(out);

                if (tmpDestination != null) {
                    // Delete old file, and rename new to replace it
                    destination.delete();
                    tmpDestination.renameTo(destination);
                }
                // Copy timestamp from original
                destination.setLastModified(timestamp);
            }
            return null;
        }

        /**
         * Moves file to data directory if it is not there already.
         *
         * @param file
         * @return File pointing at the file in data directory, or 'null' in case of
         *         failure
         */
        public static File moveToDataDirectory(File file) {
            if (isInDataDirectory(file)) {
                return file;
            }
            try {
                file = file.getCanonicalFile();
                File destination = new File(getDataDirectory(), file.getName());
                if (file.renameTo(destination)) {
                    return destination;
                }
            } catch (IOException e) {
                Log.w(FileUtility.LOG_TAG, "Failed to move file to data directory: " + file, e);
            }
            return null;
        }



        /**
         * Copies all contents of 'from' to 'to'.
         *
         * @param from
         * @param to
         * @throws IOException
         */
        public static void copyContents(InputStream from, OutputStream to) throws IOException {
            if (!(from instanceof BufferedInputStream)) {
                from = new BufferedInputStream(from);
            }
            if (!(to instanceof BufferedOutputStream)) {
                to = new BufferedOutputStream(to);
            }

            byte[] buf = new byte[1024];
            int n;
            while ((n = from.read(buf)) != -1) {
                if (n > 0) {
                    to.write(buf, 0, n);
                }
            }
            to.flush();
        }

        /**
         * Attempts to close a closeable object, typically an input or output stream.
         * Returns 'true' if the closing was successful, and 'false' if it failed with
         * an exception (closing a null object will result in no-op success). If
         * an exception was thrown, it is logged, but never thrown for the caller.
         *
         * @param stream Closeable object (not necessarily a stream) to be closed
         * @return {@code true} if closing succeeded without errors.
         */
        public static boolean tryToClose(Closeable stream) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Log.w(FileUtility.LOG_TAG, "Failed to close stream", ex);
                    return false;
                }
            }
            return true;
        }

        /**
         * Attempts to add an empty .nomedia file to a directory to indicate it does
         * not contain public image files.
         */
        private static void addNoMediaFile(File dir) {
            if (!dir.exists()) {
                return;
            }
            File noMedia = new File(dir, ".nomedia");
            if (!noMedia.exists()) {
                FileOutputStream out = null;
                try {
                    // Create an empty file
                    out = new FileOutputStream(noMedia);
                } catch (Exception e) {
                    // ignore
                } finally {
                    tryToClose(out);
                }
            }
        }


    }


