package rsa_didattico;


import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * RSAGen
 * Implementazione completa RSA con formati PEM
 *
 * Usa:
 * - Generazione chiavi con numeri primi casuali
 * - Cifratura/decifratura di testo
 * - Formato PEM per chiavi pubbliche e private
 */
public class RSAGen {
    private Random random;
    private BigInteger p, q, n, m, e, k;

    public RSAGen() {
        this.random = new Random();
    }

    /**
     * Genera le chiavi RSA
     * @return Oggetto RSAKeys con tutte le informazioni
     */
    public RSAKeys generateKeys() {
        // PASSO 1: Genera due numeri primi distinti
        p = generaNumeroPrimo();
        do {
            q = generaNumeroPrimo();
        } while (q.equals(p));

        // PASSO 2: n = p * q
        n = p.multiply(q);

        // PASSO 3: m = (p-1) * (q-1)
        BigInteger pMinus1 = p.subtract(BigInteger.ONE);
        BigInteger qMinus1 = q.subtract(BigInteger.ONE);
        m = pMinus1.multiply(qMinus1);

        // PASSO 4: e con MCD(e,m) = 1
        e = trovaE(m);

        // PASSO 5: k tale che (e * k) % m = 1
        k = trovaK(e, m);

        return new RSAKeys(p, q, n, m, e, k);
    }

    /**
     * Genera un numero primo casuale tra 50 e 200
     */
    private BigInteger generaNumeroPrimo() {
        while (true) {
            int candidato = 50 + random.nextInt(150);
            BigInteger num = BigInteger.valueOf(candidato);
            if (isPrimo(num)) {
                return num;
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

        BigInteger divisore = BigInteger.valueOf(3);
        BigInteger limite = num.sqrt().add(BigInteger.ONE);

        while (divisore.compareTo(limite) <= 0) {
            if (num.mod(divisore).equals(BigInteger.ZERO)) {
                return false;
            }
            divisore = divisore.add(BigInteger.valueOf(2));
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
        throw new RuntimeException("Non trovato e");
    }

    /**
     * Trova k come inverso moltiplicativo di e modulo m
     * k = (m*h + 1)/e con h intero
     */
    private BigInteger trovaK(BigInteger e, BigInteger m) {
        BigInteger h = BigInteger.ONE;
        while (true) {
            BigInteger numeratore = m.multiply(h).add(BigInteger.ONE);
            if (numeratore.mod(e).equals(BigInteger.ZERO)) {
                BigInteger k = numeratore.divide(e);
                if (e.multiply(k).mod(m).equals(BigInteger.ONE)) {
                    return k;
                }
            }
            h = h.add(BigInteger.ONE);
        }
    }

    /**
     * Cifra un testo usando la chiave pubblica
     */
    public String cifraTesto(String testo, BigInteger e, BigInteger n) {
        // Converte il testo in numero
        BigInteger M = convertiTestoInNumero(testo);

        // Verifica che M sia minore di n
        if (M.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Testo troppo lungo per questo modulo n");
        }

        // Cifra: X = M^e mod n
        BigInteger X = M.modPow(e, n);

        // Restituisce in Base64 per facilità di trasporto
        return Base64.getEncoder().encodeToString(X.toByteArray());
    }

    /**
     * Decifra un testo usando la chiave privata
     */
    public String decifraTesto(String testoBase64, BigInteger k, BigInteger n) {
        // Decodifica Base64
        byte[] bytes = Base64.getDecoder().decode(testoBase64);
        BigInteger X = new BigInteger(bytes);

        // Decifra: M = X^k mod n
        BigInteger M = X.modPow(k, n);

        // Riconverte in testo
        return convertiNumeroInTesto(M);
    }

    /**
     * Converte testo in numero (ogni carattere -> 3 cifre ASCII)
     */
    private BigInteger convertiTestoInNumero(String testo) {
        StringBuilder numero = new StringBuilder();
        for (char c : testo.toCharArray()) {
            numero.append(String.format("%03d", (int) c));
        }
        return new BigInteger(numero.toString());
    }

    /**
     * Converte numero in testo
     */
    private String convertiNumeroInTesto(BigInteger numero) {
        String numStr = numero.toString();
        StringBuilder testo = new StringBuilder();

        while (numStr.length() % 3 != 0) {
            numStr = "0" + numStr;
        }

        for (int i = 0; i < numStr.length(); i += 3) {
            String asciiStr = numStr.substring(i, i + 3);
            int ascii = Integer.parseInt(asciiStr);
            testo.append((char) ascii);
        }
        return testo.toString();
    }

    /**
     * Genera chiave pubblica in formato PEM
     */
    public String toPublicKeyPEM(BigInteger e, BigInteger n) {
        // Formato: "e:" + e + ",n:" + n in Base64
        String data = "e:" + e.toString() + ",n:" + n.toString();
        String base64 = Base64.getEncoder().encodeToString(data.getBytes());

        return "-----BEGIN PUBLIC KEY-----\n" +
                base64.replaceAll("(.{64})", "$1\n") +
                "\n-----END PUBLIC KEY-----";
    }

    /**
     * Genera chiave privata in formato PEM
     */
    public String toPrivateKeyPEM(BigInteger k, BigInteger n) {
        // Formato: "k:" + k + ",n:" + n in Base64
        String data = "k:" + k.toString() + ",n:" + n.toString();
        String base64 = Base64.getEncoder().encodeToString(data.getBytes());

        return "-----BEGIN PRIVATE KEY-----\n" +
                base64.replaceAll("(.{64})", "$1\n") +
                "\n-----END PRIVATE KEY-----";
    }

    /**
     * Parsa chiave pubblica da formato PEM
     */
    public static Map<String, BigInteger> parsePublicKeyPEM(String pem) {
        String base64 = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        String data = new String(Base64.getDecoder().decode(base64));
        String[] parts = data.split(",");

        Map<String, BigInteger> keys = new HashMap<>();
        for (String part : parts) {
            String[] kv = part.split(":");
            keys.put(kv[0], new BigInteger(kv[1]));
        }
        return keys;
    }

    /**
     * Parsa chiave privata da formato PEM
     */
    public static Map<String, BigInteger> parsePrivateKeyPEM(String pem) {
        String base64 = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        String data = new String(Base64.getDecoder().decode(base64));
        String[] parts = data.split(",");

        Map<String, BigInteger> keys = new HashMap<>();
        for (String part : parts) {
            String[] kv = part.split(":");
            keys.put(kv[0], new BigInteger(kv[1]));
        }
        return keys;
    }

    /**
     * Classe contenitore per le chiavi RSA
     */
    public static class RSAKeys {
        public final BigInteger p, q, n, m, e, k;
        public final String publicKeyPEM;
        public final String privateKeyPEM;

        public RSAKeys(BigInteger p, BigInteger q, BigInteger n, BigInteger m,
                       BigInteger e, BigInteger k) {
            this.p = p;
            this.q = q;
            this.n = n;
            this.m = m;
            this.e = e;
            this.k = k;

            RSAGen gen = new RSAGen();
            this.publicKeyPEM = gen.toPublicKeyPEM(e, n);
            this.privateKeyPEM = gen.toPrivateKeyPEM(k, n);
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("p", p.toString());
            map.put("q", q.toString());
            map.put("n", n.toString());
            map.put("m", m.toString());
            map.put("e", e.toString());
            map.put("k", k.toString());
            map.put("publicKeyPEM", publicKeyPEM);
            map.put("privateKeyPEM", privateKeyPEM);
            return map;
        }
    }
}