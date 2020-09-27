import java.io.IOException;

public class Start {
	static ProxyGenerator pg = new ProxyGenerator();
	
	public static void main(String[] args) throws IOException {
		pg.generateProxy();
	}

}
