// TASK #73: IMPLEMENT MESSAGE RECEPTION AND CLASSIFICATION
/** 
 * RECIPE:
 *  * Receives and validates monitoring messages, records activity timestamps,
 * and periodically refreshes recent message counts.
 * Publishes activity classifications to Blackboard for subscribed observers.
 * Receives msg through the course-provided Broker Class
 * Then it would identify the data source represneted by each msg
 * Then expected message format will follow:
 *  TYPE, data...
 * ROBOT, GAZE, AFFECT, and LIDAR
*/

// SubTask #73(a): create the course-provided Broker Connection
package edu.calpoly.monitor;
import edu.calpoly.provided.Broker;
import javax.swing.Timer;
import edu.calpoly.provided.Blackboard;
import javax.swing.SwingUtilities;
import java.util.concurrent.FutureTask;

// SubTasl #73(b): Receive msg from the system




/**
  * DisplayDataActivity is the main program that receives and 
  * validates messages, 
  * sends each source type to ActivityTracker, and 
  * uses a repeating timer to update the ActivityDisplay GUI every second.
  * @author Diego Martinez Parra (sp4msvwnz)
  * @version version 1.0 (2026-09-23)
  * ActivityDisplay
  */


public class DisplayDataActivity {
    public static void main(String[] args) throws Exception {
        Broker broker = new Broker("localhost", 5000); // Creates a broker to received msg from the local services
        // NOW CONNECT TO THE ACTIVITYTRACKER
        ActivityTracker tracker = new ActivityTracker();

        // ADD THE BEHAVIOR OF ACTIVITY DISPLAY
            FutureTask<ActivityDisplay> createDisplay = new FutureTask<>(() -> {
            ActivityDisplay activityDisplay = new ActivityDisplay();
            activityDisplay.subscribeToActivity();
            return activityDisplay;
        });

        SwingUtilities.invokeAndWait(createDisplay);

        ActivityDisplay display = createDisplay.get();


        Timer refreshTimer = new Timer(
                1000,
                event -> refreshActivity(tracker, display));

        refreshTimer.start();
        
        

        while (true) {
            // wait for and store
            String msg = broker.receive();


            //SubTask #73(c): Handle invalid formats
            if (msg == null || msg.isBlank()) { // Check if the message is empty, blank, or
                System.out.println("Invalid Message"); // if it does invalid
                continue;
            } 

            int commaPos = msg.indexOf(","); // therefore, check if there is commas

            ////If not, then  find the first comma
            if (commaPos == -1) { // if not then, it would print invalid

                System.out.println("Invalid Format.");
                continue;

            } 

            String msgType = msg.substring(0, commaPos).trim(); // then extract the text with trim() before the comma

            if (msgType.isBlank()) {
                System.out.println("INVALID SOURCE TYPE.");
                continue;
            }

            // now call the ActivityTracker:
            tracker.recordTimestamp(msgType);
            System.out.println(msgType + " count: " + tracker.getRecentCount(msgType)); // TEMPORARY

            //

            // SubTask #73(d): Classify the message to each source
            switch (msgType) {
                case "ROBOT":
                    System.out.println("Classified as Robot");
                   
                    break;
                
                case "GAZE":
                    System.out.println("Classified as Gaze");

                    break;

                case "AFFECT":
                    System.out.println("Classified as Affect");

                    break;

                case "LIDAR":
                    System.out.println("Classified as LiDAR");

                    break;
            
                default:
                    System.out.println("Unknown Source Type: " + msgType);

                    break;
            }

            refreshActivity(tracker, display); // then call the refreshActivity method to update the display

            // then display the msg
            System.out.println("Received: " + msg); //concadination
            
        }



        
        
        

    }
    
    private static synchronized void refreshActivity(ActivityTracker tracker, ActivityDisplay display) {
        int robotCount = tracker.getRecentCount("ROBOT");
        int gazeCount = tracker.getRecentCount("GAZE");
        int affectCount = tracker.getRecentCount("AFFECT");
        int lidarCount = tracker.getRecentCount("LIDAR");

        int totalCount =
                robotCount + gazeCount + affectCount + lidarCount;

        double messageRate = totalCount / 60.0;
        double secondsSinceLastMessage =
                totalCount > 0 ? 0.0 : 60.0;

        String state = ActivityClassifier.classify(
                messageRate,
                secondsSinceLastMessage);
        
        SwingUtilities.invokeLater(() -> {display.updateCounts(
                    robotCount,
                    gazeCount,
                    affectCount,
                    lidarCount);
        });

        Blackboard.put(Blackboard.ACTIVITY_STATE, state);
    }

}










