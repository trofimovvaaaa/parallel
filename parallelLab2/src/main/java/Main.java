

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import actor.BusActor;
import actor.StoppingActor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("BUS-STOPPING-PROBLEM");

        List<ActorRef> stoppings = IntStream.range(1, 4).boxed().map(i -> system.actorOf(Props.create(StoppingActor.class, "Зупинка № " + i))).collect(Collectors.toList());
        List<ActorRef> trams = IntStream.range(1, 11).boxed().map(i -> system.actorOf(Props.create(BusActor.class, stoppings, "Автобус № " + i))).collect(Collectors.toList());

        try {
            Thread.sleep(25000);
            trams.forEach(system::stop);
            stoppings.forEach(system::stop);
            system.terminate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
