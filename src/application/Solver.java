package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
	
	int FIELD_COUNT = 3;
	BufferedWriter writer;
	final String fileName = "log.txt";
	
	List<List<Double>> positions = new ArrayList<List<Double>>();
	List<Double> other = new ArrayList<Double>();

	//Prve pocty
	double[] sValue = new double[FIELD_COUNT];
	double[] aValue = new double[FIELD_COUNT];
	double[] bValue = new double[FIELD_COUNT];
	double[] cValue = new double[FIELD_COUNT];		
	
	double x,y,z,a,b,c;
	
	//Main function
	public void startSolve(List<String> varList, MainScreenController con)
	{	
		try {
			checkInput(varList,con);
		}
		catch(Exception e)
		{
			System.out.println("Wrong input!");
			con.setResult("Wrong input!");
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
		}
		
		try {
			prepare(); //OK
			makeCalculations();
			finishIt();
			String result = "::pos{"+positions.get(0).get(0)+","+positions.get(0).get(1)+","+a+","+b+","+c+"}";
			con.setResult(result);
			
			StringSelection stringSelection = new StringSelection(result);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
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
	
	private void finishIt()
	{
		double s = Math.sqrt(x*x + y*y + z*z);
		a = 90 - Math.acos(z/s)  * 180 / Math.PI;
		b = Math.atan(y/x)* 180 / Math.PI;
		c = s - 120000;
		
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

	}
	
	private void prepare()
	{
		for(int i=0; i<FIELD_COUNT;i++)
		{
			
			sValue[i] = 120000 + positions.get(i).get(4);
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
		double[] deltaA = new double[2];
		double[] deltaB = new double[2];
		double[] deltaC = new double[2];	
		double alpha,beta,gamma,x1,x2,y1,y2,z1,z2,o1,o2,p1,p2;
		double v1,v2,D1,D2, V1, V2,A1,A2,B1,C2;
		
		for(int i =0; i<2; i++)
		{
			deltaA[i] = aValue[0] - aValue[1+i]; 
			deltaB[i] = bValue[0] - bValue[1+i]; 
			deltaC[i] = cValue[0] - cValue[1+i]; 
		}	
		
		p1 = other.get(1) * other.get(1) - deltaA[0] * deltaA[0] - deltaB[0] * deltaB[0] - deltaC[0] * deltaC[0] - other.get(0) * other.get(0); 
		p2 = other.get(2) * other.get(2) - deltaA[1] * deltaA[1] - deltaB[1] * deltaB[1] - deltaC[1] * deltaC[1] - other.get(0) * other.get(0); 		
		
		A1 = 4*deltaA[0]*deltaC[1] - 4*deltaA[1]*deltaC[0];
		B1 = 4*deltaB[0]*deltaC[1] - 4*deltaB[1]*deltaC[0];
		A2 = 4*deltaA[0]*deltaB[1] - 4*deltaA[1]*deltaB[0];
		C2 = 4*deltaC[0]*deltaB[1] - 4*deltaC[1]*deltaB[0];
		D1 =-1*(-2*p1*deltaC[1] + 2*p2*deltaC[0]);
		D2 =-1*(-2*p1*deltaB[1] + 2*p2*deltaB[0]);
		
		writeToFile("A1",String.valueOf(A1));
		writeToFile("B1",String.valueOf(B1));
		writeToFile("A2",String.valueOf(A2));
		writeToFile("C2",String.valueOf(C2));
		writeToFile("D1",String.valueOf(D1));
		writeToFile("D2",String.valueOf(D2));
		
		alpha = (-1 *A1/B1)*(-1 *A1/B1) + (-1 *A2/C2) * (-1 *A2/C2) + 1;
		beta = 2*( (-A1/B1) * (D1/B1) + (-A2/C2) * (D2/C2) );
		gamma = (D1/B1) * (D1/B1) + (D2/C2) * (D2/C2) - other.get(0) * other.get(0);
		
		writeToFile("Alpha",String.valueOf(alpha));
		writeToFile("Beta",String.valueOf(beta));
		writeToFile("Gamma",String.valueOf(gamma));
		
		v1 = (-beta + Math.sqrt(beta*beta - 4 * alpha * gamma)) / (2*alpha);
		v2 = (-beta - Math.sqrt(beta*beta - 4 * alpha * gamma)) / (2*alpha);
		
		writeToFile("v1",String.valueOf(v1));
		writeToFile("v2",String.valueOf(v2));
		
		x1 = aValue[0] + v1;
		y1 = bValue[0] + v1 * (-A1/B1) + D1/B1;
		z1 = cValue[0] + v1 * (-A2/C2) + D2/C2;
		
		writeToFile("x1",String.valueOf(x1));
		writeToFile("y1",String.valueOf(y1));
		writeToFile("z1",String.valueOf(z1));
				
		x2 = aValue[0] + v2;
		y2 = bValue[0] + v2 * (-A1/B1) + D1/B1;
		z2 = cValue[0] + v2 * (-A2/C2) + D2/C2;
		
		writeToFile("x2",String.valueOf(x2));
		writeToFile("y2",String.valueOf(y2));
		writeToFile("z2",String.valueOf(z2));
		
		V1 = (aValue[0] -x1) * (aValue[0] -x1) + (bValue[0] - y1) * (bValue[0] - y1) + (cValue[0]-z1) * (cValue[0]-z1) +  (aValue[1] -x1) * (aValue[1] -x1) + (bValue[1] - y1) * (bValue[1] - y1) + (cValue[1]-z1) * (cValue[1]-z1) + (aValue[2] -x1) * (aValue[2] -x1) + (bValue[2] - y1) * (bValue[2] - y1) + (cValue[2]-z1) * (cValue[2]-z1);
		V2 = (aValue[0] -x2) * (aValue[0] -x2) + (bValue[0] - y2) * (bValue[0] - y2) + (cValue[0]-z2) * (cValue[0]-z2) +  (aValue[1] -x2) * (aValue[1] -x2) + (bValue[1] - y2) * (bValue[1] - y2) + (cValue[1]-z2) * (cValue[1]-z2) + (aValue[2] -x2) * (aValue[2] -x2) + (bValue[2] - y2) * (bValue[2] - y2) + (cValue[2]-z2) * (cValue[2]-z2);
		
		writeToFile("V1",String.valueOf(V1));
		writeToFile("V2",String.valueOf(V2));
		
		o1 = Math.sqrt(V1 - positions.get(0).get(4) - positions.get(1).get(4) - positions.get(2).get(4));
		o2 = Math.sqrt(V2 - positions.get(0).get(4) - positions.get(1).get(4) - positions.get(2).get(4));
		
		writeToFile("o1",String.valueOf(o1));
		writeToFile("o2",String.valueOf(o2));
			
		if(o1 < o2) {
			x = x1;
			y = y1;
			z = z1;
			writeToFile("Choosing variant 1","");
		} 
		else {
			x = x2;
			y = y2;
			z = z2;
			writeToFile("Choosing variant 2","");
		}
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
		
		}

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
