import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Project: Coursera Algorithms by Standford University: Greedy Algorithms, Minimum Spanning Trees, and Dynamic Programming.
 * Description: Week 1 Programming Assignment.
 *              This class finds a ordering of multiple jobs that has a variable weight
 *              and another variable length (time duration for the job to complete).
 *
 *              Define completion time C_j of job j as the sum of job lengths up to and including job j.
 *
 *              The optimal job ordering should minimize the weighted sum of complement times
 *              min(w_1*C_1 + w_2*C_2 + ... + w_n-1*C_n-1 + w_n*C_n) where n is the total number of jobs,
 *              and w_n is the weight of n th job.
 *
 * @author : Zitao He
 * @date : 2021-01-30 23:53
 **/

public class JobScheduler {
    private int numJobs;
    private diffData[] diffJobs;
    private ratioData[] ratioJobs;

    /**
     * Constructor uses input .txt file
     * This file describes a set of jobs with positive and integral weights and lengths. It has the format:
     * [number_of_jobs]
     * [job_1_weight] [job_1_length]
     * [job_2_weight] [job_2_length]
     * ...
     * @param inputFileName input file name
     * @throws FileNotFoundException throws error when file not found
     */
    public JobScheduler(String inputFileName) throws FileNotFoundException {

        Scanner fileScanner = new Scanner(new File(inputFileName));
        numJobs = Integer.parseInt(fileScanner.nextLine());
        diffJobs = new diffData[numJobs];
        ratioJobs = new ratioData[numJobs];
        int index = 0;

        while (fileScanner.hasNextLine()) {
            Scanner lineScanner = new Scanner(fileScanner.nextLine());
            int weight = lineScanner.nextInt();
            int length = lineScanner.nextInt();
            diffJobs[index] = new diffData(weight, length);
            ratioJobs[index] = new ratioData(weight, length);
            index++;
        }
    }

    /**
     * Calculates weighted sum using two greedy algorithms (schedule jobs based on weight-length or weight/length)
     * @return Two weighted sum. The first BigInteger is the weighted sum of weight-length method
     *         The second BigInteger is the weighted sum of weight/length method
     */
    public BigInteger[] weightedSum(){
        Arrays.sort(diffJobs, Collections.reverseOrder());
        Arrays.sort(ratioJobs, Collections.reverseOrder());

        BigInteger[] weightedSum = new BigInteger[2];
        BigInteger completionTimeDiff = new BigInteger("0");
        BigInteger completionTimeRatio = new BigInteger("0");
        BigInteger weightedSumDiff = new BigInteger("0");
        BigInteger weightedSumRatio = new BigInteger("0");

        for (int i = 0; i < numJobs; i++){
            completionTimeDiff = completionTimeDiff.add(new BigInteger(String.valueOf(diffJobs[i].length)));
            weightedSumDiff = weightedSumDiff.add(completionTimeDiff.multiply(new BigInteger(String.valueOf(diffJobs[i].weight))));

            completionTimeRatio = completionTimeRatio.add(new BigInteger(String.valueOf(ratioJobs[i].length)));
            weightedSumRatio = weightedSumRatio.add(completionTimeRatio.multiply(new BigInteger(String.valueOf(ratioJobs[i].weight))));

        }
        weightedSum[0] = weightedSumDiff;
        weightedSum[1] = weightedSumRatio;
        return weightedSum;
    }

    /**
     * Helper class to contain data for weight-length method.
     */
    public class diffData implements Comparable<diffData> {

        int weight;
        int length;

        public diffData(int weight, int length){
            this.weight = weight;
            this.length = length;
        }

        /**
         * Override compareTo method such that a job that has higher (weight-length) will be scheduled prior to
         * those jobs that have smaller (weight-length).
         * If two jobs have equal difference (weight - length), schedule the job with higher weight first.
         * @param data the other data to compare with
         * @return positive if current value > other value
         *         negative if current value < other value
         */
        @Override
        public int compareTo(diffData data) {
            int priorityDiff = (this.weight - this.length) - (data.weight - data.length);
            if (priorityDiff == 0){
                return this.weight - data.weight;
            }
            return priorityDiff;
        }
    }

    /**
     * Helper class to contain data for weight/length method.
     */
    public class ratioData implements Comparable<ratioData> {
        int weight;
        int length;

        public ratioData(int weight, int length){
            this.weight = weight;
            this.length = length;
        }
        /**
         * Override compareTo method such that a job that has higher (weight/length) will be scheduled prior to
         * those jobs that have smaller (weight/length).
         * If two jobs have equal ratios, schedule either job first.
         * @param data the other data to compare with
         * @return positive if current value > other value
         *         negative if current value < other value
         *         zero if current value = other value
         */
        @Override
        public int compareTo(ratioData data) {
            double priorityDiff = ((double)this.weight/(double)this.length) - ((double)data.weight/(double)data.length);
            if (priorityDiff > 0){return 1;};
            if (priorityDiff < 0){return -1;};
            return 0;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        JobScheduler tester = new JobScheduler("data/jobs.txt");
        System.out.println("Weighted sum by two greedy algorithms are: " + Arrays.toString(tester.weightedSum()));
        //answer is [69119377652, 67311454237] for (weight-length) method and (weight/length) method
    }
}