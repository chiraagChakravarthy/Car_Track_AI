package car_track.engine;

import javax.swing.*;
import java.awt.*;

public class Window {
    //Window was a custom made class that adds more functionality to a basic JFrame. Specifically the ability to easily create a fullscreen
    //engine.
    private int fsm = 0;
    private JFrame frame;
    //RunnableWindow engine the rest of the functionality runs on
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public Window(String title, int fsm, RunnableWindow runnableWindow) {
        //This constructor takes advantage of the fullscreen functionality of the class
        this.fsm = fsm;
        frame = new JFrame(title);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(runnableWindow);
        setFullScreen();
    }

    public Window(String title, int width, int height, RunnableWindow runnableWindow) {
        //Allows for a custom sized engine.
        frame = new JFrame(title);
        frame.setTitle(title);
        /////////////////////////////////////////////////
        frame.setMinimumSize(new Dimension(width, height));
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        /////////////////////////////////////////////////
        //Makes the engine unable to be drag-resized by the user
        frame.setLocationRelativeTo(null);
        //Sets engine in the middle of screen
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(runnableWindow);
        //Adds the circle_fighter.runnableWindow to the engine, allowing whatever being drawn on RunnableWindow to reflect onto the it
        frame.setVisible(true);
    }

    private void setFullScreen() {
        switch (fsm) {
            case 1:
                frame.dispose();
                frame.setSize(new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight()));
                frame.setResizable(true);
                frame.setUndecorated(false);
                frame.setVisible(true);
                break;
            //Creates a resizable non-fullScreen engine the size of the display
            case 2:
                frame.dispose();
                frame.setMinimumSize(new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight()));
                frame.setPreferredSize(new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight()));
                frame.setMaximumSize(new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight()));
                frame.setUndecorated(true);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                break;
            //Creates a non-resizable engine which takes up the entire desktop
            case 3:
                frame.dispose();
                frame.setResizable(false);
                if (device.isFullScreenSupported())
                    device.setFullScreenWindow(frame);
                else
                    System.out.println("FullScreen mode is not supported.");
                break;

            default:
                System.out.println("FullScreen mode not supported.");
                setFullScreen(1);
                //Creates a engine which covers the entire display. Cannot be swiped away from. Creates best gaming experience.
        }
    }

    public void setFullScreen(int fsm) {
        this.fsm = fsm;
        setFullScreen();
    }


    public String getTitle() {
        return frame.getTitle();
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public void dispose() {
        frame.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }
}
