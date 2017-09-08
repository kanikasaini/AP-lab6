    //Kanika Saini 2016047 lab2
import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

class Coordinate
{
    private int x;
    private int y;
    public Coordinate(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    public void setC(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public void setX(int x)
    {
        this.x=x;
    }
    public void setY(int y)
    {
        this.y=y;
    }
}
class Knight implements Comparable<Knight>
{
    private String name;
    private Coordinate position;
    private boolean removed;
    private int m;
    private Stack<Object> box;
    private static Queen queen;
    public Knight(String name,int x, int y, int m, int qx, int qy)
    {
        box= new Stack<Object>();
        this.name =name;
        position = new Coordinate(x, y);
        this.m=m;
        removed= false;
        queen= new Queen(qx, qy);
    }
    public void setBool(boolean f)
    {
        this.removed= f;
    }
    public boolean getBool()
    {
        return removed;
    }
    public String getName()
    {
        return this.name;
    }
    public int getX()
    {
        return position.getX();
    }
    public int getY()
    {
        return position.getY();
    }
    public void insert(Object o)
    {
        box.push(o);
    }
    public int compareTo(Knight b)
    {
        return name.compareTo(b.name);
    }
    public void setPosition(int x, int y)
    {
        position.setY(y);
        position.setX(x);
    }
    public Coordinate pop(PrintWriter w) throws Exception
    {
        //PrintWriter w = new PrintWriter( "output.txt", "UTF-8");
        try
        {
            Object p= box.pop();
            try
            {
                if(p instanceof Coordinate)
                {
                    Coordinate ob = (Coordinate)p;
                    return ob;
                }   
                else
                {
                    throw(new NonCoordinateException("NonCoordinateException: ​ Not a coordinate Exception​ " + p));
                }               
            }
            catch(NonCoordinateException e)
            {
                w.println(e.getMessage());
            }
        }
        catch(EmptyStackException e)
        {
            removed=true;
            w.println("StackEmptyException:​ ​ Stack​ ​ Empty​ ​ exception​");
        }
        return null;
    }
    class Queen
    {
        int qx;
        int qy;
        public Queen(int x, int y)
        {
            this.qx=x;
            this.qy=y;
        }
    }
}
class QueenFoundException extends Exception
{
      public QueenFoundException() 
      {}
      public QueenFoundException(String message) 
      {
          super(message);
      }
}
class OverlapException extends Exception
{
    public OverlapException()
    {}
    public OverlapException(String message) 
    {
          super(message);
    }
}
class NonCoordinateException extends Exception
{
    public NonCoordinateException   ()
    {}
    public NonCoordinateException(String message) 
    {
          super(message);
    }
}

public class lab
{
        public static void main(String[] args) throws Exception
        {
                Scanner in = new Scanner(System.in);
                BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
                int n= Integer.parseInt(inp.readLine());
                int iterations= Integer.parseInt(inp.readLine());
                String[] x1= inp.readLine().trim().split("\\s+");
                int qx= Integer.parseInt(x1[0]);
                int qy= Integer.parseInt(x1[1]);
                Knight[] knights = new Knight[n];
                PrintWriter w = new PrintWriter( "output.txt", "UTF-8");
                for(int i=1; i<=n; i++)
                {
                    String filename = i+".txt"; 
                    inp = new BufferedReader(new FileReader(filename));
                    String name = inp.readLine();
                    x1= inp.readLine().trim().split("\\s+");
                    int x= Integer.parseInt(x1[0]);
                    int y= Integer.parseInt(x1[1]);
                    int m =Integer.parseInt(inp.readLine());
                    knights[i-1]= new Knight(name, x, y,m, qx, qy);
                    for(int j=0; j<m; j++)
                    {
                        x1= inp.readLine().trim().split("\\s+");
                        String type = x1[0];
                        if(type.equals("String"))
                        {
                            knights[i-1].insert((String)x1[1]);
                        }
                        else if(type.equals("Float"))
                        {
                            knights[i-1].insert(new Float(x1[1]));
                        }
                        else if(type.equals("Integer"))
                        {
                            knights[i-1].insert(new Integer(x1[1]));
                        }
                        else if(type.equals("Coordinate"))
                        {
                            Coordinate temp = new Coordinate(Integer.parseInt(x1[1]), Integer.parseInt(x1[2]));
                            knights[i-1].insert(temp);
                            w.println();
                        }
                    }
                }
                boolean found= false;
                Arrays.sort(knights);
                int j=0;
                while(j<iterations && found==false)
                {
                    j++;
                    for(int i=0; i<n ;i++)
                    {
                        if(knights[i].getBool()==false)
                        {
                            w.println(j+1+ " "+ knights[i].getName()+ " "+ knights[i].getX()+" "+knights[i].getY());
                            Coordinate newc = knights[i].pop(w);
                            if(newc!=null)
                            {
                                try
                                {
                                    for(int k=0;k<n; k++)
                                    {
                                        if(newc.getX()==knights[k].getX() && newc.getY()==knights[k].getY())
                                        {
                                            knights[k].setBool(true);
                                            throw(new OverlapException("OverlapException:​ ​ Knights​ ​ Overlap​ ​ Exception​ " + knights[k].getName())) ;
                                            
                                        } 
                                    }
                                    knights[i].setPosition(newc.getX(), newc.getY()); 
                                    if(knights[i].getX()==qx && knights[i].getY()==qy)
                                            throw(new QueenFoundException("​QueenFoundException:​ ​ Queen​ ​ has​ ​ been​ ​ Found.​ ​ Abort!​"));   
                                   
                                    w.println("No​ ​ exception. " + newc.getX()+" "+ newc.getY() );
                                    
                                }
                                catch(QueenFoundException e)
                                {
                                    w.println(e.getMessage());
                                    found= true;
                                    break;
                                }
                                catch(OverlapException e)
                                {
                                    w.println(e.getMessage());
                                }
                            }
                        }
                    }
                }
                w.close();
        }
}