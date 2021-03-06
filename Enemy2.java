import java.awt.Graphics2D;

import javax.swing.ImageIcon;

//second golem enemy
public class Enemy2 {

	double x, y, speed, angle, theta, health, progress;
	int q, r;

	public Enemy2(int q, int r, int x, int y) {
		this.q = q;
		this.r = r;
		this.x = x;
		this.y = y;
		speed = 30;
		angle = 0;
		theta = Math.random() * Math.PI * 2;
		health = 25;
		progress = Math.random();
	}

	public void draw(Graphics2D g, ImageIcon golem) {
		g.drawImage(golem.getImage(), (int) x - 30, (int) y - 40, 60, 50, null);
		g.drawLine((int) x - 25, (int) y, (int) (x - 25 + 50 * health / 25),
				(int) y);
	}

	// moves and updates
	public void update(double delta, Hextile[][] hextiles, int px, int py) {
		angle = Math.atan2(py - y, px - x);
		progress = (progress + delta / 20);

		if (progress > 1) {
			progress = 0;
			ProjectileHandler.addEnemyShot((int) x, (int) y, angle);
			theta = angle + Math.random() * Math.PI / 2 - Math.PI / 4;
		}

		double newx = x + Math.cos(theta) * speed * delta
				* (1 - 0.5 * Math.abs(Math.sin(angle)));
		double newy = y + Math.sin(theta) * speed * delta
				* (1 - 0.5 * Math.abs(Math.sin(angle)));
		double[] alternatePath;

		// If the point is not in the big hexagon
		if (!Hextile.getBigContainHex().contains((int) newx, (int) newy)) {
			alternatePath = alternateRouteCal(delta);

			if (alternatePath != null) {
				if (1.5 <= Math.sqrt(Math.pow(px - x, 2)
						+ Math.pow((py - y) / 2.0, 2))) {
					x = alternatePath[0];
					y = alternatePath[1];
					updateTilePos(hextiles);
				} else {
					x = px;
					y = py;
					updateTilePos(hextiles);
				}
			}

		} else {
			if (1.5 <= Math.sqrt(Math.pow(px - x, 2)
					+ Math.pow((py - y) / 2.0, 2))) {
				x = newx;
				y = newy;
				updateTilePos(hextiles);
			} else {
				x = px;
				y = py;
				updateTilePos(hextiles);
			}
		}

	}

	// /makes sure the golem down't go out of bounds
	private double[] alternateRouteCal(double delta) {

		double newx, newy;
		double tAngle;

		for (int i = 1; i < 90; i += 1) {

			for (int j = 0; j < 2; j++) {
				if (j == 0) {
					tAngle = theta + Math.PI / 2 * i / 90;
				} else {
					tAngle = theta - Math.PI / 2 * i / 90;
				}
				newx = x + Math.cos(tAngle) * speed * delta;
				newy = y + Math.sin(tAngle) * speed * delta;

				if (Hextile.getBigContainHex().contains(newx, newy)) {
					double[] xy = new double[2];
					xy[0] = newx;
					xy[1] = newy;
					return xy;
				}
			}
		}
		return null;
	}

	private void updateTilePos(Hextile[][] hextiles) {
		int[] pos;
		pos = Hextile.hexContainCal(hextiles, (int) x, (int) y);

		if (pos != null) {
			q = pos[0];
			r = pos[1];
		}
	}

	public void addDamage(int dmg) {
		health -= dmg;
	}

	public int getHP() {
		return (int) health;
	}

	public int[] getQR() {
		int[] QR = { q, r };

		return QR;
	}

	public int getY() {
		return (int) y;
	}

	public int getX() {

		return (int) x;
	}

	public double getProgress() {
		return progress;
	}

}
