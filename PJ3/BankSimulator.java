package PJ3;

import java.util.*;
import java.io.*;

// You may add new functions or data in this class 
// You may modify any functions or data members here
// You must use Customer, Teller and ServiceArea
// to implement Bank simulator
class BankSimulator {

    String filename;
    // input parameters
    private int numTellers, customerQLimit;
    private int simulationTime, dataSource;
    private int chancesOfArrival, maxTransactionTime;

    // statistical data
    private int numGoaway, numServed, totalWaitingTime;

    // internal data
    private int customerIDCounter;   // customer ID counter
    private ServiceArea servicearea; // service area object
    private Scanner dataFile;	   // get customer data from file
    private Random dataRandom;	   // get customer data using random function

    // most recent customer arrival info, see getCustomerData()
    private boolean anyNewArrival;
    private int transactionTime;
    private int numWaiting;
    private int numFreeT;
    private int numFreeC;
    private int numBusy;
    Teller bArray[];
    Teller fArray[];
    Customer cArray[];

    // initialize data fields
    private BankSimulator() {
        numGoaway = 0;
        numServed = 0;
        totalWaitingTime = 0;
        ServiceArea sa;
        // add statements
    }

    private void setupParameters() {
        /**
         * below is where you enter in the parameters, and account for max
         * values and input mismatch exceptions
         */
        boolean foo;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("Enter Simulation time:");
            try {
                simulationTime = input.nextInt();
                if (simulationTime <= 10000 && simulationTime >= 1) {
                    foo = false;
                } else {
                    System.out.println("Max simulation time is 10000, please enter a new number between 1 and 1000");
                    foo = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter integers only");
                input.next();
                foo = true;
            }
        } while (foo);
        do {
            System.out.println("Enter Max Transaction time of c");
            try {
                maxTransactionTime = input.nextInt();
                if (maxTransactionTime <= 500 && maxTransactionTime >= 1) {
                    foo = false;
                } else {
                    System.out.println("Max transaction time is 500, please enter a new numnber between 1 and 500");
                    foo = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter integers only");
                input.next();
                foo = true;
            }
        } while (foo);

        do {
            System.out.println("Enter chances of new customer");
            try {
                chancesOfArrival = input.nextInt();
                if (chancesOfArrival <= 100 && chancesOfArrival >= 1) {
                    foo = false;
                } else {
                    System.out.println("Please enter a number between 1 and 100");
                    foo = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter integers only");
                input.next();
                foo = true;
            }
        } while (foo);
        do {
            System.out.println("Enter number of tellers");
            try {
                numTellers = input.nextInt();
                if (numTellers <= 10 && numTellers >= 1) {
                    foo = false;
                } else {
                    System.out.println("Please enter a number between 1 and 10");
                    foo = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter integers only");
                input.next();
                foo = true;
            }
        } while (foo);
        do {
            System.out.println("Enter Customer limit");
            try {
                customerQLimit = input.nextInt();
                if (customerQLimit <= 50 && customerQLimit >= 1) {
                    foo = false;
                } else {
                    System.out.println("Max customer queue limit is 50, please enter a number between 1 and 50");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter integers only");
                input.next();
                foo = true;
            }
        } while (foo);

        boolean check1 = false;
        boolean check2 = true;
        System.out.println("File/Random (1/0)");
        dataSource = input.nextInt();
        while (dataSource != 1 && dataSource != 0) {
            System.out.println("Please re-enter");
            dataSource = input.nextInt();
        }

        if (dataSource == 1) {
            String filename = input.nextLine();
            boolean check;
            do {
                check = false;
                System.out.println("Please enter direct path to file name:");
                filename = input.nextLine().trim();
                try {
                    File file = new File(filename);
                    dataFile = new Scanner(file);

                } catch (FileNotFoundException e) {
                    System.out.println("File not found please try again");
                    check = true;
                }
            } while (check);
        } else if (dataSource == 0) {
            System.out.println("Getting random");
            dataRandom = new Random();
        }

        //add check for not 1 or 0!!!
	// read input parameters
        // setup dataFile or dataRandom
        // add statements
    }

    // Refer to step 1 in doSimulation()
    private void getCustomerData() {
        if (dataSource == 1) {
            if (dataFile.hasNextLine()) {
                int data1 = dataFile.nextInt();
                int data2 = dataFile.nextInt();
                anyNewArrival = (((data1 % 100) + 1) <= chancesOfArrival);
                transactionTime = (data2 % maxTransactionTime) + 1;
            }
        } else {
            anyNewArrival = ((dataRandom.nextInt(100) + 1) <= chancesOfArrival);
            transactionTime = dataRandom.nextInt(maxTransactionTime) + 1;
        }
	// get next customer data : from file or random number generator
        // set anyNewArrival and transactionTime
        // add statements
    }

    private void doSimulation() {
        // add statements
        ServiceArea sa = new ServiceArea(numTellers, customerQLimit);

        int num = 1;

	// Initialize ServiceArea
	// Time driver simulation loop
        for (int currentTime = 0; currentTime < simulationTime; currentTime++) {
            boolean foo = true;
            boolean foo1 = true;
            System.out.println("TIME: " + currentTime);

            // Step 1: any new customer enters the bank?
            getCustomerData();

            if (anyNewArrival) {
                Customer c = new Customer(num, transactionTime, currentTime);
                System.out.println("custmer #" + c.getCustomerID() + " arrives with transaction time of " + transactionTime + " units");
                num++;
                if (sa.isCustomerQTooLong()) {
                    System.out.println("Customer " + c.getCustomerID() + " left");
                    numGoaway++;
                } else {
                    sa.insertCustomerQ(c);
                    System.out.println("Customer #" + c.getCustomerID() + " wait in customer queue");
                }

      		    // Step 1.1: setup customer data
                // Step 1.2: check customer waiting queue too long?
                //           customer goes away or enters queue
            } else {
                System.out.println("\tNo new customer!");
            }
            while (!sa.emptyBusyTellerQ()) {
                if (sa.getFrontBusyTellerQ().getEndBusyIntervalTime() == currentTime) {
                    Teller temp = sa.removeBusyTellerQ();
                    System.out.println("Customer " + temp.getCustomer().getCustomerID() + " is free");
                    System.out.println("Teller " + temp.getTellerID() + " is free");
                    temp.busyToFree();
                    sa.insertFreeTellerQ(temp);
                } else {
                    break;
                }
            }
            while (!sa.emptyCustomerQ() && !sa.emptyFreeTellerQ()) {
                Customer tempC = sa.removeCustomerQ();
                Teller tempT = sa.removeFreeTellerQ();
                tempT.freeToBusy(tempC, currentTime);
                sa.insertBusyTellerQ(tempT);
                System.out.println("Customer " + tempC.getCustomerID() + " gets a teller");
                System.out.println("Teller " + tempT.getTellerID() + " starts serving customer " + tempC.getCustomerID() + " for " + tempC.getTransactionTime() + " units");
                totalWaitingTime += (currentTime - tempC.getArrivalTime());
                numServed++;

            }

            System.out.println("-------------------------------------------");

    		// Step 2: free busy tellers, add to free tellerQ
            // Step 3: get free tellers to serve waiting customers 
        } // end simulation loop

        numFreeT = sa.numFreeTellers();
        numBusy = sa.numBusyTellers();
        numWaiting = sa.numWaitingCustomers();
        bArray = new Teller[sa.numBusyTellers()];
        for (int i = 0; i < bArray.length; i++) {
            bArray[i] = sa.removeBusyTellerQ();
        }
        fArray = new Teller[sa.numFreeTellers()];
        for (int i = 0; i < fArray.length; i++) {
            fArray[i] = sa.removeFreeTellerQ();
        }
        cArray = new Customer[sa.numWaitingCustomers()];
        for (int i = 0; i < cArray.length; i++) {
            cArray[i] = sa.removeCustomerQ();
        }
        for (Teller bArray1 : bArray) {
            bArray1.setEndIntervalTime(simulationTime, 1);
        }
        for (Teller fArray1 : fArray) {
            fArray1.setEndIntervalTime(simulationTime, 0);
        }
        // clean-up
    }

    private void printStatistics() {
        System.out.println("-------------------------------------------\n\n");
        System.out.println("End of simulation report:\n");
        System.out.println("Total arrival of customers: " + (numServed + numGoaway));
        System.out.println("Total customers served: " + numServed);
        System.out.println("Total customers gone-away: " + numGoaway + "\n\n");
        System.out.println("*** Current Tellers Info. ***\n\n");
        System.out.println("# of waiting customers : " + cArray.length);
        System.out.println("# of busy tellers      : " + bArray.length);
        System.out.println("# of free tellers      : " + fArray.length + "\n");
        System.out.println("Total waiting time     : " + totalWaitingTime);
        double avg = (totalWaitingTime / numServed);
        System.out.println("Average waiting time   : " + avg + "\n");
        System.out.println("Busy Teller Info: \n\n");
        for (Teller bArray1 : bArray) {
            bArray1.printStatistics();
        }
        System.out.println("");
        System.out.println("");
        System.out.println("Free Teller Info: \n\n");
        for (Teller fArray1 : fArray) {
            fArray1.printStatistics();
        }
    // add statements into this method!
        // print out simulation results
        // see the given example in README file
        // you need to display all free and busy gas pumps
    }

    // *** main method to run simulation ****
    public static void main(String[] args) {
        BankSimulator runBankSimulator = new BankSimulator();
        runBankSimulator.setupParameters();
        runBankSimulator.doSimulation();
        runBankSimulator.printStatistics();
    }

}
