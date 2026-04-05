package page.info.edit;

import common.CommonStatic;
import common.battle.data.AtkDataModel;
import page.*;
import page.support.ListJtfPolicy;
import utilpc.Interpret;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class AtkEditTable extends Page {

	private static final long serialVersionUID = 1L;

	private final JL latk = new JL(1, "atk");
	private final JL lpre = new JL(1, "preaa");
	private final JL lp0 = new JL(1, "p0");
	private final JL lp1 = new JL(1, "p1");
	private final JL ltp = new JL(1, "type");
	private final JL ldr = new JL(1, "dire");
	private final JL lct = new JL(1, "count");
	private final JL lab = new JL(1, "ability");
	private final JL lmv = new JL(1, "move");
	private final JTxtField fieldAtkDamage = new JTxtField();
	private final JTxtField fieldPreAtkAnim = new JTxtField();
	private final JTxtField fieldLDPoint0 = new JTxtField(); // Applies to AtkDataModel.ld0
	private final JTxtField fieldLDPoint1 = new JTxtField(); // Applies to AtkDataModel.ld1
	private final JTxtField fieldTargetType = new JTxtField(); // Applies to AtkDataModel.targ
	private final JTxtField fieldAtkDirection = new JTxtField(); // Applies to AtkDataModel.dire
	private final JTxtField fieldAtkCount = new JTxtField();
	private final JTxtField fieldAbilities = new JTxtField();
	private final JTxtField fieldMove = new JTxtField();
	private final JTG isr = new JTG(1, "isr");
	private final JTG spt = new JTG();

	private final ListJtfPolicy ljp = new ListJtfPolicy();
	private final boolean editable, isUnit;

	private double mul;
	private double lvMul;
	private final boolean changing = false;

	protected AtkDataModel adm;
	protected ProcTable.AtkProcTable apt;

	protected AtkEditTable(Page p, boolean edit, boolean unit) {
		super(p);
		editable = edit;
		isUnit = unit;
		ini();
	}

	@Override
	protected JButton getBackButton() {
		return null;
	}

	@Override
	public void callBack(Object o) {
		getFront().callBack(o);
	}

	@Override
	protected void resized(int x, int y) {
		set(latk, x, y, 0, 0, 200, 50);
		set(lpre, x, y, 0, 50, 200, 50);
		set(lp0, x, y, 0, 100, 200, 50);
		set(lp1, x, y, 0, 150, 200, 50);
		set(ltp, x, y, 0, 200, 200, 50);
		set(ldr, x, y, 0, 250, 200, 50);
		set(lct, x, y, 0, 300, 200, 50);
		set(lab, x, y, 0, 350, 200, 50);
		set(lmv, x, y, 0, 400, 200, 50);
		set(isr, x, y, 0, 450, 200, 50);
		set(spt, x, y, 200, 450, 200, 50);
		set(fieldAtkDamage, x, y, 200, 0, 200, 50);
		set(fieldPreAtkAnim, x, y, 200, 50, 200, 50);
		set(fieldLDPoint0, x, y, 200, 100, 200, 50);
		set(fieldLDPoint1, x, y, 200, 150, 200, 50);
		set(fieldTargetType, x, y, 200, 200, 200, 50);
		set(fieldAtkDirection, x, y, 200, 250, 200, 50);
		set(fieldAtkCount, x, y, 200, 300, 200, 50);
		set(fieldAbilities, x, y, 200, 350, 200, 50);
		set(fieldMove, x, y, 200, 400, 200, 50);
	}

	protected void setData(AtkDataModel data, double multi, double lvMulti) {
		adm = data;
		mul = multi;
		lvMul = lvMulti;

		fieldAtkDamage.setText(String.valueOf((int) (Math.round(adm.atk * lvMul) * mul)));
		fieldPreAtkAnim.setText(String.valueOf(adm.pre));
		fieldLDPoint0.setText(String.valueOf(adm.ld0));
		fieldLDPoint1.setText(String.valueOf(adm.ld1));
		fieldTargetType.setText(String.valueOf(adm.targ));
		fieldAtkDirection.setText(String.valueOf(adm.dire));
		fieldAtkCount.setText(String.valueOf(adm.count));
		fieldMove.setText(String.valueOf(adm.move));
		apt.setData(adm.ce.common ? adm.ce.rep.proc : adm.proc);
		int alt = adm.getAltAbi();
		int i = 0;
		StringBuilder str = new StringBuilder("{");
		while (alt > 0) {
			if ((alt & 1) == 1) {
				if (str.length() > 1)
					str.append(",");
				str.append(i);
			}
			alt >>= 1;
			i++;
		}
		fieldAbilities.setText(str + "}");
		isr.setSelected(adm.range);
		spt.setVisible(!isUnit || adm.dire != 1);
		if (spt.isVisible()) {
			spt.setSelected(adm.specialTrait);
			spt.setText(MainLocale.PAGE, !isUnit && adm.dire == -1 ? "igtr" : "cntr");
		} else
			adm.specialTrait = false;

		fireDimensionChanged();
	}

	private void ini() {
		set(latk);
		set(lpre);
		set(lp0);
		set(lp1);
		set(ltp);
		set(ldr);
		set(lct);
		set(lab);
		set(lmv);
		set(fieldAtkDamage);
		set(fieldPreAtkAnim);
		set(fieldLDPoint0);
		set(fieldLDPoint1);
		set(fieldTargetType);
		set(fieldAtkDirection);
		set(fieldAtkCount);
		set(fieldAbilities);
		set(fieldMove);
		add(isr);
		add(spt);
		fieldTargetType.setToolTipText(
				"<html>" + "+1 for normal attack<br>"
						+ "+2 to attack kb<br>"
						+ "+4 to attack underground<br>"
						+ "+8 to attack corpse<br>"
						+ "+16 to attack soul<br>"
						+ "+32 to attack ghost<br>"
						+ "+64 to attack entities that can revive others<br>"
						+ "+128 to attack enter animations</html>");
		fieldAtkDirection.setToolTipText("direction, 1 means attack enemies, 0 means not an attack, -1 means assist allies");

		fieldPreAtkAnim.setToolTipText(
				"<html>use 0 for random attack attaching to previous one.<br>pre=0 for first attack will invalidate it</html>");
		StringBuilder ttt = new StringBuilder("<html>enter ID of abilities separated by comma or space.<br>" + "it changes the ability state"
				+ "(has to hot has, not has to has)<br>"
				+ "it won't change back until you make another attack to change it<br>");

		for (int i = 0; i < Interpret.SABIS.length; i++)
			ttt.append(i).append(": ").append(Interpret.SABIS[i]).append("<br>");
		fieldAbilities.setToolTipText(ttt + "</html>");

		isr.setEnabled(editable);
		spt.setEnabled(editable);
		setFocusTraversalPolicy(ljp);
		setFocusCycleRoot(true);

		isr.setLnr(x -> adm.range = isr.isSelected());
		spt.setLnr(x -> adm.specialTrait = spt.isSelected());
	}

	private void input(JTxtField jtf, String text) {
		if (text.length() > 0) {
			if (jtf == fieldAbilities) {
				int[] ent = CommonStatic.parseIntsN(text);
				int ans = 0;
				for (int i : ent)
					if (i >= 0 && i < Interpret.ABIS.length)
						if (ans == -1)
							ans = 1 << i;
						else
							ans |= 1 << i;
				adm.alt = ans;
			}
			int v = CommonStatic.parseIntN(text);
			if (jtf == fieldAtkDamage) {
				adm.atk = findIdealAtkValue(v);
			}
			if (jtf == fieldPreAtkAnim) {
				if (v < 0)
					v = 1;
				adm.pre = v;
			}
			if (jtf == fieldLDPoint0) {
				adm.ld0 = v;
				if (adm.ld0 != 0 || adm.ld1 != 0)
					if (adm.ld1 <= v)
						adm.ld1 = v + 1;
			}
			if (jtf == fieldLDPoint1) {
				adm.ld1 = v;
				if (adm.ld0 != 0 || adm.ld1 != 0)
					if (adm.ld0 >= v)
						adm.ld0 = v - 1;
			}
			if (jtf == fieldTargetType) {
				if (v < 1)
					v = 1;
				adm.targ = v;
			}
			if (jtf == fieldAtkDirection) {
				if (v < -1)
					v = -1;
				if (v > 1)
					v = 1;
				adm.dire = v;
			}
			if (jtf == fieldAtkCount) {
				if (v < 0)
					v = -1;
				adm.count = v;
			}
			if (jtf == fieldMove)
				adm.move = v;
		}
		callBack(null);
	}

	private void set(JLabel jl) {
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		jl.setBorder(BorderFactory.createEtchedBorder());
		add(jl);
	}

	private void set(JTxtField jtf) {
		jtf.setEditable(editable);
		add(jtf);
		ljp.add(jtf);

		jtf.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent fe) {
				if (changing)
					return;
				input(jtf, jtf.getText());
				callBack(null);
			}

		});
	}

	private int findIdealAtkValue(int val) {
		double sign = Math.signum(val);
		double lvValue = Math.round(Math.abs(val) * 1.0 / mul);

		return (int) ((lvValue + 0.5) * sign / lvMul);
	}

	protected void setProcTable(ProcTable.AtkProcTable a) {
		apt = a;
	}
}