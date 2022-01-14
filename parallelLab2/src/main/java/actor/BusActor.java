package actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.List;

public class BusActor extends AbstractActor {

    private final List<ActorRef> stoppings;
    private ActorRef nearest;
    private final String name;

    public BusActor(List<ActorRef> stoppings, String name) {
        this.stoppings = stoppings;
        this.name = name;
        tryStopOnStopping();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals(StoppingActor.Command.STOPPING_IS_EMPTY, command -> stopOnStopping())
                .matchEquals(StoppingActor.Command.STOPPING_IS_FULL, command -> tryStopOnStopping())
                .build();
    }

    private void tryStopOnStopping() {
        nearest = getNearestStation();
        nearest.tell(Command.CAN_I_STOP, self());
    }

    private ActorRef getNearestStation() {
        return stoppings.get((int) (Math.random() * stoppings.size()));
    }

    private void stopOnStopping() {
        nearest.tell(Command.STOP_ON_STOPPING, self());
        System.out.println(name + " — зупинився");
        try {
            Thread.sleep(4000);
            if (Math.random() > 0.7) {
                leave();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leave() {
        nearest.tell(Command.LEAVING, ActorRef.noSender());
        System.out.println( name + " — покидає зупинку");
    }

    public enum Command {
        STOP_ON_STOPPING,
        CAN_I_STOP,
        LEAVING
    }
}

