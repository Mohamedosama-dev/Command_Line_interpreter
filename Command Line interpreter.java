

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Scanner;


public class Terminal {
    private static Parser parser = new Parser();
	
	//Implement each command in a method, for example:
	public static String pwd(){
		//get the current directory and returned
		String pwd = System.getProperty("user.dir");
		return pwd;}
	
	
	
	
	
	// Change the Directory
	public static void cd(String[] args){
		String currentDirectory = System.getProperty("user.dir");
		String state = currentDirectory;
		// the "%%" is used as a dummy input to decide 
		// if the args[] is empty or not as other functions need to check it to work
		if(args[0].equals("%%")) {
			
			System.out.println(currentDirectory);
			return;
		}
		
		// Return to the Parent Directory
		if(args[0].equalsIgnoreCase("..")) {
			try {
			File    dir;
			dir = new File(currentDirectory).getParentFile().getAbsoluteFile();
			// Set the working Directory to the desired destination
			System.setProperty("user.dir",dir.getAbsolutePath() );
			currentDirectory = System.getProperty("user.dir");
			
			return;
		}catch (Exception e) {
			System.setProperty("user.dir",state);
			System.out.println("Can not return As you are in the Main Drive");
			
			return;
		}
			}
		
		File    dir;
		if(args[0].indexOf(":")!=-1)
			dir = new File(args[0]).getAbsoluteFile();
		else
			dir = new File(currentDirectory+'\\'+(args[0])).getAbsoluteFile();
						
		// check if the wanted directory exists to change to it or it will cause an error
		if(dir.exists()) {
			
			System.setProperty("user.dir",dir.getAbsolutePath() );
			currentDirectory = System.getProperty("user.dir");
			
			
		}else {
			System.out.println("Error the desired directory does not exists or Wrong path");
			
		}
		
		
		
		
	}
	
	
	
	
	
	// print what the user writes
	public static void echo(String[] args) {
		//check if the user wrote anything or not
		if(!args[0].equals("%%")) {
		for(int i=0;i<args.length;i++) {
			System.out.print(args[i]+" ");
		}
		System.out.println();
	return;}
		System.out.println("Didn't give something to print");
		}
	
	
	
	
	
	// list all the files in the directory in order or reverse order with the "ls -r"
	// Note that in this Function there are two functions,
	// As the sheet display "ls" and "ls -r" as two different Commands
	public static String[] ls() {
		
		
		File dir =new File(System.getProperty("user.dir"));
		String[] contents = dir.list();
		String[] result = new String[contents.length];
		if(parser.getArgs()[0].equalsIgnoreCase("-r")) {
			
			int len = contents.length-1;
			for(int i=contents.length-1; i>=0; i--) {
		         result[len-i] = contents[i];
		      }
			
		}else if(parser.getArgs()[0].equals("%%")) {
		for(int i=0; i<contents.length; i++) {
			result[i] = contents[i]; }
		}else {
	    	  System.out.println("Wrong parameter");
	      }
		return result;
	}
	
	
	
	
	
	
	
	// remove all the empty files in the working directories ex: "rmdir *"
	//remove a single file if empty with full/short path ex: "rmdir fake" or "rmdir D:\fake"
	public static void rmidr(String arg) {
		
		String currentDirectory = System.getProperty("user.dir");
		if(arg.equals("*")) {
			File dir = new File (currentDirectory);

			// Load all the files in the working directory 
			String[] state = dir.list();
		
			// Loop through all the files to check if Empty
			for(int i=0;i<state.length;i++) {
				File tempFile = new File(currentDirectory+"\\"+state[i]);
				
				// Check if the file is a directory
				if(tempFile.isDirectory()) {
				String[] temp =tempFile.list();

				// Check if the File is Hidden "System File" and Check the length for any files inside
				if(!tempFile.isHidden() && temp.length == 0 ) {
					tempFile.delete();
						}
					}	
			
				}
			
		return;}
		
		// Check if the path is Full or relative
		if(arg.indexOf(":")== -1) {
			// For short Path
			arg = currentDirectory+"\\"+arg;
			File dir = new File (arg);
			
			// Check if Empty or not
			if(dir.isDirectory() && !dir.isHidden()) {
			String[] state = dir.list();
			if(state.length ==0) {
				dir.delete();
			}}else {
				System.out.println("This is not a Directory");
			}
			
			
		}else {
				// For Full path
			File dir = new File (arg);
			String[] state = dir.list();
	
			// Check if Empty or not
			if(!dir.isHidden() && dir.isDirectory()) {
				 state = dir.list();
				if(state.length ==0) {
					dir.delete();
				}}else {
					System.out.println("This is not a Directory");
				}
		}
	
	
	}
        
       
	
	
	
	
	
	// Create a new File if the file does not exist  
        public static void touch(String file_path) {
        	if(file_path.indexOf(":") == -1) {file_path = System.getProperty("user.dir")+"\\"+file_path;}
            File file = new File(file_path);
            if(parser.getArgs().length==1){
                try {
                    file.getParentFile().mkdirs();
                    
                    if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                    } 
                    else {
                    System.out.println("File already exists.");
                    }
                }
                catch (IOException e) {
                    System.out.println("An error occurred while creating the file.");
                    e.printStackTrace();
               }
            }
            else{
                System.out.println("please give one parameter");
            }
            
            
            

        }
        
        
        
        
        
        
        
        // this is two commands implemented in this function
        // first: "cp" copy the first file data into the second file data
        // second: "cp -r" copy the whole directory to the second directory
        public static void cp(String c,String d)throws Exception{
            
            
            if(parser.getArgs()[0].equalsIgnoreCase("-r")) {
                if(parser.getArgs().length == 3){
                	
                	String firstName = c;
    				String secName = d;
    				
    				if(firstName.indexOf(":") == -1 ) {firstName = System.getProperty("user.dir")+"\\"+firstName;}
    				if( secName.indexOf(":") == -1) {secName = System.getProperty("user.dir")+"\\"+secName;}
    					File firstDir = new File(firstName);
    					File secDir = new File(secName);
    					if(firstDir.isDirectory() && !firstDir.isHidden() && secDir.isDirectory() && !secDir.isHidden()) {
    						
    						looper(firstDir, secName);
    						
    						
    					}else {
    						System.out.println("you are trying to access a hidden file or it is not a directory");
    						return;
    					}
                }
                else{
                    System.out.println("please enter two parameters only afer the '-r'");
                    return;
                }
                

            }
            else{
                if(parser.getArgs().length == 2){
                	try {
						
					
                    File a = new File(c);
                    File b = new File(d);
                    FileInputStream inp = new FileInputStream(a);
                    FileOutputStream outp = new FileOutputStream(b);


                    int n;

                    // read() function to read the
                    // byte of data
                    while ((n = inp.read()) != -1) {
                        // write() function to write
                        // the byte of data
                        outp.write(n);
                    }

                    if (inp != null) {

                        // close() function to close the
                        // stream
                        inp.close();
                    }
                    // close() function to close
                    // the stream
                    if (outp != null) {
                        outp.close();
                    }


                    System.out.println("File Copied"); 
                	} catch (Exception e) {
						System.out.println("check your command syntax and parameters");
						return;
					} }
                	
                else{
                    System.out.println("An error occurred");
                }
                
                
            }
            
        }

    

        
        //This function loops in the folders and copy them with all the data inside them
        // used by "cp(String c, String d)" for the command "cp -r" 
        public  static void looper(File file, String dest ) {
			// Takes the list of files in the folder to copy
    		String[] list = file.list();
    		// Loop for every item in the directory
    		for(String name : list) {
    			
    			File temp = new File(file.getAbsolutePath()+"\\"+name);
    			
    			// check if the file is a directory and if it is hidden; means that it is system file
    			if(temp.isDirectory() && !temp.isHidden()) {
    				File newFile = new File(dest+"\\"+temp.getName());
    				//make the new folder to copy to if not existed
    				newFile.mkdir();
    				// call the looper function to loop in the folder and every folder inside it using recursion
    				looper(temp, (dest+"\\"+temp.getName()));
    				
    				// check if it is not hidden and it is not a folder but a file
    			}else if(!temp.isHidden()) {
    				File newFile = new File(dest+"\\"+temp.getName());
    				
    				
    				
    					try {
    						newFile.createNewFile();
    						Scanner read = new Scanner(temp);
    						while(read.hasNextLine()) {
    							FileWriter myWriter = new FileWriter(newFile, true);
    							
    							String content = read.nextLine();
    							myWriter.write(content+System.lineSeparator());
    							myWriter.close();
    							
    						}
    						read.close();
    						
    						
    						
    					} catch (IOException e) {

    						e.printStackTrace();
    						System.out.println("An Error Has Accured!");
    						return;
    					}
    			
    			}
    			
    		}
    	}
        
        


        // remove the file from the directory
        public static void rm(String file_path) {
        	if(file_path.indexOf(":")==-1) {file_path = System .getProperty("user.dir")+"\\"+file_path;}
            File file = new File(file_path);
            if(parser.getArgs().length == 1){
                
                file.delete();
            }
            else{
                
                System.out.println("Please give one parameter only");
            }
        }
        
        
        


        
        // Concatenate two files data and print them
        public static void cat(String[] args) throws IOException{
            if(args.length < 3){
                for(int i=0 ; i<args.length ; i++){
                    // Creating an object of BuffferedReader class
                    File file = new File(args[i]);
                    BufferedReader br
                        = new BufferedReader(new FileReader(file));

                    // Declaring a string variable
                    String st;
                    // Consition holds true till
                    // there is character in a string
                    while ((st = br.readLine()) != null){

                        // Print the string
                        System.out.println(st);
                    }

                }
            }
            else{
                
                System.out.println("An error occurred");
            }
        }
        
	
        
      //Create new Directory / Directories
    	public static void mkdir(String[] args) {
    		File file;
    		for(String fileName : args) {
    			if(fileName.indexOf(":") != -1) {
    				file = new File(fileName);
    			}else {
    				fileName = System.getProperty("user.dir")+"\\"+fileName;
    				file = new File(fileName);
    			}
    			
    			
    			if(!file.mkdirs()) {
    				System.out.println("Couldn't make the full directory for "+fileName);
    				}
    			}
    		}
        
	
	// Exists the Terminal
	public static void exit() {
		System.exit(0);
	}
	
	//This method will choose the suitable command method to be called
	public static void chooseCommandAction() throws IOException, Exception{
		
		switch(parser.getCommandName()) {
		
			case "pwd":
				System.out.println(pwd());
				break;
			
			case "cd":
				cd(parser.getArgs());
				break;
			
			case "echo":
				echo(parser.getArgs());	
				break;
			
			case "ls":
				// We implemented two function in ls() for the commands"ls" and "ls -r"
				String[] list = ls();
				if(list[0]!=null) {
				for(String x :list) {System.out.println(x);}}
				else {System.out.println("check command syntax");}
				break;
				
			case "rmdir":
				rmidr(parser.getArgs()[0]);
				break;
            case "touch" :
                touch(parser.getArgs()[0]);
                break;
            case "cp":
            	
            	// for command "cp" and "cp -r"
            	
                if(parser.getArgs()[0].equalsIgnoreCase("-r")){
                	if(parser.getArgs().length == 3)
                    {cp(parser.getArgs()[1] , parser.getArgs()[2]);}
                	else {System.out.println("please enter two parameters");}
                }
                else{if(parser.getArgs().length == 2) {
                    cp(parser.getArgs()[0] , parser.getArgs()[1]);}
                    else {System.out.println("please enter two parameters");}
                }
                break;
            case "rm":
            	
                rm(parser.getArgs()[0]);
                break;
                
            case "cat":
                cat(parser.getArgs());
                break;
                
            case "mkdir":
				mkdir(parser.getArgs());
				break;
				
			case "exit":
				exit();
				break;
				
			default:
				System.out.println("Error Wrong Command");
				
		}
		// Print the current path for a nice look ex: D:\somefile\
		System.out.print(System.getProperty("user.dir")+">");
		
	}
	
	
	public static void main(String[] args) throws IOException, Exception{
		
		Scanner sc= new Scanner(System.in);
		String input;
		System.out.print(pwd()+">");
		while(true) {
		input = sc.nextLine();
		
		if(parser.parse(input)) {
		chooseCommandAction();
		}else {
			System.out.println("Error While processing the command.  Try another command");
			System.out.print(System.getProperty("user.dir")+">");
		}
		}
		
	
		}
	
	
}
















class Parser {
String commandName;
 String[] args;

//This method will divide the input into commandName and args
//where "input" is the string command entered by the user
public boolean parse(String input){
	try {
		int co=-1;
		
		// Case 0 is the main case that work for all the functions
		// Case 1 and two are for the ">" and ">>" if you decided to implement
		// If not going to implement, then delete the two cases {1, 2, default} and the switch statement
		switch((int)input.chars().filter(num -> num == '>').count()) {
		case 0:
			co= input.indexOf(' ');
			if(co != -1) {
			commandName = input.substring(0, co).toLowerCase();
			input = input.substring( co+1); 
			args = input.split(" ");}
			else {
				commandName = input.toLowerCase();
				args= new String[] {"%%"};
			}
			break;
		
		
		case 1:
			
			co= input.indexOf('>');
			commandName = input.substring(co,co+1).toLowerCase();
			input = input.substring(0,co-1)+input.substring(co+1);
			args = input.split(" ");
			break;
			
		case 2:
			co= input.indexOf('>');
			commandName = input.substring(co,co+2).toLowerCase();
			input = input.substring(0,co-1)+input.substring(co+2);
			args = input.split(" ");
			
			break;
			
		default:
			System.out.println("You can use two of '>' at most");
			return false;
		
		}
		
		
		return true;
	} catch (Exception e) {
		return false;
	}
	}

public String getCommandName(){
	return commandName;}

public String[] getArgs(){
	return args;}
}




