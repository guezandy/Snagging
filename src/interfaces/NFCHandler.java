package interfaces;

import android.content.Intent;

public interface NFCHandler {

    /**
     * Write to an NFC tag; reacting to an intent generated from foreground
     * dispatch requesting a write
     * 
     * @param intent
     */
    public void processWriteIntent(Intent intent);
    
    /**
     * Read from an NFC tag; reacting to an intent generated from foreground
     * dispatch requesting a read
     * 
     * @param intent
     * @return content of nfctag (string)
     */
    public String processReadIntent(Intent intent);
    
    /**
     * Enable this activity to write to a tag
     * 
     * @param isWriteReady
     */
    public void setTagWriteReady(boolean isWriteReady);
    
    
}
