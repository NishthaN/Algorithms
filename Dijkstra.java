

//Problem 4- Dijkstra Algorithm
//Nishtha Nayar
//1610110235
//nn469

//In this code the GUI implementation of Dijkstra Algorithm is written.

// These are the following options in the GUI:
//1.Tap on Create New Vertex to create New vertex.
//2. A vertex can be dragged and dropped any where on the screen.
//3.Every vertex is created one above the other each time you click new vertex on the same place in the left corner.
//4.To draw the edges, 'Add Edge' button is first selected and 2 vertices are clicked on to add edge between them. This also adds weights by using distance formula
//More and more edges can be added till the radio button is on.
//5.To find the path turn on find path radio button (make sure that u turn off the lines radio button before finding the shortest path).
// While turning on the radiobutton click on the two vertices to find the shortest path between those vertices.

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import javax.swing.*;

public class Problem4 extends JFrame{

    //Initialise the problem
    Problem4()
    {
        super("Shortest Path using Dijkstra Algorithm");
        add(new ProblemGraphics());
        setSize(1000,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    //main function
    public static void main(String []args)
    {
        EventQueue.invokeLater(new Runnable(){
            public void run() {
                Problem4 shortest = new Problem4();
                shortest.setVisible(true);
            }
        });
    }
}

//Describe thw program graphics
class ProblemGraphics extends JPanel
{

    //Create the buttons, rectangles for the vertices and the radio buttons, text files
    JButton NRectangle;
    Rectangle2D.Float F1;
    Rectangle2D.Float F2;
    JButton genPath;
    JTextField tx1, tx2,tx3,tx4;
    int rectangles=0;
    boolean bool;
    int s ,s1,s2;
    JRadioButton genEdge,selVer;
    boolean findpath;
    Lineobj l;
    //initialise the Arraylist for the path and all the vertices
    ArrayList<Rectangle2D.Float> rect;
    ArrayList<Integer> PathObtained;

    //ArrayList<Line2D.Float> Lines;
    ArrayList<Lineobj> Lines;
    int[][] countmatrix;

    int i;
    ProblemGraphics(){
        i=10;
        //Describe all the buttons and the instructions for running the algorithm
        NRectangle = new JButton("Create New Vertex");
        genPath = new JButton("Show New Shortest Path");
        genEdge =  new JRadioButton("Add Edge");
        selVer = new JRadioButton("Select Vertices");
        tx1 = new JTextField("1) Create New Vertices and drag and drop them around\n ");
        tx3 = new JTextField("3)Turn 'Select Vertices' on select starting and end node ");
        tx2 = new JTextField("2)Turn 'Add Edge' on and click two vertices to add an edge between \n");
        tx4 = new JTextField("4)Click 'Show Shortest Path'");
        tx1.setEditable(false);
        tx2.setEditable(false);
        tx3.setEditable(false);
        tx4.setEditable(false);

        countmatrix = new int[50][50];
        for(int i=0;i<20;i++)
        {
            for(int j= 0;j<20;j++)
            {
                countmatrix[i][j] = 0;
                countmatrix[i][i] = 0;
            }
        }

        //All the event listeners and actions performed on the vertices and the lines
        genEdge.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub
                if(e.getStateChange()==1)
                {
                    bool=true;
                }
                else{
                    bool=false;
                }
            }
        });
        selVer.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub
                if(e.getStateChange()==1)
                {
                    findpath=true;

                }
                else{
                    findpath=false;

                    F1=F2=null;
                    PathObtained = new ArrayList<Integer>();
                    repaint();
                }

            }

        });

        rect = new ArrayList<Rectangle2D.Float>();
        Lines = new ArrayList<Lineobj>();
        NRectangle.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                rect.add(new Rectangle2D.Float(i, i, 30, 30));
                repaint();
            }
        });

        genPath.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                genMat();
            }
        });

        //creating the two panels for instructions and the buttons

        JPanel panel1 = new JPanel(new GridLayout(2,2));
        JPanel panel2 = new JPanel(new GridLayout(1,1));
        panel1.add(tx1);
        panel1.add(tx2);
        panel1.add(tx3);
        panel1.add(tx4);
        add(panel1, BorderLayout.NORTH);
        panel2.add(NRectangle);
        panel2.add(genEdge);
        panel2.add(genPath);
        panel2.add(selVer);
        add(panel2, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter(){
            int i = 0;
            public void mousePressed(MouseEvent event) {

                //Mouse pressed events.
                //If the path is found between the selected vertices
                if(findpath)
                {
                    if(s==0)
                    {
                        for(int i=0;i<rect.size();i++)
                        {
                            Point p = event.getPoint();

                            Rectangle2D on = (Rectangle2D.Float)rect.get(i);
                            if(on.contains(p)){
                                s1=i;
                                F1=(Float) on;
                                s++;
                                repaint();

                            }

                        }
                    }

                    else if(s==1)
                    {
                        for(int i=0;i<rect.size();i++)
                        {
                            Point p = event.getPoint();

                            Rectangle2D on = (Rectangle2D.Float)rect.get(i);
                            if(on.contains(p)){
                                s2=i;
                                F2=(Float) on;
                                s = 0;

                                repaint();
                            }
                        }
                    }
                }

                if(rectangles==0 && bool)
                {
                    for(int i=0;i<rect.size();i++)
                    {
                        Point p = event.getPoint();

                        Rectangle2D on = (Rectangle2D.Float)rect.get(i);
                        if(on.contains(p)){

                            l = new Lineobj();

                            l.one=(Float)on;
                            rectangles++;
                            break;
                        }
                    }
                }
                else if(rectangles==1 && bool)
                {
                    for(int i=0;i<rect.size();i++)
                    {
                        Point p = event.getPoint();
                        Rectangle2D on = (Rectangle2D.Float)rect.get(i);
                        if(on.contains(p)){
                            l.two=(Float) on;

                            rectangles++;
                            break;
                        }
                    }
                    //System.out.println("three");
                    int x1=(int) l.one.getX();
                    int x2=(int) l.two.getX();
                    int y1=(int) l.one.getY();
                    int y2=(int) l.two.getY();
                    rectangles=0;
                    if((int)(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2))) != 0)
                        Lines.add(l);
                    repaint();
                }
            }
            public void mouseReleased(MouseEvent event) {
            }
            public void mouseDragged(MouseEvent event) {
            }

        });
        addMouseMotionListener(new MouseAdapter(){
            int i = 0;
            Rectangle2D.Float temp;
            public void mousePressed(MouseEvent event) {
            }
            public void mouseReleased(MouseEvent event) {
            }
            public void mouseDragged(MouseEvent event) {
                for(int i=0;i<rect.size();i++)
                {
                    Point p = event.getPoint();
                    temp = (Rectangle2D.Float)rect.get(i);
                    if(temp.contains(p)){
                        temp.x= (float) p.getX()-15;
                        temp.y=(float)p.getY()-15;
                        break;
                    }
                }
                repaint();
            }
        });
    }
    public void AddDrawing(Graphics g){

        //add following graphics:
        //color of the vertices, the edges and the shortest path

        Graphics2D g2 = (Graphics2D)g;
        Integer car =0 ;

        for(int i=0;i<Lines.size();i++)
        {
            int x1=(int) Lines.get(i).one.getX();
            int x2=(int) Lines.get(i).two.getX();
            int y1=(int) Lines.get(i).one.getY();
            int y2=(int) Lines.get(i).two.getY();

            g2.setColor(Color.pink);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Line2D.Double(x1+15,y1+15, x2+15, y2+15));
            Integer in = new Integer((int) Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
            g2.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, 12));
            g2.drawString(in.toString(), (x1+x2)/2, (y1+y2)/2);
            g2.setColor(Color.red);
        }
        if(findpath && PathObtained != null && F1 !=null){
            for(int i = 0;i<PathObtained.size()-1;i++)
            { //for the paths obtained, add a green line for all the vertices and edges which are in the path
                int x1 = (int) (rect.get(PathObtained.get(i)).getX()+15);
                int y1 = (int) (rect.get(PathObtained.get(i)).getY()+15);
                int x2 = (int) (rect.get(PathObtained.get(i+1)).getX()+15);
                int y2 = (int) (rect.get(PathObtained.get(i+1)).getY()+15);
                g2.setColor(Color.green);
                g2.setStroke(new BasicStroke(3));
                g2.draw(new Line2D.Double(x1, y1, x2, y2));
            }
        }
        for(int i=0;i<rect.size();i++)
        {
            if(rect.get(i)==F1 || rect.get(i)==F2)
            {
                g2.setColor(Color.blue);

            }
            else{
                g2.setColor(Color.yellow);
            }
            g2.fill((Rectangle2D.Float)rect.get(i));
            g2.setColor(Color.red);
            g2.drawString(car.toString(), (int)rect.get(i).getX()+12, (int)rect.get(i).getY()+19);
            car++;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        AddDrawing(g);
    }

    //generates the adjacency matrix for the Dijkstra algorithm and keeps track of the different weights of all the edges between the vertices
    public void genMat()
    {
        for(int i=0;i<Lines.size();i++)
        {
            int a=0;
            int b=0;
            for(int j=0;j<rect.size();j++)
            {
                if(Lines.get(i).one==rect.get(j))
                {
                    a=j;
                }
                if(Lines.get(i).two==rect.get(j))
                {
                    b=j;
                }
            }

            double x1=rect.get(a).getX();
            double y1 = rect.get(a).getY();
            double x2=rect.get(b).getX();
            double y2 = rect.get(b).getY();

            //add the weight between the edges by using mathematical distance formula between the two vertices
            countmatrix[a][b] = (int) Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
            countmatrix[b][a]= (int) Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
        }

        //finds the path
        Algo  findP  = new Algo ();
        PathObtained = findP.path(countmatrix,s1,s2,rect.size());
        repaint();

    }
}

//Line object
class Lineobj
{
    Rectangle2D.Float one;
    Rectangle2D.Float two;
}
class Algo {

    public static ArrayList<Integer> path(int[][] costmat,int start, int end, int n)
    {

        //Algorithm for the Dijkstra solution
        int[][] back = new int[n][n];
        ArrayList<Integer> res = new ArrayList<Integer>();
        Stack<Integer> vio = new Stack();
        int[] pq = new int[n];
        boolean [] visited = new boolean[n];
        int found = 0; //if final vertice is hit

        for(int i=0;i<n;i++)
        {
            //initialise the start
            if(i==start)
            {
                pq[i]=0;
            }
            else{
                pq[i]=Integer.MAX_VALUE;
            }
        }

        back[0]=pq.clone();

        for(int i=0;i<n;i++)
        {

            //start with the first node then keep going through the edges
            int sv = min(pq,visited);
            if(sv==end)
            { //if end is reached, break
                found = i;
                vio.add(sv);
                break;
            }
            else{

                //set each node to visited once it's visited and add to stack of visited vertices
                visited[sv] = true;
                vio.add(sv);
            }
            for(int j=0;j<n;j++)
            {
                //if not visited and if the weight to get to node is less, add it
                if(visited[j]==false && costmat[sv][j] != 0)
                {
                    pq[j] = Math.min(pq[j], costmat[sv][j]+pq[sv]);
                }
            }
            back[i+1] = pq.clone();
        }
        int track = found;
        int temp = vio.pop();
        res.add(temp);
        while(!vio.isEmpty())
        {
            if(found==0)
            {
                break;
            }
            //once end is found, pop out all the vertices in stack to give the exact path from start to end to the user
            if(back[found][end] == back[found-1][end] )
            {
                vio.pop();
            }
            else
            {
                end=vio.pop();
                res.add(end);
            }
            found--;
        }
        return res;
    }

    //keep check of the minimum cost it is taking from the start to all the vertices which are being visited.
    //helps with the above function to keep track of minimum path and update it as a new minimum is found.
    static int min(int[] array,boolean []visited)
    {   int min = 0;
        for(int i=0;i<array.length;i++)
        {
            if(visited[i] !=  true)
            {
                min = i;
            }
        }
        for(int i=0;i<array.length;i++)
        {
            if(array[i]<array[min] && visited[i] !=  true)
            {
                min = i;
            }
        }
        return min;
    }
}