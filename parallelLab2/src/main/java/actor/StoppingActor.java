package actor;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoppingActor extends AbstractActor {

    private static final Integer MAX = 2;
    private final List<ActorRef> stayed = new ArrayList<>();
    private Integer allocated = 0;
    private String name;

    public StoppingActor(String name) {
        this.name = name;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals(BusActor.Command.CAN_I_STOP, command -> checkIfAvailable())
                .matchEquals(BusActor.Command.STOP_ON_STOPPING, command -> regisNewTram())
                .matchEquals(BusActor.Command.LEAVING, command -> registerLeave())
                .build();
    }

    private void registerLeave() {
        allocated--;
        stayed.remove(sender());
    }

    private void regisNewTram() {
        stayed.add(sender());
    }

    private void checkIfAvailable() {
        if (allocated < MAX) {
            allocated++;
            sender().tell(Command.STOPPING_IS_EMPTY, self());
            if(Objects.equals(allocated, MAX)) {
                System.out.println(name + " — повна");
            }
        } else {
            sender().tell(Command.STOPPING_IS_FULL, self());
        }
    }

    public enum Command {
        STOPPING_IS_EMPTY,
        STOPPING_IS_FULL
    }
}
