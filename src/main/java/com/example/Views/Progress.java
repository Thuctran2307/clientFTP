package com.example.Views;

import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

public class Progress implements CopyStreamListener {

    int timeUpdate = 0;
    private long time;
    public int totalBytes;
    public double speedTransfer;
    public Progress(int totalBytesTransferred) {
        time = System.currentTimeMillis()/1000;
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

                long currentTime = System.currentTimeMillis()/1000;
                long timeTransfer = currentTime - time;
                timeUpdate += 1;
                speedTransfer = (double)totalBytesTransferred / ((double)(timeTransfer)*1024);
                System.out.println( totalBytesTransferred + "---" + timeTransfer);
                if(timeUpdate > 200){
                    MainUI.getInstance().progressBar.lbProgress.setText("Speed: " + speedTransfer + " KB/s");
                    timeUpdate = 0;
                }
            }
            MainUI.getInstance().progressBar.updateProgress((int) byteTransfering);

    }

}
