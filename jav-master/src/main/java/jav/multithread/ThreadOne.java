package jav.multithread;

public class ThreadOne {
	public void threadOne() {
		Runnable task = () -> {
			for(int i = 0; i <= 5; i++) {
				System.out.println(Thread.currentThread().getName() + " - user:" + (i + 1));
			}
		};
		Thread t1 = new Thread(task, "thread 1");
		Thread t2 = new Thread(task, "thread 2");
		t1.start();
		t2.start();
	}
	
	/* ở đây trong hàm raceCondition sẽ bị tình trạng 2 thread cùng gọi đồng thời tới count và cập nhật giá trị mới cho count dẫn đến
	 * việc bị mất dữ liệu:
	 * ví dụ khi tại một thời điểm count = 50 :
	 * Thread 1
       Đọc count = 50
       Tính 50 + 1 = 51

       Thread 2
       Đọc count = 50
       Tính 50 + 1 = 51
       -> Thread 1 ghi 51 và Thread 2 ghi 51 => lấy giá trị 51 ---> đây là race condition, hiện tượng tranh chấp tài nguyên và bị mất dữ liệu
       -> Đây là Race Condition do thao tác count++ không nguyên tử.
       */
	
	/* Để xử lý tình trạng tranh chấp này ta có thể dùng synchronized
	 * 
	 * synchronized có chức năng khiến các thread phải lần lượt thực thi vùng code đó khi sử dụng cùng một khóa.
	 * 
	 * 
	 * */ 
    static int count = 0;
    
    /* tạo hàm triển khai chung cho biến count
     * khi 2 thread cùng gọi đến increase() để thực thi: giả sử thread 1 vào trước thì sẽ thực hiện count ++ xong thì thread 2 mới vào thực thi
     * */
    public synchronized int increase() {
    	return count ++;
    }
	public void raceCondition() {
	    Runnable task = () -> {
			for(int i = 0; i < 100_000; i++ ) {
				increase();
				System.out.println(Thread.currentThread().getName() + "-lan:" + i);
			}
		};
		Thread t1 = new Thread(task, "thread 1");
		Thread t2 = new Thread(task, "thread 2");
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println(count);
	}
	
	Object lock1 = new Object();
	Object lock2 = new Object();
	public void deadlock( ) {
		Thread a = new Thread(() -> {
			synchronized (lock1) {
				sleep(100);
				synchronized (lock2) {
					System.out.println("A xong");
				}
			}
		}, "Thread-A");
		
		Thread b = new Thread(() -> {
			synchronized (lock2) {
				sleep(100);
				synchronized (lock1) {
					System.out.println("B xong");
				}
			}
		}, "Thread-B");
		a.start();
		b.start();
	}
	public void sleep(int ms) {
		try { Thread.sleep(ms); } catch (InterruptedException e) {}
	}
	 
}
