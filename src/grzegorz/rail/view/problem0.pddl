(define (problem problem1) 
    (:domain domain1) 
 
    (:objects 
        train0 train1 - veh 
        tc0 tc1 tc2 tc3 tc4 tc5 tc6 tc7 tc8 - loc 
        b0 b1 b2  - block 
        s0 s1 - sigItem 
    ) 
 
    (:init 
        (train train0)
        (train train1)

        (tc tc0)
        (tc tc1)
        (tc tc2)
        (tc tc3)
        (tc tc4)
        (tc tc5)
        (tc tc6)
        (tc tc7)
        (tc tc8)

        (block b0)
        (block b1)
        (block b2)

        (sigDef s0)
        (sigDef s1)

        (trackBlock tc1 b2)
        (trackBlock tc7 b0)
        (trackBlock tc6 b0)
        (trackBlock tc0 b2)
        (trackBlock tc3 b0)
        (trackBlock tc2 b0)
        (trackBlock tc5 b0)
        (trackBlock tc8 b0)
        (trackBlock tc4 b1)

        (safeBlock b0)
        (safeBlock b1)
        (safeBlock b2)

        (track tc0 tc1)
        (track tc1 tc2)
        (track tc3 tc2)
        (track tc3 tc4)
        (track tc6 tc7)
        (track tc6 tc5)

        (switch tc3 tc2 tc5 tc2)
        (switch tc6 tc7 tc8 tc7)
        (signal s0 tc1 tc2 b0 DANGER)
        (signal s1 tc4 tc3 b0 DANGER)

        (at train0 tc0)
        (inBlock train0 b2)
        (last train0 tc0)
        (fullBlock b2)

        (at train1 tc4)
        (inBlock train1 b1)
        (last train1 tc4)
        (fullBlock b1)

    )
    (:goal
        (and
            (at train0 tc4)
            (not(crash train0))
            (at train1 tc8)
            (not(crash train1))
        )
    )
)