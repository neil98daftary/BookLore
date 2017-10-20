import java.util.concurrent.Semaphore;

class Buffer {
    int BUFFER_CAPACITY = 10;
    int data[] = new int[BUFFER_CAPACITY];
    static Semaphore sem = new Semaphore(1);
    void get(int id) {
        try {
            sem.acquire();
        } catch(InterruptedException e) {
            System.out.println("Interrupted!");
        }
        
        int pos = (int) (Math.random() * BUFFER_CAPACITY) % BUFFER_CAPACITY;
        if (this.data[pos] == 0) {
            System.out.println("Consumer with id = " + id + " failed to consume item at index " + pos + " (no item found)\n");
	for (int i = 0; i < BUFFER_CAPACITY; i++) {
		System.out.print(this.data[i]+"\t");
	}
	System.out.print("\n\n");
        } else {
            System.out.println("Consumer with id = " + id + " consumed item " + this.data[pos] + " at index " + pos + "\n");
	this.data[pos] =0;
	for (int i = 0; i < BUFFER_CAPACITY; i++) {
		System.out.print(this.data[i]+"\t");
	}
	System.out.print("\n\n");
        }
        sem.release();

    }

    void put(int data, int id) {

        try {
            sem.acquire();
        } catch(InterruptedException e) {
            System.out.println("InterruptedException caught");
        }

        int pos = (int) (Math.random() * BUFFER_CAPACITY) % BUFFER_CAPACITY;
        if (this.data[pos] == 0) {
	this.data[pos] = data;
        System.out.println("Producer with id = " + id + " produced item " + data + " at index " + pos+ "\n");
	for (int i = 0; i < BUFFER_CAPACITY; i++) {
		System.out.print(this.data[i]+"\t");
	}
	System.out.print("\n\n");
	}
        sem.release();

    }

}

class Consumer implements Runnable {

    Buffer b;
    int id;

    Consumer(Buffer b, int id) {
        this.b = b;
        this.id = id;
        new Thread(this, "Consumer").start();
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

class Producer implements Runnable {

    Buffer b;
    int id;

    Producer(Buffer b, int id) {
        this.b = b;
        this.id = id;
        new Thread(this, "Producer").start();
    }

    public void run() {
        for(int i=0; i < 5; i++) {
            b.put((int)(Math.random()*99)+1, id);
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
	new Consumer(b, 1);	
	new Producer(b, 1);
	new Producer(b, 2);
    }

}
