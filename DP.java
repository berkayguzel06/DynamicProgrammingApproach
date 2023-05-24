import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DP {
    static int year = 50;  //(n) how many years that will be calculated 
    static int promotedPlayers = 5; //(p) holds the how many player can be promoted 
    static int hire = 5;  //(c) money of hiring a player
    static int[] yearDemand = new int[50];   // holds the demand of each year
    static int[] yearSalary = new int[311];   // holds the salary in each year
    static int[][] location = new int[year+1][6];   //holds the best path
    static int[][] tempLocation = new int[year+1][6];   //holds the every possible path
    static int minMoney = 999999;  // contains min money
    public static void main(String[] args) throws Exception {
        int[][] mem = new int[year+1][promotedPlayers+1]; // memory that contains each possible action
        readTXT(); // read data from txt
        long start = System.currentTimeMillis();// start time
        DP(mem,0,0,0); // calculate the best option using dynamic programming
        readLoc(location);
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis()-start);// calculate the end time
    }
    static public void DP(int[][] mem, int currentYear, int players, int money){
        int currentMoney = money;  //holds the current money the include in a path
        int myPlayers = players;  //holds the current player the include in a path
        currentYear+=1;  // increase current year
        if(currentYear>year){  // if the current year bigger than wanted year return
            return;
        }
        int demand = yearDemand[currentYear-1];   // get demand of that year
        if(myPlayers<demand){   // is my players bigger than demand
            if((myPlayers+promotedPlayers)>=demand){   // is my player and promoted player bigger than demand    
                for (int i = 0; i <= promotedPlayers; i++) {   // for loop for calculating the every possibility
                    currentMoney=money;   //takes the previous year money
                    int currentPLayer = (myPlayers+i)-demand;   //holds index chain the 1 year to end years for each calculation
                    if((myPlayers+i)>=demand){   // is my player and promoted player bigger than demand 
                        if(currentPLayer-1!=-1){   //if there is not any player that will be hold for the next year
                            currentMoney+=yearSalary[currentPLayer-1];   // calculate the current money
                            writeMEM(mem,currentYear,currentMoney,0,currentPLayer,demand,yearSalary[currentPLayer-1],i);// wirte to mem the current stuation
                            if(currentYear==year && minMoney>currentMoney){   // is current year is the end year and minmoney is bigger than money
                                copyArr(location, tempLocation);   //copy the information of the best way from temp to main location
                                minMoney = currentMoney;   //set money the current money
                            }
                        }else{   //if at least one player will be hold for the next year
                            writeMEM(mem,currentYear,currentMoney,0,currentPLayer,demand,0,i);   // write to mem the current stution
                            if(currentYear==year && minMoney>currentMoney){   // is current year is the end year and minmoney is bigger than money
                                copyArr(location, tempLocation);   //copy the information of the best way from temp to main location
                                minMoney = currentMoney;   //set money the current money
                            }
                        }
                        DP(mem, currentYear, currentPLayer, currentMoney);   // recursive call for turning the the problem into subproblems
                    }
                }
            }else{   //if there is not enough player
                currentMoney=money;   //takes the previous year money
                int p = demand-(myPlayers+promotedPlayers);   //calculates the number of player that will be hired
                currentMoney+=p*hire;   // calculate the current money 
                writeMEM(mem,currentYear,currentMoney,demand-(myPlayers+promotedPlayers),0,demand,0,0);   // wirte to mem the current stution
                if(currentYear==year && minMoney>currentMoney){   // is current year is the end year and minmoney is bigger than money
                    copyArr(location, tempLocation);   //copy the information of the best way from temp to main location
                    minMoney = currentMoney;
                }
                DP(mem,currentYear, 0, currentMoney);   // recursive call for turning the the problem into subproblems
            }
        }else{
            for (int i = 0; i <= promotedPlayers; i++) {
                currentMoney=money;   //takes the previous year money
                int currentX = (myPlayers+i)-demand;   //holds index chain the 1 year to end years for each calculation
                if(currentX-1!=-1){   //if there is not any player that will be hold for the next year
                    currentMoney+=yearSalary[currentX-1];
                    writeMEM(mem,currentYear,currentMoney,0,currentX,demand,yearSalary[currentX-1],i);
                    if(currentYear==year && minMoney>currentMoney){   // is current year is the end year and minmoney is bigger than money
                        copyArr(location, tempLocation);   //copy the information of the best way from temp to main location
                        minMoney = currentMoney;
                    }
                }
                else{   //if at least one player will be hold for the next year
                    currentMoney=money;
                    writeMEM(mem,currentYear,currentMoney,0,currentX,demand,0,i);
                    if(currentYear==year && minMoney>currentMoney){   // is current year is the end year and minmoney is bigger than money
                        copyArr(location, tempLocation);   //copy the information of the best way from temp to main location
                        minMoney = currentMoney;
                    }
                }
                DP(mem,currentYear, currentX, currentMoney);
            }
        }
        if(currentYear==year && minMoney>currentMoney){   // is current year is the end year and minmoney is bigger than money
            copyArr(location, tempLocation);   //copy the information of the best way from temp to main location
            minMoney = currentMoney;
        }
        return;
    }
    static public void writeMEM(int[][] mem,int currentYear,int currentMoney,int hired, int currentPlayer,int demand,int salary, int currentX){
        int memIndex=currentYear-1;   //set memoty index to the current year-1
        mem[currentYear-1][currentX] = currentMoney;   //write the money info to mem array
        tempLocation[memIndex][0]=currentYear;   // write path infos to the temp location to hold every step 
        tempLocation[memIndex][1]=currentMoney;
        tempLocation[memIndex][2]=hired;
        tempLocation[memIndex][3]=demand;
        tempLocation[memIndex][4]=salary;
        tempLocation[memIndex][5]=currentPlayer;
    }
    static public void readLoc(int[][] location){//read the locaiton array
        String[] s = {"Year: ","Current Cost: ","Hired Player: ","Demand: ","Given Salary: ","Retained Player Number: "};
        for (int i = 0; i < location.length-1; i++) {
            for (int j = 0; j < location[i].length; j++) {
                System.out.print(String.format(" %s%d,",s[j], location[i][j]));
            }
            System.out.println();
        }
    }
    static public void copyArr(int[][] b, int[][] a){//copies information from array to another
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                b[i][j]=a[i][j];
            }
        }
    }
    static public void readTXT() throws FileNotFoundException{// read txt's and store data
        String salaryTxt = "players_salary.txt";
        String playerDemand = "yearly_player_demand.txt";
        File salaryFile = new File(salaryTxt);
        File demandFile = new File(playerDemand);
        Scanner s = new Scanner(salaryFile);
        Scanner d = new Scanner(demandFile);
        int ind = 0;
        s.nextLine();
        while(s.hasNextLine()){
            String[] get = s.nextLine().split("	");
            yearSalary[ind] = Integer.parseInt(get[1]);
            ind+=1;
        }
        ind = 0;
        d.nextLine();
        while(d.hasNextLine()){
            String[] get = d.nextLine().split("	");
            yearDemand[ind] = Integer.parseInt(get[1]);
            ind+=1;
        }
        d.close();
        s.close();
    }
}