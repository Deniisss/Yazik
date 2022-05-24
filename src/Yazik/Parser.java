package Yazik;

import java.util.ArrayList;

public class Parser {
    ArrayList<Token> tokens;
    int pos=0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }
    public Token receive(String[] need){
        Token curToken;
        if (pos<tokens.size()) {
            curToken = tokens.get(pos);
            for (String tokenTypeName : need)
                if (tokenTypeName.equals(curToken.type.typeName)) {
                    pos++;
                    return curToken;
                }
        }
        return null;
    }
    public Node parsFormula(){
        Node lVal= parsMultDiv();
        Token oper= receive(new String[]{"PLUS","MINUS"});
        while (oper!=null){
            Node rVal= parsMultDiv();
            lVal=new BinOpNode(oper,lVal,rVal);
            oper= receive(new String[]{"PLUS","MINUS"});
        }
        return lVal;
    }
    public Node parsVarNum(){
        if (tokens.get(pos).type.typeName.equals("NUM")){
            pos++;
            return new NumNode(tokens.get(pos-1));
        }
        if (tokens.get(pos).type.typeName.equals("VAR")){
            pos++;
            return new VarNode(tokens.get(pos-1));
        }
        throw new Error("Предполагается переменная или число на месте: "+pos);
    }
    public Node parsMultDiv(){
        Node lVal= parsPar();
        Token oper= receive(new String[]{"MULT","DIV"});
        while (oper!=null){
            Node rVal= parsPar();
            lVal=new BinOpNode(oper,lVal,rVal);
            oper= receive(new String[]{"MULT","DIV"});
        }
        return lVal;
    }
    public Node parsPar(){
        if (tokens.get(pos).type.typeName.equals("LPAR")){
            pos++;
            Node node = parsFormula();
            need(new String[]{"RPAR"});
            return node;
        }
        else
            return parsVarNum();
    }
    public Node parsString(){
        switch (tokens.get(pos).type.typeName) {
            case "VAR" -> {
                Node varNode = parsVarNum();
                Token oper = receive(new String[]{"ASSIGN"});
                if (oper != null) {
                    Node rVal = parsFormula();
                    return new BinOpNode(oper, varNode, rVal);
                }
                throw new Error("После переменной предполагается бинарный оператор на месте:" + pos);
            }
            case "PRINT" -> {
                pos++;
                return new UnOp(tokens.get(pos - 1), this.parsFormula());
            }
            case "IF" -> {
                pos++;
                return parsIf();
            }
            case "WHILE" -> {
                pos++;
                return parsWhile();
            }
            case "FOR" -> {
                pos++;
                return parsFor();
            }
        }
        throw new Error("Ошибка на месте: "+pos+". Предполагалось действие или переменная");
    }
    public void need(String[] expected){
        Token token= receive(expected);
        if(token==null){
            throw new Error("На месте "+pos+" предполагалось"+expected[0]);
        }
    }
    public Node parsIf(){
        Node leftVal= parsFormula();
        Token operator=receive(new String[]{"LESS","MORE","EQUAL"});
        Node rightVal= parsFormula();
        INode iNode =new INode(operator,leftVal,rightVal);
        need(new String[]{"LRectPar"});
        while(!tokens.get(pos).type.typeName.equals("RRectPAR")) {
            iNode.addThenOperations(getOperations());
            if (pos==tokens.size())
                throw new Error("Ошибка, ожидалось }");
        }
        pos++;
        need(new String[]{"ELSE"});
        need(new String[]{"LRectPar"});
        while(!tokens.get(pos).type.typeName.equals("RRectPAR")) {
            iNode.addElseOperations(getOperations());
            if (pos==tokens.size())
                throw new Error("Ошибка, предполагалось }");
        }
        pos++;
        return iNode;
    }
    public Node parsFor(){
        Node leftVal= parsFormula();
        Token operator=receive(new String[]{"LESS","MORE","EQUAL"});
        Node rightVal= parsFormula();

        need(new String[]{"END"});

        Node varNode = parsVarNum();
        Token assign = receive(new String[]{"ASSIGN"});
        Node rightActVal = parsFormula();
        BinOpNode action= new BinOpNode(assign, varNode, rightActVal);
        if (assign == null)
            throw new Error("После переменной ожидается = на позиции:"+pos);
        FNode forNode= new FNode(operator,leftVal,rightVal,action);
        need(new String[]{"LRectPar"});
        while(!tokens.get(pos).type.typeName.equals("RRectPAR")) {
            forNode.addOperations(getOperations());
            if (pos==tokens.size())
                throw new Error("Ошибка, ожидалось }");
        }
        pos++;
        return forNode;
    }
    public Node parsWhile(){
        Node leftVal= parsFormula();
        Token operator=receive(new String[]{"LESS","MORE","EQUAL"});
        Node rightVal= parsFormula();
        WhileNode whileNode=new WhileNode(operator,leftVal,rightVal);
        need(new String[]{"LRectPar"});
        while(!tokens.get(pos).type.typeName.equals("RRectPAR")) {
            whileNode.addOperations(getOperations());
            if (pos==tokens.size())
                throw new Error("Ошибка, предполагалось }");
        }
        pos++;
        return whileNode;
    }
    public Node getOperations(){
        Node codeStringNode= parsString();
        need(new String[]{"END"});
        return codeStringNode;
    }
    public RootNode parsTokens(){
        RootNode root=new RootNode();
        while (pos<tokens.size()){
            Node codeStringNode= parsString();
            need(new String[]{"END"});
            root.addNode(codeStringNode);
        }
        return root;
    }
}
