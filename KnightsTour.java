//Problem 3- Knight's Tour Problem
//Nishtha Nayar
//1610110235
//nn469


//This code deals with the GUI implementation of the Knight's problem.
//On running the code, there will be a frame in which the chess board loads after a few seconds
//The user can then click on any square of the chess board to see all 64 steps
//Once it's done, user can again select another square and repeat.

//When the user clicks on one square,the CSquare objects delivers an event to MyFrame conveying id of the CSquare.


import javax.swing.*;
import java.awt.*;
import java.applet.*;

public class Problem3 extends Applet {

    boolean inApplet = false;
    //initialise height and width of GUI screen
    int width = 280;
    int height = 180;

    public static void main(String argv[]){
        int i  = 0;
        int N = 250;
        if(argv.length > 0) {
            try {
                N = Integer.parseInt(argv[0]);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }


        Board board = null;
        //initialise board
        MyFrame fr= new MyFrame();
        fr.setLayout(new GridLayout(8,8));
        fr.myboard = board;
        //initialise all 64 squares
        CSquare sqr[] = new CSquare[64];
        for(i = 0; i < 64; i++) {
            sqr[i] = new CSquare();
            sqr[i].id = i;
            fr.add(sqr[i]);
        }
        fr.csq = sqr;
        fr.validate();
        fr.resize(200,200);
        fr.show();

    }

    MyFrame frame;
    Board board;

    //in
    public void  init(){
        inApplet = true;
        int i  = 0;
        int N = 0;
        setLayout(new BorderLayout());
        frame = new MyFrame();
        frame.setLayout(new GridLayout(8,8));
        frame.myboard = board;
        frame.inApplet = true;



        CSquare sqr[] = new CSquare[64];
        frame.csq = sqr;
        for(i = 0; i < 64; i++) {
            sqr[i] = new CSquare();
            sqr[i].id = i;
            frame.add(sqr[i]);
        }
        width = size().width; height = size().height;
        resize(width,height);
        frame.resize(200,200);
        board = null;
    }
    //set dimensions for the frame
    public Dimension minimumSize(){
        return new Dimension(width,height);
    }
    public Dimension preferredSize(){
        return minimumSize();
    }


}
class MyFrame extends Frame  {
    Board myboard;
    CSquare csq[];
    boolean inApplet = false;
    int interval = 250;
    Button show,hide;
    public MyFrame(){
        super("Knight's Tour Problem");

    }

    //Event handler for the given frame to make sure all the squares in our board are correctly timed and outputted
    public boolean handleEvent(Event event) {
        if((event.id >= 5000)&&(event.id < 5064)){

            if((myboard != null)&& myboard.running)  {
                csq = myboard.csquare;
                myboard.stop();
                myboard = null;
                return true;
            }
            if((myboard != null) && !myboard.running) myboard = null;
            if(myboard == null) {
                myboard = new Board();
                myboard.csquare = csq;
                myboard.stsq = event.id-5000+1;
                myboard.delayInterval = interval;
                myboard.start();
            }

        }
        if(!inApplet && (event.id == Event.WINDOW_DESTROY)) {
            dispose();
            System.exit(0); // necessary we should go to DOS prompt
            return super.handleEvent(event);
        }
        if(inApplet && (event.id == Event.WINDOW_DESTROY)) {
            hide();
            show.enable();
            hide.disable();
            return true ;
        }

        return super.handleEvent(event);
    }
}

//CSquare handles visual aspects of the Square.
class CSquare extends Canvas{
    int height,width;
    int id;
    int num = -1;
    int value = -1;

    CSquare(){
        height = 18;
        width = 18;
    }

    //Format the visuals of the chess board
    synchronized public void Visuals(Graphics gp){
        width = size().width;height = size().height;

        int rows =  id/8;
        int columns = (id%8);

        //set the black and white color for the board
        if(value == -1){
            if( ((rows+columns)%2) == 0) gp.setColor(Color.WHITE);
            else
                gp.setColor(Color.gray);
        }
        else gp.setColor(Color.pink);
        gp.fillRect(0,0,width,height);
        gp.setColor(Color.black);
        gp.drawRect(1,1,width-1,height-1);

        String s ;
        if(num != -1){
            s = num+"";
            int len = gp.getFontMetrics().stringWidth(s);
            int ht = gp.getFontMetrics().getHeight();
            if( ((rows+columns)%2) == 0) gp.setColor(Color.black);
            else
                gp.setColor(Color.white);
            gp.drawString(s,(width-len)/2,(height+ht)/2);
        }

    }
    public void updateGraphics(Graphics gp){

        Visuals(gp);
    }

    synchronized public boolean handleEvent(Event e) {
        if(e.id == Event.MOUSE_UP){
//		  System.out.println("up");
            getParent().deliverEvent(new Event(getParent(),5000+id,null));
            return true;
        }
        return super.handleEvent(e);
    }
    public Dimension minimumSize() {
        return new Dimension(width,height);
    }
    public Dimension preferredSize() {
        return minimumSize();
    }


}



//Talks about the configuration and the valid steps of the knight on the chess boards
class Square {

    //initialise the chess squares on which knight moves
    int id;
    Board b = null;
    boolean visited = false;
    Square(int n,Board b){ id =n; this.b = b;}
    int rownum(){ return (id/8) + 1;}
    int colnum(){ return (id%8) + 1;}
    int getTheId(int r, int c) { return (r-1)*8+c-1;}

    // returns  legal moves from this square
    int[] nextconfig(){
        int p[] = new int[8];
        int m = 0,ro,co;
        for(int i=0; i < 8;i++){
            ro = this.rownum(); co = this.colnum();
            //check for all rows and columns and gives moves for each configuration
            switch(i){

                case 0:  ro -= 2; co += 1; break;
                case 1:  ro -= 1; co += 2; break;
                case 2:  ro += 1; co += 2; break;
                case 3:   ro += 2; co += 1 ; break;
                case 4:  ro += 2 ; co -= 1; break;
                case 5:  ro += 1; co -= 2; break;
                case 6:  ro -= 1; co -= 2; break;
                case 7:  ro -= 2; co -= 1; break;
            }
            if(( ro < 1) || ( ro > 8) || ( co < 1) || (co > 8)) continue; //keep loop going if haven't reached end of board
            p[m] = getTheId(ro,co);
            m++;
        }
        int t[]= new int[m];
        System.arraycopy(p,0,t,0,m);
        return t;
    }
    // this will return the number of moves from the next jump.
    // and takes into account of visited squares
    int escapestoNxt(int omit){
        int nxt[];
        nxt = nextconfig();
        int e=0;
        for(int i = 0; i < nxt.length; i++){
            if(b.sq[nxt[i]].visited || (nxt[i]== omit)) continue;
            e++;
        }
        return e;
    }
    // this is the heuristics function for the problem

    // what is the best jump from the current square:
    // -1 if none. One may get trapped into a hole and can not
    // come out since all possible escape squares are already visited
    int bestExit(){
        int nxt[];
        nxt = nextconfig();
        int k = 8;
        int idx = -1;
        int e = 0;
        for(int i = 0; i < nxt.length; i++) {
            if(b.sq[nxt[i]].visited) continue;
            e = b.sq[nxt[i]].escapestoNxt(nxt[i]);
            if((e > 0) && ( e < k)) { k = e; idx = i;}
        }
        if(idx == -1) return idx;
        else
            return nxt[idx];
    }
}

//initialise chess board
class Board  extends Thread{
    Square sq[];
    CSquare csquare[];
    boolean running = false;  // this is set true while the knight is moving
    int delayInterval = 700;
    int stsq = 0;
    Board(){
        sq = new Square[64];
        for(int i = 0; i < 64; i++) sq[i] = new Square(i,this);
    }


    // Engine
    public void run(){
        running = true;
        showPathonBoard(stsq);
        running = false;
        //stop();
    }
    // Visually show the knights moves on the chess board and all the squares that are visited
    void showPathonBoard(int startSquare){
        int i  = 0;
        int path[];
        path = new int[64];
        //first initialise each square to not visited
        for(i = 0; i < 64; i++){
            sq[i].visited = false;
            csquare[i].num = -1;
            csquare[i].value = -1;
            csquare[i].updateGraphics(csquare[i].getGraphics());
        }
        sq[startSquare-1].visited = true;
        int nxt[];
        int n = -1;
        int moves = 0;
        int curSquare = startSquare-1;
        path[curSquare] = moves+1;
        csquare[curSquare].num = moves+1;
        csquare[curSquare].value = 1;
        // show the step in the GUI
        //current square is updated
        csquare[curSquare].updateGraphics(csquare[curSquare].getGraphics());
        for( i = 0; i < 64; i++) {
            try {
                sleep(delayInterval);
            } catch(InterruptedException e){
            }
            n = sq[curSquare].bestExit();
            if( n < 0 ) break;
            sq[n].visited = true;
            csquare[curSquare].value = -1;
            csquare[curSquare].updateGraphics(csquare[curSquare].getGraphics());
            moves++;
            curSquare = n;
            // remember the path, current sqaure is updated while showing the taken path
            path[curSquare] = moves+1;
            csquare[curSquare].num = moves+1;
            csquare[curSquare].value = 1;
            csquare[curSquare].updateGraphics(csquare[curSquare].getGraphics());
        }
        // Find out unvisited squares
        // Since the goodExit always looks one ahead, if every thing
        // as per the heuristic worked, when we arrive here there should be exactly one
        // unvisited square and it should be reachable from the last visited square
        // square

        int numberUnvisited = 0;
        int notvisited = -1;
        for(i = 0; i < 64; i++){
            if(!sq[i].visited){
                notvisited = i;
                numberUnvisited++;
            }
        }
        if(numberUnvisited == 1) {
            nxt = sq[curSquare].nextconfig();
            for(i = 0;i < nxt.length; i++) {
                if(nxt[i] == notvisited) {
                    csquare[curSquare].value = -1;
                    csquare[curSquare].updateGraphics(csquare[curSquare].getGraphics());
                    path[notvisited]=moves+2;
                    csquare[notvisited].num = moves+2;
                    csquare[notvisited].repaint();
                    break;
                }
            }
        } else {//if no solution
            return;
        }
        return ;
    }

}



