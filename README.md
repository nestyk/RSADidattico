# RSA Cryptography System

## Progetto di Crittografia Asimmetrica

**Sviluppatori:** Nesticò Giuseppe, Sacco Gianluca, Cristofaro Giacomo  
**Data:** 25 Marzo 2026  
**Versione:** 1.0

---

## Indice

1. [Introduzione alla Sicurezza](#1-introduzione-alla-sicurezza)
2. [La Crittografia](#2-la-crittografia)
3. [Algoritmo RSA](#3-algoritmo-rsa)
4. [Implementazione](#4-implementazione)
5. [Guida all'Uso](#5-guida-alluso)
6. [Esempio Pratico](#6-esempio-pratico)
7. [Conclusioni](#7-conclusioni)

---

## 1. Introduzione alla Sicurezza

### La Stella della Sicurezza

Prima di parlare di crittografia, è necessario introdurre il concetto fondamentale della **Stella della Sicurezza**. Questo modello aiuta a comprendere come proteggere le informazioni in modo efficace, rappresentando cinque aspetti fondamentali:
AUTENTICITÀ
▲
|
RISERVATEZZA ——+—— INTEGRITÀ
|
▼
DISPONIBILITÀ —— NON RIPUDIO



| Aspetto | Descrizione |
|---------|-------------|
| **Autenticità** | Verifica che l'identità di un utente o sistema sia vera |
| **Riservatezza** | Assicura che solo persone autorizzate possano accedere alle informazioni |
| **Integrità** | Protegge le informazioni da modifiche non autorizzate o accidentali |
| **Disponibilità** | Garantisce che informazioni e servizi siano accessibili quando necessario |
| **Non ripudio** | Impedisce che qualcuno neghi di aver compiuto un'azione |

Se anche uno solo di questi aspetti viene compromesso, l'intero sistema può risultare vulnerabile. La sicurezza non è solo tecnologia, ma anche processi e persone.

---

## 2. La Crittografia

La **crittografia** è la disciplina che trasforma le informazioni in modo che solo le persone autorizzate possano comprenderle.

### Principio di Funzionamento
TESTO NORMALE → [ALGORITMO + CHIAVE] → TESTO CIFRATO → [ALGORITMO + CHIAVE] → TESTO NORMALE



### Tipologie di Crittografia

#### Crittografia Simmetrica
- Utilizza la **stessa chiave** per cifrare e decifrare
- Vantaggio: veloce ed efficiente
- Svantaggio: problema della distribuzione sicura della chiave

#### Crittografia Asimmetrica
- Utilizza **due chiavi diverse**: pubblica e privata
- La chiave pubblica cifra, la chiave privata decifra
- Vantaggio: nessun problema di scambio chiavi

### Esempio Storico: Il Cifrario di Cesare

Attribuito a Gaio Giulio Cesare, consiste nello spostare ogni lettera di un numero fisso di posizioni:
Testo originale: CIAO
Spostamento: +3 posizioni
Testo cifrato: FLDR



Sebbene oggi non sia considerato sicuro, questo metodo aiuta a comprendere i principi base della crittografia.

### Algoritmi Crittografici Moderni

| Algoritmo | Anno | Caratteristiche |
|-----------|------|-----------------|
| **RSA** | 1977 | Il più celebre e diffuso |
| **Diffie-Hellman** | 1976 | Fondamentale per lo scambio di chiavi |
| **DES** | 1977 | Chiavi fino a 56 bit |
| **3DES** | 1998 | Applica DES tre volte |
| **AES** | 2001 | Chiavi a 128, 192 e 256 bit |

---

## 3. Algoritmo RSA

### Storia

L'algoritmo **RSA** venne sviluppato nel 1977 al Massachusetts Institute of Technology (MIT) da **Ron Rivest**, **Adi Shamir** e **Leonard Adleman**. Il nome deriva dalle iniziali dei cognomi dei tre inventori.

### Applicazioni

- Cifratura di dati e comunicazioni
- Firma digitale
- Protocolli SSL/TLS (HTTPS)
- Email sicure (S/MIME)
- Reti VPN (SSH)

### Principio di Sicurezza

La sicurezza dell'RSA si basa sulla **difficoltà computazionale di fattorizzare numeri grandi**. Mentre moltiplicare due numeri primi è semplice, il processo inverso (trovare i fattori primi di un numero) diventa estremamente complesso all'aumentare della dimensione dei numeri.

### 3.1 Generazione delle Chiavi

Il processo di generazione delle chiavi segue questi passaggi:

#### Passo 1: Scelta dei numeri primi
Si scelgono due numeri primi distinti **p** e **q** (nella pratica, numeri con centinaia di cifre decimali).
p = 61
q = 53



#### Passo 2: Calcolo del modulo n
n = p × q
n = 61 × 53 = 3233



#### Passo 3: Calcolo della funzione di Eulero φ(n)
φ(n) = (p - 1) × (q - 1)
φ(n) = (61 - 1) × (53 - 1) = 60 × 52 = 3120



#### Passo 4: Scelta della chiave pubblica e
Si sceglie un intero **e** tale che **MCD(e, φ(n)) = 1**. Operativamente, si procede per tentativi:
e = 2 → MCD(2, 3120) = 2 → non valido
e = 3 → MCD(3, 3120) = 3 → non valido
e = 5 → MCD(5, 3120) = 5 → non valido
...
e = 17 → MCD(17, 3120) = 1 → valido


#### Passo 5: Calcolo della chiave privata d
Si trova **d** tale che **(e × d) mod φ(n) = 1**. Operativamente:
Si cerca h tale che: d = (φ(n) × h + 1) / e sia intero

h = 1 → (3120 × 1 + 1) / 17 = 3121 / 17 = 183,59 → non intero
h = 2 → (3120 × 2 + 1) / 17 = 6241 / 17 = 367,12 → non intero
...
h = 15 → (3120 × 15 + 1) / 17 = 46801 / 17 = 2753 → intero


Risultato: **d = 2753**

#### Passo 6: Definizione delle chiavi
- **Chiave pubblica**: (e, n) = (17, 3233)
- **Chiave privata**: (d, n) = (2753, 3233)

### 3.2 Cifratura del Messaggio

Per evitare attacchi basati sull'analisi delle frequenze, il messaggio viene suddiviso in blocchi di dimensione **g** bit, dove **2^g < n**.

Dato un blocco di messaggio **M** (considerato come numero intero), il blocco cifrato **C** viene calcolato con la formula:
C = M^e mod n



**Esempio:**
M = 65
C = 65^17 mod 3233 = 2790


### 3.3 Decifratura del Messaggio

Il destinatario utilizza la propria chiave privata per decifrare:
M = C^d mod n



**Esempio:**
C = 2790
M = 2790^2753 mod 3233 = 65



### 3.4 Dimostrazione Matematica

La correttezza dell'algoritmo è garantita dal **Teorema di Eulero**:

Se **M** e **n** sono coprimi:
M^(φ(n)) ≡ 1 (mod n)



Da cui:
C^d = (M^e)^d = M^(e×d) = M^(k×φ(n)+1) = M × (M^(φ(n)))^k ≡ M (mod n)



---

## 4. Implementazione

### 4.1 Architettura del Software

Il progetto è composto da due classi principali:

RSAGui.java
│ │ Interfaccia Grafica │ │
│ │ - Header con titolo e sviluppatori │ │
│ │ - Pannello visualizzazione chiavi │ │
│ │ - Pannello operazioni cifratura/decifratura │ │
│ ───────────────────────────────────────────────── │
│ │ RSACore.java 
│ │ │ Generazione Chiavi 
│ │ │ - generaPrimo() 
│ │ │ - isPrimo()
│ │ │ - trovaE() 
│ │ │ - trovaK() 
│ │ │ - cifra()
│ │ │ - decifra() 



### 4.2 Classe RSACore

#### Generazione dei Numeri Primi

```java
private BigInteger generaPrimo() {
    while (true) {
        int num = 50 + random.nextInt(150);  // Range 50-200
        BigInteger b = BigInteger.valueOf(num);
        if (isPrimo(b)) {
            return b;
        }
    }
}
```
Test del numero Primo
```java
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
```
Ricerca della Chiave Pubblica e
```java
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
```
Ricerca della Chiave Privata d
```java
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
Esponenziazione Modulare (implementazione custom)
Per dimostrare la comprensione dell'algoritmo, è stata implementata una versione custom dell'esponenziazione modulare:

```java
public static BigInteger modPow(BigInteger base, BigInteger exponent, BigInteger modulus) {
    BigInteger result = BigInteger.ONE;
    BigInteger b = base.mod(modulus);
    BigInteger e = exponent;
    
    while (e.compareTo(BigInteger.ZERO) > 0) {
        // Se il bit meno significativo è 1, moltiplica
        if (e.testBit(0)) {
            result = result.multiply(b).mod(modulus);
        }
        // Piazza la base (square)
        b = b.multiply(b).mod(modulus);
        // Sposta i bit a destra (divide per 2)
        e = e.shiftRight(1);
    }
    
    return result;
}
```
4.3 Classe RSAGUI
L'interfaccia grafica è stata sviluppata con Java Swing, adottando design moderno e funzionalità intuitive.

Caratteristiche dell'Interfaccia
Header viola (#5856D6) con titolo e sviluppatori

Pannello chiavi: visualizzazione di p, q, n, φ(n), e, d

Pannello operazioni: campi per cifratura e decifratura

Scroll pane: per visualizzare tutti i campi

Feedback visivo: messaggi temporanei (1.5 secondi)

Hover effect: sui bottoni
