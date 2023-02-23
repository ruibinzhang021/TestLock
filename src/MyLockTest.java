import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MyLock{
    Lock lock = new ReentrantLock();

    public void m1() throws InterruptedException {
        lock.lock();
        try {
            Thread.sleep(1);
        }finally {
            lock.unlock();
        }
    }

    public void m2(){
        lock.lock();
        try {
            System.out.println("I am in critical section of method m2.");
        }finally {
            lock.unlock();
        }
    }

}
class T1 implements Runnable{
    MyLock m;
    public T1 (MyLock m){
        this.m = m;
    }
    @Override
    public void run() {
        try {
            m.m1();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class T2 implements Runnable{
    MyLock m;
    public T2 (MyLock myLock){
        m = myLock;
    }
    @Override
    public void run() {
        m.m2();
    }
}
public class MyLockTest {
    public static void main(String[] args) throws InterruptedException {
        MyLock m = new MyLock();
        Thread t1 = new Thread(new T1(m));
        Thread t2 = new Thread(new T2(m));
        t1.start();
        Thread.sleep(1);
        t2.start();
    }
}
