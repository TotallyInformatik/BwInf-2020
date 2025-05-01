def ausgabe(aus: str, f):
    # Ausgabe in Text und in Datei
    print(aus)
    f.write(aus + "\n")



def finde_stabile_eisdielen_positionen(haus_adressen: list, see_umfang: int):
    for position1 in range(see_umfang - 2):
        for position2 in range(position1 + 1, see_umfang - 1):
            for position3 in range(position2 + 1, see_umfang):

                # Berechnung der Grenzen:

                grenze_1_2 = (position2 - position1) / 2 + position1
                grenze_2_3 = (position3 - position2) / 2 + position2
                grenze_3_1 = position1 - (see_umfang - position3 + position1) / 2
                if grenze_3_1 < 0:
                    grenze_3_1 += see_umfang

                # Festlegen der Mengen:

                menge_1_1 = 0
                menge_1_2 = 0
                position1_besetzt = False  # -> gibt an, ob Eisdeile 1 an der selben Adresse wie ein Haus ist

                menge_2_1 = 0
                menge_2_2 = 0
                position2_besetzt = False  # -> gibt an, ob Eisdeile 2 an der selben Adresse wie ein Haus ist

                menge_3_1 = 0
                menge_3_2 = 0
                position3_besetzt = False  # -> gibt an, ob Eisdeile 3 an der selben Adresse wie ein Haus ist

                for adresse in haus_adressen:
                    if adresse == position1:
                        position1_besetzt = True
                    if adresse == position2:
                        position2_besetzt = True
                    if adresse == position3:
                        position3_besetzt = True

                    if position1 < adresse < grenze_1_2:
                        menge_1_2 += 1

                    if grenze_1_2 < adresse < position2:
                        menge_2_1 += 1
                    if position2 < adresse < grenze_2_3:
                        menge_2_2 += 1

                    if grenze_2_3 < adresse < position3:
                        menge_3_1 += 1

                    # "special case" für grenze_3_1 und die dazugehörigen Mengen:
                    if grenze_3_1 > (see_umfang / 2):
                        if grenze_3_1 > adresse > position3:
                            menge_3_2 += 1
                        if adresse > grenze_3_1 or position1 > adresse > 0:
                            menge_1_1 += 1
                        if adresse == 0:
                            menge_1_1 += 1

                    elif grenze_3_1 < (see_umfang / 2):
                        if adresse > position3 or grenze_3_1 > adresse > 0:
                            menge_3_2 += 1
                        if position1 > adresse > grenze_3_1:
                            menge_1_1 += 1
                        if adresse == 0:
                            menge_3_2 += 1

                if (menge_1_1 == menge_1_2 or (abs(menge_1_1 - menge_1_2) == 1 and position1_besetzt)) and \
                        (menge_2_1 == menge_2_2 or (abs(menge_2_1 - menge_2_2) == 1 and position2_besetzt)) and \
                        (menge_3_1 == menge_3_2 or (abs(menge_3_1 - menge_3_2) == 1 and position3_besetzt)):
                    return position1, position2, position3

    return None


if __name__ == "__main__":
    print(
        "Bitte geben sie jetzt eine Zahl n zwischen 1 und 7 ein. Die n-te Beispieldatei von der BWINF-Webseite wird dann genutzt")
    print(
        "Die Lösung und evtl. Zwischenergebnisse werden in der Konsole ausgegeben. Sie wird dann auch in der entsprechenden Beispielausgabedatei geschrieben.")
    datei_nummer = input(">> ")
    see_umfang = 0
    adressen = []

    with open(f"BspEingabe{datei_nummer}.txt", "r") as f:
        line1 = f.readline().split(" ")
        line2 = f.readline().split(" ")

        see_umfang = int(line1[0])
        adressen = list(map(lambda x: int(x), line2))

    ergebnis = finde_stabile_eisdielen_positionen(adressen, see_umfang)

    ausgabe_datei = f"BspAusgabe{datei_nummer}.txt"

    with open(ausgabe_datei, "w") as f:
        f.truncate()

    with open(ausgabe_datei, "a") as f:
        if ergebnis:
            ausgabe(f"Die stabile Standorten für die Eisdielen sind: {ergebnis}", f)
        else:
            ausgabe("Es gibt keine stabile Standorten für die Eisdielen.", f)
