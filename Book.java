import java.util.concurrent.Semaphore;

class Q
{
  String book[] = {"Micro", "The Da Vinci Code", "Angels and Demons", "Harry Potter and the Deathly Hallows", "Digital Fortress", "Hannibal"}
    // an item
    int item;

    // semCon initialized with 0 permits
    // to ensure put() executes first
    static Semaphore semCon = new Semaphore(0);

    static Semaphore AnD = new Semaphore(2);

    static Semaphore semProd = new Semaphore(1);

    // to get an item from buffer
    void get()
    {
        try {
            // Before Reader can consume an item,
            // it must acquire a permit from semCon
            semCon.acquire();
        }
        catch(InterruptedException e) {
            System.out.println("InterruptedException caught");
        }

        // Reader consuming an item
        System.out.println("Reader consumed item : " + item);

        // After Reader consumes the item,
        // it releases semProd to notify Librarian
        semProd.release();
    }

    // to put an item in buffer
    void put(int item)
    {
        try {
            // Before Librarian can produce an item,
            // it must acquire a permit from semProd
            semProd.acquire();
        } catch(InterruptedException e) {
            System.out.println("InterruptedException caught");
        }

        // Librarian producing an item
        this.item = item;

        System.out.println("Librarian produced item : " + item);

        // After Librarian produces the item,
        // it releases semCon to notify Reader
        semCon.release();
    }
}

// Librarian class
class Librarian implements Runnable
{
    Q q;
    Librarian(Q q) {
        this.q = q;
        new Thread(this, "Librarian").start();
    }

    public void run() {
        for(int i=0; i < 5; i++)
            // Librarian put items
            q.put(i);
    }
}

// Reader class
class Reader implements Runnable
{
    Q q;
    Reader(Q q){
        this.q = q;
        new Thread(this, "Reader").start();
    }

    public void run()
    {
        for(int i=0; i < 5; i++)
            // Reader get items
            q.get();
    }
}

// Driver class
class PC
{
    public static void main(String args[])
    {
        // creating buffer queue
        Q q = new Q();

        // starting Reader thread
        new Reader(q);

        // starting Librarian thread
        new Librarian(q);
    }
}
