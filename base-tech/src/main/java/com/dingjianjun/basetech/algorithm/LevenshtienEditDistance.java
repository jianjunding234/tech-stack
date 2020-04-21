package com.dingjianjun.basetech.algorithm;

/**
 * @author : Jianjun.Ding
 * @description: Levenshtien编辑距离
 * @date 2020/4/8
 */
public class LevenshtienEditDistance {
    public static void main(String[] args) {
        String word1 = "stiine";
        String word2 = "kting";
        int minEditDis = minDistance(word1, word2);
        System.out.println(minEditDis);
    }



    public static int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] v = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            v[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            v[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    v[i][j] = v[i - 1][j - 1];
                } else {
                    v[i][j] = Math.min(Math.min(v[i - 1][j - 1], v[i - 1][j]), v[i][j - 1]) + 1;
                }
            }
        }
        return v[m][n];
    }
}
