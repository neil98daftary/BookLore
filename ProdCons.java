import java.util.concurrent.Semaphore;
import java.util.*;

class Buffer {
    int BUFFER_CAPACITY = 10;
    int pos = -1;
    int data[] = new int[BUFFER_CAPACITY];
    String b_data[] = new String[BUFFER_CAPACITY];
    String book[] = {"Micro", "The Da Vinci Code", "Angels and Demons", "Harry Potter and the Deathly Hallows", "Digital Fortress", "Hannibal", "Hardy Boys", "Famous Five", "Secret Seven", "IT"};
    static Semaphore sem = new Semaphore(1);
    Scanner sc=new Scanner(System.in);
    int choice;

    void get(int id) {
        try {
            sem.acquire();
        } catch(InterruptedException e) {
            System.out.println("Interrupted!");
        }

        // int pos = (int) (Math.random() * BUFFER_CAPACITY) % BUFFER_CAPACITY;
        System.out.println("Choose which book to read:");
        choice = sc.nextInt();
        // if (this.data[pos] == 0) {
        //     System.out.println("Reader with id = " + id + " failed to consume item at index " + pos + " (no item found)\n");
	      //     for (int i = 0; i < BUFFER_CAPACITY; i++) {
		    //         System.out.print(this.data[i]+"\t");
	      //     }
	      //     System.out.print("\n\n");
        //     } else {
        //       System.out.println("Reader with id = " + id + " consumed item " + this.data[pos] + " at index " + pos + "\n");
	      //       this.data[pos] =0;
	      //       for (int i = 0; i < BUFFER_CAPACITY; i++) {
		    //           System.out.print(this.data[i]+"\t");
	      //       }
	      //        System.out.print("\n\n");
        //      }


        System.out.println("Reader read book " + b_data[choice] + "\n");
        this.data[choice] = 0;
        for (int i = 0; i < BUFFER_CAPACITY; i++) {
          if (this.data[i]==1) {
             System.out.println(b_data[i] + " [" + i + "]");
          }
        }
        System.out.print("\n\n");
             sem.release();

    }



    void put(int data, int id) {

        try {
            sem.acquire();
        } catch(InterruptedException e) {
            System.out.println("InterruptedException caught");
        }

        //int pos = (int) (Math.random() * BUFFER_CAPACITY) % BUFFER_CAPACITY;
        pos++;
        if (this.data[pos] == 0) {
	         this.data[pos] = 1;
           b_data[pos] = book[data];

           System.out.println("Librarian put book " + book[data] + "\n");
	         for (int i = 0; i < BUFFER_CAPACITY; i++) {
             if (this.data[i]==1) {
                System.out.println(b_data[i] + " [" + i + "]");
             }
	         }
	         System.out.print("\n\n");
	      }
        sem.release();

    }

}

class Reader implements Runnable {

    Buffer b;
    int id;

    Reader(Buffer b, int id) {
        this.b = b;
        this.id = id;
        new Thread(this, "Reader").start();
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            b.get(id);
	           try {
		             Thread.sleep((int)(Math.random()*50));
	              } catch (Exception e) {
		                System.out.println("Exception!");
	                 }
	      }
    }

}

class Librarian implements Runnable {

    Buffer b;
    int id;

    Librarian(Buffer b, int id) {
        this.b = b;
        this.id = id;
        new Thread(this, "Librarian").start();
    }

    public void run() {
        for(int i=0; i < 10; i++) {
            b.put((int)(Math.random()*9)+1, id);
	           try {
		             Thread.sleep((int)(Math.random()*50));
	              } catch (Exception e) {
		                System.out.println("Exception!");
	                 }
	       }
    }

}


public class ProdCons {

    public static void main(String arg[]) {
	Buffer b = new Buffer();
	new Librarian(b, 1);
	new Reader(b, 1);
	//new Librarian(b, 2);
    }

}
