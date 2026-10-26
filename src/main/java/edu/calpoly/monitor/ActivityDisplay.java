// TASK #75: Create Java Swing ActivityDisplay
package edu.calpoly.monitor;
import javax.swing.JFrame; // we need the window
import javax.swing.JPanel; // we need the container 
import javax.swing.JLabel; // we need make it visible
import java.awt.GridLayout; // we also need to place some layout using libraries
import edu.calpoly.provided.Blackboard;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;

/**
 * DisplayDataActivity -> Must receives the messages adn also has the tracker included
 * ActivityDisplay must owns the swing window and also the count labels, in order to display them correctly
 * Example: Show ROBOT: 0 Messages, Last 60s
 * Therefore we will use JFrame Library, JPanel Library, JLabel,to display (GUI) 
 * 
 */



 // we do not need to create the main Constructor bc its gonna be a helper class


 /**
  *  * Observes supported Blackboard activity-state changes and schedules
 * system-state label updates on the Swing Event Dispatch Thread.
  * ActivityDisplay creates and manages the Swing window 
  * that shows the current 60-second message counts for 
  * - Robot, 
  * -Gaze, 
  * -Affect, and 
  * -LiDAR, and 
  * updates those labels when new counts are provided.
  * @author Diego Martinez Parra (sp4msvwnz)
  * @version version 1.0 (2026-09-23)
  * ActivityDisplay
 
  */

 
public class ActivityDisplay implements PropertyChangeListener {
    private final JFrame frame;
    private boolean observing;
    private long subscriptionVersion;
    
    // four label fields (objects)
    private final JLabel robotCountLabel = new JLabel("Robot: 0 Messages"); // ROBOT
    
    private final JLabel gazeCountLabel = new JLabel("Gaze: 0 Messages"); // GAZE

    private final JLabel affectCountLabel = new JLabel("Affect: 0 Messages"); // AFFECT

    private final JLabel lidarCountLabel = new JLabel("LiDAR: 0 Messages"); // LIDAR

    private final JLabel systemStateLabel = new JLabel("System State: NO_ACTIVITY");

    // EXTRA

    private final JLabel timeWindowLabel = new JLabel("Last 60 Seconds"); // to display more UI


    // CONSTRUCTOR
    public ActivityDisplay() {this(true);} // default constructor, calls the other constructor with true

    public ActivityDisplay(boolean showWindow) {
    
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(timeWindowLabel);
        panel.add(robotCountLabel);
        panel.add(gazeCountLabel);
        panel.add(affectCountLabel);
        panel.add(lidarCountLabel);
        panel.add(systemStateLabel);
        
        if(showWindow) {
            frame = new JFrame("Activity Display");
            frame.addWindowListener(new ActivityWindowListener(this));
            frame.add(panel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // center the window on the screen
            frame.setVisible(true);
        } else {
            frame = null;
        }

        
    }
    // SECOND CONSTRUCTOR
    /**
     * Stops observation and schedules disposal of the display window.
     */
    public void dispose() {
        unsubscribeFromActivity();

        if (frame != null) {
            SwingUtilities.invokeLater(() -> frame.dispose());
        }
        
    }

    // FOR #76 I NEED TO ADD A METHOD THAT WILL BE PUBLIC TO COUNT OR UPDATE COUNTS
    public void updateCounts(int robotCount, int gazeCount, int affectCount, int lidarCount) {
        robotCountLabel.setText("Robot: " + robotCount + " messages");
        gazeCountLabel.setText("Gaze: " + gazeCount + " messages");
        affectCountLabel.setText("Affect: " + affectCount + " messages");
        lidarCountLabel.setText("LiDAR: " + lidarCount + " messages");
        
    }

    public void updateSystemState(String state) {
     systemStateLabel.setText("System State: " + state);
    }

        /**
     * Starts observing Blackboard activity-state changes.
     * Repeated calls do not register duplicate listeners.
     */
    public synchronized void subscribeToActivity() {
        if (observing) {
            return;
        }

        subscriptionVersion++;
        observing = true;
        Blackboard.subscribe(Blackboard.ACTIVITY_STATE, this);
    }

    /**
     * Stops observing activity changes and invalidates queued updates.
     */
    public synchronized void unsubscribeFromActivity() {
        observing = false;
        subscriptionVersion++;
        Blackboard.unsubscribe(Blackboard.ACTIVITY_STATE, this);
    }

   public String getSystemStateText() {
        return systemStateLabel.getText();
    }
    public String[] getCountTexts() {
        return new String[] {
            robotCountLabel.getText(),
            gazeCountLabel.getText(),
            affectCountLabel.getText(),
            lidarCountLabel.getText()
        };
    }
     
    @Override
    public synchronized void propertyChange(PropertyChangeEvent event) {
        if (!observing) {
            return;
        }

        if (!Blackboard.ACTIVITY_STATE.equals(event.getPropertyName())) {
            return;
        }

        if (!(event.getNewValue() instanceof String state)) {
            return;
        }

        boolean supported = switch (state) {
            case "IDLE", "ACTIVE", "NO_ACTIVITY",
                 "NORMAL", "HIGH_ACTIVITY" -> true;
            default -> false;
        };

        if (!supported) {
            return;
        }

                long eventVersion = subscriptionVersion;

        SwingUtilities.invokeLater(() -> {
            synchronized (ActivityDisplay.this) {
                if (observing && subscriptionVersion == eventVersion) {
                    updateSystemState(state);
                }
            }
        });

    }
}