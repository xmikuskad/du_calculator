package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
	
	int FIELD_COUNT = 4;
	BufferedWriter writer;
	BufferedReader reader;
	final String fileName = "log.txt";
	final String readerFileName = "resource/readerFile.txt";
	
	List<List<Double>> positions = new ArrayList<List<Double>>();
	List<Double> other = new ArrayList<Double>();

	double[] sValue = new double[FIELD_COUNT];
	double[] aValue = new double[FIELD_COUNT];
	double[] bValue = new double[FIELD_COUNT];
	double[] cValue = new double[FIELD_COUNT];		
	
	double x1,y1,z1,a,b,c,R;
	
	//Main function
	public void startSolve(List<String> varList, MainScreenController con)
	{	
		//Logging
		try {
			checkFileSize();
		    writer = new BufferedWriter(
                    new FileWriter(fileName, true)  //Set true for append mode
                ); 
			writeToFile("\n----- New logging",Instant.now().toString()+" -----");
		} catch (Exception e1) {
			e1.printStackTrace();
			con.showAlertBox("Error","Logging failed!");		
			con.setClipMsg(false);
			return;
		}
		
		//Opening reader file
		try {
			reader = new BufferedReader(new FileReader(readerFileName));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			con.setResult("File reading failed");
			con.setClipMsg(false);
			writeToFile("ERROR!","Cannot open fileReader.txt");
		}
		
		//Checking input and loading variables
		try {
			checkInput(varList,con);
		}
		catch(Exception e)
		{
			con.setResult("");
			con.showAlertBox("ERROR!","Input problem!\nProblem: "+e.getMessage());
			con.setClipMsg(false);
			e.printStackTrace();
			writeToFile("ERROR!","Input problem! MSG: "+e.getMessage());
			
			try {
				writer.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return;
		}
	
		
		//Making calculations
		try {
			prepare();
			
			makeCalculations();
			
			finishIt(x1,y1,z1,con);
			
			con.setClipMsg(true);
		}
		catch(Exception e)
		{
			con.showAlertBox("Error","Calculation problem!");	
			con.setResult("");
			e.printStackTrace();
			con.setClipMsg(false);
			writeToFile("ERROR","Calculation problem!");
		}
		finally {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		
	}
	
	//First part of calculations - converting coordinates from spherical to cartesian
	private void prepare()
	{
		for(int i=0; i<FIELD_COUNT;i++)
		{		
			sValue[i] = R + positions.get(i).get(4);
			aValue[i] = sValue[i] * Math.sin(positions.get(i).get(2) * Math.PI/180 + Math.PI/2) * Math.cos(positions.get(i).get(3) * Math.PI / 180 + Math.PI);
			bValue[i] = sValue[i] * Math.sin(positions.get(i).get(2) * Math.PI/180 + Math.PI/2) * Math.sin(positions.get(i).get(3) * Math.PI / 180 + Math.PI);
			cValue[i] = sValue[i] * Math.cos(positions.get(i).get(2) * Math.PI/180 + Math.PI/2);
			
		}
		
		//Logging
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("A",String.valueOf(aValue[i]));
		
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("B",String.valueOf(bValue[i]));
		
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("C",String.valueOf(cValue[i]));
		
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("S",String.valueOf(sValue[i]));
	}

	//Middle part of calculations - finding intersection of 4 spheres
	private void makeCalculations() throws Exception
	{
		double[] deltaA = new double[3];
		double[] deltaB = new double[3];
		double[] deltaC = new double[3];	
		double p1,p2,p3;
		double D1,D2,A1,A2,B1,B2;
		
		for(int i =0; i<3; i++)
		{
			deltaA[i] = aValue[0] - aValue[1+i]; 
			deltaB[i] = bValue[0] - bValue[1+i]; 
			deltaC[i] = cValue[0] - cValue[1+i]; 
		}	
		
		p1 = other.get(0) * other.get(0) - aValue[0] * aValue[0] - bValue[0] * bValue[0] - cValue[0] * cValue[0] - other.get(1) * other.get(1) + aValue[1] * aValue[1] + bValue[1] * bValue[1] + cValue[1] * cValue[1]; 
		p2 = other.get(0) * other.get(0) - aValue[0] * aValue[0] - bValue[0] * bValue[0] - cValue[0] * cValue[0] - other.get(2) * other.get(2) + aValue[2] * aValue[2] + bValue[2] * bValue[2] + cValue[2] * cValue[2];
		p3 = other.get(0) * other.get(0) - aValue[0] * aValue[0] - bValue[0] * bValue[0] - cValue[0] * cValue[0] - other.get(3) * other.get(3) + aValue[3] * aValue[3] + bValue[3] * bValue[3] + cValue[3] * cValue[3];
				
		A1 = -4*deltaA[0]*deltaC[1] + 4*deltaA[1]*deltaC[0];
		B1 = -4*deltaB[0]*deltaC[1] + 4*deltaB[1]*deltaC[0];
		A2 = -4*deltaA[0]*deltaC[2] + 4*deltaA[2]*deltaC[0];
		B2 = -4*deltaB[0]*deltaC[2] + 4*deltaB[2]*deltaC[0];
		D1 =(2*p1*deltaC[1] - 2*p2*deltaC[0]);
		D2 =(2*p1*deltaC[2] - 2*p3*deltaC[0]);
		
		/*writeToFile("A1",String.valueOf(A1));
		writeToFile("B1",String.valueOf(B1));
		writeToFile("A2",String.valueOf(A2));
		writeToFile("B2",String.valueOf(B2));
		writeToFile("D1",String.valueOf(D1));
		writeToFile("D2",String.valueOf(D2));*/
		
		x1 = (D1*B2 - D2*B1) / (A1*B2 - A2*B1);
		y1 = (D1 - A1*x1) / B1;
		z1 = (p1 +2*deltaA[0]*x1 + 2*deltaB[0]*y1) / (-2*deltaC[0]);
		
		writeToFile("x1",String.valueOf(x1));
		writeToFile("y1",String.valueOf(y1));
		writeToFile("z1",String.valueOf(z1));
		
		//Check for bullshit result
		double max = Math.max(Math.max(other.get(0),other.get(1)), Math.max(other.get(2),other.get(3)));
		
		double min = 1000;
		int min_index = 0;
		for(int i=0;i<FIELD_COUNT;i++)
		{
			if(other.get(i) < min)
			{
				min = other.get(i);
				min_index = i;
			}
		}
		
		double tmp = Math.pow(x1-aValue[min_index],2) + Math.pow(y1-bValue[min_index],2) + Math.pow(z1-cValue[min_index],2);
		
		if(tmp > max*max)
		{
			writeToFile("Bullshit check failed, sorry","");
			throw new Exception("BULLSHIT CHECK!");
		}
	}
	
	//Last part of calculations - converting from cartesian coordinates back to spherical
	private void finishIt(double x, double y, double z, MainScreenController con)
	{
		double s = Math.sqrt(x*x + y*y + z*z);
		double v = Math.sqrt(x*x + y*y);
		a = Math.acos(z/s)  * 180 / Math.PI -90;
		b = Math.acos(x/v)* 180 / Math.PI -180;
		c = s - R;
		
		if(y<0)
		{
			b=-b;
		}
		
		BigDecimal aa = new BigDecimal(a).setScale(4, RoundingMode.HALF_EVEN);
		a=aa.doubleValue();
		
		BigDecimal bb = new BigDecimal(b).setScale(4, RoundingMode.HALF_EVEN);
		b=bb.doubleValue();
		
		BigDecimal cc = new BigDecimal(c).setScale(4, RoundingMode.HALF_EVEN);
		c=cc.doubleValue();
				
		writeToFile("s",String.valueOf(s));
		writeToFile("a",String.valueOf(a));
		writeToFile("b",String.valueOf(b));
		writeToFile("c",String.valueOf(c));

		String result = "::pos{"+positions.get(0).get(0)+","+positions.get(0).get(1)+","+a+","+b+","+c+"}";

		writeToFile("Final pos is",result);
		
		//Set result to textfield
		con.setResult(result);
		
		//Save result to clipboards
		StringSelection stringSelection = new StringSelection(result);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
		
	}
	
	//Get right value of planet surface depending on position entered
	private double getRValue(String value1, String value2) throws Exception
	{
		List<String> list = new ArrayList<String>();
		String tmp;
		
		while((tmp = reader.readLine()) != null)
		{
			list.add(tmp);
		}
		
		double value = -1;
		
		//Parsing of readerFile
		for(String str:list) {
			String tmpValue = str.split("=")[1];
			String val1 = str.split("=")[0].split(",")[0];
			String val2 = str.split("=")[0].split(",")[1];
			
			if(value1.equals(val1) && value2.equals(val2))
			{
				Double foundValue = Double.valueOf(tmpValue);
				value = Math.sqrt(foundValue/(4*Math.PI));
				break;
			}
		}
		
		return value;
	}
	
	//Check input formats and try to parse it and save to list
	private void checkInput(List<String> varList, MainScreenController con) throws Exception
	{
		for(int i =0;i<FIELD_COUNT;i++) {
		
			positions.add(new ArrayList<Double>());
			
			String pos = varList.get(i*2); //example pos:{a,b,c,d,e}
			String otherVar = varList.get(i*2 +1); //example 150.23
			
			if(pos.isEmpty())
			{
				throw new Exception("Position "+(i+1)+" is empty");
			}
			
			if(otherVar.isEmpty()) {
				throw new Exception("Ore distance "+(i+1)+" is empty!");
			}
			
			//Logging
			writeToFile("pos "+(i+1), pos);
			writeToFile("distance "+(i+1), otherVar);
			
			//Get inputs from string
			List<String> tmp = Arrays.asList(pos.split("\\{"));
			
			if(tmp.size() <=1)
			{
				throw new Exception("Position "+(i+1)+" is badly formatted!");
			}
			
			String positionsRaw = Arrays.asList(tmp.get(1).split("\\}")).get(0); //example a,b,c,d,e
					
			List<String> tmpList = Arrays.asList(positionsRaw.split(",")); //put a,b,c,d,e to list
			
			//Saving positions to list
			try {
				for(String str : tmpList) {
					positions.get(i).add(Double.valueOf(str));
				}		
			}
			catch(Exception e)
			{
				throw new Exception("Position "+(i+1)+" is badly formatted!");
			}
			
			//Saving distances to list
			try {
				other.add(Double.valueOf(otherVar));		
			}
			catch(Exception e)
			{
				throw new Exception("Ore distance "+(i+1)+" is badly formatted!");
			}
			
			//Only calculate once
			if(i==0)
			{
				//Prepping R value
				if(reader == null)
				{
					throw new Exception("Reader file not found");
				}
				R = getRValue(tmpList.get(0),tmpList.get(1));
				
				if(R<0)
				{
					R=120000;
					con.showAlertBox("Warning!","Calculations for this planet will be inaccurate!");
					writeToFile("WARNING!", "Didnt find R value in readerFile");
				}
			}
		}
		
		double pos1 = positions.get(0).get(0);
		double pos2 = positions.get(0).get(1);
		
		for(int i =1;i<FIELD_COUNT;i++) {			
			if(pos1 != positions.get(i).get(0) || pos2!=positions.get(i).get(1))
				throw new Exception("All positions have to be on same planet!");			
		}
		
		reorderBySmallest();
	}
	
	
	//Reorde list by smalled ore distance positions. Should help with accuracy of calculation
	private void reorderBySmallest() throws Exception
	{		
		for(int i=0;i<3;i++)
		{
			double min = 99999.0; //This is bad but values should never be this big
			int minIndex = -1;
			for(int j=i;j<4;j++)
			{
				if(other.get(j) < min)
				{
					min = other.get(j);
					minIndex = j;
				}
			}
			
			if(minIndex < 0)
				throw new Exception("Ore distances are too big!");
			
			double tmp = other.get(i);
			other.set(i, min);
			other.set(minIndex, tmp);
			
			List<Double> tmpList = positions.get(i);
			positions.set(i, positions.get(minIndex));
			positions.set(minIndex, tmpList);			
		}
		
	}

	//Check if log isnt too big. If it is, delete it
	private void checkFileSize()
	{		
		File file = new File(fileName);
		if (file == null || !file.exists() || !file.isFile()) return;
		
		//If log file is bigger than 20mb, we should delete it
		if(getFileSizeMegaBytes(file) > 20)
		{
			file.delete();
		}
	}
	
	private double getFileSizeMegaBytes(File file) {
		return (double) file.length() / (1024 * 1024);
	}
	
	//Used for logging
	private void writeToFile(String varName, String value) {
			try {
				writer.append(varName+" "+value+"\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
}
