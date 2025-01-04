package tanktrouble.game.client.util.actions;

import tanktrouble.game.client.util.connection.NamedConnection;
import tanktrouble.game.client.util.destination.Destination;
import tanktrouble.game.client.util.destination.Source;

import java.util.LinkedList;
import java.util.ListIterator;

public class ActionManagerClient extends ActionManager {

    // Responsible for tracking emitted action requests which haven't been acknowledged yet
    private final LinkedList<Action> acknowledgementBuffer = new LinkedList<>();

    // Actions received are placed into here until an acknowledgement is received at which point the buffer is flushed
    // all DR actions are reverted, these actions are applied, then all DR actions are re-applied
    private final LinkedList<Action> preAcknowledgementFutureBuffer = new LinkedList<>();

    private boolean validDeadReckonState = true;

    public ActionManagerClient(NamedConnection connection) {
        super(connection);
    }

    @Override
    public boolean sendActionRequest(Action action) {
        if(validDeadReckonState){
            boolean result = action.apply();
            if(result) {
                acknowledgementBuffer.push(action);
                try {
                    emitAction(action, new Destination(Destination.Policy.ALL));
                }catch (Exception e){
                    e.printStackTrace();
                    acknowledgementBuffer.pop();
                    return false;
                }
            }
            return result;
        }else{
            return false;
        }
    }

    @Override
    protected void processRollbackAcknowledgement(Source source) {
        // Do nothing - not a valid client operation
    }

    @Override
    public void processActionRequest(Action action, Source source) {
        if(!acknowledgementBuffer.isEmpty()) {
            preAcknowledgementFutureBuffer.push(action);
        }else{
            action.apply();
        }
    }

    @Override
    public void processActionAcknowledgement(ActionAcknowledgement actionAcknowledgement, Source source) {
        if(actionAcknowledgement.result) {
            if(validDeadReckonState){
                revertDeadReckon();
            }
            while (!preAcknowledgementFutureBuffer.isEmpty()) {
                Action action = preAcknowledgementFutureBuffer.pollLast();
                action.apply();
            }
            Action action = acknowledgementBuffer.pollLast();
            action.apply();
            if(acknowledgementBuffer.isEmpty()){
                while (!preAcknowledgementFutureBuffer.isEmpty()) {
                    Action recvAction = preAcknowledgementFutureBuffer.pollLast();
                    recvAction.apply();
                }
            }
            attemptApplyDeadReckon();
        }else{
            flushAcknowledgementBuffer();
            while (!preAcknowledgementFutureBuffer.isEmpty()) {
                Action action = preAcknowledgementFutureBuffer.pollLast();
                action.apply();
            }
            try {
                emitRollbackAcknowledgement(new Destination(Destination.Policy.ALL));
            }catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void flushAcknowledgementBuffer(){
        while(!acknowledgementBuffer.isEmpty()){
            Action action = acknowledgementBuffer.pollLast();
            if(validDeadReckonState) {
                action.revert();
            }
        }
        validDeadReckonState = true;
    }

    private void revertDeadReckon(){
        validDeadReckonState = false;
        ListIterator<Action> iterator = acknowledgementBuffer.listIterator(acknowledgementBuffer.size());
        while (iterator.hasPrevious()) {
            Action actionToRevert = iterator.previous();
            actionToRevert.revert();
        }
    }

    private void attemptApplyDeadReckon(){
        validDeadReckonState = true;
        for(Action action : acknowledgementBuffer){
            if(!action.apply()){
                validDeadReckonState = false;
                ListIterator<Action> iterator = acknowledgementBuffer.listIterator(acknowledgementBuffer.size());
                while (iterator.hasPrevious()) {
                    Action actionToRevert = iterator.previous();
                    if(action == actionToRevert) break;
                    actionToRevert.revert();
                }
                break;
            }
        }
    }
}
