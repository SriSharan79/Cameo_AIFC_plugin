package dataloggers;

import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import com.nomagic.magicdraw.ui.notification.Notification;
import com.nomagic.magicdraw.ui.notification.NotificationManager;

/**
 * @author kata_du
 * @version 1.2.0
 * @date 2025-12-18
 */
public class BackgroundRunner {

    ModelCallLogger logger = new ModelCallLogger(this); 

    public void Notifyinbackground(String ID, String Prompt) {
        SwingUtilities.invokeLater(() -> {
            NotificationManager.getInstance().showNotification(new Notification(ID, Prompt, null));
        });
    }
    
    public void DoInbackground(Runnable task) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                task.run();
                return null;
            }
        };
        worker.execute();
    }
    
    /**
     * Phase 1: Logs the request before the LLM is called. 
     * Executed synchronously to return the rowIndex immediately.
     */
    public int logRequestSent(int currentTry, String modelName2, String sys_Msg, String prompt) throws IOException {
        return logger.logRequestSent(currentTry, modelName2, sys_Msg, prompt);
    }

    /**
     * Phase 2: Updates the Excel file with the response on a background thread.
     */
    public void logResponseReceived(int rowIndex, String result, String response) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                logger.logResponseReceived(rowIndex, result, response);
                return null;
            }
        };
        worker.execute();
    }
}