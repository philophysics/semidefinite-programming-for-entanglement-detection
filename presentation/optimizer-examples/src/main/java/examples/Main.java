package examples;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.functions.BarrierFunction;
import com.joptimizer.functions.LinearMultivariateRealFunction;
import com.joptimizer.functions.SDPLogarithmicBarrier;
import com.joptimizer.optimizers.BarrierMethod;
import com.joptimizer.optimizers.OptimizationRequest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JOptimizerException {
        example1(); // [-2 + 1.75/Math.sqrt(2), -2 + 1.75/Math.sqrt(2), 0.8141035444]
        example2(); // [2, 0, 0]
    }

    private static void example1() throws JOptimizerException {
        // Objective function (variables (x,y,t), dim = 3)
        LinearMultivariateRealFunction objectiveFunction = new LinearMultivariateRealFunction(new double[] {0, 0, 1}, 0);

        //constraint in the form (A0.x+b0)T.(A0.x+b0) - c0.x - d0 - t < 0
        double[][] A0 = new double[][] {
            {-Math.sqrt(21. / 50.), 0., 0},
            {-Math.sqrt(2) / 5., -1. / Math.sqrt(2), 0}
        };
        double[] b0 = new double[] {0, 0, 0};
        double[] c0 = new double[] {0, 0, 1};
        double d0 = 0;

        //constraint (this is a circle) in the form (A1.x+b1)T.(A1.x+b1) - c1.x - d1 < 0
        double[][] A1 = new double[][] {
            {1, 0, 0},
            {0, 1, 0}
        };
        double[] b1 = new double[] {2, 2, 0};
        double[] c1 = new double[] {0, 0, 0};
        double d1 = Math.pow(1.75, 2);

        double[][] G = new double[][] {
            {1, 0, b0[0], 0, 0, 0},
            {0, 1, b0[1], 0, 0, 0},
            {b0[0], b0[1], d0, 0, 0, 0},
            {0, 0, 0, 1, 0, b1[0]},
            {0, 0, 0, 0, 1, b1[1]},
            {0, 0, 0, b1[0], b1[1], d1}
        };

        double[][] GMatrix = new Array2DRowRealMatrix(G).scalarMultiply(-1).getData();

        double[][] F1 = new double[][] {
            {0, 0, A0[0][0], 0, 0, 0},
            {0, 0, A0[1][0], 0, 0, 0},
            {A0[0][0], A0[1][0], c0[0], 0, 0, 0},
            {0, 0, 0, 0, 0, A1[0][0]},
            {0, 0, 0, 0, 0, A1[1][0]},
            {0, 0, 0, A1[0][0], A1[1][0], c1[0]}
        };

        double[][] F2 = new double[][] {
            {0, 0, A0[0][1], 0, 0, 0},
            {0, 0, A0[1][1], 0, 0, 0},
            {A0[0][1], A0[1][1], c0[1], 0, 0, 0},
            {0, 0, 0, 0, 0, A1[0][1]},
            {0, 0, 0, 0, 0, A1[1][1]},
            {0, 0, 0, A1[0][1], A1[1][1], c1[1]}
        };

        double[][] F3 = new double[][] {
            {0, 0, A0[0][2], 0, 0, 0},
            {0, 0, A0[1][2], 0, 0, 0},
            {A0[0][2], A0[1][2], c0[2], 0, 0, 0},
            {0, 0, 0, 0, 0, A1[0][2]},
            {0, 0, 0, 0, 0, A1[1][2]},
            {0, 0, 0, A1[0][2], A1[1][2], c1[2]}
        };

        List<double[][]> FiMatrixList = new ArrayList<>();

        FiMatrixList.add(FiMatrixList.size(), new Array2DRowRealMatrix(F1).scalarMultiply(-1).getData());
        FiMatrixList.add(FiMatrixList.size(), new Array2DRowRealMatrix(F2).scalarMultiply(-1).getData());
        FiMatrixList.add(FiMatrixList.size(), new Array2DRowRealMatrix(F3).scalarMultiply(-1).getData());

        OptimizationRequest optimizationRequest = new OptimizationRequest();
        optimizationRequest.setF0(objectiveFunction);
        optimizationRequest.setInitialPoint(new double[] { -0.8, -0.8, 10});

        double[] solution = optimize(FiMatrixList, GMatrix, optimizationRequest);
        System.out.println(Arrays.toString(solution));
    }

    private static void example2() throws JOptimizerException {
        //definition of the standard form entities
        int p = 2;
        double[][] C = new double[][]{{2, 1}, {1, 3}};
        double[][] A1 = new double[][]{{2, 1}, {1, 2}};
        double[][] A2 = new double[][]{{5, 2}, {2, 5}};
        double[] b = new double[]{4, 10};

        //JOptimizer formulation: the variables are the 3 distinctive elements of the symmetric 2x2 matrix X:
        //double[][] X = new double[][]{{x00, x01}, {x01, x11}};

        // Objective function: Tr(C, X)
        double[] trCX = new double[]{2, 2, 3};//2*x00 + 2*x01 + 3*x11
        LinearMultivariateRealFunction objectiveFunction = new LinearMultivariateRealFunction(trCX, 0);

        // Linear equalities constraints: Tr(A_i, X) = b_i, i=1,2
        double[][] EQcoeff = new double[2][3];
        double[] eqLimits = new double[2];
        EQcoeff[0] = new double[] {2, 2, 2}; //2*x00 + 2*x01 + 2*x11 (Tr(A1, X))
        EQcoeff[1] = new double[] {5, 4, 5}; //5*x00 + 4*x01 + 5*x11 (Tr(A2, X))
        eqLimits[0] = b[0];
        eqLimits[1] = b[1];

        // Linear matrix inequality, i.e X must be semidefinite positive:
        //for this, we decompose X into its components relative to the standard basis of S.
        //The standard basis in the subspace of symmetric matrices S consist of n matrices
        //that have one element = 1 on the main diagonal (the rest of the elements are 0) and
        //(n-1)+(n-2)+...+2+1 = (n-1)n/2 matrices that have two elements equal 1, the elements
        //that are placed symmetrically with respect to the main diagonal (remember that
        //symmetric matrices have aij = aji, i!=j)

        double[][] G = new double[][] {
            {0, 0},
            {0, 0}
        };

        double[][] GMatrix = new Array2DRowRealMatrix(G).scalarMultiply(-1).getData();

        double[][] F1 = new double[][] {
            {1, 0},
            {0, 0}
        };

        double[][] F2 = new double[][] {
            {0, 1},
            {1, 0}
        };

        double[][] F3 = new double[][] {
            {0, 0},
            {0, 1}
        };

        List<double[][]> FiMatrixList = new ArrayList<>();

        FiMatrixList.add(FiMatrixList.size(), new Array2DRowRealMatrix(F1).scalarMultiply(-1).getData());
        FiMatrixList.add(FiMatrixList.size(), new Array2DRowRealMatrix(F2).scalarMultiply(-1).getData());
        FiMatrixList.add(FiMatrixList.size(), new Array2DRowRealMatrix(F3).scalarMultiply(-1).getData());

        OptimizationRequest optimizationRequest = new OptimizationRequest();
        optimizationRequest.setF0(objectiveFunction);
        optimizationRequest.setInitialPoint(new double[] {1, 0, 1});
        optimizationRequest.setNotFeasibleInitialPoint(new double[] { 1, 0, 1});
        optimizationRequest.setCheckKKTSolutionAccuracy(true);
        optimizationRequest.setA(EQcoeff);
        optimizationRequest.setB(eqLimits);

        double[] solution = optimize(FiMatrixList, GMatrix, optimizationRequest);
        System.out.println(Arrays.toString(solution));
    }

    private static double[] optimize(List<double[][]> fiMatrixList, double[][] gMatrix, OptimizationRequest optimizationRequest) throws JOptimizerException {
        BarrierFunction barrierFunction = new SDPLogarithmicBarrier(fiMatrixList, gMatrix);
        BarrierMethod barrierMethod = new BarrierMethod(barrierFunction);
        barrierMethod.setOptimizationRequest(optimizationRequest);
        barrierMethod.optimize();

        return barrierMethod.getOptimizationResponse().getSolution();
    }
}
