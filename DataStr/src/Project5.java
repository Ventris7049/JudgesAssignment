/*
 * David Ventris
 * COSC-4415.001 Database Systems
 * Project 5
 * 12/02/2021
 * Reads two text files and produces a text file assigning judges to projects in a science fair.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class Project5 extends JFrame implements ActionListener{
	
	private static JDesktopPane dtp;
	private static JInternalFrame mFrame;
	private static JTextField fileIn1;
	private static JTextField fileIn2;
	private static JTextField fileOut;
	private static LinkedList<String> inputFile = new LinkedList<String>();
	private static JTextArea fileBefore;
	private static JTextArea fileAfter;
	private static String inString1,inString2;
	private static JButton submitButton;
	private static ArrayList<String> names= new ArrayList<String>();
	private static ArrayList<Integer> limit = new ArrayList<Integer>();
	private static ArrayList<String> outGroups= new ArrayList<String>();

	//Builds the GUI.
	public Project5() 
	{
		
		setSize(getMaximumSize());
		setLocation(0,0); 
		setTitle("Science Fair Judges"); 
		setBackground(Color.gray); 
		addWindowListener(new windowDestroyer()); 
		dtp = new JDesktopPane();
		mFrame = new JInternalFrame();
		dtp.add(mFrame);
		setContentPane(dtp); 
		dtp.setBackground(Color.LIGHT_GRAY); 
		mFrame.setLayout(new BorderLayout());
		JPanel textPanel = new JPanel();
		JLabel inFile = new JLabel("Input Files:");
		fileIn1 = new JTextField(20);
		fileIn2 = new JTextField(20);
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		JLabel outFile = new JLabel("Output File:");
		fileOut = new JTextField(20);
		JPanel layoutPanel = new JPanel ();
		layoutPanel.setLayout(new FlowLayout());
		textPanel.setLayout(new GridLayout(6,0));
		JLabel label1 = new JLabel("");
		fileBefore = new JTextArea();
		fileAfter = new JTextArea();
		layoutPanel.add(textPanel);
		mFrame.add(layoutPanel, BorderLayout.NORTH);
		mFrame.setSize(1360, 730);
		mFrame.setLocation(0, 1);
		fileIn1.setText("projects.txt");
		fileIn2.setText("judges.txt");
		fileOut.setText("assignments.txt");
		fileBefore.setFont(new Font("Verdana", Font.PLAIN, 14));
		fileBefore.setBackground(new Color(240, 240, 240));
		fileBefore.setEditable(false);
		fileBefore.setColumns(55);
		fileBefore.setRows(28);
		fileBefore.setSize(fileBefore.getMaximumSize());
		fileAfter.setFont(new Font("Verdana", Font.PLAIN, 14));
		fileAfter.setBackground(new Color(240, 240, 240));
		fileAfter.setEditable(false);
		fileAfter.setColumns(45);
		fileAfter.setRows(28);
		fileAfter.setSize(800,1000);
		
		JScrollPane scrollL = new JScrollPane(fileBefore);
		scrollL.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane scrollR = new JScrollPane(fileAfter);
		scrollR.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel sPanel = new JPanel();
		sPanel.setLayout(new FlowLayout());
		textPanel.add(inFile);
		textPanel.add(label1);
		textPanel.add(fileIn1);
		textPanel.add(fileIn2);
		textPanel.add(outFile);
		textPanel.add(fileOut);
		textPanel.add(submitButton);
		sPanel.add(scrollL);
		sPanel.add(scrollR);
		mFrame.add(sPanel, BorderLayout.CENTER);
		mFrame.setVisible(true);
		textPanel.setVisible(true);
	
	}
	
	//Used for action of pressing submit button.
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals("Submit"))
		{
			String fileName1 = fileIn1.getText();
			String fileName2 = fileIn2.getText();
			try 
			{
				BufferedReader reader1 = new BufferedReader(new FileReader(fileName1));

				String line1,line2;
				
				while((line1 = reader1.readLine()) != null) 
				{
					inputFile.add(line1);
				}
				reader1.close();
				line1 = String.join("\n", inputFile);
				inString1=String.join("\n", inputFile);
				
				BufferedReader reader2 = new BufferedReader(new FileReader(fileName2));
				inputFile.clear();
				while((line2 = reader2.readLine()) != null) 
				{
					inputFile.add(line2);
				}
				reader2.close();
				line2 = String.join("\n", inputFile);
				inString2=String.join("\n", inputFile);
				
				String inLines = fileName1 + "\n\n" + line1 +"\n\n" + fileName2 +"\n\n"+ line2 ;
				
				fileBefore.setText(inLines);

				
				getResults();
				String outLines="";
				for(int i=0;i<outGroups.size();i++)
				{
					outLines=outLines.concat(outGroups.get(i).toString()+ "\n");
				}
				fileAfter.setText(outLines);
				
				
				PrintWriter write = new PrintWriter(fileOut.getText());
				String[] temp=outLines.split("\n");
				for(int i=0;i<temp.length;i++)
				{	
					write.println(temp[i]);
				}
				write.close();
				
				submitButton.setEnabled(false);
			}
			catch (FileNotFoundException e)
			{
				System.out.println("This file was not found!");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//Method for checking and correcting the {} in the file.
	public static void getResults()
	{
		inString1=inString1.replace(" ", "");
		String[] projects = null;
		projects=inString1.split("\n");
		ArrayList<String> judges = new ArrayList<String>();
		ArrayList<String> subject= new ArrayList<String>();
		String[] temp=null;
		temp=inString2.split("\n");
		for (int i=0;i<temp.length;i++)
		{
			if(!temp[i].contains(":"))
			{
				temp[i-1]=temp[i-1].concat(temp[i]);
				temp[i]="";
			}
		}
		for (int i=0;i<temp.length;i++)
		{
			judges.add(temp[i]);
		}
		for (int i=0;i<judges.size();i++)
		{
			if(judges.get(i).toString().equals(""))
			{
				judges.remove(i);
			}
		}
		for (int i=0;i<judges.size();i++)
		{	
			int index=0;
			for(int j=0;j<judges.get(i).toString().length();j++)
			{
				index=judges.get(i).toString().indexOf(":");
			}
			names.add(judges.get(i).toString().substring(0,index));
		}
		for (int i=0;i<judges.size();i++)
		{	
			int index=0;
			for(int j=0;j<judges.get(i).toString().length();j++)
			{
				index=judges.get(i).toString().indexOf(":");
			}
			subject.add(judges.get(i).toString().substring(index+1));
		}
		for (int i=0;i<names.size();i++) 
		{
			limit.add(0);
		}

				
		//Grouping of projects
		ArrayList<String> proChem = new ArrayList<String>();
		ArrayList<String> proBeh = new ArrayList<String>();
		ArrayList<String> proEart = new ArrayList<String>();
		ArrayList<String> proEnv = new ArrayList<String>();
		ArrayList<String> proLife = new ArrayList<String>();
		ArrayList<String> proMath = new ArrayList<String>();
		
		for(int i=0;i<projects.length;i++)
		{
			if(projects[i].contains("Chemistry"))
			{
				proChem.add(projects[i]);
			}
			else if(projects[i].contains("Behavioral/SocialScience"))
			{
				proBeh.add(projects[i]);
			}
			else if(projects[i].contains("Earth/SpaceScience"))
			{
				proEart.add(projects[i]);
			}
			else if(projects[i].contains("EnvironmentalScience"))
			{
				proEnv.add(projects[i]);
			}
			else if(projects[i].contains("LifeScience"))
			{
				proLife.add(projects[i]);
			}
			else if(projects[i].contains("Mathematics/Physics"))
			{
				proMath.add(projects[i]);
			}
		}
		

		for(int i=0;i<proChem.size();i++)
		{
			proChem.set(i, proChem.get(i).toString().substring(0,3));

		}

		for(int i=0;i<proBeh.size();i++)
		{
			proBeh.set(i, proBeh.get(i).toString().substring(0,3));

		}

		for(int i=0;i<proEart.size();i++)
		{
			proEart.set(i, proEart.get(i).toString().substring(0,3));

		}

		for(int i=0;i<proEnv.size();i++)
		{
			proEnv.set(i, proEnv.get(i).toString().substring(0,3));

		}

		for(int i=0;i<proLife.size();i++)
		{
			proLife.set(i, proLife.get(i).toString().substring(0,3));

		}

		for(int i=0;i<proMath.size();i++)
		{
			proMath.set(i, proMath.get(i).toString().substring(0,3));

		}

		//Used to build the output.
		Boolean hasMore=false;
		int count1=0,count2=0,count3=0,count4=0,count5=0,count6=0;
		while(!hasMore)
		{
			hasMore=true;

				if(proChem.size()>0 && proChem.size() >= proBeh.size() && proChem.size() >= proEart.size() && proChem.size() >= proEnv.size()
						&& proChem.size() >= proLife.size()&& proChem.size() >= proMath.size())
				{
					if(proChem.size()>=6)
					{
						
						String build="", sNames="", project="";
						int cnt=0;
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Chemistry"))
							{
								if(!checkLimit(i,6))
								{
						
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,6))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						int cnt1=0;
						for(int i=0;i<proChem.size();i++)
						{
								project=project.concat(proChem.get(i).toString() + ", ");
								cnt1++;
								if(cnt1==6)
									break;
						}
						for(int i=0;i<6;i++)
						{
							proChem.remove(0);
						}
						count1++;
						build=build.concat("Chemistry_" + count1 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}
					else
					{
						String build="", sNames="", project="";
						int cnt=0,cnt1=0;
						for(int i=0;i<proChem.size();i++)
						{
								project=project.concat(proChem.get(i).toString() + ", ");
								cnt1++;
						}
						for(int i=0;i<cnt1;i++)
						{
							proChem.remove(0);
						}		
						
						
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Chemistry"))
							{

								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						
						count1++;
						build=build.concat("Chemistry_" + count1 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}

				hasMore=false;
				}

				else if(proBeh.size()>0 && proBeh.size()>=proChem.size() && proBeh.size()>=proEart.size() && proBeh.size()>=proEnv.size()
						&& proBeh.size()>=proLife.size()&& proBeh.size()>=proMath.size() )
				{

					if(proBeh.size()>=6)
					{

						String build="", sNames="", project="";
						int cnt=0;
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Behavioral/Social Science"))
							{

								if(!checkLimit(i,6))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,6))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						int cnt1=0;
						for(int i=0;i<proBeh.size();i++)
						{
								project=project.concat(proBeh.get(i).toString() + ", ");
								cnt1++;
								if(cnt1==6)
									break;
						}
						for(int i=0;i<6;i++)
						{
							proBeh.remove(0);
						}
						count2++;
						build=build.concat("Behavioral/Social Science_" + count2 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}
					else
					{

						String build="", sNames="", project="";
						int cnt=0,cnt1=0;
						for(int i=0;i<proBeh.size();i++)
						{
								project=project.concat(proBeh.get(i).toString() + ", ");
								cnt1++;
								
						}
						for(int i=0;i<cnt1;i++)
						{
							proBeh.remove(0);
						}		
						
						
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Behavioral/Social Science"))
							{
								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						if(cnt<3)
						{

							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						
						count2++;
						build=build.concat("Behavioral/Social Science_" + count2 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}

				hasMore=false;	
				}

				else if(proEart.size()>0 && proEart.size() >= proBeh.size() && proEart.size() >= proChem.size()  && proEart.size() >= proEnv.size() 
						&& proEart.size() >= proLife.size()&& proEart.size() >= proMath.size())
				{

					if(proEart.size()>=6)
					{
						String build="", sNames="", project="";
						int cnt=0;
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Earth/Space Science"))
							{

								if(!checkLimit(i,6))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,6))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						int cnt1=0;
						for(int i=0;i<proEart.size();i++)
						{
								project=project.concat(proEart.get(i).toString() + ", ");
								cnt1++;
								if(cnt1==6)
									break;
						}
						for(int i=0;i<6;i++)
						{
							proEart.remove(0);
						}
						count3++;
						build=build.concat("Earth/Space Science_" + count3 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}
					else
					{
						String build="", sNames="", project="";
						int cnt=0,cnt1=0;
						for(int i=0;i<proEart.size();i++)
						{
								project=project.concat(proEart.get(i).toString() + ", ");
								cnt1++;
						}
						for(int i=0;i<cnt1;i++)
						{
							proEart.remove(0);
						}		
						
						
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Earth/Space Science"))
							{

								if(!checkLimit(i, cnt1))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						
						count3++;
						build=build.concat("Earth/Space Science_" + count3 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}

				hasMore=false;	
				}

				else if(proEnv.size()>0 && proEnv.size() >= proBeh.size() && proEnv.size() >= proEart.size() && proEnv.size() >= proChem.size()	 && proEnv.size() >= proMath.size()
						&& proEnv.size() >= proLife.size()&& proEnv.size() >= proMath.size())
				{
					if(proEnv.size()>=6)
					{
						String build="", sNames="", project="";
						int cnt=0;
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Environmental Science"))
							{

								if(!checkLimit(i, 6))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i)+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,6))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						int cnt1=0;
						for(int i=0;i<proEnv.size();i++)
						{
								project=project.concat(proEnv.get(i).toString() + ", ");
								cnt1++;
								if(cnt1==6)
									break;
						}
						for(int i=0;i<6;i++)
						{
							proEnv.remove(0);
						}
						count4++;
						build=build.concat("Environmental Science_" + count4 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}
					else
					{
						String build="", sNames="", project="";
						int cnt=0,cnt1=0;
						for(int i=0;i<proEnv.size();i++)
						{
								project=project.concat(proEnv.get(i).toString() + ", ");
								cnt1++;
						}
						for(int i=0;i<cnt1;i++)
						{
							proEnv.remove(0);
						}		
						
						
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Environmental Science"))
							{

								if(!checkLimit(i, cnt1))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						count4++;
						build=build.concat("Environmental Science_" + count4 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}

					hasMore=false;
				}
				
				else if(proLife.size()>0 && proLife.size() >= proBeh.size() && proLife.size() >= proEart.size() && proLife.size() >= proEnv.size()
						&& proLife.size() >= proChem.size() && proLife.size() >= proMath.size())
				{
					if(proLife.size()>=6)
					{
						String build="", sNames="", project="";
						int cnt=0;
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Life Science"))
							{

								if(!checkLimit(i,6))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,6))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						int cnt1=0;
						for(int i=0;i<proLife.size();i++)
						{
								project=project.concat(proLife.get(i).toString() + ", ");
								cnt1++;
								if(cnt1==6)
									break;
						}
						for(int i=0;i<6;i++)
						{
							proLife.remove(0);
						}
						count5++;
						build=build.concat("Life Science_" + count5 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}
					else
					{
						String build="", sNames="", project="";
						int cnt=0,cnt1=0;
						for(int i=0;i<proLife.size();i++)
						{
								project=project.concat(proLife.get(i).toString() + ", ");
								cnt1++;
						}
						for(int i=0;i<cnt1;i++)
						{
							proLife.remove(0);
						}		
						
						
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Life Science"))
							{

								if(!checkLimit(i, cnt1))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						
						count5++;
						build=build.concat("Life Science_" + count5 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}	

					hasMore=false;
				}
			
				else if(proMath.size()>0 && proMath.size() >= proBeh.size() && proMath.size() >= proEart.size() && proMath.size() >= proEnv.size()
						&& proMath.size() >= proLife.size() && proMath.size() >= proChem.size())
				{
					if(proMath.size()>=6)
					{
						String build="", sNames="", project="";
						int cnt=0;
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Mathematics/Physics"))
							{

								if(!checkLimit(i,6))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,6))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+6);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						int cnt1=0;
						for(int i=0;i<proMath.size();i++)
						{
								project=project.concat(proMath.get(i).toString() + ", ");
								cnt1++;
								if(cnt1==6)
									break;
						}
						for(int i=0;i<6;i++)
						{
							proMath.remove(0);
						}
						count6++;
						build=build.concat("Mathematics/Physics_" + count6 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}
					else
					{
						String build="", sNames="", project="";
						int cnt=0,cnt1=0;
						for(int i=0;i<proMath.size();i++)
						{
								project=project.concat(proMath.get(i).toString() + ", ");
								cnt1++;
						}
						for(int i=0;i<cnt1;i++)
						{
							proMath.remove(0);
						}		
						
						
						for(int i=0;i<judges.size();i++)
						{
							if(judges.get(i).toString().contains("Mathematics/Physics"))
							{

								if(!checkLimit(i,cnt1))
								{

									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
							
						}
						if(cnt<3)
						{
							for(int i=0;i<judges.size();i++)
							{
								if(!checkLimit(i,cnt1))
								{
									
									sNames=sNames.concat(names.get(i).toString() + ", ");
									limit.set(i, limit.get(i).intValue()+cnt1);
									cnt++;
									if(cnt==3)
										break;
								}
							}
						}
						
						count6++;
						build=build.concat("Mathematics/Physics_" + count6 + ": " + sNames.substring(0,sNames.length()-2) + "\nProjects: " + project.substring(0,project.length()-2));
						outGroups.add(build);
					}

					hasMore=false;
				}
				
				
			}
	
		for(int i=0;i<outGroups.size();i++)
		{
			System.out.println(outGroups.get(i).toString());
		}
		System.out.println(proChem);
		System.out.println(proBeh);
		System.out.println(proEart);
		System.out.println(proEnv);
		System.out.println(proLife);
		System.out.println(proMath);
		System.out.println(limit);
		
	}
	
	
	//Method to check the judges limit.
	public static Boolean checkLimit(int i, int j)
	{
		Boolean atLimit = false;
		if(limit.get(i)==6)
			atLimit=true;
		if(limit.get(i)+j>6)
			atLimit=true;
		if(limit.get(i)==0)
			atLimit=false;

		return atLimit ;
	}
	


	class windowDestroyer extends WindowAdapter
	{
		
		public void windowClosing(WindowEvent e)
		{
		    System.exit(0);
		}

	}

	//Main method to execute the program.
	public static void main(String[] args) 
	{

		Project5 start = new Project5();
		start.setVisible(true);
		start.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
	}

}
