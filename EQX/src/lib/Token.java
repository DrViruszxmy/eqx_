package lib;


class Token extends Lib{
	public final String text;
    public final TokenType type;
    
    public Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }
    
    
}