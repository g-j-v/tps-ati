package util;

public class Noises {

	static Double[][] ExponencialNoise(Double lambda, Integer rows,
			Integer columns) {
		Double[][] distribution = new Double[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				distribution[i][j] = -1 / lambda * Math.log(Math.random() + 1);
			}
		}

		return distribution;

	}

	static Double[][] GaussianNoise(Double deviation, Double medio,
			Integer rows, Integer columns) {
		Double[][] distribution = new Double[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				distribution[i][j] = Math.sqrt(-2 * Math.random())
						* Math.cos(2 * Math.PI * Math.random());
			}
		}

		return distribution;

	}

	static Double[][] RayleighNoise(Double psi, Integer rows, Integer columns) {
		Double[][] distribution = new Double[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				distribution[i][j] = psi*Math.sqrt(-2*Math.log(1-Math.random()));
			}
		}

		return distribution;

	}

	static Double[][] getUniform(Integer rows, Integer columns) {
		Double[][] distribution = new Double[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				distribution[i][j] = Math.random();
			}
		}

		return distribution;

	}
}
