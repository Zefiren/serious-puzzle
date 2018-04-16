(define (problem problem1) 
    (:domain domain1) 
 
    (:objects 
        train0 train1 - veh 
        tc1 tc2 tc3 tc4 tc5 tc6 tc7 tc8 tc9 tc10 tc11 tc12 - loc 
        b0 b1 b2  - block 
        s0 s2 - sigItem 
    ) 
 
    (:init 
        (train train0)
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

        (block b0)
        (block b1)
        (block b2)

        (sigDef s0)
        (sigDef s2)

        (trackBlock tc6 b0)
        (trackBlock tc3 b2)
        (trackBlock tc10 b0)
        (trackBlock tc1 b2)
        (trackBlock tc5 b0)
        (trackBlock tc7 b0)
        (trackBlock tc12 b0)
        (trackBlock tc11 b1)
        (trackBlock tc8 b0)
        (trackBlock tc4 b0)
        (trackBlock tc9 b0)
        (trackBlock tc2 b2)

        (safeBlock b0)
        (safeBlock b1)
        (safeBlock b2)

        (track tc1 tc2)
        (track tc2 tc3)
        (track tc3 tc4)
        (track tc4 tc5)
        (track tc5 tc10)
        (track tc6 tc7)
        (track tc7 tc8)
        (track tc10 tc11)

        (switch tc4 tc3 tc6 tc3)
        (disconnected tc4 tc6)
        (switch tc7 tc8 tc9 tc8)
        (disconnected tc7 tc9)
        (switch tc10 tc11 tc12 tc11)
        (disconnected tc10 tc12)
        (signal s0 tc3 tc4 b0 DANGER)
        (signal s2 tc11 tc10 b0 DANGER)

        (at train0 tc1)
        (inBlock train0 b2)
        (last train0 tc1)
        (fullBlock b2)

        (at train1 tc11)
        (inBlock train1 b1)
        (last train1 tc11)
        (fullBlock b1)

    )
    (:goal
        (and
            (at train0 tc12)
            (not(crash train0))
            (at train1 tc9)
            (not(crash train1))
        )
    )
)