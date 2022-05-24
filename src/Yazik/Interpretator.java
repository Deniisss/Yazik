package Yazik;

import java.util.HashMap;

public class Interpretator {
    public static HashMap<String,String> scope=new HashMap<>();

    public String run(Node node){
        if (node.getClass()==BinOpNode.class) {
            if (((BinOpNode) node).oper.type.typeName.equals("ASSIGN")) {
                String res = this.run(((BinOpNode) node).rVal);
                VarNode varNode=(VarNode)((BinOpNode) node).lVal;
                scope.put(varNode.var.value, res);
                return res;
            }
            else
            {
                int left=Integer.parseInt(this.run(((BinOpNode) node).lVal));
                int right=Integer.parseInt(this.run(((BinOpNode) node).rVal));
                return switch (((BinOpNode) node).oper.type.typeName) {
                    case "PLUS" -> String.valueOf(left + right);
                    case "MINUS" -> String.valueOf(left - right);
                    case "MULT" -> String.valueOf(left * right);
                    case "DIV" -> String.valueOf(left / right);
                    default -> throw new Error("Недопустимая операция");
                };
            }
        }
        if (node.getClass()==VarNode.class) {
            return scope.get(((VarNode) node).var.value);
        }
        if (node.getClass()==NumNode.class) {
            return ((NumNode) node).num.value;
        }
        if (node.getClass()==INode.class){
            int left=Integer.parseInt(this.run(((INode) node).lVal));
            int right=Integer.parseInt(this.run(((INode) node).rVal));
            switch (((INode) node).oper.type.typeName) {
                case "LESS":
                    if (left < right) {
                        for (int i = 0; i < ((INode) node).thenOperations.size(); i++)
                            this.run(((INode) node).thenOperations.get(i));
                    }
                    else{
                        for (int i = 0; i < ((INode) node).elseOperations.size(); i++)
                            this.run(((INode) node).elseOperations.get(i));
                    }
                    break;
                case "MORE":
                    if (left > right) {
                        for (int i = 0; i < ((INode) node).thenOperations.size(); i++)
                            this.run(((INode) node).thenOperations.get(i));
                    }
                    else{
                        for (int i = 0; i < ((INode) node).elseOperations.size(); i++)
                            this.run(((INode) node).elseOperations.get(i));
                    }
                    break;
                case "EQUAL":
                    if (left == right) {
                        for (int i = 0; i < ((INode) node).thenOperations.size(); i++)
                            this.run(((INode) node).thenOperations.get(i));
                    }
                    else{
                        for (int i = 0; i < ((INode) node).elseOperations.size(); i++)
                            this.run(((INode) node).elseOperations.get(i));
                    }
                    break;
            }
        }
        if (node.getClass()==WhileNode.class){
            int left=Integer.parseInt(this.run(((WhileNode) node).lVal));
            int right=Integer.parseInt(this.run(((WhileNode) node).rVal));
            switch (((WhileNode) node).oper.type.typeName) {
                case "LESS":
                    while (left < right) {
                        for (int i = 0; i < ((WhileNode) node).operations.size(); i++)
                            this.run(((WhileNode) node).operations.get(i));
                        left = Integer.parseInt(this.run(((WhileNode) node).lVal));
                        right = Integer.parseInt(this.run(((WhileNode) node).rVal));
                    }
                    break;
                case "MORE":
                    while (left > right) {
                        for (int i = 0; i < ((WhileNode) node).operations.size(); i++)
                            this.run(((WhileNode) node).operations.get(i));
                        left = Integer.parseInt(this.run(((WhileNode) node).lVal));
                        right = Integer.parseInt(this.run(((WhileNode) node).rVal));
                    }
                    break;
                case "EQUAL":
                    while (left == right) {
                        for (int i = 0; i < ((WhileNode) node).operations.size(); i++)
                            this.run(((WhileNode) node).operations.get(i));
                        left = Integer.parseInt(this.run(((WhileNode) node).lVal));
                        right = Integer.parseInt(this.run(((WhileNode) node).rVal));
                    }
                    break;
            }
        }
        if (node.getClass()==FNode.class){
            int left=Integer.parseInt(this.run(((FNode) node).lVal));
            int right=Integer.parseInt(this.run(((FNode) node).rVal));
            switch (((FNode) node).oper.type.typeName) {
                case "LESS":
                    while (left < right) {
                        for (int i = 0; i < ((FNode) node).operations.size(); i++)
                            this.run(((FNode) node).operations.get(i));
                        this.run(((FNode) node).action);
                        left = Integer.parseInt(this.run(((FNode) node).lVal));
                        right = Integer.parseInt(this.run(((FNode) node).rVal));
                    }
                    break;
                case "MORE":
                    while (left > right) {
                        for (int i = 0; i < ((FNode) node).operations.size(); i++)
                            this.run(((FNode) node).operations.get(i));
                        this.run(((FNode) node).action);
                        left = Integer.parseInt(this.run(((FNode) node).lVal));
                        right = Integer.parseInt(this.run(((FNode) node).rVal));
                    }
                    break;
                case "EQUAL":
                    while (left == right) {
                        for (int i = 0; i < ((FNode) node).operations.size(); i++)
                            this.run(((FNode) node).operations.get(i));
                        this.run(((FNode) node).action);
                        left = Integer.parseInt(this.run(((FNode) node).lVal));
                        right = Integer.parseInt(this.run(((FNode) node).rVal));
                    }
                    break;
            }
        }
        return "";
    }
}