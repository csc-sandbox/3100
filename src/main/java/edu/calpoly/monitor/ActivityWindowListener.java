package edu.calpoly.monitor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Removes the display's Blackboard subscription when its window closes.
 *
 * @author Diego Martinez Parra (sp4msvwnz)
 * @version 1.0 (2026-10-26)
 */
public class ActivityWindowListener extends WindowAdapter {
    private final ActivityDisplay display;

    public ActivityWindowListener(ActivityDisplay display) {
        this.display = display;
    }

    
    @Override
    public void windowClosing(WindowEvent event) {
        display.unsubscribeFromActivity();
    }

    @Override
    public void windowClosed(WindowEvent event) {
        display.unsubscribeFromActivity();
    }
}