import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
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
	List<String> lines = new ArrayList<String>();
	List<String> linesSec = new ArrayList<String>();
	boolean postField = false;
	boolean hear = false;
	boolean say = false;
	boolean getStone = false;
	boolean leave = false;
	boolean enter = false;
	boolean positionStone = false;
	boolean getName = false;
	boolean getField = false;
	String rname = IPlayroom.class.getName();
	String name = IPlayroom.class.getName().substring(1);
	
	public void generateFirstProxy () throws IOException {
		final String path = "C:" + File.separator + "temp";
		Path file = Paths.get(path + File.separator +  "FirstProxy.java");	
		generateSecProxy();
        lines.add("/*");
        lines.add(" * @generated");
        lines.add(" */");
        lines.add("");
        lines.add("package ;");
		lines.add("");
		lines.add("Public class "+name+"ServerProxy implements Runnable{");
		lines.add("Socket socket;");
		lines.add("BufferedReader reader;");
		lines.add("PrintWriter writer;");
		lines.add("boolean running = true;");
		lines.add(rname + " " + name.toLowerCase() + ";");
		lines.add(""); 
		lines.add("public "+name+"ServerProxy(Socket socket, "+rname + " " + name.toLowerCase() + ") throws IOException {");
		lines.add("writer = new FlushingWriter(socket.getOutputStream());");
		lines.add("reader = new LoggingReader(new InputStreamReader(socket.getInputStream())");
		lines.add("this.socket = socket;");
		lines.add("}");
		lines.add(""); 
		lines.add("public void run() {"); 
		lines.add("while(running){"); 
		lines.add("try {"); 
		defineProxy();
		defineFirstProxyCaseMethods();
		lines.add("}");
		lines.add("} catch (Exception e) {");
		lines.add("e.printStackTrace");
		lines.add("}");
		lines.add("}");
		lines.add("private void handleError() {");
		lines.add("System.err.println(\"Protokollfehler!\");");
		lines.add("writer.println(\"Protokollfehler Serverseitig!\");");
		lines.add("}");
		defineFirstProxyMainMethods();

		//hier muss noch die deserialize player hinzugefügt werden

        Files.write(file, lines, Charset.forName("UTF-8"));
	}

	private void defineProxy() {
		// Alle Methoden des Interfaces durchgehen und je nach Methode in Lines erstellen
		Method[] methods = IPlayroom.class.getDeclaredMethods();	
		for(int i=0; i < methods.length; i++) {		
			System.out.println(methods[i]);		
			String m = methods[i].toString();
			if (m.contains("postField")){
				postField = true;
			} else if (m.contains("hear")){
				hear = true;
			} else if (m.contains("leave")){
				leave = true;
			} else if (m.contains("enter")){
				enter = true;
			} else if (m.contains("say")){
				say = true;
			} else if (m.contains("getStone")){
				getStone = true;
			} else if (m.contains("positionStone")){
				positionStone = true;
			} else if (m.contains("getName")){
				getName = true;
			} else if (m.contains("getField")){
				getField = true;
			} else {
				handleError();
			}
		}
	}

	// hier werden die einzelnen Cases der Run-Methode definiert
	private void defineFirstProxyCaseMethods() {
		int i = 1;
		int j = 0;
		String line = "writer.println( \"Was willst du tun? ";
		String caseList[] = new String[32];
		if (enter) {
			line = line + i + ": einloggen, ";
			caseList[j] = "case "+i+ ": enter(); break;";
			i = i + 1;
			j = j + 1;
		} 
		if (getName) {
			line = line + i + " getName, ";
			caseList[j] = "case "+i+ ": getName(); break;";
			i = i + 1;
			j = j + 1;		
		} 
		if (say) {
			line = line + i + " say, ";
			caseList[j] = "case "+i+ ": say(); break;";
			i = i + 1;
			j = j + 1;		
		} 
		if (postField) {
			line = line + i + " postField, ";
			caseList[j] = "case "+i+ ": postField(); break;";
			i = i + 1;
			j = j + 1;		
		} 
		if (hear) {
			line = line + i + ": hear, ";
			caseList[j] = "case "+i+ ": hear(); break;";
			i = i + 1;
			j = j + 1;		
		} 
		if (getStone) {
			line = line + i + " getStone, ";
			caseList[j] = "case "+i+ ": getStone(); break;";
			i = i + 1;
			j = j + 1;		
		} 
		if (getField) {
			line = line + i + " getField, ";
			caseList[j] = "case "+i+ ": getField(); break;";
			i = i + 1;
			j = j + 1;		
		} 
		if (leave) {
			line = line + i + " leave, ";
			caseList[j] = "case "+i+ ": leave(); break;";
			i = i + 1;
			j = j + 1;		
		} 

		line = line + i + " exit.";
		caseList[j] = "case "+i+ ": running = false; break;";
		i = i + 1;
		j = j + 1;
		caseList[j] = "default: handleError();";
		lines.add(line);
		for (String s : caseList){
			if (s != null){
				lines.add(s);
			}
		}
	}
	private void defineFirstProxyMainMethods() {
		int i = 0;
		int j = 0;
		String caseList[][] = new String[32][32];
		if (enter) {
			caseList[i][j] = "Public void enter() throws NumberFormatException,IOException {";
			j += 1;
//			Woher soll man wissen ob es ein Chatter/ Player ist? caseList[i][j] = IPlayer +""+ player "deserializedPlayer();"
			j += 1;
			caseList[i][j] = "try {";
			j += 1;
			caseList[i][j] = rname + ".enter(player);";
			j += 1;
			caseList[i][j] = "writer.println(\"Eingeloggt!\");";
			j += 1;
			caseList[i][j] = "} catch (Exception e) {";
			j += 1;
			caseList[i][j] = "e.printStackTrace();";
			j += 1;
			caseList[i][j] = "writer.println(e.getMessage);";
			j += 1;
			caseList[i][j] = "}";
			j += 1;
			caseList[i][j] = "}";
			i += 1;
		}
		if (say) {
			caseList[i][j] = "Public void say() throws NumberFormatException,IOException {";
			j += 1;
//			Woher soll man wissen ob es ein Chatter/ Player ist? caseList[i][j] = IPlayer +""+ player "deserializedPlayer();"
			j += 1;
			caseList[i][j] = "writer.println(\"Gib mir die Message!\");";
			j += 1;
			caseList[i][j] = "String msg = reader.readLine()";
			j += 1;
			caseList[i][j] = "try {";
			j += 1;
			caseList[i][j] = rname + ".say(msg, player);";
			j += 1;
			caseList[i][j] = "writer.println(\"Success!\");";
			j += 1;
			caseList[i][j] = "} catch (Exception e) {";
			j += 1;
			caseList[i][j] = "e.printStackTrace();";
			j += 1;
			caseList[i][j] = "writer.println(e.getMessage);";
			j += 1;
			caseList[i][j] = "}";
			j += 1;
			caseList[i][j] = "}";
			i += 1;
		}
		if (leave) {
			caseList[i][j] = "Public void leave() throws NumberFormatException,IOException {";
			j += 1;
//			Woher soll man wissen ob es ein Chatter/ Player ist? caseList[i][j] = IPlayer +""+ player "deserializedPlayer();"
			j += 1;
			caseList[i][j] = "try {";
			j += 1;
			caseList[i][j] = rname + ".leave(player);";
			j += 1;
			caseList[i][j] = "writer.println(\"Ausgeloggt!\");";
			j += 1;
			caseList[i][j] = "} catch (Exception e) {";
			j += 1;
			caseList[i][j] = "e.printStackTrace();";
			j += 1;
			caseList[i][j] = "writer.println(e.getMessage);";
			j += 1;
			caseList[i][j] = "}";
			j += 1;
			caseList[i][j] = "}";
			i += 1;
		}
		if (positionStone) {
			caseList[i][j] = "Public void positionStone() throws NumberFormatException,IOException {";
			j += 1;
//			Woher soll man wissen ob es ein Chatter/ Player ist? caseList[i][j] = IPlayer +""+ player "deserializedPlayer();"
			j += 1;
			caseList[i][j] = "Gib mir die Position!";
			j += 1;
			caseList[i][j] = "int row = reader.read();";
			j += 1;
			caseList[i][j] = "try {";
			j += 1;
			caseList[i][j] = rname + ".positionStone(player,row);";
			j += 1;
			caseList[i][j] = "writer.println(\"Success\");";
			j += 1;
			caseList[i][j] = "} catch (Exception e) {";
			j += 1;
			caseList[i][j] = "e.printStackTrace();";
			j += 1;
			caseList[i][j] = "writer.println(e.getMessage);";
			j += 1;
			caseList[i][j] = "}";
			j += 1;
			caseList[i][j] = "}";
			i += 1;
		}
		if (getName) {
			caseList[i][j] = "Public void getName() {";
			j += 1;
			caseList[i][j] = "writer.println(\"Success\");";
			j += 1;
			caseList[i][j] = "writer.println("+name.toLowerCase()+".getName());";
			j += 1;
			caseList[i][j] = "}";
			i += 1;
		}
		if (getStone) {
			caseList[i][j] = "Public void getName() {";
			j += 1;
			caseList[i][j] = "writer.println(\"Success\");";
			j += 1;
			caseList[i][j] = "writer.println("+name.toLowerCase()+".getStone());";
			j += 1;
			caseList[i][j] = "try {";
			j += 1;
			caseList[i][j] = "for (int j = 0; j < (i+2); j++) {";
			j += 1;
			caseList[i][j] = "playField = playField + reader.readLine() +\"\n\";";
			j += 1;
			caseList[i][j] = "writer.println(\"success\");";
			j += 1;
			caseList[i][j] = "}";
			j += 1;
			caseList[i][j] = name.toLowerCase() + ".hear(playField);";
			j += 1;
			caseList[i][j] = "} catch (Exception e) {";
			j += 1;
			caseList[i][j] = "e.printStackTrace();";
			j += 1;
			caseList[i][j] = "writer.println(e.getMessage);";
			j += 1;
			caseList[i][j] = "}";
			j += 1;
			caseList[i][j] = "}";
			i += 1;
		}
		if(getField){
			caseList[i][j] = "private void getField() throws IOException {";
			j += 1;
			caseList[i][j] = "String playField = \" \"";
			j += 1;
			caseList[i][j] = "writer.println(\"Wie viele Zeilen?\");";
			j += 1;
			caseList[i][j] = "int i = Integer.parseInt(reader.readLine());";
			j += 1;
			
		}
		if(hear){
			j += 1;
			caseList[i][j] = "writer.println(\"Welche Nachricht?\");";
			j += 1;
			caseList[i][j] = "String msg = reader.readLine();";
			j += 1;
			caseList[i][j] = "try {";
			j += 1;
			caseList[i][j] = name.toLowerCase() + ".hear(msg)";
			j += 1;
			caseList[i][j] = "writer.println(\"success\");";
			j += 1;
			caseList[i][j] = "} catch (Exception e) {";
			j += 1;
			caseList[i][j] = "e.printStackTrace();";
			j += 1;
			caseList[i][j] = "writer.println(e.getMessage);";
			j += 1;
			caseList[i][j] = "}";
			j += 1;
			caseList[i][j] = "}";
			i += 1;
		}
		for(int t = 0; t < caseList.length; t++){
			for(int s = 0; s < caseList.length; s++){
				if (caseList[t][s] != null){
					lines.add(caseList[t][s]);
				}
			}
		}
	}



	public void generateSecProxy() throws IOException {
		final String path = "C:" + File.separator + "temp";
		Path file = Paths.get(path + File.separator +  "SecondProxy.java");	
        linesSec.add("/*");
        linesSec.add(" * @generated");
        linesSec.add(" */");
        linesSec.add("");
        linesSec.add("package ;");
		linesSec.add("");
		linesSec.add("Public class "+name+"ClientProxy implements" + rname + "{");
		linesSec.add("Socket socket;");
		linesSec.add("BufferedReader reader;");
		linesSec.add("PrintWriter writer;");
		linesSec.add(""); 
		linesSec.add("public "+name+"ServerProxy(Socket socket) throws IOException {");
		linesSec.add("writer = new FlushingWriter(socket.getOutputStream());");
		linesSec.add("reader = new LoggingReader(new InputStreamReader(socket.getInputStream())");
		linesSec.add("this.socket = socket;");
		linesSec.add("this." + name.toLowerCase() + " = " + name.toLowerCase());
		linesSec.add("}");

		defineSecProxyMethods();
		
		//hier muss noch serialize player hinzugefügt werden

        Files.write(file, linesSec, Charset.forName("UTF-8"));
	}

	private void defineSecProxyMethods() {
	}


	private void handleError() {
		System.err.println("Fehlerhafte Methode!");
	}
}

