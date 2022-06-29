import java.util.*;
import java.io.File;

public class XML {
	String xml = "";
	ArrayList<String> token;

	public XML(String xml) {
		this.xml = xml;
		this.token = new ArrayList<String>();
	}

	public String parseXML() {
		xml = xml.trim();
		if(xml.length() == 0) {
			return "";
		}
		else if(xml.charAt(0)=='<' && xml.charAt(1) != '/') {
			String st = startTag();
			//token.add(st);
			String r = parseXML();
			boolean result = endTag(st);
			String r1 = parseXML();
			if(!result || r1==null || r == null) {
				System.out.println("Invalid XML!!!");
				return null;
			}
			if(r1.length()>0) {
				return "{'"+st+"':"+r+","+r1.substring(1, r1.length()-1)+"}";
			}
			else {
				
				return "{'"+st+"':"+r+"}";
			}
		}
		else{
			String content = content();
			//token.add(content);
			return content;
		}
	}
	public String content() {
		int e = xml.indexOf("<");
		String result = xml.substring(0, e);
		if(!result.equals("")) {
			if(!(isBoolean(result) || isNumber(result))) {
				result = "'" + result + "'";
			}	
			token.add(result);
		}
		xml = xml.substring(e);
		return result;
	}
	public boolean isBoolean(String str) {
		return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
	}
	public boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	public String startTag() {
		int s = xml.indexOf("<") + 1;
		int e = xml.indexOf(">");
		String result = xml.substring(s, e);
		xml= xml.substring(e+1);
		token.add(result);
		return result;
	}
	public boolean endTag(String start) {
		int s = xml.indexOf("<") + 1;
		int e = xml.indexOf(">");
		if(e<0 || s<0) {
			return false;
		}
		String tag = xml.substring(s+1, e);
		boolean result = xml.charAt(s) == '/' && start.equalsIgnoreCase(tag);
		xml = xml.substring(e+1);
		return result;
	}
	public static void main(String args[]) {
		//Reading input from the file.
		System.out.println("Input: ");
		System.out.println("----------------------------------------------------------------------");
		String input = "";
		try{
			File file = new File(".\\input.txt");
			Scanner scan = new Scanner(file);
			String line = "";
			while(scan.hasNextLine()){
				line =scan.nextLine();
				if(!line.equals("")){
					System.out.println(line);
					input +=line;
				}
			}
		}catch(Exception e){
			System.out.println("File could not Loaded!!");
		}
		
		System.out.println("----------------------------------------------------------------------");
		//<A>    <F>      </F><D>true</D><C>abc</C><B>Text</B><E>23</E></A><A><D>true</D><C>abc</C><B>Text</B><E>23</E></A>
		XML file = new XML(input);
		String output = file.parseXML();
		System.out.println("Tokens : "+file.token.toString());
		System.out.println("----------------------------------------------------------------------");
		System.out.println(format(output));
		System.out.println("----------------------------------------------------------------------");
		
	}
	public static String format(String str) {
		if(str == null) {
			System.out.print("NOT Valid JSON");
			return "";
		}
		String result = "";
		int tabs = 0;
		for(int i=0; i<str.length(); i++) {
			if(str.charAt(i) == '{') {
				tabs++;
				result += "{\n";
				for(int j=0; j<tabs; j++)
					result += "\t";
			}
			else if(str.charAt(i)=='}'){
				result+="\n";
				tabs--;
				for(int j=0; j<tabs; j++)
					result += "\t";
				
				result += "}";
			}
			else if(str.charAt(i) == ',') {
				result += ",\n";
				for(int j=0; j<tabs; j++)
					result += "\t";
			}
			else {
				result += str.charAt(i);
			}
			
		}
		return result;
	}
	
}
