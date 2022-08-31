import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NBody extends Canvas implements ActionListener
{
    public int size;
    public double maxVel;
    public double maxMass;
    public int red;
    public int green;
    public int blue;
    public double[] xCoords;
    public double[] yCoords;
    public int[] gravity;
    public double[] mass;
    public double[] xVel;
    public double[] yVel;
    public Color[] colors;
    public int[] sizes;
    public int n;
    public double dt;

    public void init(int n){
    	this.n = n;
    	this.xCoords = new double[n];
        this.yCoords = new double[n];
        this.gravity = new int[n];
        this.mass = new double[n];
        this.xVel = new double[n];
        this.yVel = new double[n];
        this.colors = new Color[n];
        this.sizes = new int[n];
        this.dt = 0.1;
        for(int i = 0; i < n; i++){
            xCoords[i] = (Math.random() * 800);
            yCoords[i] = (Math.random() * 800);
            mass[i] = Math.random() * 10;
            sizes[i] = (int) (Math.random() * 10);
            xVel[i] = Math.random() * 10;
            yVel[i] = Math.random() * 10;
            int red = (int)(Math.random() * 128 + 128);
        	int green = (int)(Math.random() * 128 + 128);
        	int blue = (int)(Math.random() * 128 + 128);
        colors[i] = new Color(red, green, blue);
        }
    }


    public void getNextVelocities(){
        for(int i = 0; i < xCoords.length; i++){
            double xTempVel = 0;
            double yTempVel = 0;
            for(int j = 0; j < xCoords.length; j++){
                if(j != i){
                    double xDist = xCoords[j] - xCoords[i];
                    double yDist = yCoords[j] - yCoords[i];
                    double angle = angle(xDist, yDist);
                    double force = gravity(mass[i], mass[j], hypotenuse(xDist, yDist));
                    double xForce = force * Math.cos(angle);
                    double yForce = force * Math.sin(angle);
                    xTempVel += xForce / mass[i] * dt * (xDist > 0 ? 1 : -1);
                    yTempVel += yForce / mass[i] * dt * (yDist > 0 ? 1 : -1);
                }
            }
            xVel[i] += xTempVel;
            yVel[i] += yTempVel;
        }
    }


    public void getNewCoords(){
        this.getNextVelocities();
        for(int i = 0; i < xCoords.length; i++){
            xCoords[i] += xVel[i] * dt;
            yCoords[i] += yVel[i] * dt;
        }
    }
    public double hypotenuse(double xDist, double yDist){
    	double val = Math.sqrt((Math.pow(xDist, 2) + Math.pow(yDist, 2))); 
        return val < 5 ? 5 : val;
    }

    public double angle(double xDist, double yDist){
        return Math.atan(yDist/xDist);
    }

    public double gravity(double m1, double m2, double r){
        return 100 * m1 * m2 / Math.pow(r, 2);
    }

    

    // Draw a circle centered at (x, y) with radius r
    public void drawCircle(Graphics g, int x, int y, int r)
    {
        int d = 2*r;
        g.fillOval(x - r, y - r, d, d);
    }

    public void paint(Graphics g)
    {
        // Clear the screen
        super.paint(g);
        //Your drawing code here:
        for(int i = 0; i < n; i++)
        {
            g.setColor(colors[i]);
            drawCircle(g,(int) xCoords[i], (int) yCoords[i], sizes[i]);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        // Your update code here:
        getNewCoords();
        // Repaint the screen
        repaint();
        Toolkit.getDefaultToolkit().sync();
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int n = Integer.parseInt(args[0]);
        NBody nbody = new NBody();
        nbody.setBackground(Color.BLACK);
        nbody.size = 800;
        nbody.maxVel = 10;
        nbody.maxMass = 10;
        nbody.dt = 0.1;
        nbody.setPreferredSize(new Dimension(nbody.size, nbody.size));
        nbody.init(n);
        frame.add(nbody);
        frame.pack();
        Timer timer = new Timer(16, nbody);
        timer.start();
        frame.setVisible(true);
    }
}