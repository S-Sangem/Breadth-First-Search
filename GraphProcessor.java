// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

/**
 * @author Sushanth Sangem
 * Colaborated with Alex Andringa
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

public class GraphProcessor
{
    private int n;

    private static int twoN;

    private int diameter;

    private boolean diameterDone = false;

    private int[] centrality;

    private boolean[] centralityDone;

    HashMap<String, Integer> vertecies = new HashMap<>();
    ArrayList<ArrayList<String>> parents = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();

    public GraphProcessor(String graphData) throws FileNotFoundException
    {
        int i = 0;

        File graphFile = new File(graphData);
        Scanner scan = new Scanner(graphFile);


        //File graphFile; //= null;
        //Scanner scan = null;

        //try {
        // graphFile = new File(graphData);
        // scan = new Scanner(graphFile);
        //} catch (Exception e) {
        //e.printStackTrace();
        //}

        n = Integer.parseInt(scan.next());

        while(scan.hasNext())
        {
            String scanU = scan.next();
            String scanV = scan.next();

            if(!vertecies.containsKey(scanU))
            {
                children.add(new ArrayList<String>());
                parents.add(new ArrayList<String>());
                vertecies.put(scanU, i);
                i++;
            }
            if(!vertecies.containsKey(scanV))
            {
                children.add(new ArrayList<String>());
                parents.add(new ArrayList<String>());
                vertecies.put(scanV, i);
                i++;
            }

            children.get(vertecies.get(scanU)).add(scanV);
            parents.get(vertecies.get(scanV)).add(scanU);
        }

        twoN = 2*n;
        diameter = -1;
        centrality = new int[n];
        centralityDone = new boolean[n];
    }

    public int outDegree(String v)
    {
        return children.get(vertecies.get(v)).size();
    }

    public ArrayList<String> bfsPath(String u, String v)
    {
        ArrayList<String> path = new ArrayList();
        Queue<String> queue = new LinkedList();
        HashSet<String> hashSet = new HashSet();

        String[] prev = new String[n];

        queue.add(u);
        hashSet.add(u);

        String next;
        String nextAdd;

        while(!queue.isEmpty())
        {
            next = queue.remove();
            if(next.equals(v))
            {
                nextAdd = next;
                while(!nextAdd.equals(u))
                {
                    path.add(nextAdd);
                    nextAdd = prev[vertecies.get(nextAdd)];
                }
                path.add(u);
                break;
            }
            for(String addChildren : children.get(vertecies.get(next)))
            {
                if(hashSet.contains(addChildren))
                {
                    continue;
                }
                prev[vertecies.get(addChildren)] = next;

                hashSet.add(addChildren);
                queue.add(addChildren);

            }
        }


        if(path.isEmpty())
        {
            diameter = twoN;
        }


        if(path.size() > diameter)
        {
            diameter = path.size() - 1;
        }

        return path;
    }

    public int diameter()
    {
        if(!diameterDone)
        {
            for(String d : vertecies.keySet())
            {
                for(String d2 : vertecies.keySet())
                {
                    bfsPath(d,d2);
                }
            }
        }

        if(diameter == twoN)
        {
            return diameter;
        }

        return diameter;
    }

    public int centrality(String v)
    {
        if(centralityDone[vertecies.get(v)])
        {
            return centrality[vertecies.get(v)];
        }
        for(String i : vertecies.keySet()) {
            for(String j : vertecies.keySet()) {
                if(i.equals(v) || j.equals(v) || i.equals(j))
                {
                    continue;
                }
                ArrayList<String> temp = bfsPath(i,j);
                if(temp.contains(v))
                {
                    centrality[vertecies.get(v)]++;
                }
            }
        }
        centralityDone[vertecies.get(v)] = true;
        return centrality[vertecies.get(v)];
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        GraphProcessor run = new GraphProcessor("C:\\Users\\yoga\\Desktop\\test.txt");

        System.out.println("Out Degree Ames: " + run.outDegree("Ames"));
        System.out.println("BFS Path from Ames to Chicago: " + run.bfsPath("Ames", "Chicago"));
        System.out.println("Diameter: " + run.diameter());
        System.out.println("Centrality Ames: " + run.centrality("Ames"));

    }
}
