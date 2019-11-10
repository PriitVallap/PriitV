from random import *
from time import sleep
#from turtle import *

print("Tere tulemast maailma parimasse teksipõhisesse jalgpallimängu! Kõigepealt palub mäng teil määrata meeskondade tugevused skaalal 1-10")

home_rünnak = input("Sisestage kodumeeskonna ründetugevus (1-10): ")
while not (home_rünnak.isnumeric() and int(home_rünnak) > 0 and int(home_rünnak) < 11):
    print("Palun sisesta täisarvuline väärtus vahemikus 1-10!")
    home_rünnak = input("Sisestage kodumeeskonna ründetugevus (1-10): ")

home_rünnak = int(home_rünnak)
home_kaitse = input("Sisestage kodumeeskonna kaitsetugevus (1-10): ")
while not (home_kaitse.isnumeric() and int(home_kaitse) > 0 and int(home_kaitse) < 11):
    print("Palun sisesta täisarvuline väärtus vahemikus 1-10!")
    home_kaitse = input("Sisestage kodumeeskonna kaitsetugevus (1-10): ")

home_kaitse = int(home_kaitse)    
away_rünnak = input("Sisestage võõrsilmeeskonna ründetugevus (1-10): ")
while not (away_rünnak.isnumeric() and int(away_rünnak) > 0 and int(away_rünnak) < 11):
    print("Palun sisesta täisarvuline väärtus vahemikus 1-10!")
    away_rünnak = input("Sisestage võõrsilmeeskonna ründetugevus (1-10): ")  

away_rünnak = int(away_rünnak)
away_kaitse = input("Sisestage võõrsilmeeskonna kaitsetugevus (1-10): ")
while not (away_kaitse.isnumeric() and int(away_kaitse) > 0 and int(away_kaitse) < 11):
    print("Palun sisesta täisarvuline väärtus vahemikus 1-10!")
    away_kaitse = input("Sisestage võõrsilmeeskonna kaitsetugevus (1-10): ")

away_kaitse = int(away_kaitse)

print("Väga hea. Meeskondade tugevused on paika määratud. Nüüd kiire õpetus: Mäng kestab, nagu ikka, 90 minutit.")
print("Tekib olukordi, kus mäng küsib teilt sisendit.. Seletame kiiresti mida need tähendavad:")
print("1. Kui pall on kaitsjal on teil võimalus A: sööta pall keskväljale (lihtsam) või B: sööta pall ründajatele (nõuab ründajalt triblamist).")
print("2. Kui pall on keskväljal, on teil võimalus A: anda ründajale lihtne sööt (nõuab ründajalt triblamist) või B: anda talle raskem sööt, aga ründaja saab minna kohe löögile")
print("3. Kui pall on ründajal, siis olenevalt olukorrast kas A: triblada kaitsjast mööda ja minna siis löögile või B: minna kohe löögile")
print("Nagu elus ikka, on ka siin mängus vaja peale heade oskuste ja tarkade otsuste ka natukene õnne. Soovime edu!")

kaitsest = None #pikk või lühike
keskväljalt = None #lihtne või keeruline
ründest = None #tribla või löö

home = 0
away = 0
aeg = 0
home_lööke = 0
away_lööke = 0
home_tõrje = 0
away_tõrje = 0
home_sööte = 0 #sööduüritusi
away_sööte = 0 #sööduüritusi
home_söötok = 0 #õnnestunud sööte
away_söötok = 0 #õnnestunud sööte
home_possession = 0 #võimalusi kokku
away_possession = 0 #võimalusi kokku
home_tribla = 0 #triblamisguüritusi
away_tribla = 0 #triblamisuüritusi
home_triblaOK = 0 #õnnestunud triblamisi
away_triblaOK = 0 #õnnestunud triblamisi

#def värav():

def home_kaitsemäng():
    global home_sööte
    global home_söötok
    global home_possession
    global kaitsest
     
    print("###Kodumeeskonnal on pall kaitsetsoonis.###")
     
    print("###Kodumeeskond üritab palli sööduga ülespoole toimetada...###")
    home_possession += 1
    sööduviis = input("Kas kaitsja proovib lühikest või pikka söötu? (Sisesta 'LÜHIKE' või 'PIKK'): ")
    while not (sööduviis.lower() == "pikk" or sööduviis.lower() == "lühike"):
        print("Ei saanud täpselt aru. Proovi uuesti.")
        sööduviis = input("Kas kaitsja proovib lühikest või pikka söötu? (Sisesta 'LÜHIKE' või 'PIKK'): ")
    
    sööduviis = sööduviis.lower()
    if sööduviis == "pikk":
        if kaitse_pikk <= ((home_rünnak - away_kaitse) + 5):
            home_sööte += 1
            home_söötok += 1
             
            print("###Pikk sööt ründeliini õnnestus!###")
            kaitsest = "pikk"
            home_ründemäng()
        elif kaitse_pikk > ((home_rünnak - away_kaitse) +5):
             
            print("###Pikk sööt ründeliini ei õnnestunud!###")
             
            print("###Kodumeeskond kaotab palli ja rünnak luhtus.###")
            home_sööte += 1
    elif sööduviis == "lühike":
        if kaitse_lühike <= ((home_rünnak - away_kaitse) + 5):
             
            print("###Lühike sööt keskväljale õnnestus!###")
            kaitsest = "lühike"
            home_sööte += 1
            home_söötok += 1
            home_keskväli()
        elif kaitse_lühike > ((home_rünnak - away_kaitse) +5):
             
            print("###Lühike sööt keskväljale ei õnnestunud!###")
             
            print("###Kodumeeskond kaotab palli ja rünnak luhtus.###")
            home_sööte += 1

def away_kaitsemäng(): #KÄI KÕIK ÜLE!!!
    global away_sööte
    global away_söötok
    global away_possession
    global kaitsest
     
    print("###Võõrsilmeeskonnal on pall kaitsetsoonis.###")
     
    print("###Võõrsilmeeskond üritab palli sööduga ülespoole toimetada...###")
    away_possession += 1
    sööduviis = input("Kas kaitsja proovib lühikest või pikka söötu? (Sisesta 'LÜHIKE' või 'PIKK'): ")
    while not (sööduviis.lower() == "pikk" or sööduviis.lower() == "lühike"):
        print("Ei saanud täpselt aru. Proovi uuesti.")
        sööduviis = input("Kas kaitsja proovib lühikest või pikka söötu? (Sisesta 'LÜHIKE' või 'PIKK'): ")
    
    sööduviis = sööduviis.lower()
    if sööduviis == "pikk":
        if kaitse_pikk <= ((away_rünnak - home_kaitse) + 5):
            away_sööte += 1
            away_söötok += 1
             
            print("###Pikk sööt ründeliini õnnestus!###")
            kaitsest = "pikk"
            away_ründemäng()
        elif kaitse_pikk > ((away_rünnak - home_kaitse) +5):
             
            print("###Pikk sööt ründeliini ei õnnestunud!###")
             
            print("###Võõrsilmeeskond kaotab palli ja rünnak luhtus.###")
            away_sööte += 1
    elif sööduviis == "lühike":
        if kaitse_lühike <= ((away_rünnak - home_kaitse) + 5):
             
            print("###Lühike sööt keskväljale õnnestus!###")
            kaitsest = "lühike"
            away_sööte += 1
            away_söötok += 1
            away_keskväli()
        elif kaitse_lühike > ((away_rünnak - home_kaitse) +5):
             
            print("###Lühike sööt keskväljale ei õnnestunud!###")
             
            print("###Võõrsilmeeskond kaotab palli ja rünnak luhtus.###")
            away_sööte += 1

def home_keskväli():
    global home_possession
    global home_sööte
    global home_söötok
    global keskväljalt
    home_possession += 1
     
    print("###Pall on kodumeeskonna käes keskväljal.###")
    pooliksööt = input("Kas keskpoolkaitsja üritab ründajale mängida söötu 'LIHTNE' või 'KEERULINE'?")
    while not (pooliksööt.lower() == "lihtne" or pooliksööt.lower() == "keeruline"):
        print("Ei saanud täpselt aru. Proovi uuesti.")
        pooliksööt = input("Kas keskpoolkaitsja üritab ründajale mängida söötu 'LIHTNE' või 'KEERULINE'?")
    pooliksööt = pooliksööt.lower()
    if pooliksööt == "lihtne":
        if poolik_söötkerge <= ((home_rünnak - away_kaitse) + 5):
            home_sööte += 1
            home_söötok += 1
             
            print("###Lihtne sööt ründeliini õnnestus!###")
            keskväljalt = "lihtne"
            home_ründemäng()
        elif poolik_söötkerge > ((home_rünnak - away_kaitse) +5):
             
            print("###Lihtne sööt ründeliini ei õnnestunud!###")
            home_sööte += 1
             
            print("###Kodumeeskond kaotab palli ja rünnak luhtus.###")
    elif pooliksööt == "keeruline":
        if poolik_söötraske <= ((home_rünnak - away_kaitse) + 5):
            home_sööte += 1
            home_söötok += 1
            keskväljalt = "keeruline"
             
            print("Keeruline sööt ründajale õnnestus!")
            home_ründemäng()
        elif poolik_söötraske > ((home_rünnak - away_kaitse) + 5):
             
            print("###Keeruline sööt ründajale ei õnnestunud!###")
            home_sööte += 1
             
            print("###Kodumeeskond kaotab palli ja rünnak luhtus.###")

def away_keskväli():
    global away_possession
    global away_sööte
    global away_söötok
    global keskväljalt
    away_possession += 1
     
    print("###Pall on võõrsilmeeskonna käes keskväljal.###")
    pooliksööt = input("Kas keskpoolkaitsja üritab ründajale mängida söötu 'LIHTNE' või 'KEERULINE'?")
    while not (pooliksööt.lower() == "lihtne" or pooliksööt.lower() == "keeruline"):
        print("Ei saanud täpselt aru. Proovi uuesti.")
        pooliksööt = input("Kas keskpoolkaitsja üritab ründajale mängida söötu 'LIHTNE' või 'KEERULINE'?")
    pooliksööt = pooliksööt.lower()
    if pooliksööt == "lihtne":
        if poolik_söötkerge <= ((away_rünnak - home_kaitse) + 5):
            away_sööte += 1
            away_söötok += 1
             
            print("###Lihtne sööt ründeliini õnnestus!###")
            keskväljalt = "lihtne"
            away_ründemäng()
        elif poolik_söötkerge > ((away_rünnak - home_kaitse) +5):
             
            print("###Lihtne sööt ründeliini ei õnnestunud!###")
            away_sööte += 1
             
            print("###Võõrsilmeeskond kaotab palli ja rünnak luhtus.###")
    elif pooliksööt == "keeruline":
        if poolik_söötraske <= ((away_rünnak - home_kaitse) + 5):
            away_sööte += 1
            away_söötok += 1
            keskväljalt = "keeruline"
             
            print("Keeruline sööt ründajale õnnestus!")
            away_ründemäng()
        elif poolik_söötraske > ((away_rünnak - home_kaitse) + 5):
             
            print("###Keeruline sööt ründajale ei õnnestunud!###")
            away_sööte += 1
             
            print("###Võõrsilmeeskond kaotab palli ja rünnak luhtus.###")
            
def home_ründemäng():
    global home
    global home_lööke
    global home_tribla
    global home_triblaOK
    global home_tõrje
    tsoon = "rünnak"
    if (kaitsest == "pikk" or keskväljalt == "lihtne"):
         
        print("###Ründaja saab palli, aga teda takistab kaitsemängija. Ründaja mõtleb, mida teha...###")
        ründaja_kaugel = input("Kas ründaja üritab teha ('LÖÖ') raske löögi kaitsja tagant või proovib 'TRIBLA'mist ja minna lihtsamale löögile?")
        while not (ründaja_kaugel.lower() == "löö" or ründaja_kaugel.lower() == "tribla"):
            print("Ei saanud täpselt aru. Sisesta kas 'löö' või 'tribla'")
            ründaja_kaugel = input("Kas ründaja üritab teha 'RASKE' löögi kaitsja tagant või proovib 'TRIBLA'mist ja minna lihtsamale löögile?")
        ründaja_kaugel = ründaja_kaugel.lower()
        if ründaja_kaugel == "löö":
             
            print("###Ründaja otsustab kaugelt lüüa!###")
            if löömine_raske <= ((home_rünnak - away_kaitse) + 5):
                 
                print("###Kodumeeskond lõi värava!###")
                home += 1
                home_lööke += 1
                 
                print("Seisuks on nüüd: Kodu ",home,"-",away,"Võõrsil")
            elif löömine_raske > ((home_rünnak - away_kaitse) +5):
                 
                print("###Ründaja lõi peale, aga pall läks väravast mööda.###")
                home_lööke += 1
        elif ründaja_kaugel == "tribla":
            home_tribla += 1
            if triblamine <= ((home_rünnak - away_kaitse) + 5):
                 
                print("###Ründajal õnnestus kaitsjast mööda triblada!###")
                home_triblaOK += 1
                 
                print("###Ründaja saab väga heas kohas palli, ta seisab väravavahiga silmitsi!###")
                 
                print("###Ründaja mõtleb sekundi-murdosa, kuhu pall lüüa.. ###")
                 
                print("(Löögisektorid: Ü = ülesse, A = alla. P - Paremale,K - keskele, V - vasakule)")
                home_lööke += 1
                löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
                while not (löök.isnumeric() and int(löök) >= 1 and int(löök) <= 6):
                    print("Ei saanud täpselt aru. Palun sisesta oma löögisoovile vastav number")
                    löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
                löök = int(löök)
                 
                print("###Ründaja sooritab löögi... Väravavaht proovib tõrjuda!###")
                if löök == väravavaht:
                     
                    print("###Väravavaht tegi suurepärase tõrje ja suutis värava ära hoida!###")
                    home_tõrje += 1
                else:
                     
                    print("###Kodumeeskond lõi värava!###")
                    home += 1
                    print("Seisuks on nüüd: Kodu ",home,"-",away,"Võõrsil")
            elif triblamine > ((home_rünnak - away_kaitse) +5):
                 
                print("###Aga kaitsja oli siin ründajast teravam ja võtab tal palli jala pealt ära.###")    
    elif (keskväljalt ==  "keeruline" or tsoon == "rünnak"):
         
        print("###Ründaja saab väga heas kohas palli, ta seisab väravavahiga silmitsi!###")
         
        print("###Ründaja mõtleb sekundi-murdosa, kuhu pall lüüa.. ###")
         
        print("(Löögisektorid: Ü = ülesse, A = alla. P - Paremale,K - keskele, V - vasakule)")
        home_lööke += 1
        löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
        while not (löök.isnumeric() and int(löök) >= 1 and int(löök) <= 6):
            print("Ei saanud täpselt aru. Palun sisesta oma löögisoovile vastav number")
            löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
        löök = int(löök)
         
        print("###Ründaja sooritab löögi... Väravavaht proovib tõrjuda!###")
        if löök == väravavaht:
             
            print("###Väravavaht tegi suurepärase tõrje ja suutis värava ära hoida!###")
            home_tõrje += 1
        else:
             
            print("###Kodumeeskond lõi värava!###")
            home += 1
             
            print("Seisuks on nüüd: Kodu ",home,"-",away,"Võõrsil")
            
def away_ründemäng():  ##KÄI KÕIK ÜLE!!!
    global away
    global away_lööke
    global away_tribla
    global away_triblaOK
    global away_tõrje
    tsoon = "rünnak"
    if (kaitsest == "pikk" or keskväljalt == "lihtne"):
         
        print("###Ründaja saab palli, aga teda takistab kaitsemängija. Ründaja mõtleb, mida teha...###")
        ründaja_kaugel = input("Kas ründaja üritab teha ('LÖÖ') raske löögi kaitsja tagant või proovib 'TRIBLA'mist ja minna lihtsamale löögile?")
        while not (ründaja_kaugel.lower() == "löö" or ründaja_kaugel.lower() == "tribla"):
            print("Ei saanud täpselt aru. Sisesta kas 'löö' või 'tribla'")
            ründaja_kaugel = input("Kas ründaja üritab teha 'RASKE' löögi kaitsja tagant või proovib 'TRIBLA'mist ja minna lihtsamale löögile?")
        ründaja_kaugel = ründaja_kaugel.lower()
        if ründaja_kaugel == "löö":
             
            print("###Ründaja otsustab kaugelt lüüa!###")
            if löömine_raske <= ((away_rünnak - home_kaitse) + 5):
                 
                print("###Võõrsilmeeskond lõi värava!###")
                away += 1
                away_lööke += 1
                print("Seisuks on nüüd: Kodu ",home,"-",away,"Võõrsil")
            elif löömine_raske > ((away_rünnak - home_kaitse) +5):
                 
                print("###Ründaja lõi peale, aga pall läks väravast mööda.###")
                away_lööke += 1
        elif ründaja_kaugel == "tribla":
            away_tribla += 1
            if triblamine <= ((away_rünnak - home_kaitse) + 5):
                 
                print("###Ründajal õnnestus kaitsjast mööda triblada!###")
                away_triblaOK += 1
                 
                print("###Ründaja saab väga heas kohas palli, ta seisab väravavahiga silmitsi!###")
                 
                print("###Ründaja mõtleb sekundi-murdosa, kuhu pall lüüa.. ###")
                 
                print("(Löögisektorid: Ü = ülesse, A = alla. P - Paremale,K - keskele, V - vasakule)")
                away_lööke += 1
                löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
                while not (löök.isnumeric() and int(löök) >= 1 and int(löök) <= 6):
                    print("Ei saanud täpselt aru. Palun sisesta oma löögisoovile vastav number")
                    löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
                löök = int(löök)
                 
                print("###Ründaja sooritab löögi... Väravavaht proovib tõrjuda!###")
                if löök == väravavaht:
                     
                    print("###Väravavaht tegi suurepärase tõrje ja suutis värava ära hoida!###")
                    away_tõrje += 1
                else:
                     
                    print("###Võõrsilmeeskond lõi värava!###")
                    away += 1
                     
                    print("Seisuks on nüüd: Kodu ",home,"-",away,"Võõrsil")
            elif triblamine > ((away_rünnak - home_kaitse) +5):
                 
                print("###Aga kaitsja oli siin ründajast teravam ja võtab tal palli jala pealt ära.###")    
    elif (keskväljalt ==  "keeruline" or tsoon == "rünnak"):
         
        print("###Ründaja saab väga heas kohas palli, ta seisab väravavahiga silmitsi!###")
         
        print("###Ründaja mõtleb sekundi-murdosa, kuhu pall lüüa.. ###")
         
        print("(Löögisektorid: Ü = ülesse, A = alla. P - Paremale,K - keskele, V - vasakule)")
        away_lööke += 1
        löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
        while not (löök.isnumeric() and int(löök) >= 1 and int(löök) <= 6):
            print("Ei saanud täpselt aru. Palun sisesta oma löögisoovile vastav number")
            löök = input("Vali numbriga tsoon, kuhu lüüa: 1 = ÜV, 2 = ÜK, 3 = ÜP, 4 = AV, 5 = AK, 6 = AP: ")
        löök = int(löök)
         
        print("###Ründaja sooritab löögi... Väravavaht proovib tõrjuda!###")
        if löök == väravavaht:
             
            print("###Väravavaht tegi suurepärase tõrje ja suutis värava ära hoida!###")
            away_tõrje += 1
        else:
             
            print("###Võõrsilmeeskond lõi värava!###")
            away += 1
             
            print("Seisuks on nüüd: Kodu ",home,"-",away,"Võõrsil")
        
while aeg < 90:
     
    aeg += 1
    kas_toimub = randint(1,6)
    #kas_toimub = 1
    palli_valdaja = randint(1,2)
    #palli_valdaja = 1
    tsoon = randint(1,100)
    #tsoon = 5 #alustan kaitsest testis
    kaitse_lühike = randint(1,20)
    kaitse_pikk = randint(1,40)
    poolik_söötkerge = randint(1,20)
    poolik_söötraske = randint(1,40)
    löömine_raske = randint(1,40)
    triblamine = randint(1,20)
    löömine_kerge = randint (1,15)
    väravavaht = randint(1,6)
    if kas_toimub == 1:
         
        print(str(aeg)+". minutil hakkab midagi toimuma...")
         
        if palli_valdaja == 1:
            print("Kodumeeskond saab võimaluse rünnakuks.")
            if tsoon <= 10:
                home_kaitsemäng() 
            elif tsoon > 10 and tsoon < 80:
                home_keskväli()                        
            elif tsoon >= 80:
                home_ründemäng()
        elif palli_valdaja == 2:
            print("Võõrsilmeeskond saab võimaluse rünnakuks.")
            if tsoon <= 10:
                away_kaitsemäng() 
            elif tsoon > 10 and tsoon < 80:
                away_keskväli()                        
            elif tsoon >= 80:
                away_ründemäng()

print("Mängu lõppseisu: KODU     "+str(home)+" - "+str(away)+"     VÕÕRSIL")
kokku_poss = home_possession + away_possession
print("Pallivaldamine:  KODU "+str(round(((home_possession/kokku_poss)*100), 1))+"% - "+str(round(((away_possession/kokku_poss)*100), 1))+"% VÕÕRSIL")
print("Lööke:           KODU     "+str(home_lööke)+" - "+str(away_lööke)+"     VÕÕRSIL")
print("Tõrjeid:         KODU     "+str(home_tõrje)+" - "+str(away_tõrje)+"     VÕÕRSIL")
print("Söödud:          KODU   "+str(home_söötok)+"/"+str(home_sööte)+" - "+str(away_söötok)+"/"+str(away_sööte)+"   VÕÕRSIL")
print("Triblamised:     KODU   "+str(home_triblaOK)+"/"+str(home_tribla)+" - "+str(away_triblaOK)+"/"+str(away_tribla)+"   VÕÕRSIL")
