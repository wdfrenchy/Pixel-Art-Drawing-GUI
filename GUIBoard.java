import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;
import java.awt.Font;

public class GUIBoard {
    private JFrame frame;

    public GUIBoard() {
        frame = new JFrame("GUIBoard");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.add(new InnerBoard(frame.getSize()));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... argv) {
        new GUIBoard();
    }

    public static class InnerBoard extends JPanel implements Runnable, MouseListener  {


        private Thread animator;
        Dimension d;
        String str = "";
        int xPos = 0;
        int yPos = 0;

        // First the mario pixel art
        // String m = "000000111111111100000000000011111111111111111100000001100110011010100111110000000000110100110101010011111010100000011010011001101010100111110101000000110101010100111101111011110111100000000101010101010000000000011110101111010111100000011111101011110101111110011111111010010010010111111111010110100111001001001110010111010101010010010010010010010101010:y-2:b-7:c-9:d-18:w-33:g-37:r-38";
        // Now my pokéball pixel art
        String m = "111111111010101011111111111110100000101011111110000000001011111000000000101110000000000010100000101000001010100010111110001010101110101011111010101110111011111110101111111011111011111111111111111011111110101111111110101111111111111010101011111111:b-7:w-33:r-38";
        // String m = ":b-7:w-33:r-38";
        // white is 11, red is 0, black is 10

        String[] parts;

        String s2 = "";

        public InnerBoard (Dimension dimension) {
            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            addKeyListener(new TAdapter());
            setFocusable(true);
            d = getSize();
            parts = m.split(":");
            createNodes();

            //for animating the screen - you won't need to edit
            if (animator == null) {
                animator = new Thread(this);
                animator.start();
            }
            setDoubleBuffered(true);
        }

        public void createNodes(){
            ArrayList<Node> frequencyList = new ArrayList<>();
            PriorityQueue<Node> q = new PriorityQueue<Node> (parts.length-1, new MyComparator());

            for(int i = 1; i < parts.length; i++){
                String[] parts2 = parts[i].split("-");
                int t1 = Integer.parseInt(parts2[1]);
                Node temp = new Node(t1, parts2[0].charAt(0));
                q.add(temp);
            }
        
        while (q.size() > 1) {
            // Remove the two nodes with the smallest frequencies
            Node node1 = q.poll();
            Node node2 = q.poll();

            // Create a new node with these two nodes as children
            Node parentNode = new Node(node1.data + node2.data, '_');
            parentNode.left = node1;
            parentNode.right = node2;

            // Insert the new node back into the queue
            q.add(parentNode);
        }

        BinarySearchTree b = new BinarySearchTree();
        b.root = q.poll();

        Node examine = b.root;
        s2 = "";
        
        for (int i = 0; i < parts[0].length(); i++) {
            char currentChar = parts[0].charAt(i);
            int a = Character.getNumericValue(currentChar);
            System.out.println("a " + a);
            if (a == 0) {
                if (examine.left != null) {
                    //do something
                    examine = examine.left;
                     if (examine.right == null && examine.left == null) {
                        s2 += examine.c;
                   System.out.println("c " + examine.c);
                    examine = b.root;
                    }
                } 
             
           } //end of left
            
           if (a == 1) {
                if (examine.right != null) {
                    //do something
                    examine = examine.right;
                    if (examine.right == null && examine.left == null) {
                        s2 += examine.c;
                   System.out.println("c " + examine.c);
                    examine = b.root;
                    }
               } 
             
            }//end of right
        }//end of for loop
    }//end of function


        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.black);
            g2.fillRect(0, 0,(int)d.getWidth() , (int)d.getHeight());
            g2.setColor(Color.white);
            g2.fillRect(0, 0, d.width, d.height);

            Color brown = new Color(101,0,0);
            Color gold = new Color(255,215,0);
            g2.setColor(brown);
            g2.setColor(Color.yellow);



            // g2.fillRect(75, 75, 375, 50);


            int xx = 10;
            int yy = 10;
            int count = 0;

            for(int i = 0; i < 12; i++){
                for(int j = 0; j < 12; j++){
                    if (s2.charAt(count)=='w'){
                        g2.setColor(Color.white);
                    }
                    if (s2.charAt(count)=='r'){
                        g2.setColor(Color.red);
                    }
                    if (s2.charAt(count)=='b'){
                        g2.setColor(Color.black);
                    }
                    if (s2.charAt(count)=='c'){
                        g2.setColor(brown);
                    }
                    if (s2.charAt(count)=='g'){
                        g2.setColor(gold);
                    }
                    if (s2.charAt(count)=='d'){
                        g2.setColor(Color.blue);
                    }
                    if (s2.charAt(count)=='y'){
                        g2.setColor(Color.yellow);
                    }
                    //g2.setColor(Color.red);
                    count++;
                    g2.fillRect(xx, yy, 25, 25);
                    g2.setColor(Color.white);
                    g2.drawRect(xx, yy, 25, 25);
                    xx += 25;
                }
                xx = 10;
                yy += 25;
            }
        }



        public void mousePressed(MouseEvent e) {
            xPos = e.getX();
            yPos = e.getY();
            str = xPos + " " + yPos;


        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        private class TAdapter extends KeyAdapter {

            public void keyReleased(KeyEvent e) {
                int keyr = e.getKeyCode();

            }

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                // String c = KeyEvent.getKeyText(e.getKeyCode());
                //  c = Character.toString((char) key);




            }
        }//end of adapter

        public void run() {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 37;
            long time = System.currentTimeMillis();
            while (true) {// infinite loop
                // spriteManager.update();
                repaint();
                try {
                    time += animationDelay;
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    System.out.println(e);
                } // end catch
            } // end while loop
        }// end of run




    }//end of class
}
