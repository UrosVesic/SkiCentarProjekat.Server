/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.modelitabela;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import rs.ac.bg.fon.np.sc.commonLib.domen.Korisnik;

/**
 *
 * @author UrosVesic
 */
public class ModelTabeleKorisnik extends AbstractTableModel {

    List<Korisnik> korisnici;
    String[] kolone = {"Ime", "Prezime", "Email"};

    public ModelTabeleKorisnik() {
        korisnici = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return korisnici.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Korisnik k = korisnici.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return k.getIme();
            case 1:
                return k.getPrezime();
            case 2:
                return k.getEmail();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    public void dodaj(Korisnik korisnik) {
        if (!korisnici.contains(korisnik)) {
            korisnici.add(korisnik);
            fireTableDataChanged();
        }
    }

    public void obrisi(Korisnik trenutKorisnik) {
        korisnici.remove(trenutKorisnik);
        fireTableDataChanged();
    }

}
