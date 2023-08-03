
package com.ibm.icu.impl.number;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.FieldPosition;
import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.text.PluralRules.Operand;
import com.ibm.icu.text.UFieldPosition;
public abstract class DecimalQuantity_AbstractBCD implements DecimalQuantity {
    protected int scale;
    protected int precision;
    protected byte flags;
    protected static final int NEGATIVE_FLAG = 1;
    protected static final int INFINITY_FLAG = 2;
    protected static final int NAN_FLAG = 4;
    protected double origDouble;
    protected int origDelta;
    protected boolean isApproximate;
    protected int lOptPos = Integer.MAX_VALUE;
    protected int lReqPos = 0;
    protected int rReqPos = 0;
    protected int rOptPos = Integer.MIN_VALUE;
    @Override
    public void copyFrom(DecimalQuantity _other) {
        copyBcdFrom(_other);
        DecimalQuantity_AbstractBCD other = (DecimalQuantity_AbstractBCD) _other;
        lOptPos = other.lOptPos;
        lReqPos = other.lReqPos;
        rReqPos = other.rReqPos;
        rOptPos = other.rOptPos;
        scale = other.scale;
        precision = other.precision;
        flags = other.flags;
        origDouble = other.origDouble;
        origDelta = other.origDelta;
        isApproximate = other.isApproximate;
    }
    public DecimalQuantity_AbstractBCD clear() {
        lOptPos = Integer.MAX_VALUE;
        lReqPos = 0;
        rReqPos = 0;
        rOptPos = Integer.MIN_VALUE;
        flags = 0;
        setBcdToZero(); 
        return this;
    }
    @Override
    public void setIntegerLength(int minInt, int maxInt) {
        assert minInt >= 0;
        assert maxInt >= minInt;
        if (minInt < lReqPos) {
            minInt = lReqPos;
        }
        lOptPos = maxInt;
        lReqPos = minInt;
    }
    @Override
    public void setFractionLength(int minFrac, int maxFrac) {
        assert minFrac >= 0;
        assert maxFrac >= minFrac;
        rReqPos = -minFrac;
        rOptPos = -maxFrac;
    }
    @Override
    public long getPositionFingerprint() {
        long fingerprint = 0;
        fingerprint ^= lOptPos;
        fingerprint ^= (lReqPos << 16);
        fingerprint ^= ((long) rReqPos << 32);
        fingerprint ^= ((long) rOptPos << 48);
        return fingerprint;
    }
    @Override
    public void roundToIncrement(BigDecimal roundingIncrement, MathContext mathContext) {
        BigDecimal stripped = roundingIncrement.stripTrailingZeros();
        if (stripped.unscaledValue().compareTo(BigInteger.valueOf(5)) == 0) {
            roundToNickel(-stripped.scale(), mathContext);
            return;
        }
        BigDecimal temp = toBigDecimal();
        temp = temp.divide(roundingIncrement, 0, mathContext.getRoundingMode())
                .multiply(roundingIncrement).round(mathContext);
        if (temp.signum() == 0) {
            setBcdToZero(); 
        } else {
            setToBigDecimal(temp);
        }
    }
    @Override
    public void multiplyBy(BigDecimal multiplicand) {
        if (isInfinite() || isZero() || isNaN()) {
            return;
        }
        BigDecimal temp = toBigDecimal();
        temp = temp.multiply(multiplicand);
        setToBigDecimal(temp);
    }
    @Override
    public void negate() {
      flags ^= NEGATIVE_FLAG;
    }
    @Override
    public int getMagnitude() throws ArithmeticException {
        if (precision == 0) {
            throw new ArithmeticException("Magnitude is not well-defined for zero");
        } else {
            return scale + precision - 1;
        }
    }
    @Override
    public void adjustMagnitude(int delta) {
        if (precision != 0) {
            scale = Utility.addExact(scale, delta);
            origDelta = Utility.addExact(origDelta, delta);
        }
    }
    @Override
    public StandardPlural getStandardPlural(PluralRules rules) {
        if (rules == null) {
            return StandardPlural.OTHER;
        } else {
            @SuppressWarnings("deprecation")
            String ruleString = rules.select(this);
            return StandardPlural.orOtherFromString(ruleString);
        }
    }
    @Override
    public double getPluralOperand(Operand operand) {
        assert !isApproximate;
        switch (operand) {
        case i:
            return isNegative() ? -toLong(true) : toLong(true);
        case f:
            return toFractionLong(true);
        case t:
            return toFractionLong(false);
        case v:
            return fractionCount();
        case w:
            return fractionCountWithoutTrailingZeros();
        default:
            return Math.abs(toDouble());
        }
    }
    @Override
    public void populateUFieldPosition(FieldPosition fp) {
        if (fp instanceof UFieldPosition) {
            ((UFieldPosition) fp).setFractionDigits((int) getPluralOperand(Operand.v),
                    (long) getPluralOperand(Operand.f));
        }
    }
    @Override
    public int getUpperDisplayMagnitude() {
        assert !isApproximate;
        int magnitude = scale + precision;
        int result = (lReqPos > magnitude) ? lReqPos : (lOptPos < magnitude) ? lOptPos : magnitude;
        return result - 1;
    }
    @Override
    public int getLowerDisplayMagnitude() {
        assert !isApproximate;
        int magnitude = scale;
        int result = (rReqPos < magnitude) ? rReqPos : (rOptPos > magnitude) ? rOptPos : magnitude;
        return result;
    }
    @Override
    public byte getDigit(int magnitude) {
        assert !isApproximate;
        return getDigitPos(magnitude - scale);
    }
    private int fractionCount() {
        return -getLowerDisplayMagnitude();
    }
    private int fractionCountWithoutTrailingZeros() {
        return Math.max(-scale, 0);
    }
    @Override
    public boolean isNegative() {
        return (flags & NEGATIVE_FLAG) != 0;
    }
    @Override
    public int signum() {
        return isNegative() ? -1 : isZero() ? 0 : 1;
    }
    @Override
    public boolean isInfinite() {
        return (flags & INFINITY_FLAG) != 0;
    }
    @Override
    public boolean isNaN() {
        return (flags & NAN_FLAG) != 0;
    }
    @Override
    public boolean isZero() {
        return precision == 0;
    }
    public void setToInt(int n) {
        setBcdToZero();
        flags = 0;
        if (n < 0) {
            flags |= NEGATIVE_FLAG;
            n = -n;
        }
        if (n != 0) {
            _setToInt(n);
            compact();
        }
    }
    private void _setToInt(int n) {
        if (n == Integer.MIN_VALUE) {
            readLongToBcd(-(long) n);
        } else {
            readIntToBcd(n);
        }
    }
    public void setToLong(long n) {
        setBcdToZero();
        flags = 0;
        if (n < 0) {
            flags |= NEGATIVE_FLAG;
            n = -n;
        }
        if (n != 0) {
            _setToLong(n);
            compact();
        }
    }
    private void _setToLong(long n) {
        if (n == Long.MIN_VALUE) {
            readBigIntegerToBcd(BigInteger.valueOf(n).negate());
        } else if (n <= Integer.MAX_VALUE) {
            readIntToBcd((int) n);
        } else {
            readLongToBcd(n);
        }
    }
    public void setToBigInteger(BigInteger n) {
        setBcdToZero();
        flags = 0;
        if (n.signum() == -1) {
            flags |= NEGATIVE_FLAG;
            n = n.negate();
        }
        if (n.signum() != 0) {
            _setToBigInteger(n);
            compact();
        }
    }
    private void _setToBigInteger(BigInteger n) {
        if (n.bitLength() < 32) {
            readIntToBcd(n.intValue());
        } else if (n.bitLength() < 64) {
            readLongToBcd(n.longValue());
        } else {
            readBigIntegerToBcd(n);
        }
    }
    public void setToDouble(double n) {
        setBcdToZero();
        flags = 0;
        if (Double.compare(n, 0.0) < 0) {
            flags |= NEGATIVE_FLAG;
            n = -n;
        }
        if (Double.isNaN(n)) {
            flags |= NAN_FLAG;
        } else if (Double.isInfinite(n)) {
            flags |= INFINITY_FLAG;
        } else if (n != 0) {
            _setToDoubleFast(n);
            compact();
        }
    }
    private static final double[] DOUBLE_MULTIPLIERS = {
            1e0,
            1e1,
            1e2,
            1e3,
            1e4,
            1e5,
            1e6,
            1e7,
            1e8,
            1e9,
            1e10,
            1e11,
            1e12,
            1e13,
            1e14,
            1e15,
            1e16,
            1e17,
            1e18,
            1e19,
            1e20,
            1e21 };
    private void _setToDoubleFast(double n) {
        isApproximate = true;
        origDouble = n;
        origDelta = 0;
        long ieeeBits = Double.doubleToLongBits(n);
        int exponent = (int) ((ieeeBits & 0x7ff0000000000000L) >> 52) - 0x3ff;
        if (exponent <= 52 && (long) n == n) {
            _setToLong((long) n);
            return;
        }
        int fracLength = (int) ((52 - exponent) / 3.32192809489);
        if (fracLength >= 0) {
            int i = fracLength;
            for (; i >= 22; i -= 22)
                n *= 1e22;
            n *= DOUBLE_MULTIPLIERS[i];
        } else {
            int i = fracLength;
            for (; i <= -22; i += 22)
                n /= 1e22;
            n /= DOUBLE_MULTIPLIERS[-i];
        }
        long result = Math.round(n);
        if (result != 0) {
            _setToLong(result);
            scale -= fracLength;
        }
    }
    private void convertToAccurateDouble() {
        double n = origDouble;
        assert n != 0;
        int delta = origDelta;
        setBcdToZero();
        String dstr = Double.toString(n);
        if (dstr.indexOf('E') != -1) {
            assert dstr.indexOf('.') == 1;
            int expPos = dstr.indexOf('E');
            _setToLong(Long.parseLong(dstr.charAt(0) + dstr.substring(2, expPos)));
            scale += Integer.parseInt(dstr.substring(expPos + 1)) - (expPos - 1) + 1;
        } else if (dstr.charAt(0) == '0') {
            assert dstr.indexOf('.') == 1;
            _setToLong(Long.parseLong(dstr.substring(2)));
            scale += 2 - dstr.length();
        } else if (dstr.charAt(dstr.length() - 1) == '0') {
            assert dstr.indexOf('.') == dstr.length() - 2;
            assert dstr.length() - 2 <= 18;
            _setToLong(Long.parseLong(dstr.substring(0, dstr.length() - 2)));
        } else {
            int decimalPos = dstr.indexOf('.');
            _setToLong(Long.parseLong(dstr.substring(0, decimalPos) + dstr.substring(decimalPos + 1)));
            scale += decimalPos - dstr.length() + 1;
        }
        scale += delta;
        compact();
        explicitExactDouble = true;
    }
    @Deprecated
    public boolean explicitExactDouble = false;
    @Override
    public void setToBigDecimal(BigDecimal n) {
        setBcdToZero();
        flags = 0;
        if (n.signum() == -1) {
            flags |= NEGATIVE_FLAG;
            n = n.negate();
        }
        if (n.signum() != 0) {
            _setToBigDecimal(n);
            compact();
        }
    }
    private void _setToBigDecimal(BigDecimal n) {
        int fracLength = n.scale();
        n = n.scaleByPowerOfTen(fracLength);
        BigInteger bi = n.toBigInteger();
        _setToBigInteger(bi);
        scale -= fracLength;
    }
    public long toLong(boolean truncateIfOverflow) {
        assert(truncateIfOverflow || fitsInLong());
        long result = 0L;
        int upperMagnitude = Math.min(scale + precision, lOptPos) - 1;
        if (truncateIfOverflow) {
            upperMagnitude = Math.min(upperMagnitude, 17);
        }
        for (int magnitude = upperMagnitude; magnitude >= 0; magnitude--) {
            result = result * 10 + getDigitPos(magnitude - scale);
        }
        if (isNegative()) {
            result = -result;
        }
        return result;
    }
    public long toFractionLong(boolean includeTrailingZeros) {
        long result = 0L;
        int magnitude = -1;
        int lowerMagnitude = Math.max(scale, rOptPos);
        if (includeTrailingZeros) {
            lowerMagnitude = Math.min(lowerMagnitude, rReqPos);
        }
        for (; magnitude >= lowerMagnitude && result <= 1e17; magnitude--) {
            result = result * 10 + getDigitPos(magnitude - scale);
        }
        if (!includeTrailingZeros) {
            while (result > 0 && (result % 10) == 0) {
                result /= 10;
            }
        }
        return result;
    }
    static final byte[] INT64_BCD = { 9, 2, 2, 3, 3, 7, 2, 0, 3, 6, 8, 5, 4, 7, 7, 5, 8, 0, 8 };
    public boolean fitsInLong() {
        if (isZero()) {
            return true;
        }
        if (scale < 0) {
            return false;
        }
        int magnitude = getMagnitude();
        if (magnitude < 18) {
            return true;
        }
        if (magnitude > 18) {
            return false;
        }
        for (int p = 0; p < precision; p++) {
            byte digit = getDigit(18 - p);
            if (digit < INT64_BCD[p]) {
                return true;
            } else if (digit > INT64_BCD[p]) {
                return false;
            }
        }
        return isNegative();
    }
    @Override
    public double toDouble() {
        assert !isApproximate;
        if (isNaN()) {
            return Double.NaN;
        } else if (isInfinite()) {
            return isNegative() ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        long tempLong = 0L;
        int lostDigits = precision - Math.min(precision, 17);
        for (int shift = precision - 1; shift >= lostDigits; shift--) {
            tempLong = tempLong * 10 + getDigitPos(shift);
        }
        double result = tempLong;
        int _scale = scale + lostDigits;
        if (_scale >= 0) {
            int i = _scale;
            for (; i >= 22; i -= 22) {
                result *= 1e22;
                if (Double.isInfinite(result)) {
                    i = 0;
                    break;
                }
            }
            result *= DOUBLE_MULTIPLIERS[i];
        } else {
            int i = _scale;
            for (; i <= -22; i += 22) {
                result /= 1e22;
                if (result == 0.0) {
                    i = 0;
                    break;
                }
            }
            result /= DOUBLE_MULTIPLIERS[-i];
        }
        if (isNegative()) {
            result = -result;
        }
        return result;
    }
    @Override
    public BigDecimal toBigDecimal() {
        if (isApproximate) {
            convertToAccurateDouble();
        }
        return bcdToBigDecimal();
    }
    private static int safeSubtract(int a, int b) {
        int diff = a - b;
        if (b < 0 && diff < a)
            return Integer.MAX_VALUE;
        if (b > 0 && diff > a)
            return Integer.MIN_VALUE;
        return diff;
    }
    private static final int SECTION_LOWER_EDGE = -1;
    private static final int SECTION_UPPER_EDGE = -2;
    public void truncate() {
        if (scale < 0) {
            shiftRight(-scale);
            scale = 0;
            compact();
        }
    }
    @Override
    public void roundToNickel(int magnitude, MathContext mathContext) {
        roundToMagnitude(magnitude, mathContext, true);
    }
    @Override
    public void roundToMagnitude(int magnitude, MathContext mathContext) {
        roundToMagnitude(magnitude, mathContext, false);
    }
    private void roundToMagnitude(int magnitude, MathContext mathContext, boolean nickel) {
        int position = safeSubtract(magnitude, scale);
        int _mcPrecision = mathContext.getPrecision();
        if (_mcPrecision > 0 && precision - _mcPrecision > position) {
            position = precision - _mcPrecision;
        }
        byte trailingDigit = getDigitPos(position);
        if (position <= 0 && !isApproximate && (!nickel || trailingDigit == 0 || trailingDigit == 5)) {
        } else if (precision == 0) {
        } else {
            byte leadingDigit = getDigitPos(safeSubtract(position, 1));
            int section;
            if (!isApproximate) {
                if (nickel && trailingDigit != 2 && trailingDigit != 7) {
                    if (trailingDigit < 2) {
                        section = RoundingUtils.SECTION_LOWER;
                    } else if (trailingDigit < 5) {
                        section = RoundingUtils.SECTION_UPPER;
                    } else if (trailingDigit < 7) {
                        section = RoundingUtils.SECTION_LOWER;
                    } else {
                        section = RoundingUtils.SECTION_UPPER;
                    }
                } else if (leadingDigit < 5) {
                    section = RoundingUtils.SECTION_LOWER;
                } else if (leadingDigit > 5) {
                    section = RoundingUtils.SECTION_UPPER;
                } else {
                    section = RoundingUtils.SECTION_MIDPOINT;
                    for (int p = safeSubtract(position, 2); p >= 0; p--) {
                        if (getDigitPos(p) != 0) {
                            section = RoundingUtils.SECTION_UPPER;
                            break;
                        }
                    }
                }
            } else {
                int p = safeSubtract(position, 2);
                int minP = Math.max(0, precision - 14);
                if (leadingDigit == 0 && (!nickel || trailingDigit == 0 || trailingDigit == 5)) {
                    section = SECTION_LOWER_EDGE;
                    for (; p >= minP; p--) {
                        if (getDigitPos(p) != 0) {
                            section = RoundingUtils.SECTION_LOWER;
                            break;
                        }
                    }
                } else if (leadingDigit == 4 && (!nickel || trailingDigit == 2 || trailingDigit == 7)) {
                    section = RoundingUtils.SECTION_MIDPOINT;
                    for (; p >= minP; p--) {
                        if (getDigitPos(p) != 9) {
                            section = RoundingUtils.SECTION_LOWER;
                            break;
                        }
                    }
                } else if (leadingDigit == 5 && (!nickel || trailingDigit == 2 || trailingDigit == 7)) {
                    section = RoundingUtils.SECTION_MIDPOINT;
                    for (; p >= minP; p--) {
                        if (getDigitPos(p) != 0) {
                            section = RoundingUtils.SECTION_UPPER;
                            break;
                        }
                    }
                } else if (leadingDigit == 9 && (!nickel || trailingDigit == 4 || trailingDigit == 9)) {
                    section = SECTION_UPPER_EDGE;
                    for (; p >= minP; p--) {
                        if (getDigitPos(p) != 9) {
                            section = RoundingUtils.SECTION_UPPER;
                            break;
                        }
                    }
                } else if (nickel && trailingDigit != 2 && trailingDigit != 7) {
                    if (trailingDigit < 2) {
                        section = RoundingUtils.SECTION_LOWER;
                    } else if (trailingDigit < 5) {
                        section = RoundingUtils.SECTION_UPPER;
                    } else if (trailingDigit < 7) {
                        section = RoundingUtils.SECTION_LOWER;
                    } else {
                        section = RoundingUtils.SECTION_UPPER;
                    }
                } else if (leadingDigit < 5) {
                    section = RoundingUtils.SECTION_LOWER;
                } else {
                    section = RoundingUtils.SECTION_UPPER;
                }
                boolean roundsAtMidpoint = RoundingUtils
                        .roundsAtMidpoint(mathContext.getRoundingMode().ordinal());
                if (safeSubtract(position, 1) < precision - 14
                        || (roundsAtMidpoint && section == RoundingUtils.SECTION_MIDPOINT)
                        || (!roundsAtMidpoint && section < 0 )) {
                    convertToAccurateDouble();
                    roundToMagnitude(magnitude, mathContext, nickel); 
                    return;
                }
                isApproximate = false;
                origDouble = 0.0;
                origDelta = 0;
                if (position <= 0 && (!nickel || trailingDigit == 0 || trailingDigit == 5)) {
                    return;
                }
                if (section == SECTION_LOWER_EDGE)
                    section = RoundingUtils.SECTION_LOWER;
                if (section == SECTION_UPPER_EDGE)
                    section = RoundingUtils.SECTION_UPPER;
            }
            boolean isEven = nickel
                    ? (trailingDigit < 2 || trailingDigit > 7
                            || (trailingDigit == 2 && section != RoundingUtils.SECTION_UPPER)
                            || (trailingDigit == 7 && section == RoundingUtils.SECTION_UPPER))
                    : (trailingDigit % 2) == 0;
            boolean roundDown = RoundingUtils.getRoundingDirection(isEven,
                    isNegative(),
                    section,
                    mathContext.getRoundingMode().ordinal(),
                    this);
            if (position >= precision) {
                setBcdToZero();
                scale = magnitude;
            } else {
                shiftRight(position);
            }
            if (nickel) {
                if (trailingDigit < 5 && roundDown) {
                    setDigitPos(0, (byte) 0);
                    compact();
                    return;
                } else if (trailingDigit >= 5 && !roundDown) {
                    setDigitPos(0, (byte) 9);
                    trailingDigit = 9;
                } else {
                    setDigitPos(0, (byte) 5);
                    return;
                }
            }
            if (!roundDown) {
                if (trailingDigit == 9) {
                    int bubblePos = 0;
                    for (; getDigitPos(bubblePos) == 9; bubblePos++) {
                    }
                    shiftRight(bubblePos); 
                }
                byte digit0 = getDigitPos(0);
                assert digit0 != 9;
                setDigitPos(0, (byte) (digit0 + 1));
                precision += 1; 
            }
            compact();
        }
    }
    @Override
    public void roundToInfinity() {
        if (isApproximate) {
            convertToAccurateDouble();
        }
    }
    @Deprecated
    public void appendDigit(byte value, int leadingZeros, boolean appendAsInteger) {
        assert leadingZeros >= 0;
        if (value == 0) {
            if (appendAsInteger && precision != 0) {
                scale += leadingZeros + 1;
            }
            return;
        }
        if (scale > 0) {
            leadingZeros += scale;
            if (appendAsInteger) {
                scale = 0;
            }
        }
        shiftLeft(leadingZeros + 1);
        setDigitPos(0, value);
        if (appendAsInteger) {
            scale += leadingZeros + 1;
        }
    }
    @Override
    public String toPlainString() {
        StringBuilder sb = new StringBuilder();
        if (isNegative()) {
            sb.append('-');
        }
        if (precision == 0 || getMagnitude() < 0) {
            sb.append('0');
        }
        for (int m = getUpperDisplayMagnitude(); m >= getLowerDisplayMagnitude(); m--) {
            sb.append((char) ('0' + getDigit(m)));
            if (m == 0)
                sb.append('.');
        }
        return sb.toString();
    }
    public String toScientificString() {
        StringBuilder sb = new StringBuilder();
        toScientificString(sb);
        return sb.toString();
    }
    public void toScientificString(StringBuilder result) {
        assert(!isApproximate);
        if (isNegative()) {
            result.append('-');
        }
        if (precision == 0) {
            result.append("0E+0");
            return;
        }
        int upperPos = Math.min(precision + scale, lOptPos) - scale - 1;
        int lowerPos = Math.max(scale, rOptPos) - scale;
        int p = upperPos;
        result.append((char) ('0' + getDigitPos(p)));
        if ((--p) >= lowerPos) {
            result.append('.');
            for (; p >= lowerPos; p--) {
                result.append((char) ('0' + getDigitPos(p)));
            }
        }
        result.append('E');
        int _scale = upperPos + scale;
        if (_scale < 0) {
            _scale *= -1;
            result.append('-');
        } else {
            result.append('+');
        }
        if (_scale == 0) {
            result.append('0');
        }
        int insertIndex = result.length();
        while (_scale > 0) {
            int quot = _scale / 10;
            int rem = _scale % 10;
            result.insert(insertIndex, (char) ('0' + rem));
            _scale = quot;
        }
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof DecimalQuantity_AbstractBCD)) {
            return false;
        }
        DecimalQuantity_AbstractBCD _other = (DecimalQuantity_AbstractBCD) other;
        boolean basicEquals =
                scale == _other.scale
                && precision == _other.precision
                && flags == _other.flags
                && lOptPos == _other.lOptPos
                && lReqPos == _other.lReqPos
                && rReqPos == _other.rReqPos
                && rOptPos == _other.rOptPos
                && isApproximate == _other.isApproximate;
        if (!basicEquals) {
            return false;
        }
        if (precision == 0) {
            return true;
        } else if (isApproximate) {
            return origDouble == _other.origDouble && origDelta == _other.origDelta;
        } else {
            for (int m = getUpperDisplayMagnitude(); m >= getLowerDisplayMagnitude(); m--) {
                if (getDigit(m) != _other.getDigit(m)) {
                    return false;
                }
            }
            return true;
        }
    }
    protected abstract byte getDigitPos(int position);
    protected abstract void setDigitPos(int position, byte value);
    protected abstract void shiftLeft(int numDigits);
    protected abstract void shiftRight(int numDigits);
    protected abstract void setBcdToZero();
    protected abstract void readIntToBcd(int input);
    protected abstract void readLongToBcd(long input);
    protected abstract void readBigIntegerToBcd(BigInteger input);
    protected abstract BigDecimal bcdToBigDecimal();
    protected abstract void copyBcdFrom(DecimalQuantity _other);
    protected abstract void compact();
}
