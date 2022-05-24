package Yazik;

public class BinOpNode extends Node{
    Token oper;
    Node lVal;
    Node rVal;
    public BinOpNode(Token oper, Node lVal, Node rVal) {
        super();
        this.oper = oper;
        this.lVal = lVal;
        this.rVal = rVal;
    }
}