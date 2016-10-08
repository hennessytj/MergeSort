import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Arrays;

/**
 * Experimenting with different implementations of basic
 * mergesort.  Implementations are largely from Algorithms, 4th
 * Edition text.
 * 
 * Top Down mergesort is recursively and true to the divide-and-
 * conquer paradigm.  It first divides array into smaller sub
 * arrays until there are N sub arrays of size 1.  It then merges
 * each of these sub arrays.  Put another way, it builds a recursive
 * tree starting with N items and divides N in half.  Each successive
 * branch is further divided in half until sub arrays are size 1.
 * 
 * Bottom Up mergesort is iterative and starts with sub arrays of
 * size 1.  Each of these sub arrays of size 1 are merged.  Then
 * it moves on to sub arrays of size 2, then 4, then 8, and so
 * on...  NOTE: Increasing powers of 2.  Lg N times.
 */
public class Merge
{
    public Merge() { }
    
    /**
     * Merges an array that has been divided logical into two sorted
     * sub-arrays.  Sub-array one is a[lo..mid] and sub-array two
     * is a[mid + 1..hi].
     * @aux extra array used
     * @a array with final values
     * @lo start index
     * @mid line of demarcation
     * @hi endpoint
     */
    public static void merge(int[] aux, int[] a, int lo, int mid, int hi)
    {
        int N = a.length;
        for (int i = lo; i <= hi; i++)
            aux[i] = a[i];
        
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++)
        {
            if (leftArrayExhausted(i, mid))      a[k] = aux[j++];
            else if (rightArrayExhausted(j, hi)) a[k] = aux[i++];
            else if (aux[j] < aux[i])            a[k] = aux[j++];
            else                                 a[k] = aux[i++];
        }
    }
    
    public static void sortBottomUp(int[] a)
    {
        int N = a.length;
        int[] aux = new int[N];
        for (int sz = 1; sz < N; sz = sz+sz)         // Sub array sz
            for (int lo = 0; lo < N-sz; lo += sz+sz) // Sub array posn
                merge(aux, a, lo, lo+sz-1, Math.min(lo+sz+sz-1, N-1));
    }
    
    public static void sortTopDown(int[] a)
    {
        int N = a.length;
        int[] aux = new int[N];
        sortTopDown(aux, a, 0, N - 1);
    }
    
    public static void sortTopDown(int[] aux, int[] a, int lo, int hi)
    {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sortTopDown(aux, a, lo, mid);
        sortTopDown(aux, a, mid + 1, hi);
        merge(aux, a, lo, mid, hi);
    }
    
    public static void main(String[] args)
    {
        int N = 512000;
        int[] a1 = new int[N];
        int[] a2;
        int[] a3;
        
        while (true) 
        {
            fillRandomly(a1);
            a2 = a1.clone();
            a3 = a2.clone();
            
            StdOut.println("Size = " + N);
            StdOut.println();
            StdOut.println();
            
            Stopwatch sw = new Stopwatch();
            Merge.sortBottomUp(a1);
            StdOut.println("Bottom Up mergesort = " + sw.elapsedTime());
            
            sw = new Stopwatch();
            Merge.sortTopDown(a2);
            StdOut.println("Top Down mergesort = " + sw.elapsedTime());

            sw = new Stopwatch();
            Merge.sortTopDown(a3);
            StdOut.println("Java system sort = " + sw.elapsedTime());
            
            N = 2*N; // Double size
            a1 = new int[N];
        }
    }
    
    /**
     * Support method used to increase readability within merge method.
     */
    public static boolean leftArrayExhausted(int left, int midpoint)
    { return left > midpoint; }
    
    /**
     * Support method used to increase readability within merge method.
     */
    public static boolean rightArrayExhausted(int right, int endpoint)
    { return right > endpoint; }  
    
    public static void fillRandomly(int[] a)
    {
        for (int i = 0; i < a.length; i++)
            a[i] = StdRandom.uniform(-100000, 100000);
    }
    
    public static boolean isSorted(int[] a)
    {
        for (int i = 1; i < a.length; i++)
            if (a[i] < a[i-1]) return false;
        return true;
    }
}