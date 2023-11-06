/*************************************************************************
*  An Airline management system that uses a weighted-edge directed graph 
*  implemented using adjacency lists.
*************************************************************************/
import java.util.*;
import java.io.*;

public class AirlineSystem 
{
  private String [] cityNames = null;
  private Digraph G = null;
  private static Scanner scan = null;
  private static final int INFINITY = Integer.MAX_VALUE;


  /**
  * Test client.
  */
  public static void main(String[] args) throws IOException 
  {
    AirlineSystem airline = new AirlineSystem();
    scan = new Scanner(System.in);
    while(true)
    {
      switch(airline.menu())
      {
        case 1:
          airline.readGraph();
          break;
        case 2:
          airline.printGraph();
          break;
        case 3:
          airline.displayMST();
          break;
        case 4:
            airline.shortestDistance();
            break;
        case 5:
            airline.shortestCost();
            break;
        case 6:
            airline.shortestHops();
            break;
        case 7:
            airline.superSaver();
            break;
        case 8:
          airline.addRoute();
            break;
        case 9:
          airline.removeRoute();
          break;
        case 10:
          airline.quit();
          break;
        default:
          System.out.println("Incorrect option.");
      }
    }
  }

  private int menu()
  {
    System.out.println("*********************************");
    System.out.println("Welcome to FifteenO'One Airlines!");
    System.out.println("1. Read data from a file.");
    System.out.println("2. Display all routes, distances, and prices."); //QUERIE #1
    System.out.println("3. Display a minimum spanning tree for the service routes based on distances."); //QUERIE #2 
    System.out.println("4. Compute shortest path based on miles."); //QUERIE #3a
    System.out.println("5. Compute shortest path based price."); //QUERIE #3b  
    System.out.println("6. Compute shortest path based on number of hops.");//QUERIE #3c
    System.out.println("7. Find trips within price range.");//QUERIE #4
    System.out.println("8. Add a new route to the schedule.");//QUERIE #5
    System.out.println("9. Remove a new route from the schedule.");//QUERIE #6
    System.out.println("10.Exit.");//QUERIE #7
    System.out.println("*********************************");
    System.out.print("Please choose a menu option (1-10): ");

    int choice = Integer.parseInt(scan.nextLine());
    return choice;
  }

  private void readGraph() throws IOException 
  {
    System.out.println("Please enter graph filename:");
    String fileName = scan.nextLine();
    Scanner fileScan = new Scanner(new FileInputStream(fileName));
    int v = Integer.parseInt(fileScan.nextLine());
    G = new Digraph(v);

    cityNames = new String[v];
    for(int i=0; i<v; i++)
    {
      cityNames[i] = fileScan.nextLine();
    }

    while(fileScan.hasNext())
    {
      int from = fileScan.nextInt();
      int to = fileScan.nextInt();
      int weight = fileScan.nextInt(); // weight (miles) of 'from-to' on graph
      double cost = fileScan.nextDouble(); // cost of 'from-to' on graph
      G.addEdge(new WeightedDirectedEdge(from-1, to-1, weight, cost) );
      G.addEdge(new WeightedDirectedEdge(to-1, from-1, weight, cost) );//makes the graph bi-directional by adding 'complementary' path
      if(fileScan.hasNextLine())
        fileScan.nextLine();
    }
    fileScan.close();
    System.out.println("Data imported successfully.");
    System.out.print("Please press ENTER to continue ...");
    scan.nextLine();
  }//end of readGraph() method

  //QUERIE #1 --> Show the entire list of direct routes, distances and prices
  private void printGraph() 
  {
    if(G == null)
    {
      System.out.println("Please import a graph first (option 1).");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    } 
    else 
    {
      for (int i = 0; i < G.v; i++) 
      {
        System.out.print(cityNames[i] + ": ");
        for (WeightedDirectedEdge e : G.adj(i)) 
        {
          System.out.print(cityNames[e.to()] + "[miles:" + e.weight() +"|cost:$" +e.cost()+"] ");
        }//end of enhanced for-loop (inner for loop)
        System.out.println();
      }//end of outer for loop
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    }//end of if-else block for G == null
  }//end of printGraph() method

  //QUERIE #2 --> Display a minimum spanning tree for the service routes based on distances
  private void displayMST()
  {
    if(G == null)
    {
        System.out.println("Please import a graph first (option 1).");
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
    }
    else
    {
      G.kruskals();
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    }
  }//end of displayMST() method

  //QUERIE #3a --> Shortest path based on total miles (one way) from the source to the destination
  private void shortestDistance() 
  {
      if(G == null)
      {
        System.out.println("Please import a graph first (option 1).");
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
      } 
      else 
      {
        for(int i=0; i<cityNames.length; i++)
        {
          System.out.println(i+1 + ": " + cityNames[i]);
        }
        System.out.print("Please enter source city (1-" + cityNames.length + "): ");
        int source = Integer.parseInt(scan.nextLine());
        System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
        int destination = Integer.parseInt(scan.nextLine());
        source--;
        destination--;
        G.dijkstras(source, destination);
        if(!G.marked[destination])
        {
          System.out.println("There is no route from " + cityNames[source]
                              + " to " + cityNames[destination]);
        } 
        else 
        {
          Stack<Integer> path = new Stack<>();
          for (int x = destination; x != source; x = G.edgeTo[x])
          {
              path.push(x);
          }
          System.out.print("The shortest route from " + cityNames[source] +
                             " to " + cityNames[destination] + " has " +
                             G.distTo[destination] + " miles: ");

          int prevVertex = source;
          System.out.print(cityNames[source] + " ");
          while(!path.empty())
          {
            int v = path.pop();
            System.out.print(G.distTo[v] - G.distTo[prevVertex] + " "
                             + cityNames[v] + " ");
            prevVertex = v;
          }
          System.out.println();

        }//end of 'inner' if-else block
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
      }//end of 'outer' if-else block
  }//end of shortestDistance() method

  //QUERIE #3b --> Shortest path based on price from the source to the destination
  private void shortestCost()
  {
      if(G == null)
      {
        System.out.println("Please import a graph first (option 1).");
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
      } 
      else 
      {
        for(int i=0; i<cityNames.length; i++)
        {
          System.out.println(i+1 + ": " + cityNames[i]);
        }
        System.out.print("Please enter source city (1-" + cityNames.length + "): ");
        int source = Integer.parseInt(scan.nextLine());
        System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
        int destination = Integer.parseInt(scan.nextLine());
        source--;
        destination--;
        G.dijkstrasCost(source, destination);
        if(!G.marked[destination])
        {
          System.out.println("There is no route from " + cityNames[source]
                              + " to " + cityNames[destination]);
        } 
        else 
        {
          Stack<Integer> path = new Stack<>();
          for (int x = destination; x != source; x = G.edgeTo[x])
          {
              path.push(x);
          }
          System.out.print("The cheapest route from " + cityNames[source] +
                             " to " + cityNames[destination] + " costs $" +
                             G.costTo[destination] + ": ");
          
          int prevVertex = source;
          System.out.print(cityNames[source]);
          while(!path.empty())
          {
            int v = path.pop();
            System.out.print("-[$"+ (G.costTo[v] - G.costTo[prevVertex]) + "]"
                             + "-"+cityNames[v] );
            prevVertex = v;
          }
          
          System.out.println();

        }//end of 'inner' if-else block
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
      }//end of 'outer' if-else block
  }//end of ShortestCost() method

  //QUERIE #3c --> Shortest path based on number of hops (individual segments) from the source to the destination
  private void shortestHops() 
  {
    if(G == null){
      System.out.println("Please import a graph first (option 1).");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    } 
    else 
    {
      for(int i=0; i<cityNames.length; i++)
      {
        System.out.println(i+1 + ": " + cityNames[i]);
      }
      System.out.print("Please enter source city (1-" + cityNames.length + "): ");
      int source = Integer.parseInt(scan.nextLine());
      System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
      int destination = Integer.parseInt(scan.nextLine());
      source--;
      destination--;
      G.bfs(source);
      if(!G.marked[destination])
      {
        System.out.println("There is no route from " + cityNames[source]
                            + " to " + cityNames[destination]);
      } 
      else 
      {  
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(destination);

        int s = destination;
        while( s!=source  )
        {
          stack.push(G.edgeTo[s]);
          s = G.edgeTo[s];
        }

        System.out.print("The shortest route from "+cityNames[source]+" to "+cityNames[destination]
          +" has "+G.distTo[destination]+" hop(s):");
        
        while(!stack.empty())
        {
          Integer p = stack.pop();
          System.out.print(" "+cityNames[p]);
        }
        System.out.println();
      }//end of if-else 'inner' loop

      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    }//end of if-else 'outer' loop
  }//end of shortestHops() method

  //QUERIE #4 --> Given a dollar amount entered by the user, print out all trips whose cost is less than or equal to that amount
  private void superSaver()
  {
    if(G == null)
    {
      System.out.println("Please import a graph first (option 1).");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    } 
    else 
    {
      System.out.print("What is the maximum price you are willing to pay for a flight?:");
      double budget = Double.parseDouble(scan.nextLine());

      System.out.println("List of paths costing $"+budget+" or less:");
      for (int i = 0; i < G.v; i++) 
      {
         G.dfs(i, budget);
      }//end vertices for-loop

      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    }//end of if-else 'outer' loop
  }//end of superSaver() method

  //QUERIE #5 --> Add a new route to the schedule
  private void addRoute()
  {
    if(G == null)
    {
      System.out.println("Please import a graph first (option 1).");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    } 
    else
    {
      for(int i=0; i<cityNames.length; i++)
      {
         System.out.println(i+1 + ": " + cityNames[i]);
      }

      boolean isUpdate = false; //checks if vertex to add is already in chart
      
      System.out.print("Please enter source city (1-" + cityNames.length + "): ");
      int from = Integer.parseInt(scan.nextLine());

      System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
      int to = Integer.parseInt(scan.nextLine());

      //if edge is already in graph --> alert user and ask if they would still like to update the edge values
      if(G.containsEdge(new WeightedDirectedEdge(from-1, to-1, 0, 0)) )
      {
        System.out.println("Edge "+cityNames[from-1]+"-"+cityNames[to-1]+" is already in the graph.");
        System.out.print("Would you still like to update the values of the edge? (y/n):");
        char response = scan.nextLine().toLowerCase().charAt(0);
        if(response!='y')
        {
          return;
        }
        else
        {
          isUpdate = true;
        }
      }//end of findEdge if statement

      System.out.print("Please enter weight of edge: ");
      int weight = Integer.parseInt(scan.nextLine());

      System.out.print("Please enter cost of edge: ");
      int cost = Integer.parseInt(scan.nextLine());


      if(!isUpdate)
      {
        G.addEdge(new WeightedDirectedEdge(from-1, to-1, weight, cost) );
        G.addEdge(new WeightedDirectedEdge(to-1, from-1, weight, cost) );//makes the graph bi-directional by adding 'complementary' path
      }
      else 
      {
        //if edge is already in the graph --> update it with the new values of user input
        WeightedDirectedEdge temp1 = G.findEdge( new WeightedDirectedEdge(from-1, to-1,0,0) );
        WeightedDirectedEdge temp2 = G.findEdge( new WeightedDirectedEdge(to-1, from-1,0,0) );
        G.removeEdge(temp1);
        G.removeEdge(temp2);
        G.addEdge(new WeightedDirectedEdge(from-1, to-1, weight, cost) );
        G.addEdge(new WeightedDirectedEdge(to-1, from-1, weight, cost) );//makes the graph bi-directional by adding 'complementary' path
 
      }
      
      System.out.println("Added edge "+cityNames[from-1]+"-"+cityNames[to-1]+" to graph.");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
        
    }
  }//end of addRoute() method

  //QUERIE #6 --> Remove a route from the schedule
  private void removeRoute()
  {

    if(G == null)
    {
      System.out.println("Please import a graph first (option 1).");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    } 
    else
    {
      for(int i=0; i<cityNames.length; i++)
      {
         System.out.println(i+1 + ": " + cityNames[i]);
      }

      int from;
      int to;
      while(true)
      {
        System.out.print("Please enter source city (1-" + cityNames.length + "): ");
        from = Integer.parseInt(scan.nextLine());

        System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
        to = Integer.parseInt(scan.nextLine());

        if( G.containsEdge(new WeightedDirectedEdge(from-1, to-1, 0, 0)) )
        {
          break;
        }
        //reject input if it is not an edge in the graph and ask user if they want to try a different input
        else
        {
          System.out.println("Edge "+cityNames[from-1]+"-"+cityNames[to-1]+" is not in the graph.");
          System.out.print("Would you like to try another edge? (y/n):");
          char response = scan.nextLine().toLowerCase().charAt(0);
          //exit if user does not input yes
          if(response!='y')
            return;
        }//end of 'findEdge' if statement

      }//end of while loop
      

      WeightedDirectedEdge temp1 = G.findEdge( new WeightedDirectedEdge(from-1, to-1,0,0) );
      WeightedDirectedEdge temp2 = G.findEdge( new WeightedDirectedEdge(to-1, from-1,0,0) );
      G.removeEdge(temp1);
      G.removeEdge(temp2);
     
      System.out.println("Removed edge "+cityNames[from-1]+"-"+cityNames[to-1]+" from graph.");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    }//end of 'outer' if else statement
  }//end of removeRoute() method

  //QUERIE #7 --> Quit the program
  private void quit()
  {
    scan.close();
    System.exit(0);
  }

  

  /**
  *  The <tt>Digraph</tt> class represents an directed graph of vertices
  *  named 0 through v-1. It supports the following operations: add an edge to
  *  the graph, iterate over all of edges leaving a vertex.Self-loops are
  *  permitted.
  */
  private class Digraph
  {
    private final int v; // number of verticies 
    private int e; // number of edges
    private LinkedList<WeightedDirectedEdge>[] adj;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path
    private double[] costTo;


    /**
    * Create an empty digraph with v vertices.
    */
    public Digraph(int v) 
    {
      if (v < 0) throw new RuntimeException("Number of vertices must be nonnegative");
      this.v = v;
      this.e = 0;
      @SuppressWarnings("unchecked")
      LinkedList<WeightedDirectedEdge>[] temp =
      (LinkedList<WeightedDirectedEdge>[]) new LinkedList[v];
      adj = temp;
      for (int i = 0; i < v; i++)
        adj[i] = new LinkedList<WeightedDirectedEdge>();
    }

    /**
    * Add the edge e to this digraph.
    */
    public void addEdge(WeightedDirectedEdge edge) 
    {
      int from = edge.from();
      adj[from].add(edge);
      e++;
    }

    public void removeEdge(WeightedDirectedEdge edge) 
    {
      int from = edge.from();
      adj[from].remove(edge);
      e--;
    }

    //returns true if edge is in graph
    public boolean containsEdge(WeightedDirectedEdge edge)
    {
      return findEdge(edge) != null;
    }

    public WeightedDirectedEdge findEdge(WeightedDirectedEdge edge)
    {
      for(int i = 0; i < v; i++)
      {
        for (WeightedDirectedEdge w : adj(i))
        {
          if(w.from() == edge.from() && w.to() == edge.to())
            return w;
        }
      }//end of for loop

      return null;
    }//end of findEdge() method


    /**
    * Return the edges leaving vertex v as an Iterable.
    * To iterate over the edges leaving vertex v, use foreach notation:
    * <tt>for (WeightedDirectedEdge e : graph.adj(v))</tt>.
    */
    public Iterable<WeightedDirectedEdge> adj(int v) 
    {
      return adj[v];
    }


    //breadth first search -->  used for shortestHops() method in QUERIE 3c
    public void bfs(int source) 
    {
      marked = new boolean[this.v];
      distTo = new int[this.e];
      edgeTo = new int[this.v];

      Queue<Integer> q = new LinkedList<Integer>();
      for (int i = 0; i < v; i++)
      {
        distTo[i] = INFINITY;
        marked[i] = false;
      }
      distTo[source] = 0;
      marked[source] = true;
      q.add(source);

      while (!q.isEmpty()) 
      {
        int v = q.remove();
        for (WeightedDirectedEdge w : adj(v)) 
        {
          if (!marked[w.to()]) 
          {
            edgeTo[w.to()] = v;
            distTo[w.to()] = distTo[v] + 1;
            marked[w.to()] = true;
            q.add(w.to());
          }//end of if statement
        }//end of enhanced for loop
      }//end of while() loop
    }//end of bfs() method


    //depth first search --> used for superSaver() method (QUERIE #4)
    public void dfs(int source, double budget)
    {
      marked = new boolean[this.v];
      edgeTo = new int[this.v];

      double totalCost = 0;

      for (int i = 0; i < v; i++)
      {
        marked[i] = false;
      }

      dfs(source, source, budget, totalCost, marked, edgeTo);

    }//end of dfs(source) method

    public void dfs(int source, int v, double budget, double tCost, boolean[] marked, int[] edgeTo)
    {
      marked[v] = true;
      double totalCost = tCost;


      for (WeightedDirectedEdge w : adj(v))
      {
        edgeTo[w.to()] = v;
        totalCost = tCost+w.cost();
        
        if(totalCost <= budget)
        {

           System.out.print("Price:$"+totalCost+" -- Path: "+cityNames[w.to()]+" ");
          
           int x = w.to();
            
                  
           while(x!=v)
           {
            x = edgeTo[x];
            System.out.print(cityNames[x]+" ");
            //scan.nextLine();
           }
           
           System.out.println();
        }

        if (!marked[w.to()]) 
        {
          //edgeTo[w.to()] = v;
          dfs(source, w.to(), budget, totalCost, marked, edgeTo);
        }
      }//end of enhanced for loop
    }//end of dfs(source, marked) method

    //dijkstras used for shortestDistance() method in QUERIE 3a
    public void dijkstras(int source, int destination) 
    {
      marked = new boolean[this.v];
      distTo = new int[this.v];
      edgeTo = new int[this.v];

      for (int i = 0; i < v; i++)
      {
        distTo[i] = INFINITY;
        marked[i] = false;
      }

      //'initializing' source vertex on graph
      distTo[source] = 0;
      marked[source] = true;
      int nMarked = 1;

      int current = source;
      while (nMarked < this.v) 
      {
        for (WeightedDirectedEdge w : adj(current)) 
        {
          if (distTo[current]+w.weight() < distTo[w.to()]) 
          {
            distTo[w.to()] = distTo[current]+w.weight();
            edgeTo[w.to()] = current;
          }
        }//end of for() loop

        //Finding the unmarked vertex with the minimum value
        int min = INFINITY;
        current = -1;

        for(int i=0; i<distTo.length; i++)
        {
          if(marked[i])
            continue;
          if(distTo[i] < min)
          {
            min = distTo[i];
            current = i;
          }
        }

      /* if current = -1 after traversing the for loop --> 
         it means all nodes in the graph are marked and thus the search is complete --> 
         so BREAK from the while loop */
        if(current < 0)
          break;
        
        marked[current] = true;
        nMarked++;
      }//end of while() loop
    }//end of dijkstras() mehtod

    //dijkstras used for shortestCost() method in QUERIE 3b
    public void dijkstrasCost(int source, int destination) 
    {
      marked = new boolean[this.v];
      costTo = new double[this.v];
      edgeTo = new int[this.v];


      for (int i = 0; i < v; i++)
      {
        costTo[i] = INFINITY;
        marked[i] = false;
      }
      costTo[source] = 0;
      marked[source] = true;
      int nMarked = 1;

      int current = source;
      while (nMarked < this.v) 
      {
        for (WeightedDirectedEdge w : adj(current)) 
        {
          if (costTo[current]+w.cost() < costTo[w.to()]) 
          {
            costTo[w.to()] = costTo[current]+w.cost();
            edgeTo[w.to()] = current;
          }
        }//end of for() loop

        //Find the vertex with minimim path distance
        //This can be done more effiently using a priority queue!
        double min = INFINITY;
        current = -1;

        for(int i=0; i<costTo.length; i++)
        {
          if(marked[i])
            continue;
          if(costTo[i] < min)
          {
            min = costTo[i];
            current = i;
          }
        }

        // if current = -1 after traversing the for loop 
        // it means all nodes in the graph are marked and thus the search is complete so BREAK from the while loop
        if(current < 0)
          break;
        
        marked[current] = true;
        nMarked++;
      }//end of while() loop
    }//end of dijkstrasCost() mehtod

    public void kruskals()
    {
      ArrayList<WeightedDirectedEdge> allEdges = new ArrayList<WeightedDirectedEdge>();
      ArrayList<WeightedDirectedEdge> mstEdges = new ArrayList<WeightedDirectedEdge>();
      UF uf = new UF(v);
      
      for(int i = 0; i < v; i++)
      {
        for (WeightedDirectedEdge w : adj(i))
        {
          allEdges.add(w);
        }
      }//end of for loop

      //sort allEdges in by weight and in ascending order
      Collections.sort(allEdges);

      for (WeightedDirectedEdge w : allEdges)
      {
          boolean cycle = uf.union( w.to(), w.from() );

          //add the edge to the MST if it doesn't form a cycle
          if(!cycle )
            mstEdges.add(w);
      }
     
      System.out.println("The edges in MST graph along with their distances are as follows:");
      for(WeightedDirectedEdge w : mstEdges)
      {
        System.out.println(cityNames[w.from()]+"-"+cityNames[w.to()]+": "+w.weight());
      }

    }//end of kruskals() method

  }//END OF PRIVATE CLASS Digraph



  /**
  *  The <tt>WeightedDirectedEdge</tt> class represents a weighted edge in an directed graph.
  */
  private class WeightedDirectedEdge implements Comparable<WeightedDirectedEdge>
  {
    private final int v;
    private final int w;
    private int weight;
    private double cost;
    /**
    * Create a directed edge from v to w with given weight and cost.
    */
    public WeightedDirectedEdge(int v, int w, int weight, double cost) 
    {
      this.v = v;
      this.w = w;
      this.weight = weight;
      this.cost = cost;
    }

    public int from()
    {
      return v;
    }

    public int to()
    {
      return w;
    }

    public int weight()
    {
      return weight;
    }

    public double cost()
    {
      return cost;
    }

    //method compareTo() is used in the kruskals algorithm to sort edges in ascending order by weight
    public int compareTo(WeightedDirectedEdge other)
    {
      return this.weight - other.weight;
    }

  }//END OF PRIVATE CLASS WeightedDirectedEdge


  //UNION FIND CLASS - USED IN KRUSKALS
  //code adapted from hw 11
  private class UF
  {
    private int[] id;
    private int[] sz;
    private int count;

    public UF( int n) 
    {
      count = n;
      id = new int[n];
      sz = new int[n];
      for (int i = 0; i < n; i++)
      {
       id[i] = i; 
       sz[i] = 1;  
      }
    }

    public int find( int p) 
    {
      while (p != id[p])
      {
        p = id[p];
      }    
      return p;
    }

    //union() method returns true if added vertex creates cycle
    public boolean union (int p, int q) 
    {
      int i = find(p), j = find(q);
      if (i == j)
      {
        return true;
      }
      if (sz[i] < sz[j]) 
      { 
        id[i] = j; sz[j] += sz[i]; 
      }
      else 
      { 
        id[j] = i; sz[i] += sz[j]; 
      }
      count --;
      return false;
    }

    public int[] getID()
    {
      return id;
    }

    public int[] getSZ()
    {
      return sz;
    }

    public boolean createsCycle(int p, int q)
    {
      int i = find(p), j = find(q);
      if (i == j)
      {
        return true;
      }
      return false;
    }

  }//END OF PRIVATE CLASS UF

}
