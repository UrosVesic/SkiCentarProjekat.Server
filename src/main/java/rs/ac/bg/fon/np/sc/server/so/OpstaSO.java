/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this opsteIzvrsenjeSo file, choose Tools | Templates
 * and open the opsteIzvrsenjeSo in the editor.
 */
package rs.ac.bg.fon.np.sc.server.so;

import java.util.ArrayList;
import java.util.List;
import rs.ac.bg.fon.np.sc.commonlib.domen.OpstiDomenskiObjekat;
import rs.ac.bg.fon.np.sc.commonlib.validator.ValidationException;
import rs.ac.bg.fon.np.sc.server.db.BrokerBP;

/**
 * Predstavlja apstraktnu sistemsku operaciju koju nasledjuju sve sistemske operacije.
 * Sadrzi metode za izvrsenje sistemske operacije i proveru preduslova za izvrsenje.
 * @author UrosVesic
 */
public abstract class OpstaSO {
    
    /**
     * Broker baze podataka preko koga se upravlja transakcijama
     */
    protected BrokerBP b;
    /**
     * Domenski objekat nad kojim se vrsi sistemska operacija
     */
    protected OpstiDomenskiObjekat odo;
    /**
     * Lista domenskih objekata koju vracaju neke od sistemskih operacija
     */
    protected List<OpstiDomenskiObjekat> lista;
    /**
     * Parametarski kontruktor, postavlja brokera baze podataka i domenski objekat na zadate vrednosti i inicijalizuje atribut lista kao ArrayList
     * @param b  Broker baze podatak
     * @param odo Domenski objekat
     */
    public OpstaSO(BrokerBP b, OpstiDomenskiObjekat odo) {
        this.b = b;
        this.odo = odo;
        lista = new ArrayList<>();
    }
    /**
     * Postavlja domenski objekat na zadatku vrednost
     * @param odo 
     */
    public void setOdo(OpstiDomenskiObjekat odo) {
        this.odo = odo;
    }
    /**
     * Vraca domenski objekat
     * @return OpstiDomenskiObjekat nad kojim treba da se izvrsi ili je izvrsena operacija
     */
    public OpstiDomenskiObjekat getOdo() {
        return odo;
    }
    /**
     * Predstavlja template metodu za izvrsenje sistemske operacije
     * @throws Exception ako dodje do greske prilikom izvrsavanja operacije, uspostavljanja ili raskidanja konekcije, ili upravljanja transakcijom
     * @throws ValidationException ako nisu ispunjeni preduslovi za izvrsenje sistemske operacije
     */
    public void opsteIzvrsenjeSo() throws Exception,ValidationException {

        b.uspostaviKonekciju();
        try {
            proveriPreduslove();
            izvrsiOperaciju();
            b.potvrdiTransakciju();

        } catch (Exception ex) {
            b.ponistiTransakciju();
            ex.printStackTrace();
            throw ex;
        } finally {
            b.raskiniKonekciju();
        }

    }
    /**
     * Vraca listu domenskih objekata koji su dobijeni nakon izvrsavanja sistemske operacije
     * @return listu domenskih objekata 
     */
    public List<OpstiDomenskiObjekat> getLista() {
        return lista;
    }
    /**
     * Izvrsava sistemsku operaciju 
     * @throws Exception ako nije moguce izvrsiti sistemsku operaciju
     */
    public abstract void izvrsiOperaciju() throws Exception;
    /**
     * Proverava preduslove za izvrsenje sistemske operacije
     * @throws ValidationException ako preduslovi za izvrsenje sistemske operacije nisu ispunjeni
     */
    protected abstract void proveriPreduslove() throws ValidationException;
}
