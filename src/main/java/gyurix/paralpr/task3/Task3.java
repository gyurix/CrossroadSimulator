package gyurix.paralpr.task3;

import gyurix.paralpr.task3.entities.Car;
import gyurix.paralpr.task3.entities.Person;
import gyurix.paralpr.task3.generators.PersonGenerator;

import java.util.ArrayList;
import java.util.List;

public class Task3 {
  public static final List<Thread> createdThreads = new ArrayList<>();

  public static void createAndStartThread(Runnable runnable, String name) {
    Thread t = new Thread(runnable, name);
    createdThreads.add(t);
    t.start();
  }

  public static void main(String[] args) {
    System.out.println("ParalPr Task 3 - CrossRoadSimulator - by Juraj Barath");
    Crossroad cr = new Crossroad();
    cr.start();
    try {
      Thread.sleep(Settings.SIMULATION_TIME);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    cr.setFinished();
    System.out.println("[MainThread] Finish signal sent, waiting for threads to finish");
    createdThreads.forEach(t -> {
      try {
        t.join();
      } catch (InterruptedException ignored) {
      }
    });
    System.out.println("[MainThread] Finished all the threads");
    System.out.println("[MainThread] Statistics:\n" +
            "[MainThread] Generated people: " + Person.nextId.get() + "\n" +
            "[MainThread] Waiting people: " + cr.count(cr.waitingPersons) + "\n" +
            "[MainThread] Completed people: " + cr.count(cr.completedPersons) + "\n" +
            "[MainThread] Generated cars: " + Car.nextId.get() + "\n" +
            "[MainThread] Waiting cars: " + cr.count(cr.waitingCars) + "\n" +
            "[MainThread] Completed cars: " + cr.count(cr.completedCars));
  }
}
