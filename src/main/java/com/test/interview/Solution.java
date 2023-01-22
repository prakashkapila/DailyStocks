package com.test.interview;

public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int min = Double.valueOf(Math.pow(10,-6)).intValue();
        int max = Double.valueOf(Math.pow(10,-6)).intValue();
        int len1 = nums1.length;
        int len2 = nums2.length;
        if(len1 > 1000 || len2 > 1000 || (len1+len2 <0) )
      {
          System.out.println("Given Values are not permissable"+len1+  " "+len2);
        return min -1;
      }
      int[] merged = new int[len1+len2];
      
      int med = Double.valueOf((len1+len2)/2).intValue(); 
      double dbl = Double.valueOf((len1+len2)/2);
      int i=0;
      for(;i<len1;i++)
      {
          merged[i]=nums1[i];
      }
       for(int j=0;j<len2;j++)
        {
            merged[i++]=nums2[j];
        }
        double ret = 0.0; 
      if(dbl-med >0)
      {
           return merged[med+1]; 
      }
     int one = med-1;
     int tw = one+1;
     
    System.out.println(med+ " "+one+" "+tw);
    System.out.println(merged);

       ret = (merged[one]+merged[tw]);
       ret = ret/2;
       return ret;  
      
    }
    public static void main(String ar[])
    {
    	Solution sol = new Solution();
    	sol.findMedianSortedArrays(new int[] {1,2},new int[]{3,4});
    }
}