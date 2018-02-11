package cg;

import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT event classes and listener interfaces
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;    // Using Swing's components and containers

/**
 * Custom Drawing Code Template
 */
// A Swing application extends javax.swing.JFrame
class CGTemplate extends JFrame {
    // Define constants
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;

    // Constructor to set up the GUI components and event handlers
    public CGTemplate() {
        canvas = new DrawCanvas();    // Construct the drawing canvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set the Drawing JPanel as the JFrame's content-pane
        Container cp = getContentPane();
        cp.add(canvas);
        // or "setContentPane(canvas);"

        setDefaultCloseOperation(EXIT_ON_CLOSE);   // Handle the CLOSE button
        pack();              // Either pack() the components; or setSize()
        setTitle("......");  // "super" JFrame sets the title
        setVisible(true);    // "super" JFrame show
    }

    /**
     * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
     */
    private class DrawCanvas extends JPanel {
        private Graphics g2;
        ArrayList<Integer> xs = new ArrayList<>();
        ArrayList<Integer> ys = new ArrayList<>();

        public DrawCanvas() {
            super();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    drawPoint(e.getX(), e.getY());
                    repaint();
                }
            });
        }

        private void drawPoint(int x, int y) {
            int r = 4;
            if (g2 != null) {
                xs.add(x);
                ys.add(y);
                g2.drawOval(x, y, x, y);
            }

        }

        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            g2 = g;
            setBackground(Color.BLACK);  // set background color for this JPanel


            for (int i = 0; i < xs.size(); i++) {
                g.drawOval(xs.get(i) - 3, ys.get(i) - 3, 5, 5);
            }
            int number = 1;
            try {
                if (xs.size() > 2) {
                    Point2D.Double[] minimum = RotatingCalipers.getMinimumBoundingRectangle(xs.stream().mapToInt(i -> i).toArray(), ys.stream().mapToInt(i -> i).toArray());
                    g.drawLine((int) minimum[0].x, (int) minimum[0].y, (int) minimum[1].x, (int) minimum[1].y);
                    g.drawLine((int) minimum[1].x, (int) minimum[1].y, (int) minimum[2].x, (int) minimum[2].y);
                    g.drawLine((int) minimum[2].x, (int) minimum[2].y, (int) minimum[3].x, (int) minimum[3].y);
                    g.drawLine((int) minimum[3].x, (int) minimum[3].y, (int) minimum[0].x, (int) minimum[0].y);
                    for (Point2D.Double corner : minimum) {
                        System.out.printf("corner[%d] (%.1f, %.1f)%n", number++, corner.x, corner.y);
                    }

                    System.out.printf("%narea: %.1f", RotatingCalipers.getArea(minimum));
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    // The entry main method
    public static void main(String[] args) {
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CGTemplate(); // Let the constructor do the job
            }
        });
    }
}