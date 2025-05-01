def ausgabe(aus: str, f):
    # Ausgabe in Text und in Datei
    print(aus)
    f.write(aus + "\n")



class Wissen:
    def __hash__(self):
        return tuple(self.nummern).__hash__() ^ tuple(self.sorten).__hash__()

    def __eq__(self, other):
        if isinstance(other, Wissen):
            if self.nummern == other.nummern and self.sorten == other.sorten:
                return True

        return False

    def __init__(self, nummern_set: set, sorten_set: set):
        self.nummern = nummern_set
        self.sorten = sorten_set

    @classmethod
    def fromStrings(cls, nummern_string: str, sorten_string: str):
        nummern_string = nummern_string.split(" ")
        nummern_set = set(map(lambda x: int(x), nummern_string))
        sorten_set = set(sorten_string.split(" "))
        return cls(nummern_set, sorten_set)


def wissenNplus1(wissenN: set, wissenNplus1=None):
    if wissenNplus1 is None:
        wissenNplus1 = set()

    for wissen in wissenN:

        for wissen2 in wissenN:

            if wissen == wissen2:
                continue

            sorten_aehnlichkeiten = wissen.sorten & wissen2.sorten
            nummern_aehnlichkeiten = wissen.nummern & wissen2.nummern
            neuerlangtes_wissen = Wissen(nummern_aehnlichkeiten, sorten_aehnlichkeiten)

            # sorten und nummern aehnlichkeiten sollten eigentlich immer die selbe Anzahl besitzen...

            if (len(sorten_aehnlichkeiten) > 0) and (len(nummern_aehnlichkeiten) > 0) \
                    and (neuerlangtes_wissen not in wissenNplus1):

                # wissen zu gesamt_wissen hinzufügen
                wissenNplus1.add(neuerlangtes_wissen)

    return wissenNplus1


def wissenAusVorherigenSchichten(p_wissen_n, p_wissen_n_plus_1):
    neues_wissen = set()

    for wissen in p_wissen_n:
        wissen_nten_grades = Wissen(wissen.nummern.copy(), wissen.sorten.copy())

        for wissen2 in p_wissen_n_plus_1:
            wissen_nten_grades.nummern -= wissen2.nummern
            wissen_nten_grades.sorten -= wissen2.sorten

        if (wissen_nten_grades not in neues_wissen) and (wissen_nten_grades not in p_wissen_n_plus_1) and \
                (len(wissen_nten_grades.sorten) > 0):
            neues_wissen.add(wissen_nten_grades)

    return neues_wissen


def wunschSchuessel(end_wissen: set, wunsch_sorten: set, datei_nummer: str):
    nicht_geloeste_sorten = wunsch_sorten.copy()
    geloeste_sorten = set()
    schuesseln = set()

    for wissen in end_wissen:
        if wissen.sorten.issubset(nicht_geloeste_sorten):
            schuesseln |= wissen.nummern
            geloeste_sorten |= wissen.sorten
            nicht_geloeste_sorten -= wissen.sorten

    with open(f"BspAusgabe{datei_nummer}.txt", "a") as f:
        ausgabe("\n", f)

        if len(nicht_geloeste_sorten) == 0:
            ausgabe(f"Für die Wunschsorten {wunsch_sorten}, muss Donald aus den Schüsseln {schuesseln} ziehen.", f)
        else:

            if len(geloeste_sorten) == 0:
                ausgabe("Es gibt nicht genug Informationen, um überhaupt eine Aussage über die Wunschsorten zu treffen.", f)
                return

            ausgabe(f"Für die Wunschsorten {wunsch_sorten} ist keine eindeutige Lösung zu finden.", f)
            ausgabe(f"Für die Sorten {geloeste_sorten} muss Donald aus den Schüsseln {schuesseln} ziehen.", f)

            for sorte in nicht_geloeste_sorten:
                for wissen in end_wissen:
                    if sorte in wissen.sorten:
                        ausgabe(f"Für die Sorte {sorte} muss Donald aus eine der folgenden Schüsseln ziehen: {wissen.nummern}.", f)


if __name__ == "__main__":
    print("Bitte geben sie jetzt eine Zahl n zwischen 1 und 7 ein. Die n-te Beispieldatei von der BWINF-Webseite wird dann genutzt")
    print("Die Lösung und evtl. Zwischenergebnisse werden in der Konsole ausgegeben. Sie wird dann auch in der entsprechenden Beispielausgabedatei geschrieben.")
    datei_nummer = input(">> ")

    wissen_n = set()

    with open(f"BspEingabe{datei_nummer}.txt", "r") as bsp:
        anzahl_sorten = int(bsp.readline().replace("\n", "").strip(" "))
        alle_beobachteten_sorten = set()
        alle_beobachteten_nummern = set()
        wunsch_sorten = set(bsp.readline().replace("\n", "").strip(" ").split(" "))
        anzahl_beobachtungen = int(bsp.readline().replace("\n", "").strip(" "))

        for _ in range(anzahl_beobachtungen):
            schuessel_nummern = bsp.readline().replace("\n", "").strip(" ")
            sorten = bsp.readline().replace("\n", "").strip(" ")
            alle_beobachteten_sorten.update(sorten.split(" "))
            alle_beobachteten_nummern.update(set(map(lambda x: int(x), schuessel_nummern.split(" "))))
            wissen_n.add(Wissen.fromStrings(schuessel_nummern, sorten))

        alle_erkennbare_sorten = alle_beobachteten_sorten.copy()
        alle_erkennbare_sorten.update(wunsch_sorten)
        alle_nummern = set(range(1, anzahl_sorten + 1))

        alle_fehlenden_nummern = alle_nummern - alle_beobachteten_nummern
        alle_fehlenden_sorten = alle_erkennbare_sorten - alle_beobachteten_sorten

        if len(alle_fehlenden_sorten) == len(alle_fehlenden_nummern) != 0:
            wissen_n.add(Wissen(alle_fehlenden_nummern, alle_fehlenden_sorten))

    ausgabe_datei = f"BspAusgabe{datei_nummer}.txt"

    with open(ausgabe_datei, "w") as f:
        f.truncate()

    with open(ausgabe_datei, "a") as f:
        ausgabe("Schicht 0", f)
        for wissen in wissen_n:
            ausgabe(f"{wissen.nummern} : {wissen.sorten}", f)

        # Schritt 1: Erstellen von nächster Wissens-Schicht
        wissen_n_plus_1 = wissenNplus1(wissen_n)
        prev_end_wissen = wissen_n
        end_wissen = wissen_n_plus_1

        ausgabe("", f)
        ausgabe("Schicht 1", f)

        for wissen in end_wissen:
            ausgabe(f"{wissen.nummern} : {wissen.sorten}", f)

        n = 1
        while end_wissen != prev_end_wissen:
            n += 1

            # Schritt 2: Kombinieren von n-Grad Wissen mit n+1-Grad Wissen zu n+2-Grad Wissen (dieser und der nächste Schritt wird wiederholt.)
            neue_wissens_schicht = wissenAusVorherigenSchichten(wissen_n, wissen_n_plus_1)

            # Schritt 1: fertigstellen von n+2-Grad Wissen: selber Prozess mit wissen n+1-Grades. (Algorithmus wiederholt sich)
            neue_wissens_schicht = wissenNplus1(wissen_n_plus_1, neue_wissens_schicht)

            wissen_n = wissen_n_plus_1.copy()
            wissen_n_plus_1 = neue_wissens_schicht

            prev_end_wissen = end_wissen.copy()
            end_wissen = wissen_n | wissen_n_plus_1

            ausgabe("", f)
            ausgabe(f"Schicht {n}", f)

            for wissen in end_wissen:
                ausgabe(f"{wissen.nummern} : {wissen.sorten}", f)

        # somit wurde end-wissen bestimmt.
        # jetzt wird eine Ausgabe erstellt:

    wunschSchuessel(end_wissen, wunsch_sorten, datei_nummer)
