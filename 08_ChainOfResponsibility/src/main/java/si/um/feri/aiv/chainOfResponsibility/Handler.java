package si.um.feri.aiv.chainOfResponsibility;

import si.um.feri.aiv.vao.MalaSoncnaElektrarna;

public abstract class Handler {
    protected Handler successor;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public abstract void handleRequest(MalaSoncnaElektrarna mse);
}
