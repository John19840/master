package com.tsystems.javaschool.tasks.pyramid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here
        //check null pointer
        if(inputNumbers.contains(null)) throw new CannotBuildPyramidException();
        List<Integer> localNumbers;
        //check out of memory
        try {
            localNumbers = new ArrayList<>(inputNumbers);
        }
        catch (OutOfMemoryError E) {
            throw new CannotBuildPyramidException();
        }
        //check pyramid number
        int numLength = localNumbers.size();
        int calc_num = 8*numLength+1;
        int t = (int)Math.sqrt(calc_num);
        if(!(t*t == calc_num)) throw new CannotBuildPyramidException();

        Collections.sort(localNumbers);
        //find out result array size
        int sum = 1;
        int i = 1;
        while(sum != numLength){
            i++;
            sum += i;
        }
        //create result array size
        int[][] expected = new int[i][i*2-1];
        for (int j = 0; j < expected.length; j++) {
            for (int k = 0; k < expected[j].length; k++) {
                expected[j][k] = 0;
            }
        }
        //write digits in pyramid order to result array
        int j = 1;
        int summ = 0;
        int localsumm = 0;
        int horiz = (expected[0].length-1)/2;
        int horizCount;
        for (int k = 0; k < expected.length; k++) {
            horizCount = horiz;
            for (int l = 0; l < j; l++) {
                expected[k][horizCount] = localNumbers.get(localsumm);
                horizCount+=2;
                localsumm++;
            }
            j++;
            summ += j;
            horiz--;
        }
        return expected;
    }


}
