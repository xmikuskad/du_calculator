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
	final String readerFileName = "readerFile.txt";
	
	List<List<Double>> positions = new ArrayList<List<Double>>();
	List<Double> other = new ArrayList<Double>();

	//Prve pocty
	double[] sValue = new double[FIELD_COUNT];
	double[] aValue = new double[FIELD_COUNT];
	double[] bValue = new double[FIELD_COUNT];
	double[] cValue = new double[FIELD_COUNT];		
	
	double x1,y1,z1,a,b,c,R;
	
	//Main function
	public void startSolve(List<String> varList, MainScreenController con)
	{	
		//Opening reader file
		try {
			reader = new BufferedReader(new FileReader(readerFileName));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			con.setResult("File reading failed");
			con.hideClipMsg();
		}
		
		//Checking input and loading variables
		try {
			checkInput(varList,con);
		}
		catch(Exception e)
		{
			System.out.println("Wrong input!");
			con.setResult(e.getMessage());
			con.hideClipMsg();
			e.printStackTrace();
			return;
		}
		
		//Logging
		try {
			checkFileSize();
		    writer = new BufferedWriter(
                    new FileWriter(fileName, true)  //Set true for append mode
                ); 
			writeToFile("\nNew logging",Instant.now().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			con.setResult("File writing failed");
			con.hideClipMsg();
		}
		
		//Making calculations
		try {
			prepare(); //OK
			
			makeCalculations();
			
			finishIt(x1,y1,z1,con);
			
			con.showClipMsg();
		}
		catch(Exception e)
		{
			System.out.println("Calculation problem!");
			con.setResult("Calculation problem!");
			e.printStackTrace();
			con.hideClipMsg();
		}
		finally {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		
	}
	
	private void finishIt(double x, double y, double z, MainScreenController con)
	{
		double s = Math.sqrt(x*x + y*y + z*z);
		a = 90 - Math.acos(z/s)  * 180 / Math.PI;
		b = Math.atan(y/x)* 180 / Math.PI;
		c = s - R;
		
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

		con.setResult(result);
			
		StringSelection stringSelection = new StringSelection(result);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
		
	}
	
	private void prepare()
	{
		for(int i=0; i<FIELD_COUNT;i++)
		{
			
			sValue[i] = R + positions.get(i).get(4);
			aValue[i] = sValue[i] * Math.sin(Math.PI/2 - positions.get(i).get(2) * Math.PI/180) * Math.cos(positions.get(i).get(3) * Math.PI / 180);
			bValue[i] = sValue[i] * Math.sin(Math.PI/2 - positions.get(i).get(2) * Math.PI/180) * Math.sin(positions.get(i).get(3) * Math.PI / 180);
			cValue[i] = sValue[i] * Math.cos(Math.PI/2 - positions.get(i).get(2) * Math.PI/180);
			
		}
		
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("A",String.valueOf(aValue[i]));
		
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("B",String.valueOf(bValue[i]));
		
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("C",String.valueOf(cValue[i]));
		
		for(int i=0;i<FIELD_COUNT;i++)
			writeToFile("S",String.valueOf(sValue[i]));

	}

	private void makeCalculations()
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


		
		writeToFile("A1",String.valueOf(A1));
		writeToFile("B1",String.valueOf(B1));
		writeToFile("A2",String.valueOf(A2));
		writeToFile("B2",String.valueOf(B2));
		writeToFile("D1",String.valueOf(D1));
		writeToFile("D2",String.valueOf(D2));
		
		x1 = (D1*B2 - D2*B1) / (A1*B2 - A2*B1);
		y1 = (D1 - A1*x1) / B1;
		z1 = (p1 +2*deltaA[0]*x1 + 2*deltaB[0]*y1) / (-2*deltaC[0]);
		
		writeToFile("x1",String.valueOf(x1));
		writeToFile("y1",String.valueOf(y1));
		writeToFile("z1",String.valueOf(z1));
	}
	
	private double getRValue(String value1, String value2) throws Exception
	{
		List<String> list = new ArrayList<String>();
		String tmp;
		
		System.out.println("ORIG VALS "+value1 +" "+value2);
		
		while((tmp = reader.readLine()) != null)
		{
			list.add(tmp);
		}
		
		double value = -1;
		
		for(String str:list) {
			String tmpValue = str.split("=")[1];
			String val1 = str.split("=")[0].split(",")[0];
			String val2 = str.split("=")[0].split(",")[1];
			
			System.out.println("V1 "+val1+" V2 "+val2+" END "+tmpValue);
			
			if(value1.equals(val1) && value2.equals(val2))
			{
				Double foundValue = Double.valueOf(tmpValue);
				System.out.println("Found "+foundValue);
				value = Math.sqrt(foundValue/(4*Math.PI));
				break;
			}
		}
		
		System.out.println("VALUE IS "+value+" !!!!!!!!!!!!!!!!!!!!!!!!");
		return value;
	}
	
	private void checkInput(List<String> varList, MainScreenController con) throws Exception
	{
		for(int i =0;i<FIELD_COUNT;i++) {
		
			positions.add(new ArrayList<Double>());
			
			String pos = varList.get(i*2);
			String otherVar = varList.get(i*2 +1);
			
			if(otherVar.isEmpty()) {
				throw new Exception("Other field is empty!");
			}
			
			
			//Get inputs from string
			List<String> tmp = Arrays.asList(pos.split("\\{"));
			String positionsRaw = Arrays.asList(tmp.get(1).split("\\}")).get(0);
					
			List<String> tmpList = Arrays.asList(positionsRaw.split(","));
			
			//Saving values to list
			for(String str : tmpList) {
				positions.get(i).add(Double.valueOf(str));
			}		
			other.add(Double.valueOf(otherVar));		
			
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
					throw new Exception("Invalid coordinates");
				}
			}
		}
		
		//Maybe add checking if all pos are on same planets!?
		//TODO
		
	}

	private void checkFileSize()
	{		
		File file = new File(fileName);
		if (!file.exists() || !file.isFile()) return;
		
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
