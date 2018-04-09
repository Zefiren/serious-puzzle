(define (problem problem1) 
    (:domain domain1) 
 
    (:objects 
        train1 - veh 
        tc1 tc2 tc3 tc4 tc5 tc6 tc7 tc8 tc9 tc10 tc11 tc12 tc13 tc14 tc15 tc16 - loc 
        b0 b1 b2 b3 b4 b5 b6 b7  - block 
        s0 s1 s2 s3 s4 s5 s6 s6 - sigItem 
    ) 
 
    (:init 
        (train train1)

        (tc tc1)
        (tc tc2)
        (tc tc3)
        (tc tc4)
        (tc tc5)
        (tc tc6)
        (tc tc7)
        (tc tc8)
        (tc tc9)
        (tc tc10)
        (tc tc11)
        (tc tc12)
        (tc tc13)
        (tc tc14)
        (tc tc15)
        (tc tc16)

        (block b0)
        (block b1)
        (block b2)
        (block b3)
        (block b4)
        (block b5)
        (block b6)
        (block b7)

        (sigDef s0)
        (sigDef s1)
        (sigDef s2)
        (sigDef s3)
        (sigDef s4)
        (sigDef s5)
        (sigDef s6)
        (sigDef s6)

        (trackBlock tc15 b3)
        (trackBlock tc11 b2)
        (trackBlock tc5 b5)
        (trackBlock tc6 b5)
        (trackBlock tc16 b0)
        (trackBlock tc10 b2)
        (trackBlock tc2 b6)
        (trackBlock tc7 b7)
        (trackBlock tc9 b1)
        (trackBlock tc14 b4)
        (trackBlock tc4 b3)
        (trackBlock tc3 b3)
        (trackBlock tc12 b3)
        (trackBlock tc13 b3)
        (trackBlock tc1 b6)
        (trackBlock tc8 b1)

        (safeBlock b0)
        (safeBlock b1)
        (safeBlock b2)
        (safeBlock b3)
        (safeBlock b4)
        (safeBlock b5)
        (safeBlock b6)
        (safeBlock b7)

        (track tc1 tc2)
        (track tc3 tc2)
        (track tc4 tc5)
        (track tc5 tc6)
        (track tc8 tc7)
        (track tc9 tc10)
        (track tc10 tc11)
        (track tc12 tc11)
        (track tc13 tc14)

        (switch tc3 tc2 tc9 tc2)
        (switch tc4 tc5 tc12 tc5)
        (switch tc8 tc7 tc16 tc7)
        (switch tc9 tc10 tc3 tc10)
        (switch tc12 tc11 tc4 tc11)
        (switch tc13 tc14 tc15 tc14)
        (signal s0 tc2 tc3 b3 DANGER)
        (signal s1 tc5 tc4 b3 DANGER)
        (signal s2 tc7 tc8 b1 DANGER)
        (signal s3 tc14 tc13 b3 DANGER)
        (signal s4 tc10 tc8 b1 DANGER)
        (signal s5 tc11 tc13 b3 DANGER)
        (signal s6 tc15 tc13 b3 DANGER)
        (signal s6 tc16 tc8 b1 DANGER)

        (at train1 tc1)
        (inBlock train1 b6)
        (last train1 tc1)
        (fullBlock b6)

    )
    (:goal
        (and
            (at train1 tc6)
            (not(crash train1))
        )
    )
)