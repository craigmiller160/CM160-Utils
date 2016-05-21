package io.craigmiller160.utils.util;

/**
 * A utility class for handling Arrays.
 *
 * Created by craig on 5/21/16.
 */
public class ArrayUtil {

    /**
     * A deep toString() call of the provided array. If any of the array's
     * components are also an array, it does a toString() of them as well.
     * This method works through as many levels deep as necessary
     * (ie, an array with an array, that has an array, etc).
     *
     * @param arr the array to do a deep toString() of.
     * @return the String result.
     */
    public static String deepToString(Object[] arr){
        StringBuilder builder = new StringBuilder("[");
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null){
                builder.append("null");
            }
            else if(arr[i].getClass().isArray()){
                builder.append(deepToString((Object[]) arr[i]));
            }
            else{
                builder.append(arr[i].toString());
            }

            if(i < arr.length - 1){
                builder.append(", ");
            }
        }
        builder.append("]");

        return builder.toString();
    }

}
