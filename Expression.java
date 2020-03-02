package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    {
    	/** COMPLETE THIS METHOD **/
    	String delims = " \t*+-/()]";
        StringTokenizer st1 = new StringTokenizer(expr, delims, false);  
        List<String> varTokens = new ArrayList<>();
        List<String> arrTokens = new ArrayList<>();
        while (st1.hasMoreTokens()) 
        {
        	String stNext = st1.nextToken();
        	if (Character.isDigit(stNext.charAt(0))==false)
        	 {
        		 if(stNext.indexOf('[') ==-1) 
        		 { 
        			 if(varTokens.contains(stNext)==false) 
        			 {
        				 vars.add(new Variable(stNext));
        				 varTokens.add(stNext);
        			 }
        			 
        		 }
        		 else
        		 {
        			 int arrlen = stNext.length();
        			 
        			 if (stNext.substring(arrlen-1,arrlen).charAt(0) != '[')
        			 {
        				 if (Character.isDigit(stNext.charAt(arrlen-1))==false)
        	        	 {
        				 vars.add(new Variable(stNext.substring(arrlen-1,arrlen)));
        	        	 }
        				 stNext = stNext.substring(0,arrlen-1);
        	        	 
        			 }
        			 String delims2 = "[";
        	         StringTokenizer st2 = new StringTokenizer(stNext, delims2); 
        	         
        	         while(st2.hasMoreTokens())
        	         {
        	        	 String string2 = st2.nextToken();
        	        	 if (Character.isDigit(string2.charAt(0))==false)
        	        	 {
        	        		 if(arrTokens.contains(string2)==false)
        	        		 {
        	        			 arrays.add(new Array(string2));
        	        			 arrTokens.add(string2);
        	        		 }
        	        		 
             			}
        	         }
        		 }
             }
        }	
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	String delims = " \t*+-/()";
    	Stack<Float> stValues =new Stack<Float> ();
    	Stack<Character> stOperands = new Stack <Character> ();
    	StringTokenizer st3 = new StringTokenizer(expr,delims,true); 
    	
    	while(st3.hasMoreTokens())
    	{
    		String stNext = st3.nextToken();
    		if(Character.isDigit(stNext.charAt(0))==true)
			{
    			stValues.push(Float.parseFloat(stNext.toString()));
			}
    		else if(stNext.charAt(0) == '+'|| stNext.charAt(0) == '-'||stNext.charAt(0) == '*'|| stNext.charAt(0) == '/')
    		{ 
        		
    			if (!stOperands.isEmpty())
    			{
    				char stPeek = ' ';
    				stPeek =stOperands.peek();
    		
    				if ((stNext.charAt(0) == '*' || stNext.charAt(0) == '/') && (stPeek == '+' || stPeek == '-')) 
    				{
    					stOperands.push(stNext.charAt(0));	
    				}
    				else
    				{
	                	float stackVal = 0;
	                	float stackVal2 =0;
	                	char stackOp = ' ';
	                	stackOp = stOperands.pop();
	            		stackVal= stValues.pop();
	            		stackVal2= stValues.pop();
	            		float finalval = 0;
	            		if(stackOp == '*')
	            		{
	            			finalval = stackVal * stackVal2;
	            		}
	            		else if (stackOp == '+')
	            		{
	            			finalval = stackVal + stackVal2;
	            		}
	            		else if(stackOp == '-')
	            		{
	            			finalval = stackVal2 - stackVal;
	            		}
	            		else if(stackOp == '/')
	            		{
	            			finalval = stackVal2/ stackVal ;
	            		}
	            		stValues.push(finalval);
	            		stOperands.push(stNext.charAt(0));
    				}
                } else
                {
                	stOperands.push(stNext.charAt(0));	
                }
    		}
    		else if(stNext.charAt(0) == '(')
    		{
    			String recString = "";
    			String stNext2 = st3.nextToken();
    			while(stNext2.charAt(0) != ')')
    			{
    				recString = recString + stNext2;
    				stNext2 = st3.nextToken();
    			}
    		float x =	evaluate( recString, vars, arrays); 
    		stValues.push(x);
    		}
    		else 
    		{
    			int index = -1;
    			
    			for(int i = 0; i < vars.size(); i++)
    			{
    				if(vars.get(i).name.equals(stNext))
    				{
    					index = i;
    				}
    			}
    			if (index != -1)
    			{
    				float value1 = vars.get(index).value;
    				stValues.push(value1);
    			} 
    			else
    			{
    				
    				String delims2 = "[]";
    				StringTokenizer st2 = new StringTokenizer(stNext,delims2, false);  
    				String stNext2=st2.nextToken();
    				String stNext3= st2.nextToken();	
    				for(int i=0; i< arrays.size();i++)
    				{
    					if(arrays.get(i).name.equals(stNext2))
        				{
    						
        					index = i;
        				}
    				}
    				float arrVal = arrays.get(index).values[Integer.valueOf(stNext3)];
    				stValues.push(arrVal);		
    			}
    				
    		}
    			
    	} 
    	
    	float stackVal = 0;
    	float stackVal2 =0;
    	char stackOp = ' ';
    	float finalAns = 0;
    	
    	while(!stOperands.isEmpty())
    	{ 
            stackOp = stOperands.pop();
    		stackVal= stValues.pop();
    		stackVal2= stValues.pop();
    		
    		float finalval = 0;
    		if(stackOp == '*')
    		{
    			finalval = stackVal * stackVal2;
    		}
    		else if (stackOp == '+')
    		{
    			finalval = stackVal + stackVal2;
    		}
    		else if(stackOp == '-')
    		{
    			finalval = stackVal2 - stackVal;
    		}
    		else if(stackOp == '/')
    		{
    			finalval = stackVal2/ stackVal ;
    		}
    		stValues.push(finalval);
    		
    	}
    		finalAns=stValues.pop();
    		
    	return finalAns;
    }
}
