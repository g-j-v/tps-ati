package util;

import javax.swing.SpinnerNumberModel;

/*************************************************************************
 * Compilation: javac Matrix.java Execution: java Matrix
 * 
 * A bare-bones immutable data type for M-by-N matrices.
 * 
 *************************************************************************/

final public class Matrix {
	private final int M; // number of rows
	private final int N; // number of columns
	private final double[][] data; // M-by-N array

	// create M-by-N matrix of 0's
	public Matrix(int M, int N) {
		this.M = M;
		this.N = N;
		data = new double[M][N];
	}

	// create matrix based on 2d array
	public Matrix(double[][] data) {
		M = data.length;
		N = data[0].length;
		this.data = new double[M][N];
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				this.data[i][j] = data[i][j];
	}

	// copy constructor
	private Matrix(Matrix A) {
		this(A.data);
	}

	// create and return a random M-by-N matrix with values between 0 and 1
	public static Matrix random(int M, int N) {
		Matrix A = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				A.data[i][j] = Math.random();
		return A;
	}

	// create and return the N-by-N identity matrix
	public static Matrix identity(int N) {
		Matrix I = new Matrix(N, N);
		for (int i = 0; i < N; i++)
			I.data[i][i] = 1;
		return I;
	}

	public int getWidth() {
		return N;
	}

	public int getHeight() {
		return M;
	}

	// create and return the N-by-N gaussian matrix
	public static Matrix gaussian(int N, double sigma) {
		Matrix I = new Matrix(N, N);
		for (int i = -N / 2; i <= N / 2; i++)
			for (int j = -N / 2; j <= N / 2; j++)
				I.data[i + N / 2][j + N / 2] = 1
						/ (2 * Math.PI * sigma * sigma)
						* Math.pow(Math.E, -(i * i + j * j)
								/ (2 * sigma * sigma));

		I.show();

		return I;
	}

	public void setData(int i, int j, double value) {
		data[i][j] = value;
	}

	// create and return the transpose of the invoking matrix

	// swap rows i and j
	private void swap(int i, int j) {
		double[] temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}

	public double getData(int i, int j) {
		return data[i][j];
	}

	// create and return the transpose of the invoking matrix
	public Matrix transpose() {
		Matrix A = new Matrix(N, M);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				A.data[j][i] = this.data[i][j];
		return A;
	}

	// return C = A + B
	public Matrix plus(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.data[i][j] = A.data[i][j] + B.data[i][j];
		return C;
	}

	// return C = A - B
	public Matrix minus(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.data[i][j] = A.data[i][j] - B.data[i][j];
		return C;
	}

	// return A= sum(A)
	public Double norm1() {
		Double ret = 0.0;
		Matrix A = this;
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				ret += A.data[i][j];
		return ret;
	}

	// return C = A - B
	public Double norm2() {
		Double ret = 0.0;

		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++) {
				double k = this.getData(i, j);
				ret += k * k;
			}
		return Math.sqrt(ret);
	}

	// does A = B exactly?
	public boolean eq(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				if (A.data[i][j] != B.data[i][j])
					return false;
		return true;
	}

	// return C = A * B
	public Matrix times(Matrix B) {
		Matrix A = this;
		if (A.N != B.M)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(A.M, B.N);
		for (int i = 0; i < C.M; i++)
			for (int j = 0; j < C.N; j++)
				for (int k = 0; k < A.N; k++)
					C.data[i][j] += (A.data[i][k] * B.data[k][j]);
		return C;
	}

	// Matriz laplaciana para detección de bordes
	public static Matrix laplaciana() {
		double[][] datas = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
		return new Matrix(datas);
	}

	// Matriz recomendada para detección de bordes con el método de canny
	public static Matrix canny() {
		double[][] datas = {
				{ ((double) 1) / 273, ((double) 4) / 273, ((double) 7) / 273,
						((double) 4) / 273, ((double) 1) / 273 },
				{ ((double) 4) / 273, ((double) 16) / 273, ((double) 26) / 273,
						((double) 16) / 273, ((double) 4) / 273 },
				{ ((double) 7) / 273, ((double) 26) / 273, ((double) 41) / 273,
						((double) 26) / 273, ((double) 7) / 273 },
				{ ((double) 4) / 273, ((double) 16) / 273, ((double) 26) / 273,
						((double) 16) / 273, ((double) 4) / 273 },
				{ ((double) 1) / 273, ((double) 4) / 273, ((double) 7) / 273,
						((double) 4) / 273, ((double) 1) / 273 } };
		return new Matrix(datas);
	}

	// return x = A^-1 b, assuming A is square and has full rank
	public Matrix solve(Matrix rhs) {
		if (M != N || rhs.M != N || rhs.N != 1)
			throw new RuntimeException("Illegal matrix dimensions.");

		// create copies of the data
		Matrix A = new Matrix(this);
		Matrix b = new Matrix(rhs);

		// Gaussian elimination with partial pivoting
		for (int i = 0; i < N; i++) {

			// find pivot row and swap
			int max = i;
			for (int j = i + 1; j < N; j++)
				if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i]))
					max = j;
			A.swap(i, max);
			b.swap(i, max);

			// singular
			if (A.data[i][i] == 0.0)
				throw new RuntimeException("Matrix is singular.");

			// pivot within b
			for (int j = i + 1; j < N; j++)
				b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i];

			// pivot within A
			for (int j = i + 1; j < N; j++) {
				double m = A.data[j][i] / A.data[i][i];
				for (int k = i + 1; k < N; k++) {
					A.data[j][k] -= A.data[i][k] * m;
				}
				A.data[j][i] = 0.0;
			}
		}

		// back substitution
		Matrix x = new Matrix(N, 1);
		for (int j = N - 1; j >= 0; j--) {
			double t = 0.0;
			for (int k = j + 1; k < N; k++)
				t += A.data[j][k] * x.data[k][0];
			x.data[j][0] = (b.data[j][0] - t) / A.data[j][j];
		}
		return x;

	}

	// print matrix to standard output
	public void show() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++)
				System.out.printf("%9.4f ", data[i][j]);
			System.out.println();
		}
	}

	// test client
	public static void main(String[] args) {
		double[][] d = { { 1, 2, 3 }, { 4, 5, 6 }, { 9, 1, 3 } };
		Matrix D = new Matrix(d);
		D.show();
		System.out.println();

		Matrix A = Matrix.random(5, 5);
		A.show();
		System.out.println();

		A.swap(1, 2);
		A.show();
		System.out.println();

		Matrix B = A.transpose();
		B.show();
		System.out.println();

		Matrix C = Matrix.identity(5);
		C.show();
		System.out.println();

		A.plus(B).show();
		System.out.println();

		B.times(A).show();
		System.out.println();

		// shouldn't be equal since AB != BA in general
		System.out.println(A.times(B).eq(B.times(A)));
		System.out.println();

		Matrix b = Matrix.random(5, 1);
		b.show();
		System.out.println();

		Matrix x = A.solve(b);
		x.show();
		System.out.println();

		A.times(x).show();

	}

	public static Matrix idealFourierFilter(SpinnerNumberModel d0) {
		int half = d0.getNumber().intValue();
		int s = 1 + 2 * half;
		double[][] data = new double[s][s];

		for (int i = -half; i <= half; i++) {
			for (int j = -half; j <= half; j++) {
				if (Math.sqrt(i * i + j * j) <= half) {
					data[i + half][j + half] = 1.0;
				} else {
					data[i + half][j + half] = 0.0;
				}
			}
		}

		return new Matrix(data);
	}

	public static Matrix butterworthFourierFilter(SpinnerNumberModel d0) {
		int d = d0.getNumber().intValue();

		int n = 2;
		int halfsize = 10 * d;

		double[][] data = new double[2 * halfsize + 1][2 * halfsize + 1];

		for (int i = -halfsize; i <= halfsize; i++) {
			for (int j = -halfsize; j <= halfsize; j++) {
				double dist = Math.sqrt(i * i + j * j);
				data[i + halfsize][j + halfsize] = 1 / (1 + Math.pow(dist / d,
						2 * n));
			}
		}

		return new Matrix(data);
	}

	public static Matrix mediaFourierFilter(SpinnerNumberModel d0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Matrix sobelFourierFilter(SpinnerNumberModel d0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Matrix prewittFourierFilter(SpinnerNumberModel d0) {
		// TODO Auto-generated method stub
		return null;
	}
}
