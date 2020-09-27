import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ProxyGenerator {
	Scanner s = new Scanner(System.in);
	
	public void generateProxy () throws IOException {
		final String path = "C:" + File.separator + "temp";
		Path file = Paths.get(path + File.separator +  "Test.java");	
		List<String> lines = new ArrayList<String>();
        lines.add("/*");
        lines.add(" * @generated");
        lines.add(" */");
        lines.add("");
        lines.add("package generator;");
        lines.add("");
        
        int e = defineProxy(); //je nach Proxy andere implementierung
        switch (e) {
        	case 1: lines.add("public class .... implements ... {"); 
        		lines.add(""); 
        		
        	break;
        	case 2: lines.add("public class ... implements ... {"); 
        		lines.add(""); 
        		
        	break;
        	case 3: lines.add("public class ... implements ... {");
        		lines.add(""); 
        		
        	break;
        	case 4: lines.add("public class ... implements ... {");
        		lines.add(""); 
        		
        	break;
        }
        Files.write(file, lines, Charset.forName("UTF-8"));
	}

	private int defineProxy() {
		boolean getName = false;
		boolean hear = false;
		boolean leave = false;
		boolean enter = false;
		boolean setStone = false;
		boolean getStone = false;
		Method[] methods = InputInterface.class.getDeclaredMethods();
		
		for(int i=0; i < methods.length; i++) {
			
			System.out.println(methods[i]);		
			String m = methods[i].toString();
			switch (m) {
				case "public abstract java.lang.String generator.InputInterface.getName()" : System.out.println("getName"); 
					getName = true;
				
				break;
				case "public abstract void generator.InputInterface.hear(java.lang.String)" : System.out.println("hear"); 
					hear = true;
				
				break;
				case "public abstract void generator.InputInterface.leave(generator.IChatter)" : System.out.println("leave"); 
					leave = true;
				
				break;
				case "public abstract void generator.InputInterface.enter(generator.IChatter)" : System.out.println("enter"); 
					enter = true;
				
				break;
				case "public abstract java.lang.Char generator.InputInterface.setStone()" : System.out.println("setStone");
					setStone = true;
				break;
				case "public abstract java.lang.Char generator.InputInterface.getStone()" : System.out.println("getStone");
					getStone = true;
					
				break;
				default: handleError(); break;
				
			}
		}
		
		//hier muss dann überprüft werden anhand der booleaschen werte um welches Interface es sich handelt
		//je nachdem wird dann der zu erstellene Proxy definiert
		
		
		return 0;
	}

	private void handleError() {
		System.err.println("Protokollfehler!");
	}
}

