package Yazik;

import java.util.ArrayList;

public class WhileNode extends Node{
    Token oper;
    Node lVal;
    Node rVal;
    public ArrayList<Node> operations = new ArrayList<>();

    public WhileNode(Token oper, Node lVal, Node rVal) {
        this.oper = oper;
        this.lVal = lVal;
        this.rVal = rVal;
    }
    public void addOperations(Node op){
        operations.add(op);
    }
}
