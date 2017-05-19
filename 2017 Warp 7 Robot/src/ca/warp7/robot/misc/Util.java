package ca.warp7.robot.misc;

public class Util {
	
	public static double limit(double val) {
		return limit(val, 1);
	}

	public static double limit(double val, double lim) {
		lim = Math.abs(lim);
		return Math.max(-lim, Math.min(val, lim));
	}

	public static double correct_angle(double angle) {
		return angle + 360 * Math.floor(0.5 - angle / 360);
	}

	public static double deadband(double num) {
		return Math.abs(num) < 0.18 ? 0 : (num - (0.18* Math.signum(num))) * 1.22;
	}

	public static double sinScale(double val, double non_linearity, int passes, double lim) {
		/*
		 * recursive sin scaling! :D
		 * 
		 * :param val: input :param non_linearity: :param passes: how many times
		 * to recurse :return: scaled val
		 */
		double scaled = lim* Math.sin(Math.PI / 2 * non_linearity * val) / Math.sin(Math.PI / 2 * non_linearity);
		if (passes == 1) {
			return scaled;
		} else {
			return sinScale(scaled, non_linearity, passes - 1, lim);
		}

	}

	public static double wrap_accumulator(double acc) {
		if (acc > 1) {
			acc -= 1;
		} else if (acc < -1) {
			acc += 1;
		} else {
			acc = 0;
		}
		return acc;
	}
}
