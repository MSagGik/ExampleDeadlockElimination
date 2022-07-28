import java.util.Random;
public class ExampleDeadlockElimination {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();
        // создание потоков
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                runner.firstThread();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                runner.secondThread();
            }
        });
        // выполнение потоков
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        // выполнение метода finished() после исполнения потоков
        runner.finished();
    }
}
class Runner {
    private Account account1 = new Account();
    private Account account2 = new Account();

    // метод, выполняемый первым потоком
    public void firstThread() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            synchronized (account1) {
                synchronized (account2) {
                    // вымышленные переводы от account1 к account2 рандомных сумм от 0 до 99
                    Account.transfer(account1, account2, random.nextInt(100));
                }
            }
        }
    }
    // метод, выполняемый вторым потоком
    public void secondThread() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            synchronized (account1) {
                synchronized (account2) {
                    // вымышленные переводы от account2 к account1 рандомных сумм от 0 до 99
                    Account.transfer(account2, account1, random.nextInt(100));
                }
            }
        }
    }
    // метод, выполняемый после исполнения потоков
    public void finished() {
        System.out.println(account1.getBalance());
        System.out.println(account2.getBalance());
        System.out.println("Total balance " + (account1.getBalance() + account2.getBalance()));
    }
}
// моделирование операций с вымышленными счетами
class Account {
    private  int balance = 10000;
    // метод пополнения вымышленного счёта
    public void deposit(int amount) {
        balance += amount;
    }
    // метод списания с вымышленного счёта
    public void withdraw(int amount) {
        balance -= amount;
    }
    // метод просмотра вымышленного счёта
    public int getBalance() {
        return balance;
    }
    // метод перевода между вымышленными счетами
    public static void transfer(Account acc1, Account acc2, int amount) {
        acc1.withdraw(amount);
        acc2.deposit(amount);
    }
}
