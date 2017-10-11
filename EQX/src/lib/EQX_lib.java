package lib;
import java.util.*;
public class EQX_lib extends Lib {
    private final Map<String, Value> var;
    private final Map<String, Integer> lbl;
    private int curStt;
    /**
     * Dinhi na method kay mao ni ang pag lexical sa usa ka statement
     * */
    private List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<Token>();
        String token = "";
        TokenizeState state = TokenizeState.DFLT;
        /**
         * dinhi dapat equal ang index sa array na charTokens ug sa List TokenTypes para magka parehas cya ug unas na meaning 
         * ang gi match sa token
         * _P and P_ stands for parenthesis gamiton ni cya sa parsing sa statement para ma queue cya kinsay unahon e execute()
         * */
        String charTokens = "\n=+-*/<>()";
        TokenType[] tokenTypes = { TokenType.LINE, TokenType.EQUALS,
            TokenType.OPRTR, TokenType.OPRTR, TokenType.OPRTR,
            TokenType.OPRTR, TokenType.OPRTR, TokenType.OPRTR,
            TokenType._P, TokenType.P_
        };
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (state) {
            //DFLT stands for default ni cya kung wala cya nag match sa uban tokenType so e ignore ni niya para dili mag error
            case DFLT:
            	//ginacheck ang source or ang segmented statement if DEFAULT bani cya na token or kanang wala cyay bili murag gugma
                if (charTokens.indexOf(c) != -1) {
                    tokens.add(new Token(Character.toString(c),tokenTypes[charTokens.indexOf(c)]));
                } else if (Character.isLetter(c)) {
                    token += c;
                    state = TokenizeState.WRD;
                } else if (Character.isDigit(c)) {
                    token += c;
                    state = TokenizeState.NUM;
                } else if (c == '"') {
                    state = TokenizeState.STR;
                } 
                break;
            case WRD:
                if (Character.isLetterOrDigit(c)) { token += c; } 
                else {
                    tokens.add(new Token(token, TokenType.WRD));
                    token = "";
                    state = TokenizeState.DFLT;
                    i--; 
                }
                break;
            case NUM:
                if (Character.isDigit(c)) { token += c; } 
                else {
                    tokens.add(new Token(token, TokenType.NUM));
                    token = "";
                    state = TokenizeState.DFLT;
                    i--; 
                }
                break;
            case STR:
                if (c == '"') {
                    tokens.add(new Token(token, TokenType.STR));
                    token = "";
                    state = TokenizeState.DFLT;
                } else {token += c;}
                break;
            }
        }
        return tokens;
    }
    /**
     *(PARSER STATEMENT)
     * kani na class kay gihimu ni aron makuha ang pag ka sunod-sunod sa kada expressions ug statement sa usa ka 
     * code ug para ma butangan pud kong unsa na position sya ma execute ug kanusa cya ma trigger sa pag execute sa code
     * kintahay ang mga token naka queue na na sila sa prepective na butang kibali by hirarchy cya kung kinsa ang una 
     * */
    private class Parser {
    	//constructor sa parser na class na naay parameter na list<token>a
        public Parser(List<Token> tokens) {
            this.tokens = tokens;
            //mao ni ang starting sa position sa usa ka code nag depende sa token ug sa parenthesis
            pos = 0;
        }
        public List<Statement> parse(Map<String, Integer> labels) {
            List<Statement> statements = new ArrayList<Statement>();
            //dinhia gi check if nag match ba cya sa TokenType og sa statement na gikan sa enterpreter
            while (true) {
                while (match(TokenType.LINE));
                if (match(TokenType.LABEL)) {
                    labels.put(last(1).text, statements.size());
                } else if (match(TokenType.WRD, TokenType.EQUALS)) {
                    String name = last(2).text;
                    Expression value = expression();
                    statements.add(new AssignStatement(name, value));
                } else if (match("print")) {
                    statements.add(new PrintStatement(expression()));
                } else break; 
            }
            return statements;
        }
        //dinhi gi tawag ang expressions na operator na naay DataType na Expression kini na method nag hatag ug meaning 
        private Expression expression() { return operator(); }
        private Expression operator() {
            Expression expression = atomic();
            while (match(TokenType.OPRTR) || match(TokenType.EQUALS)) {
                char operator = last(1).text.charAt(0);
                Expression right = atomic();
                expression = new OperatorExpression(expression, operator, right);
            }
            return expression;
        }
        private Expression atomic() {
            if (match(TokenType.WRD)) {
                return new VariableExpression(last(1).text);
            } else if (match(TokenType.NUM)) {
                return new NumberValue(Integer.parseInt(last(1).text));
            } else if (match(TokenType.STR)) {
                return new StringValue(last(1).text);
            } else if (match(TokenType._P)) {
                Expression expression = expression();
                consume(TokenType.P_);
                return expression;
            }
            throw new Error("Couldn't parse :(");
        }
        private boolean match(TokenType type1, TokenType type2) {
            if (get(0).type != type1) return false;
            if (get(1).type != type2) return false;
            pos += 2;
            return true;
        }
        //method na kung na match ba tong sa tokenType like NUMBER,WORDS,OPERATORS . etc
        private boolean match(TokenType type) {
            if (get(0).type != type) return false;
            pos++;
            return true;
        }
        //Method na kung e match ang string
        private boolean match(String name) {
            if (get(0).type != TokenType.WRD) return false;
            if (!get(0).text.equals(name)) return false;
            pos++;
            return true;
        }
        private Token consume(TokenType type) {
            if (get(0).type != type) throw new Error("Expected " + type + ".");
            return tokens.get(pos++);
        }
        //kuhaon niya ang pinaka last na offset
        private Token last(int offset) {return tokens.get(pos - offset);}
        //kuhaon niya ang sulod sa position
        private Token get(int offset) {
            if (pos + offset >= tokens.size()) {
                return new Token("", TokenType.EOF);
            }
            return tokens.get(pos + offset);
        }
        private final List<Token> tokens;
        private int pos;
    }
    //kani na interface gi kuha nya ang method na execute gikan sa EQX_lib
    public interface Statement {void execute();}
    //interface na expression na gikan sa method na evaluate
    public interface Expression { Value evaluate();}
    /**
     * kani na class kay diri gina print ang mga na normalize na mga statement
     * gikan sa source padulong sa pag lexical then padulong sa pag parse ug pag execute sa interpreter
     * */
    public class PrintStatement implements Statement {
        private final Expression ex;
        public PrintStatement(Expression expression) {
            this.ex = expression;
        }
        public void execute() {
            System.out.println(ex.evaluate().toString());
        }
    }
    /**
     * kani na class kay gi gamit ni para mag assign og variable sa usa ka value
     * a = 12
     * print a
     * */
    public class AssignStatement implements Statement {
        public AssignStatement(String name, Expression value) {
            this.name = name;
            this.value = value;
        }
        public void execute() {var.put(name, value.evaluate());}
        private final String name;
        private final Expression value;
    }
    public class VariableExpression implements Expression {
        public VariableExpression(String name) {
            this.name = name;
        }
        public Value evaluate() {
            if (var.containsKey(name)) {
                return var.get(name);
            }
            return new NumberValue(0);
        }
        private final String name;
    }
    /**
     * mao ni gi segmented ang usa ka expression like for example (3 + 5) 
     * mao ni ang pagka breakdown sa token
     * (LEFT EXPRESSION , 3)
     * (OPERATOR, +)
     * (RIGHT EXPRESSION, 5)
     * */
    public class OperatorExpression implements Expression {
        public OperatorExpression(Expression left, char operator, Expression right) {
            this._l = left;
            this.oprtr = operator;
            this.r_ = right;
        }
        public Value evaluate() {
            Value leftVal = _l.evaluate();
            Value rightVal = r_.evaluate();
            //mao ni gi check kung unsa na mga operator ang nag exist sa usa ka statement
            //dihia gi na compute ang mga arithmetic operations like addition, subtraction, multiplication and division also
            switch (oprtr) {
            case '=':
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber() == rightVal.toNumber()) ? 1 : 0);
                } else {
                    return new NumberValue(leftVal.toString().equals(rightVal.toString()) ? 1 : 0);
                }
            case '+':
                if (leftVal instanceof NumberValue) {
                    return new NumberValue(leftVal.toNumber() + rightVal.toNumber());
                } else {
                    return new StringValue(leftVal.toString() +rightVal.toString());
                }
            case '-':
                return new NumberValue(leftVal.toNumber() - rightVal.toNumber());
            case '*':
                return new NumberValue(leftVal.toNumber() * rightVal.toNumber());
            case '/':
                return new NumberValue(leftVal.toNumber() / rightVal.toNumber());
            }
            throw new Error("Unknown operator.");
        }
        private final Expression _l, r_;
        private final char oprtr;
    }
    public interface Value extends Expression {
        String toString();
        int toNumber();
    }
   //kani na class kay para sa expression na ang dataType kay Intger form tokenize na method dinhi niya gina padulong tanan match na integer
    public class NumberValue implements Value {
        //constructor for NumberValue
        public NumberValue(int value) {this.value = value; }
        @Override 
        public String toString() { return Integer.toString(value); }
        public int toNumber() { return value; }
        public Value evaluate() { return this; }
        private final int value;
    }
    //kani na class kay for the expression na ang dataType kay String 
    public class StringValue implements Value {
    	private final String value;
    	/**Constructor sa StringValue na class:*/ 
        public StringValue(String value) {this.value = value;}
        @Override public String toString() { return value; }
        public int toNumber() { return Integer.parseInt(value); }
        public Value evaluate() { return this; }
    }
    //constructor sa Library
    public EQX_lib() {
    	/**
    	 * Constructor sa EQX_lib na class: 
    	 * Iyahang mga variables : [variables as HashMap<String, Value>,
    	 * 				labels as HashMap<String,Integer>,
    	 * 				converter as InputStreamReader,
    	 * 				lineIn as BufferedReader with @param converter]
    	 * */ 
    	var = new HashMap<String, Value>();
        lbl = new HashMap<String, Integer>();
    }
    /**
     * dinhi na method mag sugod ang tanan para makuha ang mga token ug ang every every meaning sa usa ka source or file,
     * e interpret niya ang sulod na data gikan sa source code
     * @param source/files
     */
    public void interpret(String source) {
    	//Ang data nga sulod sa usa ka source/file kay gisulod sa usa ka list(data)
        List<Token> tokens = tokenize(source);
        //gi Instance or ang Parse na class para didto isulod ang list of data gikan sa source/file
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse(lbl);
        //gi set ang current statement into 0
        curStt = 0;
        //while ang currentStatement kay gamay sa size sa statement na gikang gikuha sa Parse na Class
        while (curStt < statements.size()) {
        	//gibutang sa thisStatement ang kadtong current na statment while true
            int thisStatement = curStt;
            //so gi increment ang currentStatement na counter
            curStt++;
            //gi execute dayon ang kadto na napiliian na statement
            statements.get(thisStatement).execute();
        }
    }
}
