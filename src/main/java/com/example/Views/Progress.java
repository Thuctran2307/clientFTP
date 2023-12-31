package com.example.Views;

import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

public class Progress implements CopyStreamListener {

    int timeUpdate = 1;
    private long time;
    public long totalBytes;
    public double speedTransfer;
    public Progress(long totalBytesTransferred) {
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
                if(timeTransfer == 0){
                    timeTransfer = 1;
                }
                speedTransfer = (double)totalBytesTransferred / ((double)(timeTransfer)*1024);
                System.out.println( totalBytesTransferred + "---" + timeTransfer);
                if(timeUpdate > 200){
                    MainUI.getInstance().progressBar.lbProgress.setText("Speed: " + speedTransfer + " KB/s");
                    timeUpdate = 1;
                }
            }
            MainUI.getInstance().progressBar.updateProgress((int) byteTransfering);

    }

}
