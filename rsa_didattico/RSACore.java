

package rsa_didattico;

import java.math.BigInteger;
import java.util.Random;

/**
 * RSACore
 */
public class RSACore {
    private Random random;
    private BigInteger p, q, n, m, e, k;

    public RSACore() {
        this.random = new Random();
    }

    /**
     * Genera le chiavi RSA

     */
    public boolean generaChiavi() {
        try {
            // PASSO 1: Due numeri primi distinti
            p = generaPrimo();
            do {
                q = generaPrimo();
            } while (q.equals(p));

            // PASSO 2: n = p * q
            n = p.multiply(q);

            // PASSO 3: m = (p-1) * (q-1)
            m = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            // PASSO 4: e con MCD(e,m)=1
            e = trovaE(m);

            // PASSO 5: k con (e*k)%m=1
            k = trovaK(e, m);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
    Cifra con la formula X = M^e mod n
     */
    public BigInteger cifra(BigInteger M, BigInteger eVal, BigInteger nVal) {
        try {
            if (M.compareTo(nVal) >= 0) {
                return null; // Numero troppo grande
            }
            // X = M^e mod n
            return M.modPow(eVal, nVal);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Decifra un numero usando la chiave privata

     */
    public BigInteger decifra(BigInteger X, BigInteger kVal, BigInteger nVal) {
        try {
            // M = X^k mod n
            return X.modPow(kVal, nVal);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Restituisce la chiave pubblica
     */
    public BigInteger[] getChiavePubblica() {
        return new BigInteger[]{e, n};
    }

    /**
     * Restituisce la chiave privata
     */
    public BigInteger[] getChiavePrivata() {
        return new BigInteger[]{k, n};
    }

    /**
     * Restituisce tutti i valori
     */
    public BigInteger[] getTuttiValori() {
        return new BigInteger[]{p, q, n, m, e, k};
    }

    /**
     * Genera un numero primo casuale tra 50 e 200, abbiamo utilizzato il range per evitare di usare il metodo isprobableprime, built in.
     */
    private BigInteger generaPrimo() {
        while (true) {
            int num = 50 + random.nextInt(150);
            BigInteger b = BigInteger.valueOf(num);
            if (isPrimo(b)) {
                return b;
            }
        }
    }

    /**
     * Verifica se un numero è primo
     */
    private boolean isPrimo(BigInteger num) {
        if (num.compareTo(BigInteger.ONE) <= 0) return false;
        if (num.equals(BigInteger.valueOf(2))) return true;
        if (num.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) return false;

        for (BigInteger i = BigInteger.valueOf(3);
             i.multiply(i).compareTo(num) <= 0;
             i = i.add(BigInteger.valueOf(2))) {
            if (num.mod(i).equals(BigInteger.ZERO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trova e coprimo con m
     */
    private BigInteger trovaE(BigInteger m) {
        BigInteger e = BigInteger.valueOf(3);
        while (e.compareTo(m) < 0) {
            if (e.gcd(m).equals(BigInteger.ONE)) {
                return e;
            }
            e = e.add(BigInteger.valueOf(2));
        }
        throw new RuntimeException("Non trovo e");
    }

    /**
     * Trova k come inverso di e modulo m
     */
    private BigInteger trovaK(BigInteger e, BigInteger m) {
        BigInteger h = BigInteger.ONE;
        while (true) {
            BigInteger num = m.multiply(h).add(BigInteger.ONE);
            if (num.mod(e).equals(BigInteger.ZERO)) {
                BigInteger k = num.divide(e);
                if (e.multiply(k).mod(m).equals(BigInteger.ONE)) {
                    return k;
                }
            }
            h = h.add(BigInteger.ONE);
        }
    }
}