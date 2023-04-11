package com.somemone.dynamiceeconomy.economy;

public class LinearRegression {
    private final float intercept, slope;
    private final float r2;
    private final float svar0, svar1;

    public LinearRegression(float[] x, float[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        int n = x.length;

        // first pass
        float sumx = 0f, sumy = 0f, sumx2 = 0f;
        for (int i = 0; i < n; i++) {
            sumx  += x[i];
            sumx2 += x[i]*x[i];
            sumy  += y[i];
        }
        float xbar = sumx / n;
        float ybar = sumy / n;

        // second pass: compute summary statistics
        float xxbar = 0f, yybar = 0f, xybar = 0f;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;

        // more statistical analysis
        float rss = 0f;      // residual sum of squares
        float ssr = 0f;      // regression sum of squares
        for (int i = 0; i < n; i++) {
            float fit = slope*x[i] + intercept;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }

        int degreesOfFreedom = n-2;
        r2    = ssr / yybar;
        float svar  = rss / degreesOfFreedom;
        svar1 = svar / xxbar;
        svar0 = svar/n + xbar*xbar*svar1;
    }

    public float intercept() {
        return intercept;
    }

    public float slope() {
        return slope;
    }

    public float R2() {
        return r2;
    }

    public float interceptStdErr() {
        return (float) Math.sqrt(svar0);
    }

    public float slopeStdErr() {
        return (float) Math.sqrt(svar1);
    }

    public float predict(float x) {
        return slope*x + intercept;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%.2f n + %.2f", slope(), intercept()));
        s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
        return s.toString();
    }

}
