import java.util.concurrent.*;

/**
 * Created by S416995 on 03/07/2017.
 */
public class ConcurrencyJava8 {
    public static void main(String[] args){
        Runnable task = () -> {

                String name = Thread.currentThread().getName();
                System.out.println("Foo " + name);


        };
        task.run();
        Thread thread = new Thread(task);
        thread.start();

        System.out.println("Done!");
        executors();
        callables();

    }

    private static void callables() {
        //Callables are functional interfaces just like runnables but instead of being void they return a value.
        Callable<Integer> task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };
        try {
           Integer var =  task.call();
           System.out.println("value returned from direct callable :" + var);

        //Callables can be submitted to executor services just like runnables.
        //But what about the callables result? Since submit() doesn't wait until the task completes,
        // the executor service cannot return the result of the callable directly.
        //Instead the executor returns a special result of type Future which can be used to retrieve the actual result at a later point in time.

            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<Integer> future = executor.submit(task);

            //After submitting the callable to the executor we first check if the future has already been finished execution via isDone()
            System.out.println("future done? " + future.isDone());

            Integer result = null;
            System.out.println("fist future get");

            result = future.get();
            //with time out
            //result = future.get(1, TimeUnit.SECONDS);


            System.out.println("future done? " + future.isDone());
            System.out.println("result from executor call: " + result);
            executor.shutdownNow();

            System.out.println("future get after shutdown");
            result = future.get();
            System.out.println("result after shutdown: " + result);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executors() {
    /*Executors are capable of running asynchronous tasks and typically manage a pool of threads, so we don't have to create new threads manually.
    All threads of the internal pool will be reused under the hood for revenant tasks,
    so we can run as many concurrent tasks as we want throughout the life-cycle of our application with a single executor service.*/
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Inside executor : " + threadName);

        };
        executor.submit(task);
        //The result looks similar to the above sample but when running the code you'll notice an important difference:
        // the java process never stops! Executors have to be stopped explicitly - otherwise they keep listening for new tasks.
        executeShutdown(executor);


    }

    private static void executeShutdown(ExecutorService executor) {
    /*An ExecutorService provides two methods for that purpose:
    shutdown() waits for currently running tasks to finish
    while shutdownNow() interrupts all running tasks and shut the executor down immediately.*/
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }
}
