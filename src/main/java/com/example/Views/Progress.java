package com.example.Views;

import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

public class Progress implements CopyStreamListener {

    public int totalBytes;

    public Progress(int totalBytesTransferred) {
        this.totalBytes = totalBytesTransferred;
    }

    @Override
    public void bytesTransferred(CopyStreamEvent arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bytesTransferred'");
    }

    @Override
    public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {

            MainUI.getInstance().addProgressBar();
            double byteTransfering = 0;
            if (totalBytes > 0) {
                byteTransfering = ((double) totalBytesTransferred / (double) totalBytes) * 100.0;
            }
            MainUI.getInstance().progressBar.updateProgress((int) byteTransfering);

    }

}
