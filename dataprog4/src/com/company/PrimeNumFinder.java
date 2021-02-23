package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimeNumFinder {
    public static final int NTHREADS = 1000;
    private static final AtomicInteger currentNumber = new AtomicInteger(1);
    private final int maxNumber;
    private static final ArrayList<Integer> primes = new ArrayList<>();
    PrimeNumThread[] primeNumThreads = new PrimeNumThread[NTHREADS];

    public PrimeNumFinder(int maxNumber, int minNumber) {
        if (minNumber%2==0){
            minNumber+=1;
        }
        currentNumber.set(minNumber);
        this.maxNumber = maxNumber;
    }

    /**
     * function that makes and runs the threads, also prints number of primes found
     * @throws InterruptedException
     */
    public void bigFunc() throws InterruptedException {
        for (int i = 0;i<NTHREADS;i++){
            primeNumThreads[i] = new PrimeNumThread();
            primeNumThreads[i].start();
        }
        for (int i = 0;i<NTHREADS;i++){
            primeNumThreads[i].join();
        }
        System.out.println("number of primes total: " + primes.size());
    }

    /**
     * for getting the next available number
     * @return
     */
    public static synchronized int getNumber(){
        int tempNumber= currentNumber.get();
        currentNumber.addAndGet(2);
        return tempNumber;
    }

    /**
     * atomic function to add to arraylist
     * @param prime
     */
    public static synchronized void addToPrimes(int prime){
        primes.add(prime);
    }

    public class PrimeNumThread extends Thread{
        @Override
        public void run() {
            while(true){
                int pointer = getNumber();
                if (pointer >maxNumber) break;
                if (isPrime(pointer)){
                    if (primes.contains(pointer)) {
                        System.out.println("oops");
                    }
                    addToPrimes(pointer);
                }
            }
        }

        /**
         * to calculate whether a number is prime
         * @param n
         * @return
         */
        private boolean isPrime(int n){
            for (int i = 2; i*i<= n; i++) {
                if (n % i == 0){
                    return false;
                }
            }
            return true;
        }

    }
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("min number: ");
        int tempMin = scanner.nextInt();
        System.out.println("max number: ");
        int tempMax = scanner.nextInt();
        PrimeNumFinder primeNumFinder = new PrimeNumFinder(tempMax,tempMin);
        primeNumFinder.bigFunc();

    }
}
